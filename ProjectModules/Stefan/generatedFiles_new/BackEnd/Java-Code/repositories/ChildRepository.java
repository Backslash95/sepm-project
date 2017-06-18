package at.qe.sepm.skeleton.repositories;

import at.qe.sepm.skeleton.model.Child;

/**
 * Repository for managing {@link Child} entities.
 *
 * @author Steve
 */
public interface ChildRepository extends AbstractRepository<Child, Long> {

	Child findFirstByChildID(int childID);

	Child findFirstByName(String name);

}
