package sepm.creche.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.data.domain.Persistable;

/**
 * Entity for a Day in the Scheduler containing children {@link Child}.
 * 
 * @author Daniel, Fabian
 *
 */

@Entity
public class CrecheSettings implements Persistable<String> {

	private static final long serialVersionUID = 1L;

	// Id for the entity
	@Id
	@GeneratedValue
	private int settingsId;

	// the entity contains the following attributes
	private double lunchPrice;

	private int deadline;

	private String beginDropOffTime;
	private String endDropOffTime;

	private String beginPickUpTime;
	private String endPickUpTime;

	public double getLunchPrice() {
		return lunchPrice;
	}

	public void setLunchPrice(double lunchPrice) {
		this.lunchPrice = lunchPrice;
	}

	public int getDeadline() {
		return deadline;
	}

	public void setDeadline(int deadline) {
		this.deadline = deadline;
	}

	public String getBeginDropOffTime() {
		return beginDropOffTime;
	}

	public void setBeginDropOffTime(String beginDropOffTime) {
		this.beginDropOffTime = beginDropOffTime;
	}

	public String getEndDropOffTime() {
		return endDropOffTime;
	}

	public void setEndDropOffTime(String endDropOffTime) {
		this.endDropOffTime = endDropOffTime;
	}

	public String getBeginPickUpTime() {
		return beginPickUpTime;
	}

	public void setBeginPickUpTime(String beginPickUpTime) {
		this.beginPickUpTime = beginPickUpTime;
	}

	public String getEndPickUpTime() {
		return endPickUpTime;
	}

	public void setEndPickUpTime(String endPickUpTime) {
		this.endPickUpTime = endPickUpTime;
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
