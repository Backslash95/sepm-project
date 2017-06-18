package at.qe.sepm.skeleton.stefan.models;

import java.sql.Date;
import java.util.Collection;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

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
	private String hasBirthday;
	
	private Date date; 
	

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "Day_Kids")
	private Set<Day> daySet;
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
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
	public String getHasBirthday() {
		return hasBirthday;
	}
	public void setHasBirthday(String hasBirthday) {
		this.hasBirthday = hasBirthday;
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
