package sepm.creche.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import sepm.creche.models.User;

/**
 * Loads Information for NewUser(Parent) from Page and writes to User Object
 * which is then passed to the Generator-Class.
 * 
 * @author Stefan
 */

@Component
@Scope("session")
public class NewUserHandler {

	// wired beans
	@Autowired
	private UserGenerator userGenerator;

	// attribute for the class
	private User newUser;

	/**
	 * This method is the constructor of the class. Several initializations are
	 * made in this method.
	 * 
	 */
	public NewUserHandler() {

		newUser = new User();
	}

	/**
	 * This method passes the new user(parent) to the generator class.
	 */
	@PreAuthorize("hasAuthority('EMPLOYEE')")
	public void addNewUser() {
		userGenerator.addNewUser(newUser);
		newUser = new User();

	}

	// getters and setters
	public User getNewUser() {
		return newUser;
	}

	public void setNewUser(User newUser) {
		this.newUser = newUser;
	}

}
