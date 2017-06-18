package at.qe.sepm.skeleton.dani.DayScheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import at.qe.sepm.skeleton.stefan.models.Child;
import at.qe.sepm.skeleton.stefan.models.Day;
import at.qe.sepm.skeleton.stefan.models.Person;
import at.qe.sepm.skeleton.stefan.models.TodaysKids;
import at.qe.sepm.skeleton.stefan.repositories.ChildRepository;
import at.qe.sepm.skeleton.stefan.repositories.DayRepository;
import at.qe.sepm.skeleton.stefan.repositories.TodaysKidsRepository;
import at.qe.sepm.skeleton.ui.beans.SessionInfoBean;

@Component
@Scope("session")
public class TodaysKidsController {

	public TodaysKidsController(){		
		newKid = new TodaysKids();
	}
	
	@Autowired
	private TodaysKidsRepository todaysKidsRepository;
	
	@Autowired
	private DayRepository dayRepository;
		
	@Autowired
	private SessionInfoBean sessionInfoBean;

	private int childId;
	
	@Autowired
	private ChildRepository childRepository;
	
	private Date calDate = new Date();
	
	private TodaysKids newKid;
	
	private Collection<Child> parentsKids;
	
	public Date getCalDate() {
		return calDate;
	}
	public int getChildId() {
		return childId;
	}
	public void setChildId(String childName) {
		for(Child kid : parentsKids){
			if(kid.getName() == childName){
				this.childId = kid.getChildID();
			}
		}
	}
	public void setCalDate(Date calDate) {
		this.calDate = calDate;
	}
	
	public String getName(int childId){
		String name = null;
		Collection<Child> allKids = new HashSet<Child>();
		allKids.addAll(childRepository.findAll());
		
		for(Child kid : allKids){
			if(kid.getChildID() == childId){
				name = kid.getName();
			}
		}
				
		return name;
	}
	
	/*
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void addKidsToDay(){
	
		Collection<TodaysKids> kidDay = new HashSet<TodaysKids>();
		kidDay.addAll(dayRepository.findFirstByDate(calDate).getKidSet());	
		kidDay.addAll(todaysKidsRepository.getKidCollection(calDate));		
		
		Day selectedDay = dayRepository.findFirstByDate(calDate);
		if(selectedDay == null){
			Day newDay = new Day();
			newDay.setDate(calDate);
			dayRepository.save(newDay);
			selectedDay = newDay;
		}
		selectedDay.setKidSet(kidDay);
		dayRepository.save(selectedDay);
	}
	*/
	
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    public Collection<TodaysKids> getSignedKids(){
		Date tempDate = new Date();
				
		Collection<Day> dayCol = new HashSet<Day>();
		dayCol.addAll(dayRepository.findAll());
		boolean dayFound = false;
		while(dayFound == false){
		
			for(Day dayInCol:dayCol){
				if(dayInCol.getDate().getDate() == calDate.getDate() && dayInCol.getDate().getMonth() == calDate.getMonth() && dayInCol.getDate().getYear() == calDate.getYear()){
					tempDate = dayInCol.getDate();
					dayFound=true;
				}
			}
			break;
			
		}
		//evtl löschen
		if(dayFound == false){
			Collection<TodaysKids> noCol = new HashSet<TodaysKids>();
			return noCol;
		}
        return todaysKidsRepository.getKidCollection(dayRepository.findFirstByDate(tempDate).getDate());
        //if null return empty col
    }
	
