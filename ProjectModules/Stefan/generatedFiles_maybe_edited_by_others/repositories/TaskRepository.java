package at.qe.sepm.skeleton.repositories;

import at.qe.sepm.skeleton.model.Task;

/**
 * Repository for managing {@link Task} entities.
 *
 * @author Steve
 */
public interface TaskRepository extends AbstractRepository<Task, Long> {

	Task findFirstByTaskID(int taskID);

	Task findFirstByName(String name);

}
