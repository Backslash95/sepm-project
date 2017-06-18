package at.qe.sepm.skeleton.model;


import java.util.Date;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


import org.springframework.data.domain.Persistable;

/**
 * Entity for a child, which is assigned to a/multiple parent/s {@link User}.
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
	private String photo;
	private String address;
	private String acquaintances;
	private String otherInformation;
	private Date registerDate;
	private Date signOutDate;

	@ElementCollection(targetClass = User.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "User_Child")
	private Set<Child> userSetChild;
	
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

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAcquaintances() {
		return acquaintances;
	}

	public void setAcquaintances(String acquaintances) {
		this.acquaintances = acquaintances;
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
	
	public Set<Child> getUserSetChild() {
		return userSetChild;
	}

	public void setUserSetChild(Set<Child> userSetChild) {
		this.userSetChild = userSetChild;
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
