package sepm.creche.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.models.Person;

/**
 * This class handles information provided by the user via the "Bezugsperson
 * erstellen" page which is then passed to the Generator-Class.
 * 
 * @author Sebastian Grabher
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
