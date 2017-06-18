package at.qe.sepm.skeleton.ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.sebastian.logger.AuditLog;
import at.qe.sepm.skeleton.services.UserService;
import at.qe.sepm.skeleton.stefan.models.User;
import at.qe.sepm.skeleton.stefan.models.UserRole;

/**
 * Controller for the user detail view.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Softwaredevelopment and Project Management" offered by the University
 * of Innsbruck.
 */
@Component
@Scope("view")
public class UserDetailController
{

	@Autowired
	private UserService userService;
	@Autowired
	private AuditLog auditLog;

	/**
	 * Attribute to cache the currently displayed user
	 */
	private User user;

	/**
	 * Sets the currently displayed user and reloads it form db. This user is
	 * targeted by any further calls of {@link #doReloadUser()},
	 * {@link #doSaveUser()} and {@link #doDeleteUser()}.
	 *
	 * @param user
	 */
	public void setUser(User user)
	{
		this.user = user;
		doReloadUser();
	}

	/**
	 * Returns the currently displayed user.
	 *
	 * @return
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * Action to force a reload of the currently displayed user.
	 */
	public void doReloadUser()
	{
		user = userService.loadUser(user.getUsername());
	}

	/**
	 * Action to save the currently displayed user.
	 */
	public void doSaveUser()
	{
		user = this.userService.saveUser(user);
	}

	/**
	 * Action to delete the currently displayed user.
	 */
	public void doDeleteUser()
	{
		this.userService.deleteUser(user);
		user = null;
	}

	/**
	 * function to add the role EMPLOYEE to a user
	 */
	public void makeEmployee()
	{
		user.getRoles().add(UserRole.EMPLOYEE);
		auditLog.log("user " + user.getFirstName() + " " + user.getLastName() + " granted access level: EMPLOYEE");
	}

	/**
	 * function to remove the role EMPLOYEE from a user
	 */
	public void removeEmployee()
	{

		user.getRoles().remove(UserRole.EMPLOYEE);
		auditLog.log("user " + user.getFirstName() + " " + user.getLastName() + " removed access level: EMPLOYEE");

	}

	/**
	 * function to add the role MANAGER to a user
	 */
	public void makeParent()
	{
		user.getRoles().add(UserRole.MANAGER);
		auditLog.log("user " + user.getFirstName() + " " + user.getLastName() + " granted access level: PARENT");

	}

	/**
	 * function to remove the role MANAGER from a user
	 */
	public void removeParent()
	{

		user.getRoles().remove(UserRole.MANAGER);
		auditLog.log("user " + user.getFirstName() + " " + user.getLastName() + " removed access level: PARENT");

	}

	/**
	 * function to add the role ADMIN to a user
	 */
	public void makeAdmin()
	{
		user.getRoles().add(UserRole.ADMIN);
		auditLog.log("user " + user.getFirstName() + " " + user.getLastName() + " granted access level: ADMIN");

	}

	/**
	 * function to remove the role ADMIN from a user
	 */

	public void removeAdmin()
	{
		// TODO: determine if user has rights to do this
		user.getRoles().remove(UserRole.ADMIN);
		auditLog.log("user " + user.getFirstName() + " " + user.getLastName() + " removed access level: ADMIN");

	}

}
