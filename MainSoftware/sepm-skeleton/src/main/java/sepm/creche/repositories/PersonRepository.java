package sepm.creche.repositories;

import java.util.List;

import sepm.creche.models.Person;

/**
 * Repository for managing {@link Person} entities. This repository implements
 * the abstract repository{@link AbstractRepository}.
 *
 * @author Stefan
 */

public interface PersonRepository extends AbstractRepository<Person, Long>
{

	/**
	 * Finds the first entry for the matching Identifier (Id of the class).
	 *
	 * @param personID
	 *            the Identifier for the search
	 * @return the found Person
	 */
	Person findFirstByPersonID(int personID);

	/**
	 * Finds the first entry for the matching name.
	 *
	 * @param name
	 *            the name for the search
	 * @return the found Person
	 */
	Person findFirstByName(String name);

	List<Person> findByActivated(boolean activated);

}
