package at.qe.sepm.skeleton.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.domain.Persistable;

/**
 * Entity representing workPackages.
 * 
 * @author Daniel Proksch <Daniel.Proksch@student.uibk.ac.at>, Sebastian Grabher <sebastian.grabher@student.uibk.ac.at>
 */
@Entity
public class WorkPackage implements Persistable<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne(optional = false)
	private Project project;

	@Column(nullable = true)
	private String packageName;
	
	@Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
	@ManyToOne(optional = false)
    private User createUser;
	
	public Project getProject() {
		return project;
	}
	
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public boolean isNew() {
		return (createDate == null);
	}
}
