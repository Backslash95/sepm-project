package at.qe.sepm.skeleton.services;

import java.util.Date;
import java.util.List;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.model.UserRole;
import at.qe.sepm.skeleton.model.WorkPackage;
import at.qe.sepm.skeleton.model.WorksOn;
import at.qe.sepm.skeleton.repositories.ProjectRepository;
import at.qe.sepm.skeleton.repositories.UserRepository;
import at.qe.sepm.skeleton.repositories.WorkPackageRepository;
import at.qe.sepm.skeleton.repositories.WorksOnRepository;

/**
 * Service for accessing and manipulating project data.
 *
 * @author Daniel Proksch <Daniel.Proksch@student.uibk.ac.at>, Elias Jochum <elias.jochum@student.uibk.ac.at>, Sebastian Grabher <sebastian.grabher@student.uibk.ac.at>
 */
@Component
@Scope("application")
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WorkPackageRepository workPackageRepository;

	@Autowired
	private WorksOnRepository worksOnRepository;

	/**
	 * Returns a collection of all project.
	 *
	 * @return
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	public Collection<Project> getAllProjects() {
		return projectRepository.findAll();
	}

	/**
	 * Loads a collection of all projects being supervised by a specific
	 * psleader.
	 *
	 * @param psleader
	 *            the user supervising the projects
	 * @return a collection of all projects supervised by the psleader
	 */
	//@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PSLEADER')")
	public Collection<Project> getProjectsByPSLeader(User psleader) {
		return projectRepository.findByPsleader(psleader);
	}

	/**
	 * Loads a single project identified by its title.
	 *
	 * @param projectName
	 *            the title to search for
	 * @return the project with the given title
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PSLEADER')")
	public Project loadProject(String projectName) {
		// TODO changeProject
		return projectRepository.findFirstByProjectName(projectName);
	}

	/**
	 * Saves the project. This method will also set {@link Project#createDate}
	 * for new entities.
	 *
	 * @param project
	 *            the project to save
	 * @return the updated project
	 */
	public Project saveProject(Project project) {
		if (project.isNew()) {
			project.setCreateDate(new Date());
		} else {
			project.setUpdateDate(new Date());
		}
		return projectRepository.save(project);
	}

	/**
	 * Deletes the project and removes the project from all users assigned to
	 * it. Also deletes all work packages and statistics belonging to the
	 * project.
	 * 
	 * @param user
	 *            the project to delete
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PSLEADER')")
	public void deleteProject(Project project) {
		for (User student : userRepository.findByProject(project)) {
			student.setProject(null);
			userRepository.save(student);
		}

		for (WorkPackage workPackage : workPackageRepository.findByProject(project)) {
			for (WorksOn worksOn : worksOnRepository.findByWorkPackage(workPackage)) {
				worksOnRepository.delete(worksOn);
			}
			workPackageRepository.delete(workPackage);
		}
		projectRepository.delete(project);
		// TODO: logging mechanic for project deleting
	}

	/**
	 * Create a new project.
	 *
	 * @param psleader
	 *            the psleader or admin creating and supervising the project
	 * @param projectName
	 *            the title of the new project
	 * @return saves the new project
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PSLEADER')")
	public Project createProject(User psleader, String projectName) {
		Project project = new Project();
		project.setPsleader(psleader);
		project.setProjectName(projectName);
		project.setCreateDate(new Date());
		return projectRepository.save(project);
	}

	/**
	 * Reassigns the project to another psleader.
	 *
	 * @param project
	 *            the project to reassign
	 * @param psleader
	 *            the user to assign the project to
	 * @return true if the project was successfully reassigned, else false
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PSLEADER') or principal eq #project.psleader")
	public boolean reassignProjectToNewPSLeader(Project project, User psleader) {
		if (psleader.getRoles().contains(UserRole.PSLEADER)) {
			project.setPsleader(psleader);
			saveProject(project);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Assigns a student to a project.
	 *
	 * @param student
	 *            the user to assign
	 * @param project
	 *            the project to assign the student to
	 * @return true if the student was successfully assigned, else false
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PSLEADER') ")
	public boolean assignStudentToProject(User student, Project project) {
		if (student.getProject() == null && student.getRoles().contains(UserRole.STUDENT)) {
			student.setProject(project);
			userRepository.save(student);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes a student from a specific project. If the student is not assigned
	 * to the project, it will stay unaltered.
	 *
	 * @param student
	 *            to remove
	 * @param project
	 *            the project the student is currently assigned to
	 * @return false if the student is not assigned to the project and thus was
	 *         not removed from it, else true
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PSLEADER')")
	public boolean removeStudentFromProject(User student, @Param("project") Project project) {
		if (student.getProject().getId() == project.getId()) {
			student.setProject(null);
			userRepository.save(student);
			return true;
		} else {
			return false;
		}
	}

	/**
     * Get hours of work from the last week
     * 
     * @param project
     * @return hours of work on workpackages
     * 
     */ 
	public int getMinutesByProject (Project project) {
		Date date = new Date();
		long earliestDate = date.getTime() - 604800000;
		date = new Date(earliestDate);
		List<WorksOn> worksOnProject = worksOnRepository.findByProjectAndEarliestStartDate(project, date);
		long result = 0;
		for(WorksOn worksOn : worksOnProject) {
			Date startDate = worksOn.getStartDate();
			Date endDate = worksOn.getEndDate();
			if(startDate != null && endDate != null) {
				result = result + (endDate.getTime() - startDate.getTime());
			}
		}
		return (int) result/60000 -1;
	}
	
	/**
	     * Get hours of work from the last week by Student
	     * 
	     * @param project and student
	     * @return hours of work on workpackages by student
	     * 
	     */ 
	public int getMinutesByProjectAndStudent (Project project, User student) {
		Date date = new Date();
		long earliestDate = date.getTime() - 604800000;
		date = new Date(earliestDate);
		List<WorksOn> studentWorksOnProject = worksOnRepository.findByProjectAndStudentAndEarliestStartDate(project, student ,date);
		long result = 0;
		for(WorksOn worksOn : studentWorksOnProject) {
			Date startDate = worksOn.getStartDate();
			Date endDate = worksOn.getEndDate();
			if(startDate != null && endDate != null) {
				result = result + (endDate.getTime() - startDate.getTime());
			}
		}
		return (int) result/60000;
	}
}
