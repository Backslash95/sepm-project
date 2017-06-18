package at.qe.sepm.skeleton.model;


import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


import org.springframework.data.domain.Persistable;

/**
 * Entity for a task, which is assigned to a/multiple parent/s {@link User}.
 * 
 * @author Steve
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

	@ElementCollection(targetClass = User.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "User_Task")
	private Set<User> userSetTask;

	// Getters and Setters of the Entity Task
	public int getTaskID() {
		return taskID;
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
		return userSetTask;
	}

	public void setUserSet(Set<User> userSetTask) {
		this.userSetTask = userSetTask;
	}

	// Override the methods for the Persistable interface.
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

}
