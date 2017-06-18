package at.qe.sepm.skeleton.stefan.repositories;

import java.util.Collection;

import at.qe.sepm.skeleton.stefan.models.Task;
import at.qe.sepm.skeleton.stefan.models.TaskState;

/**
 * Repository for managing {@link Task} entities.
 *
 * @author Steve
 */
public interface TaskRepository extends AbstractRepository<Task, Long>
{

	Task findFirstByTaskID(int taskID);

	Task findFirstByName(String name);

	Collection<Task> findByTaskState(TaskState state);

}
