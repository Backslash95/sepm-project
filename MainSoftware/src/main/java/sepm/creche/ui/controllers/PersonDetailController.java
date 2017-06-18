package sepm.creche.ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.logger.AuditLog;
import sepm.creche.models.Person;
import sepm.creche.repositories.PersonRepository;

/**
 * Controller for the person detail view.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Softwaredevelopment and Project Management" offered by the University
 * of Innsbruck.
 */
@Component
@Scope("view")
public class PersonDetailController
{

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private AuditLog auditLog;

	/**
	 * Attribute to cache the currently displayed person
	 */
	private Person person;

	/**
	 * Sets the currently displayed person and reloads it form db. This person is
	 * targeted by any further calls of {@link #doReloadPerson()},
	 * {@link #doSavePerson()} and {@link #doDeletePerson()}.
	 *
	 * @param person
	 */
	public void setPerson(Person person)
	{
		this.person = person;
		doReloadPerson();
	}

	/**
	 * Returns the currently displayed person.
	 *
	 * @return
	 */
	public Person getPerson()
	{
		return person;
	}

	/**
	 * Action to force a reload of the currently displayed person.
	 */
	public void doReloadPerson()
	{
		person = personRepository.findFirstByPersonID(person.getPersonID());
	}

	/**
	 * Action to save the currently displayed person.
	 */
	public void doSavePerson()
	{
		personRepository.save(person);
		auditLog.log("Person: "+person.getName()+" with ID: "+person.getId()+" was changed");
	}

	/**
	 * Action to delete the currently displayed person.
	 */
	public void doDeletePerson()
	{
		this.personRepository.delete(person);
		person = null;
	}
	
	
}
