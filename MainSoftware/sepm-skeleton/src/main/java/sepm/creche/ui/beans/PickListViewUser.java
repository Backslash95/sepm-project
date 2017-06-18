package sepm.creche.ui.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sepm.creche.models.Child;
import sepm.creche.models.User;
import sepm.creche.repositories.UserRepository;

/**
 * Controller for the PickList
 * 
 * @author Stefan
 * 
 */

@Component
@Scope("session")
public class PickListViewUser {

	// wired beans
	@Autowired
	private UserRepository userRepository;

	// attributes for the class
	private DualListModel<String> usernames;
	private DualListModel<String> parentsToCurrentChild;
	private List<String> userSource;
	private List<String> userTarget;
	private List<String> chosenUsernames;
	private List<User> chosenUsers;
	private User tempUser;
	private String filterByUsername;

	/**
	 * This method is the constructor of the class. Several initializations are
	 * made in this method.
	 */
	public PickListViewUser() {

		userSource = new ArrayList<String>();
		userTarget = new ArrayList<String>();
		chosenUsernames = new ArrayList<String>();
		chosenUsers = new ArrayList<User>();
		filterByUsername = "";
	}

	/**
	 * This method reinitializes the view, when used once.
	 */
	public void reinit() {

		userSource = new ArrayList<String>();
		userTarget = new ArrayList<String>();
		chosenUsernames = new ArrayList<String>();
		chosenUsers = new ArrayList<User>();
		filterByUsername = "";
	}

	/**
	 * This method creates a popup on the GUI.
	 * 
	 * @param actionEvent
	 *            the event triggered by the GUI (user clicked on something)
	 */
	public void popUp(ActionEvent actionEvent) {
		addMessage("Assigned chosen parents to child!");
	}

	/**
	 * This method prints a string on the GUI.
	 * 
	 * @param summary
	 *            the string to be printed
	 */
	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	/**
	 * This method adds previous users to the left list.
	 */
	public void addUsers() {

		chosenUsernames = usernames.getTarget();

		for (String usrname : chosenUsernames) {
			tempUser = userRepository.findFirstByUsername(extractUserName(usrname));
			if (!chosenUsers.contains(tempUser)) {
				chosenUsers.add(tempUser);
			}
		}
	}

	private String addKidNames(String username, Set<Child> kids) {
		if (kids.isEmpty())
			return username;
		StringBuilder finalName = new StringBuilder();
		finalName.append(username);
		finalName.append("[");
		for (Child c : kids) {
			finalName.append(c.getName());
			finalName.append(",");
		}
		finalName.replace(finalName.length() - 1, finalName.length(), "]");
		return finalName.toString();
	}

	/**
	 * This method finally saves the chosen parents.
	 */
	public void addNewUsers() {

		chosenUsernames = parentsToCurrentChild.getTarget();

		for (String usrname : chosenUsernames) {
			tempUser = userRepository.findFirstByUsername(extractUserName(usrname));
			if (!chosenUsers.contains(tempUser)) {
				chosenUsers.add(tempUser);
			}
		}
	}

	public void setUsernames2(DualListModel<String> names) {

		for (String n : names.getTarget()) {
			System.out.println(n);
		}
		setUsernames(names);
	}

	/**
	 * This method fills and creates the dual list model, which is displayed on
	 * the GUI.
	 * 
	 */
	public DualListModel<String> getUsernames() {

		userSource.clear();
		List<User> users = new ArrayList<User>();

		if (filterByUsername.matches("") || filterByUsername.startsWith(" ")) {

			for (User usr : userRepository.findAll()) {
				users.add(usr);
			}
		} else {

			for (User usr : userRepository.findByUsernameContaining(filterByUsername)) {
				users.add(usr);
			}
		}

		for (String usrname : chosenUsernames) {
			if (!userTarget.contains(usrname)) {
				userTarget.add(usrname);
			}
		}

		for (User usr : users) {
			if (usr.isEnabled()
					&& (!userSource
							.contains(addUsernameToFullName(usr.getUsername(), usr.getFirstName(), usr.getLastName())))
					&& (!userTarget.contains(usr.getUsername()))) {
				userSource.add(

						addKidNames(

								addUsernameToFullName(usr.getUsername(), usr.getFirstName(), usr.getLastName()),
								usr.getMyChildren())

				);
			}
		}
		usernames = new DualListModel<String>(userSource, userTarget);

		return usernames;
	}

	public void setParentsToCurrentChild(DualListModel<String> parentsToCurrentChild) {
		this.parentsToCurrentChild = parentsToCurrentChild;
	}

	/**
	 * This method returns all parents for a given child.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public DualListModel<String> getParentsToCurrentChild() {
		userSource.clear();
		Collection<User> users = new ArrayList<User>();

		users.addAll(userRepository.findAll());

		if (filterByUsername.matches("") || filterByUsername.startsWith(" ")) {
			for (User usr : userRepository.findAll()) {
				if (!users.contains(usr)) {
					userSource.add(addUsernameToFullName(usr.getUsername(), usr.getFirstName(), usr.getLastName()));
				}
			}
		} else {

			for (User usr : userRepository.findByUsernameContaining(filterByUsername)) {
				if (!users.contains(usr)) {
					userSource.add(addUsernameToFullName(usr.getUsername(), usr.getFirstName(), usr.getLastName()));
				}
			}

		}

		parentsToCurrentChild = new DualListModel<String>(userSource, userTarget);

		return parentsToCurrentChild;

	}

	// getters and setters
	public List<User> getChosenUsers() {
		return chosenUsers;
	}

	public List<String> getChosenUsernames() {
		return chosenUsernames;
	}

	public List<String> getUserSource() {
		return userSource;
	}

	public void setUserSource(List<String> userSource) {
		this.userSource = userSource;
	}

	public List<String> getUserTarget() {
		return userTarget;
	}

	public void setUserTarget(List<String> userTarget) {
		this.userTarget = userTarget;
	}

	public void setUsernames(DualListModel<String> usernames) {
		this.usernames = usernames;
	}

	public String getFilterByUsername() {
		return filterByUsername;
	}

	public void setFilterByUsername(String filterByUsername) {
		this.filterByUsername = filterByUsername;
	}

	/**
	 * Transforms the names of a user into one string.
	 * 
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @return returns the concatenated string
	 */
	private String addUsernameToFullName(String username, String firstName, String lastName) {
		StringBuilder finalName = new StringBuilder();
		return finalName.append(firstName).append(" ").append(lastName).append("(").append(username).append(")")
				.toString();
	}

	/**
	 * Returns the username of a transformed name string.
	 * 
	 * @param name
	 *            concatenated name
	 * @return the extracted username
	 */
	public String extractUserName(String name) {
		String username[] = name.split("\\(");
		username = username[1].split("\\)");
		return username[0];
	}

	public void setChosenUsers(List<User> chosenUsers) {
		this.chosenUsers = chosenUsers;
	}

}