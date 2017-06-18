package sepm.creche.ui.controllers;

import java.io.IOException;
import java.util.Set;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.logger.AuditLog;
import sepm.creche.models.Child;
import sepm.creche.models.Person;
import sepm.creche.repositories.PersonRepository;
import sepm.creche.utils.FileUploader;

/**
 * The controller for editing the Parent
 * 
 * @author Stefan
 */

@Component
@Scope("view")
public class EditPersonController
{

	// wired beans
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private AuditLog auditLog;

	@Autowired
	FileUploader fileUploader;

	private Person currentPerson;

	String fileName;

	public EditPersonController()
	{
		fileName = "";
	}

	/**
	 * This method takes the given information from the input and saves it for
	 * the selected user, when met the guidelines.
	 * 
	 */

	public Person getCurrentPerson()
	{
		return currentPerson;
	}

	public void setCurrentPerson(Person currentPerson)
	{
		this.currentPerson = currentPerson;
		doReloadPerson();
	}

	public void removePerson()
	{
		Set<Child> kids = currentPerson.getKnownChildren();
		kids.clear();
		currentPerson.setKnownChildren(kids);
		currentPerson = personRepository.save(currentPerson);
		personRepository.delete(currentPerson);
		auditLog.log(currentPerson.getName() + " was removed as a PERSON");
	}

	public void enablePerson()
	{
		currentPerson.setActivated(true);
		personRepository.save(currentPerson);
		auditLog.log(currentPerson.getName() + " was enabled as a PERSON");
	}

	public void doSavePerson()
	{
		currentPerson = personRepository.save(currentPerson);
	}

	private void setTempProfilePic(String filename)
	{
		currentPerson.setPictureReference(fileName);
	}

	public void doReloadPerson()
	{
		currentPerson = personRepository.findFirstByPersonID(currentPerson.getPersonID());
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException
	{
		fileName = fileUploader.handleFileUpload(event);
		setTempProfilePic(fileName);
	}

}
