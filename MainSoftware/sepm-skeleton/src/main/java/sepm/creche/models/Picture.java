package sepm.creche.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.data.domain.Persistable;

/**
 * Entity for a child, which is assigned to a picutre
 * 
 * @author Stefan
 *
 */
@Entity
public class Picture implements Persistable<String> {

	private static final long serialVersionUID = 1L;

	// Id for the entity
	@Id
	@GeneratedValue
	private int pictureID;

	private int childID;

	private String pictureReference;

	public int getChildID() {
		return childID;
	}

	public void setChildID(int childID) {
		this.childID = childID;
	}

	public String getPictureReference() {
		return pictureReference;
	}

	public void setPictureReference(String pictureReference) {
		this.pictureReference = pictureReference;
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
