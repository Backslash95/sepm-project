package sepm.creche.services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sepm.creche.logger.AuditLog;
import sepm.creche.models.Task;
import sepm.creche.models.TaskState;
import sepm.creche.models.User;
import sepm.creche.models.UserRole;
import sepm.creche.repositories.TaskRepository;
import sepm.creche.repositories.UserRepository;
import sepm.creche.utils.CodeGenerator;

/**
 * Service for accessing and manipulating user data.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Softwaredevelopment and Project Management" offered by the University
 * of Innsbruck.
 */
@Component
@Scope("application")
public class UserService
{
	@Autowired
	private AuditLog auditLog;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TaskRepository taskRepository;

	private CodeGenerator codeGenerator;

	public UserService()
	{
		codeGenerator = new CodeGenerator();
	}

	/**
	 * Returns a collection of all users.
	 *
	 * @return
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	public Collection<User> getAllUsers()
	{
		return userRepository.findAll();
	}

	public User getUser()
	{
		// return userRepository.findAll();
		return userRepository.findFirstByUsername("user3");
	}

	/**
	 * Loads a single user identified by its username.
	 *
	 * @param username
	 *            the username to search for
	 * @return the user with the given username
	 */
	@PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username")
	public User loadUser(String username)
	{
		return userRepository.findFirstByUsername(username);
	}

	/**
	 * Saves the user. This method will also set {@link User#createDate} for new
	 * entities or {@link User#updateDate} for updated entities. The user
	 * requesting this operation will also be stored as {@link User#createDate}
	 * or {@link User#updateUser} respectively.
	 *
	 * @param user
	 *            the user to save
	 * @return the updated user
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	public User saveUser(User user)
	{
		if (user.isNew())
		{
			user.setCreateDate(new Date());
			user.setCreateUser(getAuthenticatedUser());
		} else
		{
			user.setUpdateDate(new Date());
			user.setUpdateUser(getAuthenticatedUser());
		}
		return userRepository.save(user);
	}

	/**
	 * Deletes the user.
	 *
	 * @param user
	 *            the user to delete
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteUser(User user)
	{
		userRepository.delete(user);
		// :TODO: write some audit log stating who and when this user was
		// permanently deleted.
	}

	private User getAuthenticatedUser()
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userRepository.findFirstByUsername(auth.getName());
	}

	public boolean userExists(User user)
	{
		return userRepository.findFirstByUsername(user.getUsername()) != null;
	}

	@PreAuthorize("hasAuthority('ADMIN') and principal.username eq 'admin'")
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public User createEmployee(String username, String firstname, String lastname, String password)
	{
		String newUsername;
		User user = new User();

		PasswordEncoder encoder = new StandardPasswordEncoder();
		user.setUsername(username);
		user.setFirstName(firstname);
		user.setLastName(lastname);
		user.setPassword(encoder.encode(password));
		user.setCreateDate(new Date());
		user.setCreateUser(getAuthenticatedUser());
		user.setEnabled(true);

		Set<UserRole> newset = new HashSet<UserRole>();
		newset.add(UserRole.EMPLOYEE);
		user.setRoles(newset);
		auditLog.log("New Employee \"" + username + "\" CREATED");

		if (userRepository.findFirstByUsername(username) == null)
		{
			return userRepository.save(user);
		} else
		{
			while (true)
			{
				newUsername = username + codeGenerator.generateCode();
				if (userRepository.findFirstByUsername(newUsername) == null)
					break;
			}

			user.setUsername(newUsername);

			return userRepository.save(user);
		}

	}

	/**
	 * Loads every parent without a task.
	 * 
	 * @return Collection<User>
	 */
	public Collection<User> parentsWithoutTask()
	{
		Collection<User> parents = new HashSet<User>();
		parents.addAll(userRepository.findByRole(UserRole.PARENT));

		Collection<User> parentsWithTask = new HashSet<User>();
		List<Task> openTasks = taskRepository.findByTaskState(TaskState.ASSIGNED);
		for (Task t : openTasks)
		{
			parentsWithTask.addAll(t.getAssignedUsers());
		}
		parents.removeAll(parentsWithTask);
		parents.removeAll(inActiveUsers());
		return parents;
	}

	public Collection<User> inActiveUsers()
	{
		return userRepository.findInactiveUsers(true);
	}

}
