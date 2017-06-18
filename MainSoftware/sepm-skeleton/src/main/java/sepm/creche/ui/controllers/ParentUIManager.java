package sepm.creche.ui.controllers;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.models.User;
import sepm.creche.models.UserRole;
import sepm.creche.repositories.UserRepository;

/**
 * The controller for the list of the related children to be displayed.
 * 
 * @author Stefan
 */

@Component
@Scope("view")
public class ParentUIManager {

	// wired beans
	@Autowired
	private UserRepository userRepo;

	// attributes for the class
	private Collection<User> collectionParent;

	/**
	 * This method is the constructor of the class. Several initializations are
	 * made in this method.
	 */
	public ParentUIManager() {
		collectionParent = new HashSet<User>();
	}

	public Collection<User> loadParentList() {
		collectionParent = new HashSet<User>();

		for (User usr : userRepo.findAll()) {
			Collection<UserRole> usrRole = usr.getRoles();
			if ((usrRole.contains(UserRole.PARENT) || usrRole.contains(UserRole.INACTIVEPARENT))
					&& !usrRole.contains(UserRole.ADMIN)) {
				collectionParent.add(usr);
			}
		}

		return collectionParent;
	}

}
