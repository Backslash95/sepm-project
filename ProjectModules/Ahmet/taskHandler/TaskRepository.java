package at.qe.sepm.skeleton.taskHandler;

import at.qe.sepm.skeleton.repositories.AbstractRepository;

public interface TaskRepository extends AbstractRepository<Task, Long>
{

	Task findFirstByTaskID(int taskID);
}
