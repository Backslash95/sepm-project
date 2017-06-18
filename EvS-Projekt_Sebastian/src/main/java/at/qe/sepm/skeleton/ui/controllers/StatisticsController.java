package at.qe.sepm.skeleton.ui.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.model.UserWorkingTime;
import at.qe.sepm.skeleton.model.WorkPackage;
import at.qe.sepm.skeleton.model.WorkPackageWorkingTime;
import at.qe.sepm.skeleton.model.WorksOn;
import at.qe.sepm.skeleton.services.UserService;
import at.qe.sepm.skeleton.services.WorkPackageService;
import at.qe.sepm.skeleton.services.WorksOnService;

/**
 * Controller for the user detail view.
 *
 * @author Michael Brunner <Michael.Brunner@uibk.ac.at>
 */
@Component
@Scope("view")
public class StatisticsController {
	
	@Autowired
	private WorksOnService worksOnService;
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private WorkPackageService workPackageService;
	
	public int getMinutesInProjectByUser(User user, Project project){
		Collection<WorksOn> worksOnCollection = worksOnService.getStatisticsByUserAndProject(user, project);
		int workingTime = 0;
		for (WorksOn worksOn : worksOnCollection) {
			workingTime = workingTime + getTimeDifferenceInMinutes(worksOn.getStartDate(), worksOn.getEndDate());
		}
		return workingTime;
	}
	
	public int getMinutesOfUserInWorkPackage(User user, WorkPackage workPackage){
		Collection<WorksOn> worksOnCollection = worksOnService.getStatisticsByUserAndWorkPackage(user, workPackage);
		int workingTime = 0;
		for (WorksOn worksOn : worksOnCollection) {
			workingTime = workingTime + getTimeDifferenceInMinutes(worksOn.getStartDate(), worksOn.getEndDate());
		}
		return workingTime;
	}
	
	public int getMinutesByProject(Project project){
		List<User> userList = (List<User>) userService.getUsersByProject(project);
		int time = 0;
		for (User user : userList) {
			time = time + getMinutesInProjectByUser(user, project);
		}
		return time;
	}
	
	public Collection<UserWorkingTime> getMinutesPerUserInProject(Project project){
		Collection<UserWorkingTime> minutesPerUserList = new ArrayList<UserWorkingTime>();
		Collection<User> userList = userService.getUsersByProject(project);
		for (User user : userList) {
			UserWorkingTime userWorkingTime = new UserWorkingTime(user, getMinutesInProjectByUser(user, project));
			minutesPerUserList.add(userWorkingTime);
		}
		return minutesPerUserList;
	}
	
	public Collection<WorkPackageWorkingTime> getMinutesOfUserPerWorkPackage(User user, Project project){
		Collection<WorkPackageWorkingTime> minutesPerWorkPackageList = new ArrayList<WorkPackageWorkingTime>();
		Collection<WorkPackage> workPackageList = workPackageService.getWorkPackagesByProject(project);
		for (WorkPackage workPackage : workPackageList) {
			WorkPackageWorkingTime workPackageWorkingTime = new WorkPackageWorkingTime(workPackage, getMinutesOfUserInWorkPackage(user, workPackage));
			minutesPerWorkPackageList.add(workPackageWorkingTime);
		}
		return minutesPerWorkPackageList;
	}
	
	private int getTimeDifferenceInMinutes(Date date1, Date date2){
		if((date1==null)||(date2==null))
			return 0;
		return (int) ((date2.getTime() - date1.getTime())/60000);
	}
	
	
}
