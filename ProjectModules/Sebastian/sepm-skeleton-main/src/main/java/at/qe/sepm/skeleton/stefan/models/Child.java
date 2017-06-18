package at.qe.sepm.skeleton.stefan.models;

import java.util.Date;
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
 * @author Steve
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

	@Column(nullable = true)
	private String photoPath;

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
	private String siblings;

	@ManyToMany(mappedBy = "childSetUser", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<User> userSetChild;

	@ManyToMany(mappedBy = "childSetPerson", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Person> personSet;

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

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<Person> getPersonSet() {
		return personSet;
	}

	public void setPersonSet(Set<Person> personSet) {
		this.personSet = personSet;
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

	public Set<User> getUserSetChild() {
		return userSetChild;
	}

	public void setUserSetChild(Set<User> userSetChild) {
		this.userSetChild = userSetChild;
	}

	public String getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
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

}
