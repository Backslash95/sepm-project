package at.qe.sepm.skeleton.ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.services.ProjectService;
import at.qe.sepm.skeleton.services.UserService;
import at.qe.sepm.skeleton.ui.beans.MessageBean;

/**
 * Controller for the project detail view.
 *
 * @author Daniel Proksch <Daniel.Proksch@student.uibk.ac.at>
 */
@Component
@Scope("view")
public class ProjectDetailController {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageBean messageBean;
	
	/**
     * Attribute to cache the currently displayed user
     */
	private Project project;
	
	 /**
     * Sets the currently displayed project and reloads it form db. This project is
     * targeted by any further calls of
     * {@link #doReloadProject()}, {@link #doSaveProject()} and
     * {@link #doDeleteProject()}.
     *
     * @param project
     */
	public void setProject(Project project) {
		this.project = project;
		doReloadProject();
	}
	
	/**
     * Returns the currently displayed project.
     *
     * @return
     */
	public Project getProject() {
		return project;
	}
	
	/**
     * Action to force a reload of the currently displayed project.
     */
	public void doReloadProject() {
		//TODO change to id
		project = projectService.loadProject(project.getProjectName());
	}
	
	/**
     * Action to save the currently displayed project.
     */
	public void doSaveProject() {
		project = projectService.saveProject(project);
	}
	
	/**
     * Action to delete the currently displayed user.
     */
	public void doDeleteProject() {
		projectService.deleteProject(project);
	}
	
	/**
     * Action the currently displayed project to another psleader.
     *
     * @param psleaderUsername the username of the user to assign the project to
     * @return true if the project was successfully reassigned, else false
     */
	public boolean doReassignProjectToNewPSLeader(User psleader){
		if(psleader == null){
			return false;
		}else{
			messageBean.infoMessage("The Project was successfully reassigned to another PS-Leader.");
			return projectService.reassignProjectToNewPSLeader(project, psleader);
		}
	}
	
	/**
     * Action to assign a student to the currently displayed project.
     *
     * @param studentUsername the username of the user to assign
     * @return true if the student was successfully assigned, else false
     */
	public boolean doAssignStudentToProject(String studentUsername){
		User student = userService.loadUser(studentUsername);
		if(student == null){
			return false;
		}else{
			return projectService.assignStudentToProject(student, project);
		}
	}
	
	/**
     * Action to remove a student from the currently displayed project.
     *
     * @param studentUsername the username of the user to assign
     * @return true if the student was successfully removed, else false
     */
	public void doRemoveStudentFromProject(String studentUsername){
		User student = userService.loadUser(studentUsername);
		if(student == null){
			
		}else{
			projectService.removeStudentFromProject(student, project);
		}
	}
	
}