package at.qe.sepm.skeleton.model;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.domain.Persistable;

/**
 * This Entity is representing users.
 * 
 * It is used for e.g. parents, which is assigned to a/many child/ren {@link Child} and
 * a/multiple task/s {@link Task}.
 * 
 * @author Steve
 * 
 */

@Entity
public class User implements Persistable<String> {

	private static final long serialVersionUID = 1L;

	// Id for the entity
	@Id
	//@GeneratedValue
	//private int userID;
	private String username;

	// the entity contains the following attributes
	@ManyToOne(optional = false)
	private User createUser;
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@ManyToOne(optional = true)
	private User updateUser;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	private String password;
	private String firstName;
    private String lastName;
	private String email;
	private String sex;
	private String address;
	private String phone;
	private boolean enabled;
	private boolean inactive;

	@ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "User_UserRole")
	@Enumerated(EnumType.STRING)
	private Collection<UserRole> roles;
	
	//Special attributes for parents
	@ElementCollection(targetClass = Child.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "User_Child")
	private Set<Child> childSet;
	
	@ElementCollection(targetClass = Task.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "User_Task")
	private Set<Task> taskSet;
	
	// Getters and Setters of the Entity User

	/*public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
*/
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	public Set<Child> getChildSet() {
		return childSet;
	}

	public void setChildSet(Set<Child> childSet) {
		this.childSet = childSet;
	}

	public Set<Task> getTaskSet() {
		return taskSet;
	}

	public void setTaskSet(Set<Task> taskSet) {
		this.taskSet = taskSet;
	}

	public User getCreateUser() {
		return createUser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public User getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(User updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAdress() {
		return address;
	}

	public void setAdress(String adress) {
		this.address = adress;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isInactive() {
		return inactive;
	}

	public void setInactive(boolean inactive) {
		this.inactive = inactive;
	}

	public Collection<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Collection<UserRole> roles) {
		this.roles = roles;
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
