package at.qe.sepm.skeleton.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.model.WorkPackage;
import at.qe.sepm.skeleton.model.WorksOn;

/**
 * Repository for managing {@link WorksOn} entities.
 *
 * @author Daniel Proksch <Daniel.Proksch@student.uibk.ac.at>, Elias Jochum <elias.jochum@student.uibk.ac.at>, Sebastian Grabher <sebastian.grabher@student.uibk.ac.at>
 */
public interface WorksOnRepository extends AbstractRepository<WorksOn, Long> {
	
	List<WorksOn> findByProject(Project project);
	
	List<WorksOn> findByWorkPackage(WorkPackage workPackage);
	
	List<WorksOn> findByStudent(User student);
	
	List<WorksOn> findByStudentAndProject(User student, Project project);
	
	List<WorksOn> findByStudentAndWorkPackage(User student, WorkPackage workPackage);
	
	@Query("SELECT wo FROM WorksOn wo WHERE wo.project = :project AND wo.startDate >= :earliestStartDate")
	List<WorksOn> findByProjectAndEarliestStartDate(@Param("project") Project project, @Param("earliestStartDate") Date earliestStartDate);
	
	@Query("SELECT wo FROM WorksOn wo WHERE wo.project = :project AND wo.student = :student AND wo.startDate >= :earliestStartDate")
	List<WorksOn> findByProjectAndStudentAndEarliestStartDate(@Param("project") Project project, @Param("student") User student, @Param("earliestStartDate") Date earliestStartDate);
}
