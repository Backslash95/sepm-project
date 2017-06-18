
package sepm.creche.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.data.domain.Persistable;

/**
 * Entity for a Reminder.
 * 
 * @author aspir
 */

@Entity
public class Reminder implements Persistable<String>
{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int reminderID;

	private Date date;
	private String userID;
	private String reminderMessage;
	private String topic;

	public int getReminderID()
	{
		return reminderID;
	}

	public void setReminderID(int reminderID)
	{
		this.reminderID = reminderID;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public String getReminderMessage()
	{
		return reminderMessage;
	}

	public void setReminderMessage(String reminderMessage)
	{
		this.reminderMessage = reminderMessage;
	}

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

	public String getTopic()
	{
		return topic;
	}

	public void setTopic(String topic)
	{
		this.topic = topic;
	}

	public String getUserID()
	{
		return userID;
	}

	public void setUserID(String userID)
	{
		this.userID = userID;
	}

}
