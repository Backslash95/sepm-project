package at.qe.sepm.skeleton.repositories;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.model.UserRole;
import at.qe.sepm.skeleton.model.WorkPackage;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for managing {@link User} entities.
 *
 * @author Michael Brunner <Michael.Brunner@uibk.ac.at>, Daniel Proksch <Daniel.Proksch@student.uibk.ac.at>, Elias Jochum <elias.jochum@student.uibk.ac.at>
 */
public interface UserRepository extends AbstractRepository<User, Long> {

    User findFirstByUsername(String username);

    List<User> findByUsernameContaining(String username);
    
    List<User> findByProject(Project project);

    @Query("SELECT u FROM User u WHERE CONCAT(u.firstName, ' ', u.lastName) = :wholeName")
    List<User> findByWholeNameConcat(@Param("wholeName") String wholeName);

    @Query("SELECT u FROM User u WHERE :role MEMBER OF u.roles")
    List<User> findByRole(@Param("role") UserRole role);
    
    @Query("SELECT DISTINCT u.student FROM WorksOn u WHERE u.workPackage = :workPackage")
    List<User> findByWorkPackage(@Param("workPackage") WorkPackage workPackage);
}
