package sepm.creche.ui.controllers;

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

import sepm.creche.executors.NewTaskMailNotifierThread;
import sepm.creche.mail.MailService;
import sepm.creche.models.Reminder;
import sepm.creche.models.Task;
import sepm.creche.models.TaskState;
import sepm.creche.models.User;
import sepm.creche.repositories.TaskRepository;
import sepm.creche.repositories.UserRepository;
import sepm.creche.services.ReminderService;

/**
 * This class is used to manage all tasks. The attribute newTask is used for
 * creating a newTask. The attribute selectedTask is used for managing the
 * selected Task.
 * 
 * @author Aspir
 */
@Component
@Scope("session")
public class TaskHandler
{
	public TaskHandler()
	{
		newTask = new Task();
		newTask.setAmountOfWorkers(1);
		newTask.setTimeSpan(1);
		dialogEnabled = false;
		dialogEnabled2 = false;
		setFrontEndResponseEnabled(true);

	}

	private boolean frontEndResponseEnabled;

	@Autowired
	private ParentPickListController parentPickListController;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ReminderService reminderService;

	@Autowired
	private MailService mailService;

	private Task newTask;
	private boolean singleDaySelected;
	private Task selectedTask;
	private int timeSpanInDays;
	private Date startDate;
	private Date endDate;
	private boolean dialogEnabled;
	private boolean dialogEnabled2;
	private boolean dialogEnabled3;

	/**
	 * Opens up the menu for assignment and loads the data.
	 * 
	 * @param taskID
	 *            for the selected task
	 */
	public void loadTask(int taskID)
	{
		parentPickListController.setCurrentUsernames(parentPickListController.getUsernames());
		selectedTask = taskRepository.findFirstByTaskID(taskID);
		dialogEnabled2 = true;

	}

	public void loadTask2(int taskID)
	{
		parentPickListController.setCurrentUsernames(parentPickListController.getUsernames());
		selectedTask = taskRepository.findFirstByTaskID(taskID);
		dialogEnabled3 = true;

	}

	public void windowClosed()
	{
		dialogEnabled = false;
		dialogEnabled2 = false;
		dialogEnabled3 = false;
	}

	/**
	 * Assigns users to the selected task.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void assignTask()
	{
		parentPickListController.addUsers();
		selectedTask.setAssignedUsers(new HashSet<User>(parentPickListController.getChosenUsers()));
		if (selectedTask.getUserSet().size() < selectedTask.getAmountOfWorkers())
		{
			popUpMsg("Bitte genug Leute zuweisen!\n");
			return;
		}

		for (User u : selectedTask.getAssignedUsers())
		{
			Set<Task> currentSet = new HashSet<Task>();
			currentSet.addAll(u.getMyTasks());
			currentSet.add(selectedTask);
			u.setMyTasks(currentSet);
			userRepository.save(u);

			createReminder(u, selectedTask);
		}

		selectedTask.setTaskState(TaskState.ASSIGNED);
		taskRepository.save(selectedTask);
		parentPickListController.reinit();
		windowClosed();
		notifyAssignedUsers(selectedTask);

	}

	private void notifyAssignedUsers(Task task)
	{
		NewTaskMailNotifierThread p = new NewTaskMailNotifierThread(mailService, task);
		new Thread(p).start();
	}

	@SuppressWarnings("deprecation")
	public void createReminder(User user, Task task)
	{
		Reminder reminder = new Reminder();

		reminder.setUserID(user.getUsername());
		reminder.setReminderMessage("Wir bitten sie folgende Aufgabe nicht zu vergessen: " + task.getName());

		Date reminderDate = new Date();
		if (task.isSingleDayTask())
		{
			reminderDate = task.getDeadline();

		} else
		{
			reminderDate = task.getStartDate();
		}

		reminder.setTopic("Aufgabe bis " + reminderDate.toLocaleString());
		reminderDate.setTime(reminderDate.getTime() - task.getTimeSpan() * CalendarController.dayInMilSecs);
		reminder.setDate(reminderDate);
		reminderService.saveReminder(reminder);

	}

	/**
	 * Adds new task to the database.
	 */
	public Task saveNewTask()
	{
		if ((newTask.getDeadline() != null && newTask.getDeadline().before(new Date()))
				|| (newTask.getStartDate() != null && newTask.getStartDate().before(new Date()))
				|| (newTask.getEndDate() != null && newTask.getEndDate().before(new Date()))
				|| (newTask.getEndDate() != null && newTask.getStartDate() != null
						&& newTask.getEndDate().before(newTask.getStartDate())))
		{
			popUpMsg("Datum darf nicht vor dem Jetzigen sein!\n");
			return newTask;
		}
		newTask.setTaskState(TaskState.OPEN);
		if (singleDaySelected)
		{
			newTask.setSingleDayTask(true);
		} else
		{
			newTask.setSingleDayTask(false);
			newTask.setDeadline(newTask.getEndDate());
		}
		Task savedTasked = taskRepository.save(newTask);
		newTask = new Task();
		newTask.setAmountOfWorkers(1);
		newTask.setTimeSpan(1);
		windowClosed();
		return savedTasked;

	}

	public void popUpMsg(String msg)
	{
		if (!frontEndResponseEnabled)
			return;
		if (FacesContext.getCurrentInstance() != null)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
		}
	}

	public void removeTask()
	{
		selectedTask.setTaskState(TaskState.CLOSED);
		taskRepository.save(selectedTask);
	}

	public void deleteTask()
	{
		taskRepository.delete(selectedTask);
	}

	public Task getNewTask()
	{

		return newTask;
	}

	public void setNewTask(Task newTask)
	{
		this.newTask = newTask;
	}

	public Collection<Task> getOpenTasks()
	{
		return taskRepository.findByTaskState(TaskState.OPEN);
	}

	public Task getSelectedTask()
	{
		return selectedTask;
	}

	public void setSelectedTask(Task selectedTask)
	{
		this.selectedTask = selectedTask;
	}

	public boolean isSingleDaySelected()
	{
		return singleDaySelected;
	}

	public void setSingleDaySelected(boolean singleDaySelected)
	{
		dialogEnabled = true;
		this.singleDaySelected = singleDaySelected;
	}

	public int getTimeSpanInDays()
	{
		return timeSpanInDays;
	}

	public void setTimeSpanInDays(int timeSpanInDays)
	{
		this.timeSpanInDays = timeSpanInDays;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}

	public boolean getDialogEnabled()
	{
		return dialogEnabled;
	}

	public void setDialogEnabled(boolean dialogEnabled)
	{
		this.dialogEnabled = dialogEnabled;
	}

	public boolean isDialogEnabled2()
	{
		return dialogEnabled2;
	}

	public void setDialogEnabled2(boolean dialogEnabled2)
	{
		this.dialogEnabled2 = dialogEnabled2;
	}

	public boolean isFrontEndResponseEnabled()
	{
		return frontEndResponseEnabled;
	}

	public void setFrontEndResponseEnabled(boolean frontEndResponseEnabled)
	{
		this.frontEndResponseEnabled = frontEndResponseEnabled;
	}

	public boolean isDialogEnabled3()
	{
		return dialogEnabled3;
	}

	public void setDialogEnabled3(boolean dialogEnabled3)
	{
		this.dialogEnabled3 = dialogEnabled3;
	}

}
