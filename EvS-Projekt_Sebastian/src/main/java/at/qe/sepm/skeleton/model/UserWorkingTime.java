package at.qe.sepm.skeleton.model;

public class UserWorkingTime {
	
	private User user;
	private int workingTime;
	
	public UserWorkingTime(User user, int workingTime) {
		super();
		this.user = user;
		this.workingTime = workingTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getWorkingTime() {
		return workingTime;
	}

	public void setWorkingTime(int workingTime) {
		this.workingTime = workingTime;
	}
	
	
	

}
