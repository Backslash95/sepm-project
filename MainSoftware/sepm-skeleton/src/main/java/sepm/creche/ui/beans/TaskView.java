package sepm.creche.ui.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.models.Task;
import sepm.creche.services.TaskService;

@Component
@Scope("view")
public class TaskView implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	TaskService taskService;

	private List<Task> tasks;

	public TaskView()
	{
		setTasks(new ArrayList<Task>());
	}

	public List<Task> getTasks()
	{
		return tasks;
	}

	public void loadTasks()
	{
		tasks.clear();
		tasks.addAll(taskService.getAllTasks());

	}

	public void setTasks(List<Task> tasks)
	{
		this.tasks = tasks;
	}

}
