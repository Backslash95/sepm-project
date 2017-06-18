package sepm.creche.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sepm.creche.models.Task;
import sepm.creche.models.TaskState;
import sepm.creche.models.User;

/**
 * Repository for managing {@link Task} entities. This repository implements the
 * abstract repository{@link AbstractRepository}.
 *
 * @author Stefan
 */

public interface TaskRepository extends AbstractRepository<Task, Long>
{

	/**
	 * Finds the first entry for the matching Identifier (Id of the class).
	 *
	 * @param taskID
	 *            the Identifier for the search
	 * @return the found Task
	 */
	Task findFirstByTaskID(int taskID);

	/**
	 * Finds the first entry for the matching name.
	 *
	 * @param name
	 *            the name for the search
	 * @return the found Person
	 */
	Task findFirstByName(String name);

	/**
	 * Finds all tasks containing the given taskstate and writes it into a list.
	 *
	 * @param state
	 *            the state for the search
	 * @return the list of all found Tasks
	 */
	List<Task> findByTaskState(TaskState state);

	@Query("SELECT u FROM Task u WHERE :user MEMBER OF u.assignedUsers AND u.taskState = :taskState")
	List<Task> findMyTasks(@Param("user") User user, @Param("taskState") TaskState taskState);

}
