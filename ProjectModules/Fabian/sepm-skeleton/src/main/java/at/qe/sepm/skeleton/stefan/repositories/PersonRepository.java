package at.qe.sepm.skeleton.stefan.repositories;

import at.qe.sepm.skeleton.stefan.models.Person;

/**
 * Repository for managing {@link Person} entities.
 *
 * @author Steve
 */

public interface PersonRepository extends AbstractRepository<Person, Long> {

	Person findFirstByPersonID(int personID);

	Person findFirstByName(String name);

}
