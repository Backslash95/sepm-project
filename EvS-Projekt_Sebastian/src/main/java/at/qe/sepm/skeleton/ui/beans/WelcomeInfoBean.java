package at.qe.sepm.skeleton.ui.beans;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.ui.controllers.ProjectListController;

@Component
@Scope("request")
public class WelcomeInfoBean {
	
	@Autowired
	private SessionInfoBean sessionInfoBean;
	
	@Autowired
	private ProjectListController projectListController;
	
	private User currentStudent;
	
	private User currentPSLeader;
	
	public boolean studentIsInProject() {
		currentStudent = sessionInfoBean.getCurrentUser();
		return (currentStudent.getProject() != null) ? true : false;
	}
	
	public boolean studentIsCheckedIn() {
		return (currentStudent.getSession() != null) ? true : false;
		
	}
		
	public String studentInProjectMessage() {
		
		if(studentIsInProject()) {
			return currentStudent.getProject().getProjectName();
		}
		
		return "You're currently not assigned to a project!";
	}
	
	public String studentProjectSupervisorMessage() {
		return currentStudent.getProject().getPsleader().getUsername();
	}
	
	public String studentCheckedInMessage() {
		currentStudent = sessionInfoBean.getCurrentUser();
				
		if(studentIsCheckedIn()) {
			return currentStudent.getSession().getWorkPackage().getPackageName();
		}
		
		return "non work package!";
	}
	
	public String psLeaderSupervisorMessage() {
		currentPSLeader = sessionInfoBean.getCurrentUser();
		Collection<Project> projects = projectListController.getProjectsByPSLeader(currentPSLeader);
		StringBuilder message = new StringBuilder("");
		
		if(projects.size() != 0) {
			for(Project project : projects) {
				message.append(project.getProjectName() + ", ");
			}
			message.setCharAt(message.length()-2, ' ');
			return message.toString();
		}
		return "You're currently not a supervisor of any project!"; 
	}
}
