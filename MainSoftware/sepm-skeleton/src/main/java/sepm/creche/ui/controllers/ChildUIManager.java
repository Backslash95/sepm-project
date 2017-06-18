package sepm.creche.ui.controllers;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.models.Child;
import sepm.creche.models.User;
import sepm.creche.repositories.ChildRepository;
import sepm.creche.services.UserService;
import sepm.creche.ui.beans.SessionInfoBean;

/**
 * The controller for the list of the related children to be displayed.
 * 
 * @author Stefan
 */

@Component
@Scope("view")
public class ChildUIManager {

	// wired beans
	@Autowired
	private SessionInfoBean session;
	@Autowired
	private UserService userService;
	@Autowired
	private ChildRepository childRepo;

	// attributes for the class
	private Collection<Child> collectionChild;
	private Collection<Child> tempSet;
	private Collection<Child> collectionChildEmployee;

	/**
	 * This method is the constructor of the class. Several initializations are
	 * made in this method.
	 */
	public ChildUIManager() {
		collectionChild = new HashSet<Child>();
		tempSet = new HashSet<Child>();
		collectionChildEmployee = new HashSet<Child>();
	}

	/**
	 * This method loads all children for the user.
	 * 
	 * @return a collection of all found children (not inactive and related
	 *         current user)
	 */
	public Collection<Child> loadChildList() {
		collectionChild = new HashSet<Child>();
		tempSet = new HashSet<Child>();
		User currentUsr = userService.loadUser(session.getCurrentUserName());

		if (currentUsr != null) {
			if (currentUsr.getMyChildren() != null) {
				tempSet.addAll(currentUsr.getMyChildren());
			}
			for (Child child : tempSet) {
				collectionChild.add(child);
			}
		}

		return collectionChild;
	}

	public Collection<Child> loadChildListEmployee() {
		collectionChildEmployee = new HashSet<Child>();

		for (Child child : childRepo.findAll()) {
			collectionChildEmployee.add(child);
		}

		return collectionChildEmployee;
	}

}
