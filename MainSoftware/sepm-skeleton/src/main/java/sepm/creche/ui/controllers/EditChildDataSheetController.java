package sepm.creche.ui.controllers;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sepm.creche.logger.AuditLog;
import sepm.creche.models.Child;
import sepm.creche.models.Person;
import sepm.creche.models.Picture;
import sepm.creche.repositories.ChildRepository;
import sepm.creche.repositories.PersonRepository;
import sepm.creche.repositories.PictureRepository;
import sepm.creche.ui.beans.CheckboxView;
import sepm.creche.ui.beans.SessionInfoBean;
import sepm.creche.utils.FileUploader;

/**
 * The controller for editing the ChildDataSheet.
 * 
 * @author Stefan
 */

@Component
@Scope("session")
public class EditChildDataSheetController {

	// wired beans
	@Autowired
	private ChildRepository childRepository;
	@Autowired
	private CheckboxView checkboxView;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private AuditLog auditLog;
	@Autowired
	private SessionInfoBean sessionInfo;
	@Autowired
	private FileUploader fileUploader;
	@Autowired
	private PictureRepository pictureRepository;

	// attributes for Generator
	private Set<Person> personList;
	private Child selectedChild;
	private String fileName;

	/**
	 * This method is the constructor of the class. Several initializations are
	 * made in this method.
	 */
	public EditChildDataSheetController() {
		personList = new HashSet<Person>();
		fileName = "";

	}

	/**
	 * This method takes the given information from the input and saves it for
	 * the selected child, when met the guidelines. Guidelines: name given,
	 * address given, sex given, birthdate given, emergency contact given
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void saveData() {

		// fill the list with chosen names from checkbox view
		List<String> choosenNames = checkboxView.getSelectedNames();
		for (String usrname : choosenNames) {

			personList.add(personRepository.findFirstByName(usrname));

		}

		FacesContext facesContext = FacesContext.getCurrentInstance();
		// check if inputs are valid
		if (selectedChild.getName().length() > 0 && selectedChild.getAddress().length() > 0
				&& selectedChild.getSex().length() > 0 && selectedChild.getBirthdate() != null
				&& selectedChild.getEmergencyContact().length() > 0) {

			selectedChild.setMyRelatives(new HashSet<Person>());

			childRepository.save(selectedChild);

			Set<Child> tempSetChildren = new HashSet<Child>();
			// adding acquaintance child relations
			if (personList.size() > 0) {
				Set<Person> tempSetPerson = new HashSet<Person>();
				tempSetChildren = new HashSet<Child>();
				for (Person person : personList) {
					if (selectedChild.getMyRelatives() != null)
						tempSetPerson.addAll(selectedChild.getMyRelatives());
					tempSetPerson.add(person);
					selectedChild.setMyRelatives(tempSetPerson);
					selectedChild = childRepository.save(selectedChild);

					if (person.getKnownChildren() != null)
						tempSetChildren.addAll(person.getKnownChildren());
					tempSetChildren.add(selectedChild);
					person.setKnownChildren(tempSetChildren);
					personRepository.save(person);
				}
			}

			// log event
			auditLog.log(sessionInfo.getCurrentUser(), "Child " + selectedChild.getName() + " was edited");

			// adding child picture relations
			Picture pic = pictureRepository.findFirstByChildID(selectedChild.getChildID());
			if (fileName != "") {
				pic.setPictureReference(fileName);
			}
			if (pic != null)
				pictureRepository.save(pic);

			try {
				if (FacesContext.getCurrentInstance() != null)
					FacesContext.getCurrentInstance().getExternalContext().redirect("selectedChild.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// display the wrong input
			if (selectedChild.getName().length() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bitte das Feld Name ausfüllen!", ""));
			} else if (selectedChild.getBirthdate() == null) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bitte das Feld Geburtsdatum ausfüllen!", ""));
			} else if (selectedChild.getAddress().length() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bitte das Feld Adresse ausfüllen!", ""));
			} else if (selectedChild.getEmergencyContact().length() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bitte das Feld Notfallkontakt ausfüllen!", ""));
			} else if (selectedChild.getSex().length() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bitte ein Geschlecht auswählen!", ""));
			}
		}

		// reinitialize checkbox view and lists from this class
		reinit();
	}

	/**
	 * This method handles the fileuploader for the photo.
	 * 
	 * @param event
	 *            an event to get the uploaded file from
	 * @throws IOException
	 * 
	 */
	public void handleFileUpload(FileUploadEvent event) throws IOException {

		fileName = fileUploader.handleFileUpload(event);
	}

	/**
	 * This method reinitializes the needed attributes of the class and the
	 * checkboxview, when used once.
	 */
	public void reinit() {
		checkboxView.reinit();
		personList = new HashSet<Person>();
		fileName = "";
	}

	// getter for the selected child id
	public Child getSelectedChild() {
		return selectedChild;
	}

	/**
	 * This method sets the selected child ID and redirects to the next page.
	 * 
	 * @param selectedChild
	 *            the ID of the chosen child
	 */
	public void setSelectedChild(Child selectedChild) {
		this.selectedChild = selectedChild;
		try {
			if (FacesContext.getCurrentInstance() != null)
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/secured/restricted/KinderStammblatt/editChild.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// getters and setters
	public Set<Person> getPersonList() {
		return personList;
	}

	public void setPersonList(Set<Person> personList) {
		this.personList = personList;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
