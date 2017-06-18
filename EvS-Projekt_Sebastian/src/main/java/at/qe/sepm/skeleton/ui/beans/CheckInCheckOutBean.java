package at.qe.sepm.skeleton.ui.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.WorkPackage;
import at.qe.sepm.skeleton.services.WorkPackageService;
import at.qe.sepm.skeleton.ui.controllers.UserDetailController;


/**
 * A Bean that handles the check-in / check-out on work packages
 * 
 * @author Andre Potocnik <andre.potocnik@student.uibk.ac.at>
 *
 */
@Component
@Scope("request")
public class CheckInCheckOutBean {
	
	@Autowired
	private WorkPackageService workPackageService;
	
	@Autowired
	private SessionInfoBean sessionInfoBean;
		
	@Autowired
	private UserDetailController userDetailController;
	
	/* The selected work package */
	private String workPackage;
	
	private boolean checkedIn;
	
	/* Real selected work package */
	private WorkPackage selectedWorkPackage;
	
	/* Cache all available work packages */	
	private Collection<WorkPackage> allWorkPackages;
	
	/* Selectable projects */
	private List<SelectItem> workPackages;
	
	/* Cache current students project*/
	private Project currentProject;
	
	
	/**
	 * Initialization of the selectable items
	 */
	@PostConstruct
	public void init() {
		workPackages = new ArrayList<SelectItem>();
		currentProject = sessionInfoBean.getCurrentUser().getProject();
		allWorkPackages = workPackageService.getWorkPackagesByProject(currentProject);
		userDetailController.setUser(sessionInfoBean.getCurrentUser());
		
		for(WorkPackage workPackage : allWorkPackages) {
			workPackages.add(new SelectItem(workPackage.getPackageName(), workPackage.getPackageName()));
		}
		
		if(userDetailController.getUser().getSession() != null) {
			this.checkedIn = true;
		} else {
			this.checkedIn = false;
		}	
	}
	
	/**
	 * Listens to changes at the listbox and acts accordingly
	 *  
	 * @param event which fired value change listener
	 */
	public void valueChanged(ValueChangeEvent event) {
		String eventString = event.getNewValue().toString();
		this.workPackage = eventString;
		this.selectedWorkPackage = workPackageConverter(eventString);
	}
	
	/**
	 * Converts the return string of the selected item from the
	 * listbox, into the real object
	 * 
	 * @param workPackage string to convert
	 * @return object from converted string
	 */
	public WorkPackage workPackageConverter(String workPackage) {
		WorkPackage convertedWorkPackage = workPackageService.loadWorkPackage(currentProject, workPackage);
		return convertedWorkPackage;
	}
	
	public String getWorkPackage() {
		return workPackage;
	}

	public void setWorkPackage(String workPackage) {
		this.workPackage = workPackage;
	}

	public WorkPackage getSelectedWorkPackage() {
		return selectedWorkPackage;
	}

	public void setSelectedWorkPackage(WorkPackage selectedWorkPackage) {
		this.selectedWorkPackage = selectedWorkPackage;
	}

	public Collection<WorkPackage> getAllWorkPackages() {
		return allWorkPackages;
	}

	public void setAllWorkPackages(Collection<WorkPackage> allWorkPackages) {
		this.allWorkPackages = allWorkPackages;
	}

	public List<SelectItem> getWorkPackages() {
		return workPackages;
	}

	public void setWorkPackages(List<SelectItem> workPackages) {
		this.workPackages = workPackages;
	}

	public Project getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(Project currentProject) {
		this.currentProject = currentProject;
	}

	public boolean isCheckedIn() {
		return checkedIn;
	}
	
	public void setCheckedIn(boolean checkedIn) {
		this.checkedIn = checkedIn;
	}
	
	public boolean getCheckedIn() {
		return this.checkedIn;
	}
}
