package sepm.creche.ui.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.models.User;
import sepm.creche.models.UserRole;
import sepm.creche.repositories.UserRepository;
import sepm.creche.services.UserService;

/**
 * A controller to provide a "Primefaces-Picklist-Element" with the desired
 * users. The chosen users are assigned to a task. The users are loaded in
 * trough the repository and the picklist is provided with the usernames as
 * strings.
 * 
 * @author Aspir
 */
@Component
@Scope("session")
public class ParentPickListController
{
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userSerivce;

	private DualListModel<String> usernames;
	private DualListModel<String> currentUsernames;
	private List<String> userSource;
	private List<String> userTarget;
	private List<String> chosenUsernames;
	private List<User> chosenUsers;
	private User tempUser;

	public ParentPickListController()
	{
		userSource = new ArrayList<String>();
		userTarget = new ArrayList<String>();
		chosenUsernames = new ArrayList<String>();
		chosenUsers = new ArrayList<User>();
		currentUsernames = new DualListModel<String>();
	}

	@PostConstruct
	private void initialListEntries()
	{
		setCurrentUsernames(getUsernames());
	}

	/**
	 * Reset the attributes for a new call.
	 */
	public void reinit()
	{
		userSource = new ArrayList<String>();
		userTarget = new ArrayList<String>();
		chosenUsernames = new ArrayList<String>();
		chosenUsers = new ArrayList<User>();
		setCurrentUsernames(getUsernames());
	}

	/**
	 * Adds the currently selected users in the picklist to the collection
	 * choosenUsernames held by the bean.
	 */
	public void addUsers()
	{
		chosenUsernames = currentUsernames.getTarget();
		for (String usrname : chosenUsernames)
		{
			System.out.println(usrname);
			tempUser = userRepository.findFirstByUsername(extractUserName(usrname));
			if (!chosenUsers.contains(tempUser))
			{
				chosenUsers.add(tempUser);
			}
		}
	}

	/**
	 * Returns a model of the users for the picklist. DualListModel - one
	 * collection for source usernames and one collection for target usernames
	 * 
	 * @return returns the DualListModel for the Picklist
	 */
	public DualListModel<String> getUsernames()
	{
		userSource.clear();
		List<User> users = new ArrayList<User>();
		users.addAll(userRepository.findByRole(UserRole.PARENT));
		users.removeAll(userSerivce.inActiveUsers());

		for (String usrname : chosenUsernames)
		{
			if (!userTarget.contains(usrname))
			{
				userTarget.add(usrname);
			}
		}
		for (User usr : users)
		{
			if (usr.isEnabled() && (!userSource.contains(usr.getUsername()))
					&& (!userTarget.contains(usr.getUsername())))
			{
				userSource.add(addUsernameToFullName(usr.getUsername(), usr.getFirstName(), usr.getLastName()));
			}
		}
		usernames = new DualListModel<String>(userSource, userTarget);
		return usernames;
	}

	/**
	 * This method is used to control the view of the displayed elements. If the
	 * maximum amount of elements are selected for the target list, then the
	 * source is cleared. Anytime there is place for one more element in the
	 * target list, all elements in the source are displayed again.
	 * 
	 * @return returns the DualListModel for the Picklist
	 */
	public void showTargetList(int maxElements)
	{
		if (currentUsernames.getTarget().size() == maxElements)
		{
			currentUsernames.getSource().clear();

		} else if (currentUsernames.getTarget().size() > maxElements)
		{
			currentUsernames.getSource().clear();
			List<String> limitedUsers = new ArrayList<String>();
			for (int i = 0; i < maxElements; i++)
			{
				limitedUsers.add(currentUsernames.getTarget().get(i));
			}
			currentUsernames.getTarget().clear();
			currentUsernames.getTarget().addAll(limitedUsers);
		} else
		{
			resetCurrentUsers();
		}
	}

	public DualListModel<String> getCurrentUsernames()
	{
		return currentUsernames;
	}

	public void setCurrentUsernames(DualListModel<String> currentUsernames)
	{
		this.currentUsernames = currentUsernames;
	}

	/**
	 * Transforms the names of a user into one string.
	 * 
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @return returns the concatenated string
	 */
	private String addUsernameToFullName(String username, String firstName, String lastName)
	{
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
	private String extractUserName(String name)
	{
		String username[] = name.split("\\(");
		username = username[1].split("\\)");
		return username[0];
	}

	public void resetCurrentUsers()
	{
		currentUsernames.setSource(getUsernames().getSource());
		currentUsernames.getSource().removeAll(currentUsernames.getTarget());
	}

	public List<User> getChosenUsers()
	{
		return chosenUsers;
	}

	public List<String> getChosenUsernames()
	{
		return chosenUsernames;
	}

	public List<String> getUserSource()
	{
		return userSource;
	}

	public void setUserSource(List<String> userSource)
	{
		this.userSource = userSource;
	}

	public List<String> getUserTarget()
	{
		return userTarget;
	}

	public void setUserTarget(List<String> userTarget)
	{
		this.userTarget = userTarget;
	}

	public void setUsernames(DualListModel<String> usernames)
	{
		this.usernames = usernames;
	}

}