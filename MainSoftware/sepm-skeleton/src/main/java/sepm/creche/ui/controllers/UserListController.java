package sepm.creche.ui.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.models.User;
import sepm.creche.services.UserService;

/**
 * Controller for the user list view.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Softwaredevelopment and Project Management" offered by the University
 * of Innsbruck.
 */
@Component
@Scope("view")
public class UserListController
{

	@Autowired
	private UserService userService;

	/**
	 * Returns a list of all users.
	 *
	 * @return
	 */
	public Collection<User> getUsers()
	{
		return userService.getAllUsers();
	}

}
