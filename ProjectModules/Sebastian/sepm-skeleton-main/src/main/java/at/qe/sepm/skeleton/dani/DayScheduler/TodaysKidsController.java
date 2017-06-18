package at.qe.sepm.skeleton.dani.DayScheduler;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.stefan.models.TodaysKids;
import at.qe.sepm.skeleton.stefan.repositories.TodaysKidsRepository;

@Component
@Scope("session")
public class TodaysKidsController {

	public TodaysKidsController(){
		newKid = new TodaysKids();
	}
	
	@Autowired
	private TodaysKidsRepository todaysKidsRepository;
	
	private TodaysKids newKid;
	private TodaysKids selectedKid;
	
	public TodaysKids getNewKid() {
		return newKid;
	}
	public void setNewKid(TodaysKids newKid) {
		this.newKid = newKid;
	}
	public TodaysKids getSelectedKid() {
		return selectedKid;
	}
	public void setSelectedKid(TodaysKids selectedKid) {
		this.selectedKid = selectedKid;
	}
	
	public void saveNewTodaysKid(){
		if(newKid.getDropOffTime() == null || newKid.getPickUpPerson() == null || newKid.getPickUpTime() == null){
			popUpMsg(
					" Abhol-, Bringzeit und Abholperson sind Pflichtfelder!\n!");
			return;
		}
		todaysKidsRepository.save(newKid);
	}
	
	public static void popUpMsg(String msg)
	{
		if (FacesContext.getCurrentInstance() != null)
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
		}
	}
	
}
