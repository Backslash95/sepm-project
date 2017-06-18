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
import sepm.creche.repositories.ChildRepository;
import sepm.creche.repositories.PersonRepository;
import sepm.creche.repositories.UserRepository;
import sepm.creche.services.GalleryPicService;
import sepm.creche.ui.beans.ChildCheckboxView;
import sepm.creche.ui.beans.SessionInfoBean;
import sepm.creche.utils.FileUploader;

/**
 * 
 * This class generates a new person for the database.
 * 
 * @author Sebastian
 */

@Component
@Scope("session")
public class PersonGenerator
{

	// wired beans
	@Autowired
	private ChildRepository childRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private AuditLog auditLog;
	@Autowired
	private ChildCheckboxView childCheckboxView;
	@Autowired
	private FileUploader fileUploader;
	@Autowired
	private SessionInfoBean sessionInfoBean;
	@Autowired
	private UserRepository userRepository;

	// attributes for Generator
	private List<Child> childList;
	private String fileName;

	public PersonGenerator()
	{
		childList = new ArrayList<Child>();
		fileName = "";
	}

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void addNewPerson(Person newPerson)
	{
		Person person = new Person();

		person.setName(newPerson.getName());
		person.setPhone(newPerson.getPhone());
		person.setEmail(newPerson.getEmail());
		person.setRelation(newPerson.getRelation());
		person.setActivated(false);
		person.setCreateDate(new Date());
		person.setCreateUser(userRepository.findFirstByUsername(sessionInfoBean.getCurrentUser().getUsername()));

		if (fileName == "")
		{
			person.setPictureReference(GalleryPicService.defaultProfilePic);
		} else
		{
			person.setPictureReference(fileName);
		}

		List<String> choosenNames = childCheckboxView.getSelectedNames();
		if (choosenNames != null)
		{
			for (String name : choosenNames)
			{
				auditLog.log(name);
				childList.add(childRepository.findFirstByName(name));

			}
		}

		FacesContext facesContext = FacesContext.getCurrentInstance();

		// adding children and person relations
		if (childList.size() > 0 && person.getEmail().contains("@") && person.getEmail().length() > 4
				&& person.getName().length() > 0 && person.getPhone().length() > 0)
		{
			Set<Person> tempSetPerson = new HashSet<Person>();
			Set<Child> tempSetChildren = new HashSet<Child>();

			person = personRepository.save(person);

			for (Child child : childList)
			{
				if (person.getKnownChildren() != null)
					tempSetChildren.addAll(person.getKnownChildren());
				tempSetChildren.add(child);
				person.setKnownChildren(tempSetChildren);
				person = personRepository.save(person);

				if (child.getMyRelatives() != null)
					tempSetPerson.addAll(child.getMyRelatives());
				tempSetPerson.add(person);
				child.setMyRelatives(tempSetPerson);

				childRepository.save(child);

			}

			try
			{
				if (FacesContext.getCurrentInstance() != null)
					FacesContext.getCurrentInstance().getExternalContext().redirect("successRegistration.xhtml");
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			personRepository.save(person);
			auditLog.log("New person \"" + newPerson.getName() + "\" CREATED");
		} else
		{
			if (person.getName().length() == 0)
			{
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Enter valid Name", ""));
			} else if (!person.getEmail().contains("@") || person.getEmail().length() < 4)
			{
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Invalid E-mail Address!", ""));
			} else if (person.getPhone().length() == 0)
			{
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Enter valid Phonenumber", ""));
			}
		}

		reinit();
	}

	public void reinit()
	{
		childList = new ArrayList<Child>();
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
	public void handleFileUpload(FileUploadEvent event) throws IOException
	{

		fileName = fileUploader.handleFileUpload(event);
	}

	public List<Child> getChildList()
	{
		return childList;
	}

	public void setChildList(List<Child> childList)
	{
		this.childList = childList;
	}

}
