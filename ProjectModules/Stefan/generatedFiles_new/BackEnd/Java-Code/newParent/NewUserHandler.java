package at.qe.sepm.skeleton.newParent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.model.User;

/**
 * Loads Information for NewUser(Parent) from Page and writes to User Object which is
 * then passed to the Generator-Class.
 * 
*/

@Component
@Scope("session")
public class NewUserHandler
{
	@Autowired
	private UserGenerator userGenerator;
	private User newUser;

	public NewUserHandler()
	{

		newUser = new User();
	}

	public void addNewUser()
	{
		userGenerator.addNewUser(newUser);
		newUser = new User();

	}

	public User getNewUser()
	{
		return newUser;
	}

	public void setNewUser(User newUser)
	{
		this.newUser = newUser;
	}

}
