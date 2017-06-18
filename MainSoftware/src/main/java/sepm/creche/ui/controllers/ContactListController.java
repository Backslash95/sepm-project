package sepm.creche.ui.controllers;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import sepm.creche.models.Child;
import sepm.creche.models.Person;
import sepm.creche.models.User;
import sepm.creche.repositories.ChildRepository;

/**
 * The controller for the ContactList.
 * 
 * @author Stefan
 */

@Component
@Scope("view")
public class ContactListController {

	// wired beans
	@Autowired
	private ChildRepository childRepository;

	// attribute for the class
	private Collection<User> collectionUser;
	private Collection<Person> collectionPerson;

	/**
	 * This method is the constructor of the class. An initialization is made in
	 * this method.
	 */
	public ContactListController() {
		collectionUser = new HashSet<User>();
		collectionPerson = new HashSet<Person>();
	}

	/**
	 * This method requests the parents of each child of the database.
	 * 
	 * @return list of all active parents
	 */
	@PreAuthorize("hasAuthority('EMPLOYEE')")
	public Collection<User> loadParentContactList() {

		for (Child child : childRepository.findAll()) {
			collectionUser.addAll(child.getMyParents());
		}

		return collectionUser;

	}

	/**
	 * This method formats all children of a specific parent.
	 * 
	 * @return formatted String of children names
	 */
	public String convertChildTable(Collection<Child> childCollection) {

		if (childCollection.isEmpty()) {
			return "";
		}

		StringBuilder childnames = new StringBuilder();
		for (Child child : childCollection) {
			if (child.isDeregistered() == false) {
				childnames.append(child.getName());
				childnames.append(",");
			}
		}
		// get rid off last comma
		if (childnames.length() > 0) {
			childnames.deleteCharAt(childnames.length() - 1);
		}

		return childnames.toString();
	}

	@PreAuthorize("hasAuthority('EMPLOYEE')")
	public Collection<Person> loadPersonContactList() {

		for (Child child : childRepository.findAll()) {
			collectionPerson.addAll(child.getMyRelatives());
		}

		return collectionPerson;

	}

}
