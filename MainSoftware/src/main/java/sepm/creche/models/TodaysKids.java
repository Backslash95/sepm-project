package sepm.creche.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.data.domain.Persistable;

/**
 * Entity for a Day in the Scheduler containing children {@link Child}.
 * 
 * @author Daniel,Fabian
 *
 */

@Entity
public class TodaysKids implements Persistable<String> {

	private static final long serialVersionUID = 1L;

	// Id for the entity
	@Id
	@GeneratedValue
	private int todaysKidsID;

	// the entity contains the following attributes
	private int childId;
	private String dropOffTime;
	private String pickUpTime;
	private boolean lunch;
	private String pickUpPerson;

	private Date signedDate;
	
	private String notes;
	


	// Getters and Setters of the Entity Task
	
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public Date getDate() {
		return signedDate;
	}

	public void setDate(Date date) {
		this.signedDate = date;
	}

	public int getTodaysKidsID() {
		return todaysKidsID;
	}

	public void setTodaysKidsID(int todaysKidsID) {
		this.todaysKidsID = todaysKidsID;
	}

	public int getChildId() {
		return childId;
	}

	public void setChildId(int childId) {
		this.childId = childId;
	}

	public String getDropOffTime() {
		return dropOffTime;
	}

	public void setDropOffTime(String dropOffTime) {
		this.dropOffTime = dropOffTime;
	}

	public String getPickUpTime() {
		return pickUpTime;
	}

	public void setPickUpTime(String pickUpTime) {
		this.pickUpTime = pickUpTime;
	}

	public boolean getLunch() {
		return lunch;
	}

	public void setLunch(boolean lunch) {
		this.lunch = lunch;
	}

	public String getPickUpPerson() {
		return pickUpPerson;
	}

	public void setPickUpPerson(String pickUpPerson) {
		this.pickUpPerson = pickUpPerson;
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
}
