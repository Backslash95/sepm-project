package sepm.creche.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import sepm.creche.models.User;
import sepm.creche.repositories.ChildRepository;
import sepm.creche.repositories.PersonRepository;
import sepm.creche.repositories.PictureRepository;
import sepm.creche.repositories.UserRepository;
import sepm.creche.ui.beans.CheckboxView;
import sepm.creche.ui.beans.SessionInfoBean;
import sepm.creche.ui.controllers.ParentPickListController;
import sepm.creche.utils.FileUploader;

/**
 * 
 * Generates a new Child for the Database.
 * 
 * @author Stefan
 */

@Component
@Scope("session")
public class ChildGenerator {

	// wired beans
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ChildRepository childRepository;
	@Autowired
	private ParentPickListController pickList;
	@Autowired
	private CheckboxView checkboxView;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private PictureRepository pictureRepository;
	@Autowired
	private AuditLog auditLog;
	@Autowired
	private SessionInfoBean sessionInfo;
	@Autowired
	private FileUploader fileUploader;

	// attributes for Generator
	private List<User> parentList;
	private List<Person> personList;
	private String fileName;

	/**
	 * This method is the constructor of the class. Several initializations are
	 * made in this method.
	 */
	public ChildGenerator() {

		parentList = new ArrayList<User>();
		personList = new ArrayList<Person>();
		fileName = "";
	}

	/**
	 * This method takes the given information from the input and saves it, when
	 * met the guidelines. Guidelines: name given, address given, sex given,
	 * birthdate given, emergency contact given and at least one parent
	 * assigned.
	 * 
	 * @param newChild
	 *            the new child passed by the handler class
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void addNewChild(Child newChild) {

		pickList.addUsers();
		// create a Child-Entity
		Child child = newChild;

		// filled in values
		child.setName(newChild.getName());
		child.setSex(newChild.getSex());
		child.setBirthdate(newChild.getBirthdate());
		child.setDeregistered(false);
		child.setAddress(newChild.getAddress());
		child.setEmergencyContact(newChild.getEmergencyContact());
		child.setOtherInformation(newChild.getOtherInformation());
		child.setAllergies(newChild.getAllergies());
		child.setRegisterDate(new Date());
		child.setSiblings(newChild.getSiblings());
		child.setGivenSignOutDate(newChild.getGivenSignOutDate());

		parentList.addAll(pickList.getChosenUsers());

		List<String> choosenNames = checkboxView.getSelectedNames();
		for (String usrname : choosenNames) {

			personList.add(personRepository.findFirstByName(usrname));

		}

		FacesContext facesContext = FacesContext.getCurrentInstance();
		// check if inputs are valid
		if (child.getName().length() > 0 && child.getAddress().length() > 0 && child.getSex().length() > 0
				&& child.getBirthdate() != null && parentList.size() > 0 && child.getEmergencyContact().length() > 0) {

			child = childRepository.save(child);

			// adding children and parent relations
			Set<Child> tempSetChildren = new HashSet<Child>();
			Set<User> tempSetParents = new HashSet<User>();

			for (User usr : parentList) {
				if (usr.getMyChildren() != null)
					tempSetChildren.addAll(usr.getMyChildren());
				tempSetChildren.add(child);
				usr.setMyChildren(tempSetChildren);
				usr.setInactive(false);
				usr = userRepository.save(usr);

				if (child.getMyParents() != null)
					tempSetParents.addAll(child.getMyParents());
				tempSetParents.add(usr);
				child.setMyParents(tempSetParents);
				child = childRepository.save(child);
			}

			// adding acquaintance child relations
			if (personList.size() > 0) {
				Set<Person> tempSetPerson = new HashSet<Person>();
				tempSetChildren = new HashSet<Child>();
				for (Person person : personList) {
					if (child.getMyRelatives() != null)
						tempSetPerson.addAll(child.getMyRelatives());
					tempSetPerson.add(person);
					child.setMyRelatives(tempSetPerson);
					child = childRepository.save(child);

					if (person.getKnownChildren() != null)
						tempSetChildren.addAll(person.getKnownChildren());
					tempSetChildren.add(child);
					person.setKnownChildren(tempSetChildren);
					personRepository.save(person);
				}
			}

			// adding child picture relations
			Picture pic = new Picture();
			pic.setChildID(child.getChildID());
			if (fileName != "") {
				pic.setPictureReference(fileName);
			} else {
				pic.setPictureReference("user-silhouette.jpg");
			}
			pictureRepository.save(pic);

			// reset picklist, checkbox view and lists of this class
			reinit();

			// log event
			auditLog.log(sessionInfo.getCurrentUser(), "New child with name " + child.getName() + " generated");

			// redirect, if Registration was successful
			try {
				if (FacesContext.getCurrentInstance() != null)
					FacesContext.getCurrentInstance().getExternalContext().redirect("successRegistration.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			// display the wrong input
			if (child.getName().length() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bitte das Feld Name ausfüllen!", ""));
			} else if (child.getBirthdate() == null) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bitte das Feld Geburtsdatum ausfüllen!", ""));
			} else if (child.getAddress().length() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bitte das Feld Adresse ausfüllen!", ""));
			} else if (child.getEmergencyContact().length() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bitte das Feld Notfallkontakt ausfüllen!", ""));
			} else if (child.getSex().length() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bitte ein Geschlecht auswählen!", ""));
			} else if (parentList.size() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Es wurden keine Eltern ausgewählt!", ""));
			}
		}
	}

	/**
	 * This method reinitializes the class attributes and the specific views.
	 */
	public void reinit() {
		parentList = new ArrayList<User>();
		personList = new ArrayList<Person>();
		pickList.reinit();
		checkboxView.reinit();
		fileName = "";
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

	public List<User> getParentList() {
		return parentList;
	}

	public void setParentList(List<User> parentList) {
		this.parentList = parentList;
	}

	public List<Person> getPersonList() {
		return personList;
	}

	public void setPersonList(List<Person> personList) {
		this.personList = personList;
	}

}
