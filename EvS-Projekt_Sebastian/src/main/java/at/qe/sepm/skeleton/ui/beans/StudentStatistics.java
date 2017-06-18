package at.qe.sepm.skeleton.ui.beans;

import java.io.Serializable;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.primefaces.model.chart.PieChartModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.model.WorkPackage;
import at.qe.sepm.skeleton.services.WorkPackageService;
import at.qe.sepm.skeleton.ui.controllers.StatisticsController;

@Component
@Scope("view")
public class StudentStatistics implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private SessionInfoBean sessionInfoBean;
	
	@Autowired
	private WorkPackageService workPackageService;
	
	@Autowired
	private StatisticsController statisticsController;
	
	/* Cache current student */
	private User currentStudent;
	
	/* Cache current project */
	private Project currentProject;
	
	/* Cache all work packages*/
	private Collection<WorkPackage> allWorkPackages;
	
    private PieChartModel pieModel;

    /**
     * Initialization of the pie chart
     */
    @PostConstruct
    public void init() {
        createPieModel();
    }
 
    public PieChartModel getPieModel() {
        return pieModel;
    }
     
    /**
     * Fill pie chart with data
     */
    private void createPieModel() {
    	int workingTime = 0;
    	pieModel = new PieChartModel();
        currentStudent = sessionInfoBean.getCurrentUser();
        currentProject = currentStudent.getProject();
        allWorkPackages = workPackageService.getWorkPackagesByProject(currentProject);
                
        for(WorkPackage workPackage : allWorkPackages) {
        	workingTime = statisticsController.getMinutesOfUserInWorkPackage(currentStudent, workPackage);
        	pieModel.set(workPackage.getPackageName(), workingTime);
        }
        pieModel.setTitle("Time per work package");
        pieModel.setLegendPosition("w");
    }   
}
