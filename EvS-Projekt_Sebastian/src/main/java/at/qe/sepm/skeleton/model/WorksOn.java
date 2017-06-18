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
 * Entity representing specified sessions, in which users.
 * 
 * @author Daniel Proksch <Daniel.Proksch@student.uibk.ac.at>
 */
@Entity
public class WorksOn implements Persistable<Long>{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne(optional = false)
	private User student;
	
	@ManyToOne(optional = false)
	private WorkPackage workPackage;
	
	@ManyToOne(optional = false)
	private Project project;
	
	@Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
	
	@Column(nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
	
	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public WorkPackage getWorkPackage() {
		return workPackage;
	}

	public void setWorkPackage(WorkPackage workPackage) {
		this.workPackage = workPackage;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public boolean isNew() {
		return (null == startDate);
	}
	
}
