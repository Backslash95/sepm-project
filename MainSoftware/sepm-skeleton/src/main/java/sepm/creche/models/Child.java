package sepm.creche.models;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.data.domain.Persistable;

/**
 * Entity for a child, which is assigned to a/multiple parent/s {@link User} and
 * acquaintances {@link Person}, if they exist.
 * 
 * @author Stefan
 *
 */
@Entity
public class Child implements Persistable<String> {

	private static final long serialVersionUID = 1L;

	// Id for the entity
	@Id
	@GeneratedValue
	private int childID;

	// the entity contains the following attributes
	private String name;
	private String sex;
	private Date birthdate;

	private boolean deregistered;

	private String address;
	private String emergencyContact;

	@Column(nullable = true)
	private String otherInformation;

	@Column(nullable = true)
	private String allergies;

	private Date registerDate;

	@Column(nullable = true)
	private Date signOutDate;

	@Column(nullable = true)
	private Date givenSignOutDate;

	@Column(nullable = true)
	private String siblings;

	@ManyToMany(mappedBy = "myChildren", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<User> myParents;

	@ManyToMany(mappedBy = "knownChildren", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Person> myRelatives;

	// Getters and Setters of the Entity Child
	public int getChildID() {
		return childID;
	}

	public void setChildID(int childID) {
		this.childID = childID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<Person> getMyRelatives() {
		return myRelatives;
	}

	public void setMyRelatives(Set<Person> personSet) {
		this.myRelatives = personSet;
	}

	public String getOtherInformation() {
		return otherInformation;
	}

	public void setOtherInformation(String otherInformation) {
		this.otherInformation = otherInformation;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Date getSignOutDate() {
		return signOutDate;
	}

	public void setSignOutDate(Date signOutDate) {
		this.signOutDate = signOutDate;
	}

	public Set<User> getMyParents() {
		return myParents;
	}

	public void setMyParents(Set<User> userSetChild) {
		this.myParents = userSetChild;
	}

	public String getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

	public Date getGivenSignOutDate() {
		return givenSignOutDate;
	}

	public void setGivenSignOutDate(Date givenSignOutDate) {
		this.givenSignOutDate = givenSignOutDate;
	}

	public String getAllergies() {
		return allergies;
	}

	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}

	public String getSiblings() {
		return siblings;
	}

	public void setSiblings(String siblings) {
		this.siblings = siblings;
	}

	public boolean isDeregistered() {
		return deregistered;
	}

	public void setDeregistered(boolean deregistered) {
		this.deregistered = deregistered;
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
		if (!(obj instanceof Child)) {
			return false;
		}
		Child child = (Child) obj;
		return child.getChildID() == getChildID();
	}

	@Override
	public int hashCode() {
		return Objects.hash(childID, name, birthdate, address, sex);
	}
}
