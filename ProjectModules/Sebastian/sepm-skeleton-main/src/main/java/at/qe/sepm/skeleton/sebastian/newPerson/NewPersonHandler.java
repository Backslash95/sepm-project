package at.qe.sepm.skeleton.sebastian.newPerson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.stefan.models.Person;

/**
 * Loads Information for NewUser(Parent) from Page and writes to User Object
 * which is then passed to the Generator-Class.
 * 
 */

@Component
@Scope("session")
public class NewPersonHandler {

	@Autowired
	private PersonGenerator personGenerator;
	private Person newPerson;

	public NewPersonHandler() {

		newPerson = new Person();
	}

	public void addNewPerson() {
		personGenerator.addNewPerson(newPerson);
		newPerson = new Person();
	}

	public Person getNewPerson() {
		return newPerson;
	}

	public void setNewUser(Person newPerson) {
		this.newPerson = newPerson;
	}

}
