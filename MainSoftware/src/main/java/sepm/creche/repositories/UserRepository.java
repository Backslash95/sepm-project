package sepm.creche.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sepm.creche.models.User;
import sepm.creche.models.UserRole;

/**
 * Repository for managing {@link User} entities. This repository implements the
 * abstract repository{@link AbstractRepository}.
 *
 * @author Stefan
 */
public interface UserRepository extends AbstractRepository<User, Long>
{

	/**
	 * Finds the first entry for the matching username (Id of the class).
	 *
	 * @param username
	 *            the username for the search
	 * @return the found User
	 */
	User findFirstByUsername(String username);

	/**
	 * Finds the first entry for the matching e-mail.
	 *
	 * @param email
	 *            the e-mail for the search
	 * @return the found User
	 */
	User findByEmail(String email);

	/**
	 * Finds all users containing the given string and writes it into a list.
	 *
	 * @param username
	 *            the String for the search
	 * @return the list of all found User
	 */
	List<User> findByUsernameContaining(String username);

	/**
	 * Finds all users by firstname concatenated with the lastname and writes it
	 * into a list.
	 *
	 * @param wholeName
	 *            the String for the search (contains of first + lastname)
	 * @return the list of all found User
	 */
	@Query("SELECT u FROM User u WHERE CONCAT(u.firstName, ' ', u.lastName) = :wholeName")
	List<User> findByWholeNameConcat(@Param("wholeName") String wholeName);

	/**
	 * Finds all users by the given role and writes it into a list.
	 *
	 * @param role
	 *            the UserRole for the search
	 * @return the list of all found User
	 */
	@Query("SELECT u FROM User u WHERE :role MEMBER OF u.roles")
	List<User> findByRole(@Param("role") UserRole role);

	/**
	 * Finds all users(specific: parents) which are assigned to a task and
	 * writes it into a list.
	 *
	 * @param role
	 *            the UserRole(parent) for the search
	 * @return the list of all found User (parents)
	 */
	@Query("SELECT u FROM User u JOIN u.myTasks WHERE :role MEMBER OF u.roles")
	List<User> findParentsWithTask(@Param("role") UserRole role);

	@Query("SELECT u FROM User u WHERE :actv = u.inactive")
	List<User> findInactiveUsers(@Param("actv") boolean actv);

}
