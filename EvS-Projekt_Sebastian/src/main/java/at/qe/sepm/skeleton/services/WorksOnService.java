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
import at.qe.sepm.skeleton.model.WorksOn;
import at.qe.sepm.skeleton.repositories.UserRepository;
import at.qe.sepm.skeleton.repositories.WorksOnRepository;
import at.qe.sepm.skeleton.ui.beans.SessionInfoBean;
import at.qe.sepm.skeleton.ui.controllers.UserDetailController;

/**
 * Service for accessing and manipulating the project statistics.
 *
 * @author Daniel Proksch <Daniel.Proksch@student.uibk.ac.at>, Sebastian Grabher <sebastian.grabher@student.uibk.ac.at>
 */
@Component
@Scope("application")
public class WorksOnService {

	@Autowired
	private WorksOnRepository worksOnRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserDetailController userDetailController;
	
	@PreAuthorize("hasAuthority('STUDENT') or principal eq #student")
	public WorksOn startSession(@Param("student") User student, WorkPackage workPackage){
		WorksOn session = new WorksOn();
		session.setStudent(student);
		session.setStartDate(new Date());
		session.setWorkPackage(workPackage);
		session.setProject(workPackage.getProject());
		session = worksOnRepository.save(session);
		student.setSession(session);
		userRepository.save(student);
		return session;
	}
	
	@PreAuthorize("hasAuthority('STUDENT') or principal eq #session.student")
	public void endSession(@Param("session") WorksOn session){
		userDetailController.setUser(session.getStudent());
		if(session.getEndDate() == null){
			session.setEndDate(new Date());
			worksOnRepository.save(session);
			userDetailController.getUser().setSession(null);
			userDetailController.doSaveUser();
		}
	}
	
	/**
     * Loads a collection of all saved check ins of a specific project.
     *
     * @param project the project to search for
     * @return a collection of all saved check ins for the project
     */
	public Collection<WorksOn> getStatisticsByProject(Project project){
		return worksOnRepository.findByProject(project);
	}
	
	/**
     * Loads a collection of all saved check ins of a specific user.
     *
     * @param user the user to search for
     * @return a collection of all saved check ins for the user
     */
	public Collection<WorksOn> getStatisticsByUser(User user){
	    	return worksOnRepository.findByStudent(user);
	}
	
	/**
     * Loads a collection of all saved check ins for a specific work package.
     *
     * @param workPackage the work packages to search for
     * @return a collection of all saved check ins of the work package
     */
	public Collection<WorksOn> getStatisticsByWorkPackage(WorkPackage workPackage) {
		return worksOnRepository.findByWorkPackage(workPackage);
	} 
	
	/**
     * Loads a collection of all saved check ins of a user in a specific project.
     *
     * @param user the user to search for
     * @param project the project of the check ins you want to get
     * @return a collection of all saved check ins of the user in the project
     */
	public Collection<WorksOn> getStatisticsByUserAndProject(User user, Project project){
		return worksOnRepository.findByStudentAndProject(user, project);
	}
	
	/**
     * Loads a collection of all saved check ins of a user in a specific work package.
     *
     * @param user the user to search for
     * @param workPackage the work package to search for
     * @return a collection of all saved check ins of the user in the work package
     */
	public Collection<WorksOn> getStatisticsByUserAndWorkPackage(User user, WorkPackage workPackage){
		return worksOnRepository.findByStudentAndWorkPackage(user, workPackage);
	}
	
	
}
