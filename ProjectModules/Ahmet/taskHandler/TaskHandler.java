package at.qe.sepm.skeleton.taskHandler;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class TaskHandler
{
	public TaskHandler()
	{
		dialogEnabled = false;
		newTask = new Task();
		newTask.setAmountOfWorkers(1);

	}

	@Autowired
	private TaskRepository taskRepository;

	private Task newTask;
	private boolean dialogEnabled;

	public void createNewTask()
	{
		dialogEnabled = true;
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

}
