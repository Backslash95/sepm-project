package at.qe.sepm.skeleton.newParent;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.model.UserRole;
import at.qe.sepm.skeleton.repositories.UserRepository;
import at.qe.sepm.skeleton.services.UserService;

/**
 * 
 * Generates a new User(Parent) for the User Database.
 * 
 * @author Steve
 */

@Component
@Scope("session")
public class UserGenerator {

	// wired beans
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	// attributes for Generator
	public static final int passWordMinLegth = 6;
	private String checkpassword;
	// PasswordEncoder pw;
	private String tempUserName = "";

	public UserGenerator() {
		// pw = new StandardPasswordEncoder();
	}

	public void addNewUser(User newUser) {
		// create a Parent-User
		User user = newUser;
		// filled in values
		user.setFirstName(newUser.getFirstName());
		user.setLastName(newUser.getLastName());
		user.setUsername(newUser.getUsername());
		user.setEmail(newUser.getEmail());
		user.setPhone(newUser.getPhone());
		user.setAddress(newUser.getAddress());
		user.setSex(newUser.getSex());

		// other values to be filled for a User
		user.setEnabled(true);
		user.setInactive(false); // -> at least one child has to be selected in
									// registration
		user.setCreateDate(new Date());
		user.setCreateUser(userRepository.findFirstByUsername("admin"));
		Set<UserRole> newset = new HashSet<UserRole>();
		newset.add(UserRole.PARENT);
		user.setRoles(newset);

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (checkpassword.equals(newUser.getPassword())
				&& newUser.getPassword().length() > UserGenerator.passWordMinLegth && !userService.userExists(user)
				&& user.getEmail().contains("@") && user.getEmail().length() > 4 && user.getFirstName().length() > 0
				&& user.getLastName().length() > 0 && userRepository.findByEmail(user.getEmail()) == null
				&& newUser.getPhone().length() > 0 && newUser.getAddress().length() > 0) {

			user.setPassword(newUser.getPassword());
			userRepository.save(user);
			checkpassword = "";
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("successRegistration.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			if (!checkpassword.equals(newUser.getPassword())) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Passwords do not match!", ""));
				System.out.println("Passwords do not match!");
			} else if (newUser.getPassword().length() <= UserGenerator.passWordMinLegth) {
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Password too short!", ""));
				System.out.println("Password too short!");
			} else if (userService.userExists(user)) {
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Username taken!", ""));
				System.out.println("Username taken!");
			} else if (!user.getEmail().contains("@") || user.getEmail().length() < 4) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Invalid E-mail Address!", ""));
				System.out.println("Invalid E-mail Address!");
			} else if (userRepository.findByEmail(user.getEmail()) != null) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "E-mail Address taken!", ""));
				System.out.println("E-mail Address taken!");
			} else if (user.getFirstName().length() == 0 || user.getLastName().length() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Enter valid FIRST and LAST-Name", ""));
				System.out.println("Enter valid FIRST and LAST-Name");
			} else if (newUser.getAddress().length() == 0) {
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Enter valid Address", ""));
				System.out.println("Enter valid Address");
			} else if (newUser.getPhone().length() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Enter valid Phonenumber", ""));
				System.out.println("Enter valid Phonenumber");
			}

		}

	}

	public String getCheckpassword() {
		return checkpassword;
	}

	public void setCheckpassword(String checkpassword) {
		this.checkpassword = checkpassword;
	}

	public String getTempUserName() {
		return tempUserName;
	}

	public void setTempUserName(String tempUserName) {
		this.tempUserName = tempUserName;
	}

}
