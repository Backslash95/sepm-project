package at.qe.sepm.skeleton.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.WorkPackage;

/**
 * Repository for managing {@link WorkPackage} entities.
 *
 * @author Daniel Proksch <Daniel.Proksch@student.uibk.ac.at>
 */
public interface WorkPackageRepository extends AbstractRepository<WorkPackage, Long> {
	
	List<WorkPackage> findByProject(Project project);
	
	@Query("SELECT wp FROM WorkPackage wp WHERE wp.project = :project AND wp.packageName = :packageName")
	WorkPackage findByPackageNameInProject(@Param("project") Project project, @Param("packageName") String packageName);
	
}
