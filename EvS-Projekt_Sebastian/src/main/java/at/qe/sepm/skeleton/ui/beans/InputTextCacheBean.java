package at.qe.sepm.skeleton.ui.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.services.UserService;

//TODO this bean should be flushed, had some huge problems with returnig old values
//TODO solved this problem with a page refresh
/**
 * A Bean that caches input from inputtext fields
 * 
 * @author Andre Potocnik <andre.potocnik@student.uibk.ac.at>
 *
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
	
	/* Cache new project name */
	private String projectName;
	
	/* Cache the new workpackage name */
	private String workPackageName;
	
	/* Cache recipient e-mail address*/
	private String emailTo;
	
	/* Cache the ps leader*/
	private User psLeader;
	
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

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public User getPsLeader() {
		return psLeader;
	}

	public void setPsLeader(User psLeader) {
		this.psLeader = psLeader;
	}

	public String getWorkPackageName() {
		return workPackageName;
	}

	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}
}
