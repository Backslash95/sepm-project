package at.qe.sepm.skeleton.services;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.model.UserRole;
import at.qe.sepm.skeleton.model.WorkPackage;
import at.qe.sepm.skeleton.repositories.UserRepository;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Service for accessing and manipulating user data.
 *
 * @author Michael Brunner <Michael.Brunner@uibk.ac.at>, Daniel Proksch <Daniel.Proksch@student.uibk.ac.at>, 
 * Andre Potocnik <andre.potocnik@student.uibk.ac.at>, Sebastian Grabher <sebastian.grabher@student.uibk.ac.at>,
 * Elias Jochum <elias.jochum@student.uibk.ac.at>
 */
@Component
@Scope("application")
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserService.class);

    /**
     * Returns a collection of all users.
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Returns a collection of all users with the role student.
     *
     * @return
     */
    public Collection<User> getAllStudents(){
    	return getUsersByRole(UserRole.STUDENT);
    }
    
    /**
     * Returns a collection of all users with the role psleader.
     *
     * @return
     */
    //@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PSLEADER')")
    public Collection<User> getAllPSLeaders(){
    	return getUsersByRole(UserRole.PSLEADER);
    }
    
    /**
     * Returns a collection of all users with the role admin.
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public Collection<User> getAllAdmins(){
    	return getUsersByRole(UserRole.ADMIN);
    }
    
    /**
     * Loads a collection of all users assigned to a specific project.
     *
     * @param project the project to search for
     * @return a collection of all users assigned to the project
     */
    //@PreAuthorize("hasAuthority('ADMIN') or principal eq #project.psleader or principal.project eq #project")
    public Collection<User> getUsersByProject(Project project){
    	return userRepository.findByProject(project);
    }

    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PSLEADER') or principal.username eq #username or hasAuthority('STUDENT')")
    public User loadUser(@Param("username") String username) {
        return userRepository.findFirstByUsername(username);
    }

    /**
     * Saves the user. This method will also set {@link User#createDate} for new
     * entities or {@link User#updateDate} for updated entities. The user
     * requesting this operation will also be stored as {@link User#createUser}
     * or {@link User#updateUser} respectively.
     *
     * @param user the user to save
     * @return the updated user
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PSLEADER') or hasAuthority('STUDENT')")
    public User saveUser(User user) {
        if (user.isNew()) {
            user.setCreateDate(new Date());
            user.setCreateUser(getAuthenticatedUser());
        } else {
            user.setUpdateDate(new Date());
            user.setUpdateUser(getAuthenticatedUser());
        }
        return userRepository.save(user);
    }

    /**
     * Deletes the user.
     * Logs a warning in UserService.log
     * @param user the user to delete
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(User user) {
        userRepository.delete(user);
        logger.warn("DELETED User: " + user.getUsername() + " " +
        		user.getFirstName() + " " + user.getLastName());
    }
    
    /**
     * Create a new user.
     *
     * @param user details
     * @return saves the new user
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public User createUser(String username, String firstname, String lastname, String password) {
    	User user = new User();
    	
    	user.setUsername(username);
    	user.setFirstName(firstname);
    	user.setLastName(lastname);
    	user.setPassword(new BCryptPasswordEncoder().encode(password));
    	user.setCreateDate(new Date());
    	user.setCreateUser(getAuthenticatedUser());
    	return userRepository.save(user);    	
    }
    
    private Collection<User> getUsersByRole(UserRole role){
    	try{
    		return userRepository.findByRole(role);
    	}catch(NullPointerException e){
    		System.err.println("Hallo");
    	}
    	return null;
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }
    
    public Collection<User> getUsersByWorkpackage(WorkPackage workPackage){
    	return userRepository.findByWorkPackage(workPackage);
    }
 }
