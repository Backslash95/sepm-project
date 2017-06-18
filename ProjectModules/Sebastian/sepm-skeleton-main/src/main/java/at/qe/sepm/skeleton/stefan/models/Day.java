package at.qe.sepm.skeleton.stefan.models;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.data.domain.Persistable;

/**
 * Entity for a Day.
 * 
 * @author Steve
 */

@Entity
public class Day implements Persistable<String> {

	private static final long serialVersionUID = 1L;

	// Id for the entity
	@Id
	@GeneratedValue
	private int dayID;

	// the entity contains the following attributes
	private Date date;
	private String dayOfTheWeek;
	// TODO: add the needed attributes here

	// added set of kids which are signed in for specific day
	@ManyToMany(mappedBy = "daySet", fetch = FetchType.EAGER)
	private Set<TodaysKids> kidSet;
	
	
	// Getters and Setters of the Entity Day
	public int getDayID() {
		return dayID;
	}

	public void setDayID(int dayID) {
		this.dayID = dayID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDayOfTheWeek() {
		return dayOfTheWeek;
	}

	public void setDayOfTheWeek(String dayOfTheWeek) {
		this.dayOfTheWeek = dayOfTheWeek;
	}
	// TODO: add the needed getters/setters for attributes here

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
	
	public Set<TodaysKids> getKidSet() {
		return kidSet;
	}

	public void setKidSet(Set<TodaysKids> kidSet) {
		this.kidSet = kidSet;
	}

}
