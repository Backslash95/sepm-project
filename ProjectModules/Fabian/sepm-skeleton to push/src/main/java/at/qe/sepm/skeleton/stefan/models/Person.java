package at.qe.sepm.skeleton.stefan.models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.springframework.data.domain.Persistable;

/**
 * This Entity is representing a person, for adding acquaintances to the
 * {@link Child} entity.
 * 
 * @author Steve
 * 
 */

@Entity
public class Person implements Persistable<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int personID;

	// the entity contains the following attributes
	private String name;
	private String phone;
	private String email;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "Person_Child")
	private Set<Child> childSetPerson;

	// getters and setters for attributes
	public int getPersonID() {
		return personID;
	}

	public void setPersonID(int personID) {
		this.personID = personID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Child> getChildSetPerson() {
		return childSetPerson;
	}

	public void setChildSetPerson(Set<Child> childSetPerson) {
		this.childSetPerson = childSetPerson;
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
