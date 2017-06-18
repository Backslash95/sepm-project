package at.qe.sepm.skeleton.stefan.models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


import org.springframework.data.domain.Persistable;

@Entity
public class TodaysKids implements Persistable<String> {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private int todaysKidsID;
	
	private int childId;
	private String dropOffTime;
	private String pickUpTime;
	private String lunch;
	private String pickUpPerson;
	
	private Date signedDate; 
		
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
	public String getLunch() {
		return lunch;
	}
	public void setLunch(String lunch) {
		this.lunch = lunch;
	}
	public String getPickUpPerson() {
		return pickUpPerson;
	}
	public void setPickUpPerson(String pickUpPerson) {
		this.pickUpPerson = pickUpPerson;
	}

	
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
