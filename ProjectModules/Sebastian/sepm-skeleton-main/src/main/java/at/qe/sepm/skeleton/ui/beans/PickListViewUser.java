package at.qe.sepm.skeleton.ui.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import at.qe.sepm.skeleton.stefan.models.User;
import at.qe.sepm.skeleton.stefan.repositories.UserRepository;

/**
 * 
 * Controller for the PickList
 * 
 * @author Steve
 * 
 */

@Component
@Scope("session")
public class PickListViewUser {

	@Autowired
	private UserRepository userRepository;

	private DualListModel<String> usernames;
	private DualListModel<String> parentsToCurrentChild;
	private List<String> userSource;
	private List<String> userTarget;
	private List<String> choosenUsernames;
	private List<User> choosenUsers;
	private User tempUser;
	private String filterByUsername;

	public PickListViewUser() {

		userSource = new ArrayList<String>();
		userTarget = new ArrayList<String>();
		choosenUsernames = new ArrayList<String>();
		choosenUsers = new ArrayList<User>();
		filterByUsername = "";
	}

	public void reinit() {

		userSource = new ArrayList<String>();
		userTarget = new ArrayList<String>();
		choosenUsernames = new ArrayList<String>();
		choosenUsers = new ArrayList<User>();
		filterByUsername = "";
	}

	public void popUp(ActionEvent actionEvent) {
		addMessage("Assigned choosen parents to child!");
	}

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addUsers() {

		choosenUsernames = usernames.getTarget();

		for (String usrname : choosenUsernames) {
			tempUser = userRepository.findFirstByUsername(usrname);
			if (!choosenUsers.contains(tempUser)) {
				choosenUsers.add(tempUser);
			}
		}
	}

	public void addNewUsers() {

		choosenUsernames = parentsToCurrentChild.getTarget();

		for (String usrname : choosenUsernames) {
			tempUser = userRepository.findFirstByUsername(usrname);
			if (!choosenUsers.contains(tempUser)) {
				choosenUsers.add(tempUser);
			}
		}
	}

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

		for (String usrname : choosenUsernames) {
			if (!userTarget.contains(usrname)) {
				userTarget.add(usrname);
			}
		}

		for (User usr : users) {
			if (usr.isEnabled() && (!userSource.contains(usr.getUsername()))
					&& (!userTarget.contains(usr.getUsername()))) {
				userSource.add(usr.getUsername());
			}
		}
		usernames = new DualListModel<String>(userSource, userTarget);

		return usernames;
	}

	public void setParentsToCurrentChild(DualListModel<String> parentsToCurrentChild) {
		this.parentsToCurrentChild = parentsToCurrentChild;
	}

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public DualListModel<String> getParentsToCurrentChild() {
		userSource.clear();
		Collection<User> users = new ArrayList<User>();

		users.addAll(userRepository.findAll());

		if (filterByUsername.matches("") || filterByUsername.startsWith(" ")) {
			for (User usr : userRepository.findAll()) {
				if (!users.contains(usr)) {
					userSource.add(usr.getUsername());
				}
			}
		} else {

			for (User usr : userRepository.findByUsernameContaining(filterByUsername)) {
				if (!users.contains(usr)) {
					userSource.add(usr.getUsername());
				}
			}

		}

		parentsToCurrentChild = new DualListModel<String>(userSource, userTarget);

		return parentsToCurrentChild;

	}

	public List<User> getChoosenUsers() {
		return choosenUsers;
	}

	public List<String> getChoosenUsernames() {
		return choosenUsernames;
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

}