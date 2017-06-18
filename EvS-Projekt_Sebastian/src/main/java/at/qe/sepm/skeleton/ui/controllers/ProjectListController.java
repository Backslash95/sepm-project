package at.qe.sepm.skeleton.ui.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.services.ProjectService;


/**
 * Controller for the project list view.
 *
 * @author Andre Potocnik <andre.potocnik@student.uibk.ac.at>, Daniel Proksch <Daniel.Proksch@student.uibk.ac.at>
 */
@Component
@Scope("view")
public class ProjectListController {
	
    @Autowired
    private ProjectService projectService;

    /**
     * Returns a list of all projects.
     *
     * @return
     */
    public Collection<Project> getProjects() {
        return projectService.getAllProjects();
    }
    
    /**
     * Returns a collection of all projects being supervised by the psleader.
     *
     * @param psleader the user supervising the projects
     * @return
     */
    public Collection<Project> getProjectsByPSLeader(User psleader) {
    	return projectService.getProjectsByPSLeader(psleader);
    }
}