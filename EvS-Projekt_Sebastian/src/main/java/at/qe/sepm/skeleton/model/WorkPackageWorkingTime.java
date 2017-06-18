package at.qe.sepm.skeleton.model;

public class WorkPackageWorkingTime {
	
	private WorkPackage workPackage;
	private int workingTime;
	
	public WorkPackageWorkingTime(WorkPackage workPackage, int workingTime) {
		super();
		this.workPackage = workPackage;
		this.workingTime = workingTime;
	}

	public WorkPackage getWorkPackage() {
		return workPackage;
	}

	public void setWorkPackage(WorkPackage workPackage) {
		this.workPackage = workPackage;
	}

	public int getWorkingTime() {
		return workingTime;
	}

	public void setWorkingTime(int workingTime) {
		this.workingTime = workingTime;
	}
	
	
	

}
