package at.qe.sepm.skeleton.services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.model.WorkPackage;
import at.qe.sepm.skeleton.repositories.WorkPackageRepository;

/**
 * Service for accessing and manipulating work package data.
 *
 * @author Daniel Proksch <Daniel.Proksch@student.uibk.ac.at>, Sebastian Grabher <sebastian.grabher@student.uibk.ac.at>
 */
@Component
@Scope("application")
public class WorkPackageService {

	@Autowired
	private WorkPackageRepository workPackageRepository;

	/**
     * Loads a collection of all work packages of a specific project.
     *
     * @param project the project involving the work packages
     * @return a collection of all work packages of the project
     */
	public Collection<WorkPackage> getWorkPackagesByProject(Project project) {
		return workPackageRepository.findByProject(project);
	}
	
	/**
     * Loads a single project identified by its project and title.
     *
     * @param projectName the title to search for
     * @return the project with the given title
     */
	public WorkPackage loadWorkPackage(Project project, String packageName){
		return workPackageRepository.findByPackageNameInProject(project, packageName);
	}
	
	/**
     * Saves the workpackage. This method will also set {@link WorkPackage#createDate} for new
     * entities.
     *
     * @param workPackage the work package to save
     * @return the updated work package
     */
	public WorkPackage saveWorkPackage(WorkPackage workPackage){
		if(workPackage.isNew()){
			workPackage.setCreateDate(new Date());
		}
		return workPackageRepository.save(workPackage);
	}
	
	/**
     * Creates a new work package.
     *
     * @param project the project the new work package will be assigned to
     * @param creator the user creating creating the work package
     * @param packageName the title of the new package
     * @return saves the new work package or returns an already created work package with the same title
     * and project
     */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STUDENT') or principal.project eq #project or principal eq #project.psleader")
	public WorkPackage createWorkPackage(@Param("project") Project project, User creator, String packageName){
		WorkPackage test = workPackageRepository.findByPackageNameInProject(project, packageName);
		if(test != null){
			return test;
		}else{
			WorkPackage workPackage = new WorkPackage();
			workPackage.setCreateUser(creator);
			workPackage.setPackageName(packageName);
			workPackage.setProject(project);
			return saveWorkPackage(workPackage);
		}
	}
}
