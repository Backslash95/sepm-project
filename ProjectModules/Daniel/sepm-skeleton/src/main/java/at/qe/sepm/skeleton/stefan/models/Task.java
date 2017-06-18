package at.qe.sepm.skeleton.stefan.models;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.springframework.data.domain.Persistable;

/**
 * Entity for a task, which is assigned to a/multiple parent/s {@link User}.
 * 
 * @author Steve
 *
 */
@Entity
public class Task implements Persistable<String>
{
	private static final long serialVersionUID = 1L;
	// Id for the entity
	@Id
	@GeneratedValue
	private int taskID;
	// the entity contains the following attributes
	private String name;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "User_Task")
	private Set<User> userSetTask;
	
	private Date deadline;
	private TaskState taskState;
	private int amountOfWorkers;

	// Getters and Setters of the Enstity Task
	public int getTaskID()
	{
		return taskID;
	}

	public TaskState getTaskState()
	{
		return taskState;
	}

	public void setTaskState(TaskState taskState)
	{
		this.taskState = taskState;
	}

	public Set<User> getUserSetTask()
	{
		return userSetTask;
	}

	public void setUserSetTask(Set<User> userSetTask)
	{
		this.userSetTask = userSetTask;
	}

	public Date getDeadline()
	{
		return deadline;
	}

	public void setDeadline(Date deadline)
	{
		this.deadline = deadline;
	}

	public void setTaskID(int taskID)
	{
		this.taskID = taskID;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Set<User> getUserSet()
	{
		return userSetTask;
	}

	public void setUserSet(Set<User> userSetTask)
	{
		this.userSetTask = userSetTask;
	}

	// Override the methods for the Persistable interface.
	@Override
	public String getId()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isNew()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public int getAmountOfWorkers()
	{
		return amountOfWorkers;
	}

	public void setAmountOfWorkers(int amountOfWorkers)
	{
		this.amountOfWorkers = amountOfWorkers;
	}
}