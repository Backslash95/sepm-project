package at.qe.sepm.skeleton.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.model.UserRole;

/**
 * Repository for managing {@link User} entities.
 *
 * @author Steve
 */
public interface UserRepository extends AbstractRepository<User, Long> {

	//User findFirstByUserID(int userID);

    User findFirstByUsername(String username);

    List<User> findByUsernameContaining(String username);

    @Query("SELECT u FROM User u WHERE CONCAT(u.firstName, ' ', u.lastName) = :wholeName")
    List<User> findByWholeNameConcat(@Param("wholeName") String wholeName);

    @Query("SELECT u FROM User u WHERE :role MEMBER OF u.roles")
    List<User> findByRole(@Param("role") UserRole role);

}
