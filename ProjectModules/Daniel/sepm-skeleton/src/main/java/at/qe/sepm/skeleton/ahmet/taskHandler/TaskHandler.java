package at.qe.sepm.skeleton.ahmet.taskHandler;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import at.qe.sepm.skeleton.stefan.models.Task;
import at.qe.sepm.skeleton.stefan.models.TaskState;
import at.qe.sepm.skeleton.stefan.models.User;
import at.qe.sepm.skeleton.stefan.repositories.TaskRepository;
import at.qe.sepm.skeleton.stefan.repositories.UserRepository;

@Component
@Scope("session")
public class TaskHandler
{
	public TaskHandler()
	{
		dialogEnabled = false;
		dialogEnabled2 = false;
		newTask = new Task();
		newTask.setAmountOfWorkers(1);

	}

	@Autowired
	private PickListViewParents pickListViewParents;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private UserRepository userRepository;

	private Task newTask;
	private boolean dialogEnabled;
	private boolean dialogEnabled2;
	private Task selectedTask;

	public Task getSelectedTask()
	{
		return selectedTask;
	}

	public void setSelectedTask(Task selectedTask)
	{
		this.selectedTask = selectedTask;
	}

	public void createNewTask()
	{
		dialogEnabled = true;
	}

	public void assignTask(int taskID)
	{
		selectedTask = taskRepository.findFirstByTaskID(taskID);
		dialogEnabled2 = true;

	}

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void finishAssignment()
	{
		pickListViewParents.addUsers();
		selectedTask.setUserSetTask(new HashSet<User>(pickListViewParents.getChoosenUsers()));

		for (User u : selectedTask.getUserSetTask())
		{
			Set<Task> currentSet = new HashSet<Task>();
			currentSet.addAll(u.getTaskSet());
			currentSet.add(selectedTask);
			u.setTaskSet(currentSet);
			userRepository.save(u);
		}

		selectedTask.setTaskState(TaskState.ASSIGNED);
		taskRepository.save(selectedTask);
		pickListViewParents.reinit();
		windowClosed();

	}

	public void saveNewTask()
	{
		if (newTask.getName() == null || newTask.getName().length() == 0 || newTask.getDeadline() == null
				|| newTask.getAmountOfWorkers() <= 0 || newTask.getDeadline().before(new Date()))
		{
			popUpMsg(
					" Aufgabenname ist Pflicht!\n Datum darf nicht vor dem Jetzigen sein!\n Anzahl der Personen darf nicht kleinergleich 0 sein!");
			return;
		}
		newTask.setTaskState(TaskState.OPEN);
		taskRepository.save(newTask);
		newTask = new Task();
		newTask.setAmountOfWorkers(1);
		windowClosed();

	}

	public boolean isDialogEnabled2()
	{
		return dialogEnabled2;
	}

	public void setDialogEnabled2(boolean dialogEnabled2)
	{
		this.dialogEnabled2 = dialogEnabled2;
	}

	public static void popUpMsg(String msg)
	{
		if (FacesContext.getCurrentInstance() != null)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
		}
	}

	public void windowClosed()
	{
		dialogEnabled = false;
		dialogEnabled2 = false;
	}

	public Task getNewTask()
	{
		return newTask;
	}

	public void setNewTask(Task newTask)
	{
		this.newTask = newTask;
	}

	public boolean isDialogEnabled()
	{
		return dialogEnabled;
	}

	public void setDialogEnabled(boolean dialogEnabled)
	{
		this.dialogEnabled = dialogEnabled;
	}

	public Collection<Task> getOpenTasks()
	{
		return taskRepository.findByTaskState(TaskState.OPEN);
	}

}
