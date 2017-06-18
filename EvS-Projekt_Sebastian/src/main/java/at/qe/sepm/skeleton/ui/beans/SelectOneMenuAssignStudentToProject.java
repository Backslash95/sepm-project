package at.qe.sepm.skeleton.ui.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.services.ProjectService;
import at.qe.sepm.skeleton.services.UserService;


/**
 * A Bean that handles the selectOnMenu
 * 
 * @author Andre Potocnik <andre.potocnik@student.uibk.ac.at>
 *
 */
@Component
@Scope("view")
public class SelectOneMenuAssignStudentToProject {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SessionInfoBean sessionInfoBean;
	
	/* The selected project */
	private String project;	//selectOneMenu returns a string
	
	/* Real selected project*/
	private Project selectedProject;
	
	/* Cache all available projects */	
	private Collection<Project> allProjects;

	/* Selectable projects */
	private List<SelectItem> projects;
	
	/* The selected ps leader*/
	private String psLeader;	//selectOneMenu returns a string
	
	/* Real selected ps leader */
	private User selectedPSLeader;
	
	/* Cache all available ps leader*/
	private Collection<User> allPSLeaders;
	
	/* Selectable PS Leaders*/
	private List<SelectItem> psLeaders;
	
	/**
	 * Initialization of the selectable items
	 */
	@PostConstruct
	public void init() {
		projects = new ArrayList<SelectItem>();
		psLeaders = new ArrayList<SelectItem>();
		allPSLeaders = userService.getAllPSLeaders();
		
		
		if(sessionInfoBean.hasRole("ADMIN")) {
			allProjects = projectService.getAllProjects();
			
		} else if(sessionInfoBean.hasRole("PSLEADER")) {
			allProjects = projectService.getProjectsByPSLeader(sessionInfoBean.getCurrentUser());
		}
		
		for(Project project : allProjects) {
			projects.add(new SelectItem(project.getProjectName(), project.getProjectName()));
		}
			
		for(User user : allPSLeaders) {
			psLeaders.add(new SelectItem(user.getUsername(), user.getUsername()));
		}
	}
	
	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
		this.selectedProject = projectService.loadProject(project);
	}

	public Project getSelectedProject() {
		return selectedProject;
	}

	public Collection<Project> getAllProjects() {
		return allProjects;
	}

	public void setAllProjects(Collection<Project> allProjects) {
		this.allProjects = allProjects;
	}

	public List<SelectItem> getProjects() {
		return projects;
	}

	public void setProjects(List<SelectItem> projects) {
		this.projects = projects;
	}

	public String getPsLeader() {
		return psLeader;
	}

	public void setPsLeader(String psLeader) {
		this.psLeader = psLeader;
		this.selectedPSLeader = userService.loadUser(psLeader);
	}

	public User getSelectedPSLeader() {
		return selectedPSLeader;
	}

	public Collection<User> getAllPSLeaders() {
		return allPSLeaders;
	}

	public void setAllPSLeaders(Collection<User> allPSLeaders) {
		this.allPSLeaders = allPSLeaders;
	}

	public List<SelectItem> getPsLeaders() {
		return psLeaders;
	}

	public void setPsLeaders(List<SelectItem> psLeaders) {
		this.psLeaders = psLeaders;
	}
}
