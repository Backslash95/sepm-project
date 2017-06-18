package sepm.creche.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.models.Task;
import sepm.creche.models.TaskState;
import sepm.creche.repositories.TaskRepository;
import sepm.creche.ui.beans.SessionInfoBean;

@Component
@Scope("application")
public class TaskService
{

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	SessionInfoBean sessionInfoBean;

	@Autowired
	UserService userService;

	public Collection<Task> findMyTasks()
	{
		return taskRepository.findMyTasks(userService.loadUser(sessionInfoBean.getCurrentUserName()),
				TaskState.ASSIGNED);

	}

	public Collection<Task> getAllTasks()
	{
		List<Task> tasks = new ArrayList<Task>();
		tasks.addAll(taskRepository.findAll());
		return tasks;

	}

}
