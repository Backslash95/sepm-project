package DayScheduler;

import java.util.List;



public class todaysKids {
	
	private List<Integer> childId;
	private List<String> dropOffTime;
	private List<String> pickUpTime;
	private List<String> lunch;
	private List<String> pickUpPerson;
	private List<String> hasBirthday;
	
	
	
	public List<Integer> getChildId() {
		return childId;
	}
	public void setChildId(int childId) {
		this.childId.add(childId);
	}
	public List<String> getDropOffTime() {
		return dropOffTime;
	}
	public void setDropOffTime(String time) {
		this.dropOffTime.add(time);
	}
	public List<String> getPickUpTime() {
		return pickUpTime;
	}
	public void setPickUpTime(String pickUpTime) {
		this.pickUpTime.add(pickUpTime);
	}
	public List<String> getLunch() {
		return lunch;
	}
	public void setLunch(String lunch) {
		this.lunch.add(lunch);
	}
	public List<String> getPickUpPerson() {
		return pickUpPerson;
	}
	public void setPickUpPerson(String pickUpPerson) {
		this.pickUpPerson.add(pickUpPerson);
	}
	public List<String> getHasBirthday() {
		return hasBirthday;
	}
	public void setHasBirthday(String hasBirthday) {
		this.hasBirthday.add(hasBirthday);
	}
	
	
}
