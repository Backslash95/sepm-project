package sepm.creche.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.data.domain.Persistable;

/**
 * Entity for a gallery picture
 * 
 * @author aspir
 *
 */
@Entity
public class GalleryPic implements Persistable<String>
{

	private static final long serialVersionUID = 1L;

	// Id for the entity
	@Id
	@GeneratedValue
	private int pictureID;

	private String userID;

	private String pictureReference;

	public String description;

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	private boolean isProfilePic;

	public String getPictureReference()
	{
		return pictureReference;
	}

	public void setPictureReference(String pictureRefernce)
	{
		this.pictureReference = pictureRefernce;
	}

	public int getPictureID()
	{
		return pictureID;
	}

	public void setPictureID(int pictureID)
	{
		this.pictureID = pictureID;
	}

	// Override the methods for the Persistable interface.
	@Override
	public String getId()
	{
		return null;
	}

	@Override
	public boolean isNew()
	{
		return false;
	}

	public String getUserID()
	{
		return userID;
	}

	public void setUserID(String userID)
	{
		this.userID = userID;
	}

	public boolean isProfilePic()
	{
		return isProfilePic;
	}

	public void setProfilePic(boolean isProfilePic)
	{
		this.isProfilePic = isProfilePic;
	}

}