package at.qe.sepm.skeleton.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.domain.Persistable;

/**
 * Entity representing projects.
 * 
 * @author Daniel Proksch <Daniel.Proksch@student.uibk.ac.at>
 */
@Entity
public class Project implements Persistable<Long> {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String projectName;
	
	@Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
	@Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

	@ManyToOne(optional = false)
	private User psleader;
	
	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public User getPsleader() {
		return psleader;
	}
	
	public void setPsleader(User psleader) {
		this.psleader = psleader;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Project)) {
            return false;
        }
        final Project other = (Project) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public boolean isNew() {
		return createDate == null;
	}

}