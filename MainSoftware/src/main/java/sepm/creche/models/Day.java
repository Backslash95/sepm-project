package sepm.creche.models;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
	private DayType dayType;
	private String description;
	@Column(nullable = true)
	private Integer maxOccupation;

	public DayType getDayType() {
		return dayType;
	}

	public void setDayType(DayType dayType) {
		this.dayType = dayType;
	}

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

	public Integer getMaxOccupation() {
		return maxOccupation;
	}

	public void setMaxOccupation(Integer maxOccupation) {
		this.maxOccupation = maxOccupation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Day)) {
			return false;
		}
		Day day = (Day) obj;
		return new Integer(day.getDayID()).equals(new Integer(getDayID()));
	}

	@Override
	public int hashCode() {
		return Objects.hash(dayID, date, description);
	}

}
