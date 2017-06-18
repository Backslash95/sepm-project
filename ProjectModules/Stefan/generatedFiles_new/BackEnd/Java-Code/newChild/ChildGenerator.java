package at.qe.sepm.skeleton.newChild;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.model.Child;
import at.qe.sepm.skeleton.model.Person;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.repositories.ChildRepository;
import at.qe.sepm.skeleton.repositories.UserRepository;

/**
 * 
 * Generates a new Child for the Database.
 * 
 * @author Steve
 */

@Component
@Scope("session")
public class ChildGenerator {

	// wired beans
	@Autowired
	private UserRepository userRepository;
	private ChildRepository childRepository;

	// attributes for Generator
	private String tempUserName = "";
	private List<User> parentList;
	private List<Person> personList;

	public ChildGenerator() {

		parentList = new ArrayList<User>();
		personList = new ArrayList<Person>();
	}

	// TODO: load photo from captured image
	// TODO: write selected parents into parentList
	// TODO: write selected acquaintances into personList
	public void addNewChild(Child newChild) {
		// create a Parent-User
		Child child = newChild;
		// filled in values
		child.setChildID(newChild.getChildID());
		child.setName(newChild.getName());
		child.setSex(newChild.getSex());
		child.setBirthdate(newChild.getBirthdate());
		// Photo
		child.setAddress(newChild.getAddress());
		// select emergency contact
		child.setOtherInformation(newChild.getOtherInformation());
		child.setAllergies(newChild.getAllergies());
		child.setRegisterDate(new Date());
		child.setSiblings(newChild.getSiblings());

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (child.getName().length() > 0 && newChild.getAddress().length() > 0 && child.getSex().length() > 0
				&& child.getBirthdate().length() > 0 && parentList.size() > 0 && personList.size() > 0) {

			// adding children and parent relations
			Set<Child> tempSetChildren = new HashSet<Child>();
			Set<User> tempSetParents = new HashSet<User>();
			for (User usr : parentList) {

				tempSetChildren = usr.getChildSet();
				tempSetChildren.add(child);
				usr.setChildSet(tempSetChildren);
				usr.setInactive(false);
				userRepository.save(usr);

				tempSetParents = child.getUserSetChild();
				tempSetParents.add(usr);
				newChild.setUserSetChild(tempSetParents);
			}

			// adding acquaintance child relations
			Set<Person> tempSetPerson = new HashSet<Person>();
			for (Person person : personList) {

				tempSetPerson = child.getPersonSet();
				tempSetPerson.add(person);
				newChild.setPersonSet(tempSetPerson);
			}

			childRepository.save(child);

			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("successRegistration.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			if (child.getName().length() == 0) {
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Enter valid Name", ""));
				System.out.println("Enter valid FIRST and LAST-Name");
			} else if (child.getAddress().length() == 0) {
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Enter valid Address", ""));
				System.out.println("Enter valid Address");
			} else if (child.getSex().length() == 0) {
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Enter valid Sex", ""));
				System.out.println("Enter valid Sex");
			} else if (child.getBirthdate().length() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Enter valid Birthdate", ""));
				System.out.println("Enter valid Address");
			} else if (parentList.size() == 0) {
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "No parents selected", ""));
				System.out.println("No parents selected");
			} else if (personList.size() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "No acquaintances selected", ""));
				System.out.println("No acquaintances selected");
			}
		}

	}

	public String getTempUserName() {
		return tempUserName;
	}

	public void setTempUserName(String tempUserName) {
		this.tempUserName = tempUserName;
	}

}
