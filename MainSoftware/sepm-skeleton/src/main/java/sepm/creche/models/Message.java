package sepm.creche.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.domain.Persistable;

/**
 * Message Main Database.
 * 
 * @author ASPIR
 *
 */
@Entity
public class Message implements Persistable<String>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private int msgID;

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	private String userIDFrom;
	private String userIDTo;
	@Column(nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	private String message;

	public boolean isAlreadyRead()
	{
		return alreadyRead;
	}

	public void setAlreadyRead(boolean alreadyRead)
	{
		this.alreadyRead = alreadyRead;
	}

	private boolean alreadyRead;

	public int getMsgID()
	{
		return msgID;
	}

	public void setMsgID(int msgID)
	{
		this.msgID = msgID;
	}

	public String getUserIDFrom()
	{
		return userIDFrom;
	}

	public void setUserIDFrom(String userIDFrom)
	{
		this.userIDFrom = userIDFrom;
	}

	public String getUserIDTo()
	{
		return userIDTo;
	}

	public void setUserIDTo(String userIDTo)
	{
		this.userIDTo = userIDTo;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
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

}