	public Collection<TodaysKids> getSignedKidsParent(){
		Date tempDate = new Date();

		
		Collection<Day> dayCol = new HashSet<Day>();
		dayCol.addAll(dayRepository.findAll());
		boolean dayFound = false;
		while(dayFound == false){
		
			for(Day dayInCol:dayCol){
				if(dayInCol.getDate().getDate() == calDate.getDate() && dayInCol.getDate().getMonth() == calDate.getMonth() && dayInCol.getDate().getYear() == calDate.getYear()){
					tempDate = dayInCol.getDate();
					dayFound=true;
				}
			}
			break;
		}
		if(dayFound == false){
			Collection<TodaysKids> noCol = new HashSet<TodaysKids>();
			return noCol;
		}
		
		Collection<TodaysKids> allKidsDay = new HashSet<TodaysKids>();
        allKidsDay.addAll(todaysKidsRepository.getKidCollection(dayRepository.findFirstByDate(tempDate).getDate()));
        Collection<Child> parentsKids = new HashSet<Child>();

        parentsKids.addAll(sessionInfoBean.getCurrentUser().getChildSetUser());
        
        //System.out.println(parentsKids.toString());
        
        Collection<TodaysKids> parentsKidsToReturn = new HashSet<TodaysKids>();
		
		for(TodaysKids kidInCol:allKidsDay){
			for(Child kid:parentsKids){
				if(kidInCol.getChildId() == kid.getChildID()){
					parentsKidsToReturn.add(kidInCol);
				}
			}		
		}
			
		return parentsKidsToReturn;
    }
	
	
	public TodaysKids getNewKid() {
		return newKid;
	}
	public void setNewKid(TodaysKids newKid) {
		this.newKid = newKid;
	}
	
	public void saveNewKid() throws ParseException
	{			
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date dropOffTime = sdf.parse(newKid.getDropOffTime());
		Date pickUpTime = sdf.parse(newKid.getPickUpTime());
		Date maxVal = sdf.parse("23:59");
		
		if((dropOffTime.before(maxVal)) && (pickUpTime.before(maxVal)) && dropOffTime.before(pickUpTime) && newKid.getPickUpPerson() != null){

			newKid.setDate(calDate);
			
			Day selectedDay = dayRepository.findFirstByDate(calDate);
			if(selectedDay == null){
				Day newDay = new Day();
				newDay.setDate(calDate);
				dayRepository.save(newDay);
			}
			
			
			newKid.setChildId(childId);
			todaysKidsRepository.save(newKid);
			
			newKid = new TodaysKids();
		} else {		
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fehler bei der Anmeldung!", "Bitte überprüfen Sie Ihre Eingaben");
			RequestContext.getCurrentInstance().showMessageInDialog(message);
		}

	}
	
	
	//Get Individual Kids for the calendarlist.
	public Collection<String> getKids()
	{
		parentsKids = new HashSet<Child>();
		parentsKids.addAll(sessionInfoBean.getCurrentUser().getChildSetUser());
        Collection<String> parentsKidsToReturn = new HashSet<String>();
        for(Child kid:parentsKids){
			parentsKidsToReturn.add(kid.getName());
		}

		return parentsKidsToReturn;
	}
	
	//Get Acquaintances
	public Collection<Person> getAcquaintances()
	{
		Collection<Child> kids = new HashSet<Child>();
		kids.addAll(sessionInfoBean.getCurrentUser().getChildSetUser());
		Collection<Person> kidsAcquaintances = new HashSet<Person>();
		
		for(Child c : kids){
			kidsAcquaintances.addAll(c.getPersonSet());
		}
		
		return kidsAcquaintances;
	}
	
	public void setAcquaintances(String acquantainces){
		newKid.setPickUpPerson(acquantainces);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void deleteKidFromDay(int childId){
		Collection<TodaysKids> kidDay = new HashSet<TodaysKids>();
		
		
		kidDay.addAll(todaysKidsRepository.getKidCollection(calDate));
		for(TodaysKids kid:kidDay){
			if(kid.getChildId() == childId){
				todaysKidsRepository.delete(todaysKidsRepository.findFirstByTodaysKidsID(kid.getTodaysKidsID()));				
			}
		}

	}
	
	public String kidHasBirthday(int childId){
		Date kidsBirthday = new Date();
		kidsBirthday = childRepository.findFirstByChildID(childId).getBirthdate();
		
		if((kidsBirthday.getDate() == calDate.getDate() && kidsBirthday.getMonth() == calDate.getMonth() && kidsBirthday.getYear() == calDate.getYear())){
			return "ja";
		}
		
		return "nein";
	}
	
	public String kidHasLunch(boolean hasLunch){
		if (hasLunch == true){
			return "ja";
		} else {
			return "nein";
		}
		
	}
}
