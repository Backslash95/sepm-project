package at.qe.sepm.skeleton.stefan.repositories;

import at.qe.sepm.skeleton.stefan.models.Child;

/**
 * Repository for managing {@link Child} entities.
 *
 * @author Steve
 */
public interface ChildRepository extends AbstractRepository<Child, Long>
{

	Child findFirstByChildID(int childID);

	Child findFirstByName(String name);

}
