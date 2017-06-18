package sepm.creche.ui.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A Bean which caches text fields
 * 
 * @author Sebastian Grabher
 */

@Component
@Scope("view")
public class InputTextCacheBean {

	/* Cache new user username */
	private String username;

	/* Cache new user firstname */
	private String firstName;

	/* Cache new user lastname */
	private String lastName;

	/* Cache new user password */
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
