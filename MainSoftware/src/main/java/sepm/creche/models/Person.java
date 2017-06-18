package sepm.creche.models;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.springframework.data.domain.Persistable;

/**
 * This Entity is representing a person, for adding acquaintances to the
 * {@link Child} entity.
 * 
 * @author Stefan
 * 
 */

@Entity
public class Person implements Persistable<String>
{

	private static final long serialVersionUID = 1L;

	// Id for the entity
	@Id
	@GeneratedValue
	private int personID;

	// the entity contains the following attributes
	private String name;
	private String phone;
	private String email;
	private String relation;
	private String pictureReference;
	private boolean activated;
	private Date createDate;
	@ManyToOne
	private User createUser;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "Person_Child")
	private Set<Child> knownChildren;

	// getters and setters for attributes
	public int getPersonID()
	{
		return personID;
	}

	public void setPersonID(int personID)
	{
		this.personID = personID;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Set<Child> getKnownChildren()
	{
		return knownChildren;
	}

	public void setKnownChildren(Set<Child> childSetPerson)
	{
		this.knownChildren = childSetPerson;
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

	public String getPictureReference()
	{
		return pictureReference;
	}

	public void setPictureReference(String pictureReference)
	{
		this.pictureReference = pictureReference;
	}

	public boolean isActivated()
	{
		return activated;
	}

	public void setActivated(boolean activated)
	{
		this.activated = activated;
	}

	public User getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(User createUser)
	{
		this.createUser = createUser;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Person))
		{
			return false;
		}
		Person person = (Person) obj;
		if (personID == 0)
			return false;
		return person.getPersonID() == getPersonID();
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name, personID);
	}

	public String getRelation()
	{
		return relation;
	}

	public void setRelation(String relation)
	{
		this.relation = relation;
	}

}
