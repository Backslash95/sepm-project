package at.qe.sepm.skeleton.stefan.newChild;

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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import at.qe.sepm.skeleton.stefan.models.Child;
import at.qe.sepm.skeleton.stefan.models.Person;
import at.qe.sepm.skeleton.stefan.models.User;
import at.qe.sepm.skeleton.stefan.repositories.ChildRepository;
import at.qe.sepm.skeleton.stefan.repositories.PersonRepository;
import at.qe.sepm.skeleton.stefan.repositories.UserRepository;
import at.qe.sepm.skeleton.ui.beans.CheckboxView;
import at.qe.sepm.skeleton.ui.beans.PickListViewUser;

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
	@Autowired
	private ChildRepository childRepository;
	@Autowired
	private PickListViewUser pickList;
	@Autowired
	private CheckboxView checkboxView;
	@Autowired
	private PersonRepository personRepository;

	// attributes for Generator
	private String tempUserName = "";
	private List<User> parentList;
	private List<Person> personList;

	public ChildGenerator() {

		parentList = new ArrayList<User>();
		personList = new ArrayList<Person>();
	}

	/*
	 * @Transactional(propagation = Propagation.REQUIRED, noRollbackFor =
	 * Exception.class) public void test(Child newChild) { Child child =
	 * newChild;
	 * 
	 * // filled in values child.setName(newChild.getName());
	 * child.setSex(newChild.getSex());
	 * child.setBirthdate(newChild.getBirthdate()); // Photo
	 * child.setAddress(newChild.getAddress());
	 * child.setEmergencyContact(newChild.getEmergencyContact());
	 * child.setOtherInformation(newChild.getOtherInformation());
	 * child.setAllergies(newChild.getAllergies()); child.setRegisterDate(new
	 * Date()); child.setSiblings(newChild.getSiblings());
	 * 
	 * child = childRepository.save(child);
	 * 
	 * Set<Person> temp = new HashSet<Person>(); Set<Child> tCs = new
	 * HashSet<Child>(); temp.addAll(personRepository.findAll());
	 * child.setPersonSet(temp);
	 * 
	 * for (Person person : temp) {
	 * System.out.println("****************************************"); if
	 * (person.getChildSetPerson() != null) {
	 * tCs.addAll(person.getChildSetPerson()); } System.out.println(tCs + "NIG"
	 * + child.getName()); tCs.add(child); person.setChildSetPerson(tCs);
	 * personRepository.save(person); }
	 * 
	 * Set<User> parents = new HashSet<User>(); tCs.clear();
	 * parents.addAll(userRepository.findAll()); child.setUserSetChild(parents);
	 * child = childRepository.save(child);
	 * 
	 * for (User parent : parents) { if (parent.getChildSetUser() != null) {
	 * tCs.addAll(parent.getChildSetUser()); } tCs.add(child);
	 * parent.setChildSetUser(tCs); userRepository.save(parent); }
	 * 
	 * }
	 */

	// TODO: load photo from captured image
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void addNewChild(Child newChild) {
		// adds previous parents to List
		pickList.addUsers();

		// create a Child-Entity
		Child child = newChild;

		// filled in values
		child.setName(newChild.getName());
		child.setSex(newChild.getSex());
		child.setBirthdate(newChild.getBirthdate());
		// Photo
		child.setAddress(newChild.getAddress());
		child.setEmergencyContact(newChild.getEmergencyContact());
		child.setOtherInformation(newChild.getOtherInformation());
		child.setAllergies(newChild.getAllergies());
		child.setRegisterDate(new Date());
		child.setSiblings(newChild.getSiblings());

		parentList.addAll(pickList.getChoosenUsers());

		List<String> choosenNames = checkboxView.getSelectedNames();
		for (String usrname : choosenNames) {

			personList.add(personRepository.findFirstByName(usrname));

		}

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (child.getName().length() > 0 && child.getAddress().length() > 0 && child.getSex().length() > 0
				&& child.getBirthdate().toString().length() > 0 && parentList.size() > 0
				&& child.getEmergencyContact().length() > 0) {

			child = childRepository.save(child);

			// adding children and parent relations
			Set<Child> tempSetChildren = new HashSet<Child>();
			Set<User> tempSetParents = new HashSet<User>();

			for (User usr : parentList) {
				if (usr.getChildSetUser() != null)
					tempSetChildren.addAll(usr.getChildSetUser());
				tempSetChildren.add(child);
				usr.setChildSetUser(tempSetChildren);
				usr.setInactive(false);
				userRepository.save(usr);

				if (child.getUserSetChild() != null)
					tempSetParents.addAll(child.getUserSetChild());
				tempSetParents.add(usr);
				child.setUserSetChild(tempSetParents);
				child = childRepository.save(child);
			}

			// adding acquaintance child relations
			if (personList.size() > 0) {
				Set<Person> tempSetPerson = new HashSet<Person>();
				tempSetChildren = new HashSet<Child>();
				for (Person person : personList) {
					if (child.getPersonSet() != null)
						tempSetPerson.addAll(child.getPersonSet());
					tempSetPerson.add(person);
					child.setPersonSet(tempSetPerson);
					child = childRepository.save(child);

					if (person.getChildSetPerson() != null)
						tempSetChildren.addAll(person.getChildSetPerson());
					tempSetChildren.add(child);
					person.setChildSetPerson(tempSetChildren);
					personRepository.save(person);
				}
			}

			reinit();

			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("successRegistration.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			if (child.getName().length() == 0) {
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Enter valid Name", ""));
				System.out.println("Enter valid Name");
			} else if (child.getBirthdate().toString().length() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Enter valid Birthdate", ""));
				System.out.println("Enter valid Brithdate");
			} else if (child.getAddress().length() == 0) {
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Enter valid Address", ""));
				System.out.println("Enter valid Address");
			} else if (child.getEmergencyContact().length() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Enter valid Emergency Contact", ""));
				System.out.println("Enter valid Emergency Contact");
			} else if (child.getSex().length() == 0) {
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Enter valid Sex", ""));
				System.out.println("Enter valid Sex");
			} else if (parentList.size() == 0) {
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "No parents selected", ""));
				System.out.println("No parents selected");
			}
		}
	}

	public String getTempUserName() {
		return tempUserName;
	}

	public void setTempUserName(String tempUserName) {
		this.tempUserName = tempUserName;
	}

	public void reinit() {
		parentList = new ArrayList<User>();
		personList = new ArrayList<Person>();
		pickList.reinit();
	}

}
