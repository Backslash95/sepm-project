package sepm.creche.models;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.data.domain.Persistable;

/**
 * Entity for a task, which is assigned to a/multiple parent/s {@link User}.
 * 
 * @author Stefan
 *
 */
@Entity
public class Task implements Persistable<String> {
	private static final long serialVersionUID = 1L;

	// Id for the entity
	@Id
	@GeneratedValue
	private int taskID;

	// the entity contains the following attributes
	private String name;
	private Date deadline;
	private TaskState taskState;
	private int amountOfWorkers;
	private String description;
	private boolean singleDayTask;
	private Date startDate;
	private Date endDate;
	private int timeSpan;

	@ManyToMany(mappedBy = "myTasks", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<User> assignedUsers;

	// Getters and Setters of the Entity Task
	public int getTaskID() {
		return taskID;
	}

	public TaskState getTaskState() {
		return taskState;
	}

	public void setTaskState(TaskState taskState) {
		this.taskState = taskState;
	}

	public Set<User> getAssignedUsers() {
		return assignedUsers;
	}

	public void setAssignedUsers(Set<User> userSetTask) {

		this.assignedUsers = userSetTask;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUserSet() {
		return assignedUsers;
	}

	public void setUserSet(Set<User> userSetTask) {
		this.assignedUsers = userSetTask;

	}

	public int getAmountOfWorkers() {
		return amountOfWorkers;
	}

	public void setAmountOfWorkers(int amountOfWorkers) {
		this.amountOfWorkers = amountOfWorkers;
	}

	// Override the methods for the Persistable interface.
	@Override
	public String getId() {
		return null;
	}

	@Override
	public boolean isNew() {
		return false;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSingleDayTask() {
		return singleDayTask;
	}

	public void setSingleDayTask(boolean singleDayTask) {
		this.singleDayTask = singleDayTask;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getTimeSpan() {
		return timeSpan;
	}

	public void setTimeSpan(int timeSpan) {
		this.timeSpan = timeSpan;
	}
}