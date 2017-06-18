package sepm.creche.ui.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.logger.AuditLog;
import sepm.creche.models.User;
import sepm.creche.models.UserRole;
import sepm.creche.services.UserService;
import sepm.creche.ui.beans.SessionInfoBean;

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
	@Autowired
	private SessionInfoBean sessionInfoBean;

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
		if (user.isInactive() == true)
		{
			Collection<UserRole> roles = user.getRoles();
			if (roles.contains(UserRole.PARENT))
				roles.remove(UserRole.PARENT);

			if (!roles.contains(UserRole.INACTIVEPARENT))
				roles.add(UserRole.INACTIVEPARENT);
			user.setRoles(roles);
		} else
		{
			Collection<UserRole> roles = user.getRoles();
			if (roles.contains(UserRole.INACTIVEPARENT))
			{
				roles.remove(UserRole.INACTIVEPARENT);

				if (!roles.contains(UserRole.PARENT))
					roles.add(UserRole.PARENT);
			}
			user.setRoles(roles);
		}
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
		if (!user.getRoles().contains(UserRole.EMPLOYEE))
		{
			user.getRoles().add(UserRole.EMPLOYEE);
			auditLog.log("user " + user.getFirstName() + " " + user.getLastName() + " granted access level: EMPLOYEE");
		}
	}

	/**
	 * function to remove the role EMPLOYEE from a user
	 */
	public void removeEmployee()
	{

		if (user.getRoles().contains(UserRole.EMPLOYEE))
		{
			user.getRoles().remove(UserRole.EMPLOYEE);
			auditLog.log("user " + user.getFirstName() + " " + user.getLastName() + " removed access level: EMPLOYEE");
		}
	}

	/**
	 * function to add the role MANAGER to a user
	 */
	public void makeParent()
	{
		if (!user.getRoles().contains(UserRole.PARENT))
		{
			user.getRoles().add(UserRole.PARENT);
			auditLog.log("user " + user.getFirstName() + " " + user.getLastName() + " granted access level: PARENT");
		}

	}

	/**
	 * function to remove the role Parent from a user
	 */
	public void removeParent()
	{
		if (user.getRoles().contains(UserRole.PARENT))
		{
			user.getRoles().remove(UserRole.PARENT);
			auditLog.log("user " + user.getFirstName() + " " + user.getLastName() + " removed access level: PARENT");
		}
	}

	/**
	 * function to add the role ADMIN to a user
	 */

	public void makeAdmin()
	{
		if (!user.getRoles().contains(UserRole.ADMIN))
		{
			user.getRoles().add(UserRole.ADMIN);
			auditLog.log("user " + user.getFirstName() + " " + user.getLastName() + " granted access level: ADMIN");
		}
	}

	/**
	 * function to remove the role ADMIN from a user
	 */

	public void removeAdmin()
	{

		if (user.getRoles().contains(UserRole.ADMIN))
		{
			// TODO: determine if user has rights to do this
			user.getRoles().remove(UserRole.ADMIN);
			auditLog.log("user " + user.getFirstName() + " " + user.getLastName() + " removed access level: ADMIN");
		}
	}

}
