package sepm.creche.repositories;

import java.util.List;

import sepm.creche.models.Child;

/**
 * Repository for managing {@link Child} entities. This repository implements
 * the abstract repository{@link AbstractRepository}.
 *
 * @author Stefan
 */
public interface ChildRepository extends AbstractRepository<Child, Long> {

	/**
	 * Finds the first entry for the matching ID.
	 *
	 * @param childID
	 *            the Identifier for the search
	 * @return the found Child
	 */
	Child findFirstByChildID(int childID);

	/**
	 * Finds the first entry for the matching name.
	 *
	 * @param name
	 *            the name for the search
	 * @return the found Child
	 */
	Child findFirstByName(String name);

	/**
	 * Finds all children containing the given string and writes it into a list.
	 *
	 * @param name
	 *            the String for the search
	 * @return list of all found children
	 */
	List<Child> findByNameContaining(String name);

}
