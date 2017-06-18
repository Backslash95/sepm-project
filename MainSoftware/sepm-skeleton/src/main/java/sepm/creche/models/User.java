package sepm.creche.models;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.domain.Persistable;

/**
 * This Entity is representing users.
 * 
 * It is used for e.g. parents, which is assigned to a/many child/ren
 * {@link Child} and a/multiple task/s {@link Task}.
 * 
 * @author Stefan
 * 
 */

@Entity
public class User implements Persistable<String> {

	private static final long serialVersionUID = 1L;

	// Id for the entity
	@Id
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
	private boolean sendEmails;

	@ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "User_UserRole")
	@Enumerated(EnumType.STRING)
	private Collection<UserRole> roles;

	// Special attributes for parents
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "User_Child")
	private Set<Child> myChildren;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "User_Task")
	private Set<Task> myTasks;

	// Getters and Setters of the Entity User
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

	public Set<Child> getMyChildren() {
		return myChildren;
	}

	public void setMyChildren(Set<Child> childSetUser) {
		this.myChildren = childSetUser;
	}

	public Set<Task> getMyTasks() {
		return myTasks;
	}

	public void setMyTasks(Set<Task> taskSet) {

		this.myTasks = taskSet;
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
		return null;
	}

	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof User)) {
			return false;
		}
		User user = (User) obj;
		if (username == null)
			return false;
		return user.getUsername().equals(getUsername());
	}

	@Override
	public int hashCode() {
		return Objects.hash(username, firstName, lastName, phone, sex);
	}

	public boolean getSendEmails() {
		return sendEmails;
	}

	public void setSendEmails(boolean sendEmails) {
		this.sendEmails = sendEmails;
	}

}
