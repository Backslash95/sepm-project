package at.qe.sepm.skeleton.ui.beans;

import java.io.Serializable;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.model.WorkPackage;
import at.qe.sepm.skeleton.services.ProjectService;
import at.qe.sepm.skeleton.services.UserService;
import at.qe.sepm.skeleton.services.WorkPackageService;
import at.qe.sepm.skeleton.ui.controllers.StatisticsController;


/**
 * A Bean that handles the PSLeader Statistics
 * 
 * @author Elias Jochum <elias.jochum@student.uibk.ac.at>
 *
 */

@Component
@Scope("view")
public class PSLeaderStatistics implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionInfoBean sessionInfoBean;
	
	@Autowired
	private StatisticsController statisticsController;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private UserService userService;
	
	@Autowired 
	private WorkPackageService workService;
	
	/* Cache current PSLeader */
	private User currentPSLeader;
	
	/* Cache current project */
	private Collection<Project> allProjects;
	
    private BarChartModel barModel;
    private HorizontalBarChartModel[] allBar;
    private PieChartModel[] allPie;
    private int allPieSize;
    private int allBarSize;
 
    @PostConstruct
    public void init() {
    	createBarModel();
    	createAllBar();
    	createAllPie();
    }
    
    public BarChartModel getBarModel() {
    	return barModel;
    }
    
    public HorizontalBarChartModel[] getAllBar() {
    	return allBar;
    }
    
    public PieChartModel[] getAllPie() {
    	return allPie;
    }
    
    public int getAllPieSize() {
    	return allPieSize;
    }
    
    public int getAllBarSize() {
    	return allBarSize;
    }
    
    private BarChartModel initBarModel() {
    	BarChartModel model = new BarChartModel();
    	
    	currentPSLeader = sessionInfoBean.getCurrentUser();
        allProjects = projectService.getProjectsByPSLeader(currentPSLeader);
        ChartSeries tmp = new ChartSeries();
        for(Project project : allProjects) {
        	int workingTime=0;
        	workingTime = statisticsController.getMinutesByProject(project);
        	tmp.set(project.getProjectName(), workingTime);	
        }
        model.addSeries(tmp);
    	return model;
    }
    
    private void createBarModel() {
    	barModel = initBarModel();
    	barModel.setTitle("Minutes by Project");
    	barModel.setAnimate(true);
    	
    	Axis xAxis = barModel.getAxis(AxisType.X);
    	xAxis.setLabel("Projects");
    	
    	Axis yAxis = barModel.getAxis(AxisType.Y);
    	yAxis.setLabel("Minutes");
    	
    }    
    
    private void createAllBar() {
    	currentPSLeader = sessionInfoBean.getCurrentUser();
    	int counter = 0;
        allProjects = projectService.getProjectsByPSLeader(currentPSLeader);
        allBarSize = allProjects.size();
        allBar = new HorizontalBarChartModel[allBarSize];
        for(Project project : allProjects) {
        	HorizontalBarChartModel bar = new HorizontalBarChartModel();
        	Collection<WorkPackage> allWorkpackages = workService.getWorkPackagesByProject(project);
        	for (WorkPackage workPackage : allWorkpackages) {
        		ChartSeries tmp = new ChartSeries();
        		int workingTime=0;
        		tmp.setLabel(workPackage.getPackageName());
        		Collection<User> allStudents = userService.getUsersByWorkpackage(workPackage);
            	for(User student : allStudents) {
            		workingTime = workingTime + statisticsController.getMinutesOfUserInWorkPackage(student, workPackage);
            	}
            	tmp.set(project.getProjectName(), workingTime);
            	bar.addSeries(tmp);
			}
        	bar.setTitle(project.getProjectName());
            bar.setLegendPosition("ne");
            bar.setLegendPlacement(LegendPlacement.OUTSIDE);
            bar.setAnimate(true);
            bar.setStacked(true);
            
            Axis xAxis = bar.getAxis(AxisType.X);
        	xAxis.setLabel("Minutes");
        	
        	allBar[counter++] = bar;
        }	
    }
    
    private void createAllPie() {
    	currentPSLeader = sessionInfoBean.getCurrentUser();
        allProjects = projectService.getProjectsByPSLeader(currentPSLeader);
        allPieSize = allProjects.size();
        allPie = new PieChartModel[allPieSize];
        int counter = 0;
        for(Project project : allProjects) {
        	PieChartModel tmp = new PieChartModel();
        	Collection<User> allStudents = userService.getUsersByProject(project);
        	for(User student : allStudents) {
        		int workingTime=0;
        		workingTime = statisticsController.getMinutesInProjectByUser(student, project);
        		String currentStudent = student.getFirstName() + " " + student.getLastName();
        		tmp.set(currentStudent, workingTime);
        	}
        	tmp.setTitle(project.getProjectName());
        	tmp.setShowDataLabels(true);
        	tmp.setLegendPosition("e");
        	allPie[counter++] = tmp;
        }
    }
}
