package at.qe.sepm.skeleton.ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.model.WorkPackage;
import at.qe.sepm.skeleton.services.WorkPackageService;

/**
 * Controller for the project detail view.
 *
 * @author Sebastian Grabher <sebastian.grabher@student.uibk.ac.at>
 */
@Component
@Scope("view")
public class WorkPackageDetailController {
	
	@Autowired
	private WorkPackageService workPackageService;
	
	/**
     * Attribute to cache the currently displayed work package
     */
	private WorkPackage workPackage;

	/**
     * Sets the currently displayed work package and reloads it form db. This work package is
     * targeted by any further calls of
     * {@link #doReloadWorkPackage()} and {@link #doSaveWorkPackage()}.
     *
     * @param WorkPackage
     */
	public void setWorkPackage(WorkPackage workPackage) {
		this.workPackage = workPackage;
		doReloadWorkPackage();
	}
	
	/**
     * Returns the currently displayed work package.
     *
     * @return 
     */
	public WorkPackage getWorkPackage() {
		return workPackage;
	}
	
	/**
     * Reloads the current work package
     */
	public void doReloadWorkPackage() {
		workPackage = workPackageService.loadWorkPackage(workPackage.getProject(), workPackage.getPackageName());
	}
	
	/**
     * Saves the current work package
     */
	public void doSaveWorkPackage() {
		workPackage = workPackageService.saveWorkPackage(workPackage);
	}
}
