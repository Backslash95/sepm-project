package at.qe.sepm.skeleton.repositories;

import java.util.List;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.User;

/**
 * Repository for managing {@link Project} entities.
 *
 * @author Daniel Proksch <Daniel.Proksch@student.uibk.ac.at>
 */
public interface ProjectRepository extends AbstractRepository<Project, Long> {
	
	Project findFirstByProjectName(String projectName);
	
	List<Project> findByProjectNameContaining(String projectName);
	
	Project findFirstById(Long id);
	
	List<Project> findByPsleader(User psleader);
	
}
