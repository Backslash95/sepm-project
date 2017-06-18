package at.qe.sepm.skeleton.ui.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.WorkPackage;
import at.qe.sepm.skeleton.services.WorkPackageService;

/**
 * Controller for the work package list view.
 *
 * @author Sebastian Grabher <sebastian.grabher@student.uibk.ac.at>
 */
@Component
@Scope("view")
public class WorkPackageListController {
	
	@Autowired
	private WorkPackageService workPackageService;
	
	/**
     * Returns a list of all work packages of a specific project.
     * 
     * @param Project
     * @return
     */
	public Collection<WorkPackage> getWorkPackages(Project project) {
		return workPackageService.getWorkPackagesByProject(project);
	}
	
}
