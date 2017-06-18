package at.qe.sepm.skeleton.ui.controllers;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.services.UserService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the user list view.
 *
 * @author Michael Brunner <Michael.Brunner@uibk.ac.at>, Daniel Proksch <Daniel.Proksch@student.uibk.ac.at>, Elias Jochum <elias.jochum@student.uibk.ac.at>
 */
@Component
@Scope("view")
public class UserListController {

    @Autowired
    private UserService userService;

    /**
     * Returns a list of all users.
     *
     * @return
     */
    public Collection<User> getUsers() {
        return userService.getAllUsers();
    }
    
    /**
     * Returns a list of all students.
     *
     * @return
     */
    public Collection<User> getStudents() {
        return userService.getAllStudents();
    }
    
    /**
     * Returns a collection of all users assigned to a specific project.
     *
     * @param project the project to search for
     * @return
     */
    public Collection<User> getUsersByProject(Project project) {
    	return userService.getUsersByProject(project);
    }
    
    /**
     * Returns a list of all PSLeader.
     *
     * @return
     */
    public Collection<User> getPSLeaders() {
        return userService.getAllPSLeaders();
    }

}
