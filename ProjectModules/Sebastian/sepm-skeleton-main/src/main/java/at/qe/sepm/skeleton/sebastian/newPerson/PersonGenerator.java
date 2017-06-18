package at.qe.sepm.skeleton.sebastian.newPerson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import at.qe.sepm.skeleton.sebastian.logger.AuditLog;
import at.qe.sepm.skeleton.stefan.models.Child;
import at.qe.sepm.skeleton.stefan.models.Person;
import at.qe.sepm.skeleton.stefan.repositories.ChildRepository;
import at.qe.sepm.skeleton.stefan.repositories.PersonRepository;
import at.qe.sepm.skeleton.ui.beans.ChildCheckboxView;

/**
 * 
 * Generates a new Person for the Database.
 * 
 * @author Sebastian
 */

@Component
@Scope("session")
public class PersonGenerator {

	// wired beans
	@Autowired
	private ChildRepository childRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private AuditLog auditLog;
	@Autowired
	private ChildCheckboxView childCheckboxView;

	// attributes for Generator
	private List<Child> childList;
	
	public PersonGenerator(){
		childList = new ArrayList<Child>();
	}

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void addNewPerson(Person newPerson) {
		Person person = new Person();
		
		person.setName(newPerson.getName());
		person.setPhone(newPerson.getPhone());
		person.setEmail(newPerson.getEmail());
				
		List<String> choosenNames = childCheckboxView.getSelectedNames();

		for (String name : choosenNames) {
			auditLog.log(name);
			childList.add(childRepository.findFirstByName(name));

		}
		
		
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
				
		// adding children and person relations	
		if (childList.size() > 0 && 
			person.getEmail().contains("@") &&
			person.getEmail().length() > 4 &&
			person.getName().length() > 0 && 
			person.getPhone().length() > 0) 
		{
			Set<Person> tempSetPerson = new HashSet<Person>();
			Set<Child> tempSetChildren = new HashSet<Child>();
			for (Child child : childList) {
				if(person.getChildSetPerson() != null)
					tempSetChildren.addAll(person.getChildSetPerson());
				tempSetChildren.add(child);
				person.setChildSetPerson(tempSetChildren);
				person = personRepository.save(person);
				
				if(child.getPersonSet() != null)
					tempSetPerson.addAll(child.getPersonSet());
				tempSetPerson.add(person);
				child.setPersonSet(tempSetPerson);
				childRepository.save(child);
			}
			
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("successRegistration.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			if (person.getName().length() == 0) {
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Enter valid Name", ""));
			} else if (!person.getEmail().contains("@") || person.getEmail().length() < 4) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Invalid E-mail Address!", ""));
			} else if (person.getPhone().length() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Enter valid Phonenumber", ""));
			}
		}
		personRepository.save(person);
		
		auditLog.log("New person \""+newPerson.getName()+"\" CREATED");

		reinit();
		
		return;
	}
	
	public void reinit() {
		childList = new ArrayList<Child>();
	}

}
