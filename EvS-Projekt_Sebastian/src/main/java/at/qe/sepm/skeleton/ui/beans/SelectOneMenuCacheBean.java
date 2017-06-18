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
import at.qe.sepm.skeleton.services.ProjectService;


/**
 * A Bean that handles the selectOnMenu
 * 
 * @author Andre Potocnik <andre.potocnik@student.uibk.ac.at>
 *
 */
@Component
@Scope("view")
public class SelectOneMenuCacheBean {
	
	@Autowired
	private ProjectService projectService;
	
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
	
	/**
	 * Initialization of the selectable items
	 */
	@PostConstruct
	public void init() {
		projects = new ArrayList<SelectItem>();
		
		if(sessionInfoBean.hasRole("ADMIN")) {
			allProjects = projectService.getAllProjects();
		} else if(sessionInfoBean.hasRole("PSLEADER")) {
			allProjects = projectService.getProjectsByPSLeader(sessionInfoBean.getCurrentUser());
		}
		
		for(Project project : allProjects) {
			projects.add(new SelectItem(project.getProjectName(), project.getProjectName()));
		}
	}
	
	/**
	 * Convert the selected item string to the effectively selected project
	 */
	public void setSelectedProject() {
		this.selectedProject = projectService.loadProject(project); 
	}
	
	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
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

	public Project getSelectedProject() {
		return selectedProject;
	}

	public void setSelectedProject(Project selectedProject) {
		this.selectedProject = selectedProject;
	}
}
