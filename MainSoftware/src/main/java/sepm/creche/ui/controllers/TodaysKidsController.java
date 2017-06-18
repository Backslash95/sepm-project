package sepm.creche.ui.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sepm.creche.logger.AuditLog;
import sepm.creche.models.Child;
import sepm.creche.models.Day;
import sepm.creche.models.DayType;
import sepm.creche.models.Person;
import sepm.creche.models.TodaysKids;
import sepm.creche.models.User;
import sepm.creche.repositories.ChildRepository;
import sepm.creche.repositories.DayRepository;
import sepm.creche.repositories.TodaysKidsRepository;
import sepm.creche.services.UserService;
import sepm.creche.ui.beans.SessionInfoBean;

/**
 * Controller Class for the DayScheduler. This Class provides methods for
 * loading, saving and removing TodaysKids entities.
 *
 * @author Daniel, Fabian
 */

@Component
@Scope("session")
public class TodaysKidsController {

	public CrecheSettingsController getCrecheSettingsController() {
		return crecheSettingsController;
	}

	public void setCrecheSettingsController(CrecheSettingsController crecheSettingsController) {
		this.crecheSettingsController = crecheSettingsController;
	}

	public ChildRepository getChildRepository() {
		return childRepository;
	}

	public void setChildRepository(ChildRepository childRepository) {
		this.childRepository = childRepository;
	}

	/**
	 * Basic Constructor for TodaysKidsController.
	 */
	public TodaysKidsController() {
	}

	@Autowired
	private AuditLog auditLog;

	@Autowired
	private TodaysKidsRepository todaysKidsRepository;

	@Autowired
	private DayRepository dayRepository;

	@Autowired
	private SessionInfoBean sessionInfoBean;

	@Autowired
	private ChildRepository childRepository;

	@Autowired
	private CrecheSettingsController crecheSettingsController;

	@Autowired
	private UserService userService;

	private int childId;

	// used for the deadline to sign in a kid
	// private int deadline;
	// private int deadline = crecheSettingsController.getDeadline2();

	private Date calDate = new Date();

	private TodaysKids newKid = new TodaysKids();

	private Collection<Child> parentsKids;

	private String childIdFromName = null;

	private String acquaintance = null;

	private String dropOffTimeToDisplay = null;

	private String pickUpTimeToDisplay = null;

	// for monthly and yearly overview
	private int month;

	private int year = 0;

	// private double mealPrice;

	// private double mealPrice = crecheSettingsController.getLunchPrice2();

	private boolean monday, tuesday, wednesday, thursday, friday, saturday, lunch;

	private Date mondayDate, tuesdayDate, wednesdayDate, thursdayDate, fridayDate, saturdayDate;

	public String getDropOffTimeToDisplay() {
		return dropOffTimeToDisplay;
	}

	public void setDropOffTimeToDisplay(String dropOffTime) {
		this.dropOffTimeToDisplay = dropOffTime;
	}

	public String getPickUpTimeToDisplay() {
		return pickUpTimeToDisplay;
	}

	public void setPickUpTimeToDisplay(String pickUpTime) {
		this.pickUpTimeToDisplay = pickUpTime;
	}

	public Date getMondayDate() {
		return mondayDate;
	}

	public void setMondayDate(Date mondayDate) {
		this.mondayDate = mondayDate;
	}

	public Date getTuesdayDate() {
		return tuesdayDate;
	}

	public void setTuesdayDate(Date tuesdayDate) {
		this.tuesdayDate = tuesdayDate;
	}

	public Date getWednesdayDate() {
		return wednesdayDate;
	}

	public void setWednesdayDate(Date wednesdayDate) {
		this.wednesdayDate = wednesdayDate;
	}

	public Date getThursdayDate() {
		return thursdayDate;
	}

	public void setThursdayDate(Date thursdayDate) {
		this.thursdayDate = thursdayDate;
	}

	public Date getFridayDate() {
		return fridayDate;
	}

	public void setFridayDate(Date fridayDate) {
		this.fridayDate = fridayDate;
	}

	public Date getSaturdayDate() {
		return saturdayDate;
	}

	public void setSaturdayDate(Date saturdayDate) {
		this.saturdayDate = saturdayDate;
	}

	public boolean getLunch() {
		return lunch;
	}

	public void setLunch(boolean lunch) {
		this.lunch = lunch;
	}

	public boolean isMonday() {
		return monday;
	}

	public void setMonday(boolean monday) {
		this.monday = monday;
	}

	public boolean isTuesday() {
		return tuesday;
	}

	public void setTuesday(boolean tuesday) {
		this.tuesday = tuesday;
	}

	public boolean isWednesday() {
		return wednesday;
	}

	public void setWednesday(boolean wednesday) {
		this.wednesday = wednesday;
	}

	public boolean isThursday() {
		return thursday;
	}

	public void setThursday(boolean thursday) {
		this.thursday = thursday;
	}

	public boolean isFriday() {
		return friday;
	}

	public void setFriday(boolean friday) {
		this.friday = friday;
	}

	public boolean isSaturday() {
		return saturday;
	}

	public void setSaturday(boolean saturday) {
		this.saturday = saturday;
	}

	private Collection<Date> datesToAdd = new HashSet<Date>();

	public Collection<Date> getDatesToAdd() {
		return datesToAdd;
	}

	public void setDatesToAdd(Collection<Date> datesToAdd) {
		this.datesToAdd = datesToAdd;
	}

	public void addDate() {
		if (monday == true) {
			datesToAdd.add(mondayDate);
		}
		if (tuesday == true) {
			datesToAdd.add(tuesdayDate);
		}
		if (wednesday == true) {
			datesToAdd.add(wednesdayDate);
		}
		if (thursday == true) {
			datesToAdd.add(thursdayDate);
		}
		if (friday == true) {
			datesToAdd.add(fridayDate);
		}
		if (saturday == true) {
			datesToAdd.add(saturdayDate);
		}
		try {
			saveNewKid();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	/*
	 * public int getDeadline() { return deadline; }
	 * 
	 * public void setDeadline(int deadline) { this.deadline = deadline; }
	 */

	public int getChildId() {
		return childId;
	}

	public void setChildId(int childId) {
		this.childId = childId;
	}

	public Date getCalDate() {
		return calDate;
	}

	public void setCalDate(Date calDate) {
		this.calDate = calDate;
		setDatesFromCalDate();
	}

	@SuppressWarnings("deprecation")
	public void setDatesFromCalDate() {
		// calDate = specified Date
		int tempDay = calDate.getDay();

		mondayDate = new Date();
		tuesdayDate = new Date();
		wednesdayDate = new Date();
		thursdayDate = new Date();
		fridayDate = new Date();
		saturdayDate = new Date();
		mondayDate.setMonth(calDate.getMonth());
		tuesdayDate.setMonth(calDate.getMonth());
		wednesdayDate.setMonth(calDate.getMonth());
		;
		thursdayDate.setMonth(calDate.getMonth());
		fridayDate.setMonth(calDate.getMonth());
		saturdayDate.setMonth(calDate.getMonth());

		switch (tempDay) {
		case (1):// if selected Day is a Monday
			mondayDate.setDate(calDate.getDate());
			tuesdayDate.setDate(calDate.getDate() + 1);
			wednesdayDate.setDate(calDate.getDate() + 2);
			thursdayDate.setDate(calDate.getDate() + 3);
			fridayDate.setDate(calDate.getDate() + 4);
			saturdayDate.setDate(calDate.getDate() + 5);
			break;
		case (2):// if selected Day is a Tuesday
			mondayDate.setDate(calDate.getDate() - 1);
			tuesdayDate.setDate(calDate.getDate());
			wednesdayDate.setDate(calDate.getDate() + 1);
			thursdayDate.setDate(calDate.getDate() + 2);
			fridayDate.setDate(calDate.getDate() + 3);
			saturdayDate.setDate(calDate.getDate() + 4);
			break;
		case (3):// if selected Day is a Wednesday
			mondayDate.setDate(calDate.getDate() - 2);
			tuesdayDate.setDate(calDate.getDate() - 1);
			wednesdayDate.setDate(calDate.getDate());
			thursdayDate.setDate(calDate.getDate() + 1);
			fridayDate.setDate(calDate.getDate() + 2);
			saturdayDate.setDate(calDate.getDate() + 3);
			break;
		case (4):// if selected Day is a Thursday
			mondayDate.setDate(calDate.getDate() - 3);
			tuesdayDate.setDate(calDate.getDate() - 2);
			wednesdayDate.setDate(calDate.getDate() - 1);
			thursdayDate.setDate(calDate.getDate());
			fridayDate.setDate(calDate.getDate() + 1);
			saturdayDate.setDate(calDate.getDate() + 2);
			break;
		case (5):// if selected Day is a Friday
			mondayDate.setDate(calDate.getDate() - 4);
			tuesdayDate.setDate(calDate.getDate() - 3);
			wednesdayDate.setDate(calDate.getDate() - 2);
			thursdayDate.setDate(calDate.getDate() - 1);
			fridayDate.setDate(calDate.getDate());
			saturdayDate.setDate(calDate.getDate() + 1);
			break;
		case (6):// if selected Day is a Saturday
			mondayDate.setDate(calDate.getDate() - 5);
			tuesdayDate.setDate(calDate.getDate() - 4);
			wednesdayDate.setDate(calDate.getDate() - 3);
			thursdayDate.setDate(calDate.getDate() - 2);
			fridayDate.setDate(calDate.getDate() - 1);
			saturdayDate.setDate(calDate.getDate());
			break;
		case (0):// if selected Day is a Sunday
			mondayDate.setDate(calDate.getDate() + 1);
			tuesdayDate.setDate(calDate.getDate() + 2);
			wednesdayDate.setDate(calDate.getDate() + 3);
			thursdayDate.setDate(calDate.getDate() + 4);
			fridayDate.setDate(calDate.getDate() + 5);
			saturdayDate.setDate(calDate.getDate() + 6);
		}
	}

	public TodaysKids getNewKid() {
		return newKid;
	}

	public void setNewKid(TodaysKids newKid) {
		this.newKid = newKid;
	}

	/**
	 * This method takes a String as argument and checks the parentsKids for a
	 * matching name to set the kids childId
	 * 
	 * @param childName
	 *            The childName whose ID is to be found.
	 */
	public void setChildIdFromName(String childIdFromName) {
		for (Child kid : parentsKids) {
			if (kid.getName().equals(childIdFromName)) {
				this.childId = kid.getChildID();
				this.childIdFromName = childIdFromName;
			}
		}
	}

	/**
	 * Dummy method, so SelectOneMenu on DaySchedulerParent.xhtml works
	 * 
	 * @return a non used String
	 */
	public String getChildIdFromName() {
		return childIdFromName;
	}

	/**
	 * Returns a Collection of all signed TodaysKids for a specific calendar
	 * Date (calDate).
	 * 
	 * @param childId
	 *            The childId whose name is to be found.
	 * @return Name of the child.
	 */
	public String getName(int childId) {
		String name = null;

		Collection<Child> allKids = new HashSet<Child>();
		allKids.addAll(childRepository.findAll());

		// go through the collection and look for a matching childId
		for (Child kid : allKids) {
			if (kid.getChildID() == childId) {
				name = kid.getName();
			}
		}

		return name;
	}

	/**
	 * Returns a Collection of all signed TodaysKids for a specific calendar
	 * Date (calDate).
	 * 
	 * @return the created collection of TodaysKids.
	 */
	@SuppressWarnings("deprecation")
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public Collection<TodaysKids> getSignedKids() {
		Date tempDate = new Date();

		Collection<Day> dayCol = new HashSet<Day>();
		// load in all Days from the DB into dayCol
		dayCol.addAll(dayRepository.findAll());

		boolean dayFound = false;
		while (dayFound == false) {

			for (Day dayInCol : dayCol) {
				// checks if there is a Day in the collection where Date, Month
				// and Year of the specific calendar Date matches the Date in
				// the collection
				if (dayInCol.getDate().getDate() == calDate.getDate()
						&& dayInCol.getDate().getMonth() == calDate.getMonth()
						&& dayInCol.getDate().getYear() == calDate.getYear()) {
					// when a matching Day is found it is saved in tempDate
					tempDate = dayInCol.getDate();
					dayFound = true;
				}
			}
			break;

		}
		// if no matching Day was found, an empty collection is returned
		if (dayFound == false) {
			Collection<TodaysKids> noCol = new HashSet<TodaysKids>();
			return noCol;
		}
		return todaysKidsRepository.getKidCollection(dayRepository.findFirstByDate(tempDate).getDate());
	}

	/**
	 * Returns a Collection of TodaysKids for a specific calendar Date (calDate)
	 * and a specific user (parent).
	 * 
	 * @return the created collection of TodaysKids.
	 */
	@SuppressWarnings("deprecation")
	public Collection<TodaysKids> getSignedKidsParent() {
		Date tempDate = new Date();

		Collection<Day> dayCol = new HashSet<Day>();
		// load in all Days from the DB into dayCol
		dayCol.addAll(dayRepository.findAll());
		boolean dayFound = false;
		while (dayFound == false) {

			for (Day dayInCol : dayCol) {
				// checks if there is a Day in the collection where Date, Month
				// and Year of the specific calendar Date matches the Date in
				// the collection
				if (dayInCol.getDate().getDate() == calDate.getDate()
						&& dayInCol.getDate().getMonth() == calDate.getMonth()
						&& dayInCol.getDate().getYear() == calDate.getYear()) {
					// when a matching Day is found it is saved in tempDate
					tempDate = dayInCol.getDate();
					dayFound = true;
				}
			}
			break;
		}
		// if no matching Day was found, an empty collection is returned
		if (dayFound == false) {
			Collection<TodaysKids> noCol = new HashSet<TodaysKids>();
			return noCol;
		}

		Collection<TodaysKids> allKidsDay = new HashSet<TodaysKids>();
		// load in TodaysKids which are signed to tempDate
		allKidsDay.addAll(todaysKidsRepository.getKidCollection(dayRepository.findFirstByDate(tempDate).getDate()));

		Collection<Child> parentsKids = new HashSet<Child>();
		// load in Childs which belong to the specific user (parent)
		parentsKids.addAll(sessionInfoBean.getCurrentUser().getMyChildren());

		Collection<TodaysKids> parentsKidsToReturn = new HashSet<TodaysKids>();
		// find the entries in TodaysKids and Child where the childID matches
		for (TodaysKids kidInCol : allKidsDay) {
			for (Child kid : parentsKids) {
				if (kidInCol.getChildId() == kid.getChildID()) {
					parentsKidsToReturn.add(kidInCol);
				}
			}
		}

		return parentsKidsToReturn;
	}

	/**
	 * Saves a new entry in TodaysKids. The attributes for the new TodaysKid are
	 * set from the .xhtml. The method also checks for maximum occupancy,
	 * holiday and if the selected Day already exists in the DB.
	 */
	@SuppressWarnings("deprecation")
	public void saveNewKid() throws ParseException {
		for (Date date : datesToAdd) {
			calDate.setDate(date.getDate());
			calDate.setMonth(date.getMonth());
			calDate.setYear(date.getYear());

			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Date dropOffTime = sdf.parse(getDropOffTimeToDisplay());
			Date pickUpTime = sdf.parse(getPickUpTimeToDisplay());
			Date maxVal = sdf.parse("23:59");

			String drop = getDropOffTimeToDisplay().substring(0, 2);
			String drop1 = getDropOffTimeToDisplay().substring(3, 5);

			int dropO = Integer.parseInt(drop);
			int dropO1 = Integer.parseInt(drop1);

			String pick = getPickUpTimeToDisplay().substring(0, 2);
			String pick1 = getPickUpTimeToDisplay().substring(3, 5);

			int pickU = Integer.parseInt(pick);
			int pickU1 = Integer.parseInt(pick1);

			Date dropTime = new Date();
			dropTime.setDate(calDate.getDate());
			dropTime.setHours(dropO);
			dropTime.setMinutes(dropO1);

			Date pickTime = new Date();
			pickTime.setDate(calDate.getDate());
			pickTime.setHours(pickU);
			pickTime.setMinutes(pickU1);

			Date beginDropOff = new Date();
			Date endDropOff = new Date();

			Date beginPickUp = new Date();
			Date endPickUp = new Date();

			beginDropOff.setDate(calDate.getDate());
			beginDropOff.setHours(Integer.parseInt(crecheSettingsController.getBeginDropOffTime2().substring(0, 2)));
			beginDropOff.setMinutes(Integer.parseInt(crecheSettingsController.getBeginDropOffTime2().substring(3, 5)));
			endDropOff.setDate(calDate.getDate());
			endDropOff.setHours(Integer.parseInt(crecheSettingsController.getEndDropOffTime2().substring(0, 2)));
			endDropOff.setMinutes(Integer.parseInt(crecheSettingsController.getEndDropOffTime2().substring(3, 5)));

			beginPickUp.setDate(calDate.getDate());
			beginPickUp.setHours(Integer.parseInt(crecheSettingsController.getBeginPickUpTime2().substring(0, 2)));
			beginPickUp.setMinutes(Integer.parseInt(crecheSettingsController.getBeginPickUpTime2().substring(3, 5)));
			endPickUp.setDate(calDate.getDate());
			endPickUp.setHours(Integer.parseInt(crecheSettingsController.getEndPickUpTime2().substring(0, 2)));
			endPickUp.setMinutes(Integer.parseInt(crecheSettingsController.getEndPickUpTime2().substring(3, 5)));

			// Latest possible Date to add Kids is 'deadline' days before
			Date latestDate = new Date();
			latestDate.setHours(23);
			latestDate.setMinutes(59);
			latestDate.setDate(latestDate.getDate() + crecheSettingsController.getDeadline2());
			latestDate.setSeconds(59);

			Collection<Day> dayCol = new HashSet<Day>();
			dayCol.addAll(dayRepository.findAll());

			boolean dayExists = false;

			// check if the calDate already exists in Day DB
			for (Day dayInCol : dayCol) {
				if (dayInCol.getDate().getDate() == calDate.getDate()
						&& dayInCol.getDate().getMonth() == calDate.getMonth()
						&& dayInCol.getDate().getYear() == calDate.getYear()) {
					dayExists = true;
					break;
				}
			}

			// if selected DropOffTime or PickUpTime is not in the boundaries
			if (dropTime.before(beginDropOff) || dropTime.after(endDropOff) || pickTime.before(beginPickUp)
					|| pickTime.after(endPickUp)) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fehler bei der Anmeldung!",
						"Bitte orientieren Sie sich an den angegebenen Bring- und Abholzeiten.");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				return;
			}

			// if the selected calDate does not exist in the Day DB, the new
			// TodaysKid will not be saved and an error message will be
			// displayed
			if (dayExists == false) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fehler bei der Anmeldung!",
						"Für den ausgewählten Tag wurde noch keine maximal Belegung eingetragen.");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				return;
			}

			// check if the selected calDate is after the latestDate
			if (calDate.before(latestDate)) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fehler bei der Anmeldung!",
						"Der Anmeldeschluss für das gewählte Datum wurde bereits überschritten. Bitte wählen Sie ein späteres Datum.");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				return;
			}

			// if the selected calDate exists in the DB, the entry for DayType
			// is checked - if it is not OCCUPATION an error message is
			// displayed
			if (dayRepository.findFirstByDate(calDate).getDayType() != DayType.OCCUPATION) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fehler bei der Anmeldung!",
						"Der ausgewählte Tag ist ein Urlaubs- oder Ferientag.");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				return;
			}

			// check for the signOutDate, if it is not Null, the kids either
			// inactive or the deregister Date is already set for the future
			if ((childRepository.findFirstByChildID(childId).getSignOutDate()) != null) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fehler bei der Anmeldung!",
						"Ihr Kind ist bereits von der Kinderkrippe abgemeldet.");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				return;
			}

			if (calDate.after(childRepository.findFirstByChildID(childId).getGivenSignOutDate())) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fehler bei der Anmeldung!",
						"Ihr Kind ist zu diesem Zeitpunkt bereits von der Kinderkrippe abgemeldet.");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				return;
			}

			newKid.setDropOffTime(dropOffTimeToDisplay);
			newKid.setPickUpTime(pickUpTimeToDisplay);
			newKid.setPickUpPerson(acquaintance);
			newKid.setLunch(lunch);

			// check if all attributes are set for the new TodaysKid
			if ((dropOffTime.before(maxVal)) && (pickUpTime.before(maxVal)) && dropOffTime.before(pickUpTime)
					&& getAcquaintance() != null) {
				// set the signedDate of the new Kid
				newKid.setDate(calDate);

				Collection<TodaysKids> allKids = new HashSet<TodaysKids>();
				allKids.addAll(todaysKidsRepository.findAll());
				Collection<TodaysKids> attendanceDay = new HashSet<TodaysKids>();

				// check TodaysKids for already assigned Kids, to not exceed the
				// max occupation
				for (TodaysKids tk : allKids) {
					if (tk.getDate().getDate() == calDate.getDate() && tk.getDate().getMonth() == calDate.getMonth()
							&& tk.getDate().getYear() == calDate.getYear()) {
						attendanceDay.add(tk);
					}
				}

				newKid.setChildId(childId);

				for (TodaysKids tk : attendanceDay) {
					if (tk.getChildId() == newKid.getChildId()) {
						FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fehler bei der Anmeldung!",
								"Ihr Kind ist bereits an diesem Tag angemeldet.");
						RequestContext.getCurrentInstance().showMessageInDialog(message);
						return;
					}
				}

				// if the max occupation is not exceeded, the new TodaysKid is
				// saved, otherwise an error message is displayed
				if (dayRepository.findFirstByDate(calDate).getMaxOccupation() > attendanceDay.size()) {
					if (newKid.getLunch() == true) {
						auditLog.log(sessionInfoBean.getCurrentUser(),
								"The Child " + childRepository.findFirstByChildID(newKid.getChildId()).getName()
										+ " was registered for lunch on " + date);
					}

					for (Person p : childRepository.findFirstByChildID((newKid.getChildId())).getMyRelatives()) {

						if (p.getName().equals(newKid.getPickUpPerson())) {
							auditLog.log(sessionInfoBean.getCurrentUser(), "As PickUpPerson for "
									+ childRepository.findFirstByChildID(newKid.getChildId()).getName() + " on " + date
									+ " the childs relative " + newKid.getPickUpPerson() + " was registered.");
						}
					}

					todaysKidsRepository.save(newKid);
				} else {
					auditLog.log(sessionInfoBean.getCurrentUser(),
							"On " + date + " the maximum occupancy has been reached.");
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fehler bei der Anmeldung!",
							"Die maximal Belegung ist bereits erreicht.");
					RequestContext.getCurrentInstance().showMessageInDialog(message);
				}

				newKid = new TodaysKids();
			} else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fehler bei der Anmeldung!",
						"Bitte überprüfen Sie Ihre Eingaben.");
				RequestContext.getCurrentInstance().showMessageInDialog(message);
				return;
			}
		}
		datesToAdd.clear();
		setMonday(false);
		setTuesday(false);
		setWednesday(false);
		setThursday(false);
		setFriday(false);
		setSaturday(false);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg!",
				"Ihr Kind wurde erfolgreich angemeldet.");
		RequestContext.getCurrentInstance().showMessageInDialog(message);
	}

	/**
	 * This method returns a collection of child names (Strings). This
	 * collection is used for parents, to only display their own kids.
	 * 
	 * @return collection of Strings (child names belonging to a parent
	 */
	public List<String> getKids() {
		parentsKids = new HashSet<Child>();
		// add all childs from the logged in user to collection parentsKids
		parentsKids.addAll(sessionInfoBean.getCurrentUser().getMyChildren());
		List<String> parentsKidsToReturn = new ArrayList<String>();
		for (Child kid : parentsKids) {
			if(!kid.isDeregistered()){
				parentsKidsToReturn.add(kid.getName());
			}
		}

		return parentsKidsToReturn;
	}

	/**
	 * This method returns a collection of the childs acquaintances. This
	 * collection is used for parents, to display their kids acquantainces.
	 * 
	 * @return collection of persons (child names belonging to a parent).
	 */
	public List<String> getAcquaintances() {
		Collection<Child> kids = new HashSet<Child>();
		kids.addAll(userService.loadUser(sessionInfoBean.getCurrentUser().getUsername()).getMyChildren());
		List<Person> kidsAcquaintances = new ArrayList<Person>();
		List<String> kidsAcquaintancesNames = new ArrayList<String>();

		for (Child c : kids) {

			for (Person p : c.getMyRelatives()) {
				if (p.isActivated())
					kidsAcquaintances.add(p);
			}

			for (User u : c.getMyParents()) {
				if (!kidsAcquaintancesNames.contains(u.getFirstName() + " " + u.getLastName())) {
					kidsAcquaintancesNames.add(u.getFirstName() + " " + u.getLastName());
				}
			}

		}

		for (Person p : kidsAcquaintances) {
			if (!kidsAcquaintancesNames.contains(p.getName())) {
				kidsAcquaintancesNames.add(p.getName());
			}
		}
		return kidsAcquaintancesNames;
	}

	/**
	 * This method sets the pickUpPerson for the new TodaysKid.
	 * 
	 */
	public void setAcquaintance(String acquaintance) {
		this.acquaintance = acquaintance;
	}

	public String getAcquaintance() {
		return acquaintance;
	}

	/**
	 * Method to edit a kids note for a specified day, can be edited until
	 * midnight the day before the aimed day. Employees can always edit the
	 * note.
	 *
	 * @param note:
	 *            the text, which will be added.
	 */
	@SuppressWarnings("deprecation")
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void editKidsNote(String note) {
		Collection<TodaysKids> kidDay = new HashSet<TodaysKids>();
		kidDay.addAll(todaysKidsRepository.getKidCollection(calDate));

		// Latest possible Date to edit the kids note: the day before at 23:59
		Date latestDate = new Date();
		latestDate.setHours(23);
		latestDate.setMinutes(59);
		latestDate.setDate(calDate.getDate() - 1);
		latestDate.setSeconds(59);

		// Goes through all the kids of the specified Date, checks if the
		// deadline is not due yet or if its a Employee.
		// Otherwise creates a new message and sets the note empty.
		for (TodaysKids kid : kidDay) {
			if (kid.getChildId() == childId) {
				if (sessionInfoBean.getCurrentUserRoles().contains("EMPLOYEE") || new Date().before(latestDate)) {
					kid.setNotes(note);
				} else {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fehler bei der Notiz!",
							"Die Ã„nderungszeit ist bereits abgelaufen, bitte melden Sie sich telefonisch beim Kinderkrippen Personal.");
					RequestContext.getCurrentInstance().showMessageInDialog(message);
					kid.setNotes("");
				}
			}
		}
	}

	/**
	 * Method to delete a kid from a specified day. Parents can delete their kid
	 * before the deadline (midnight 5 days before the aimed day). If it is too
	 * late to delete the kid, parents still have the possibility to add a note.
	 * Employees can always delete a kid from a specified day.
	 *
	 * @param childId:
	 *            the child, who needs to be deleted.
	 */
	@SuppressWarnings("deprecation")
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void deleteKidFromDay(int childId) {

		Collection<TodaysKids> kidDay = new HashSet<TodaysKids>();
		kidDay.addAll(todaysKidsRepository.getKidCollection(calDate));

		// Latest possible Date to delete the kid from the day: 'deadline' days
		// before at 23:59
		Date latestDate = new Date();
		latestDate.setHours(23);
		latestDate.setMinutes(59);
		latestDate.setDate(calDate.getDate() - crecheSettingsController.getDeadline2());
		Date currentDate = new Date();

		// Go through all the kids of the specified day.
		// When it's too late (Parent) to delete the kid, it creates a message.
		for (TodaysKids kid : kidDay) {
			if (kid.getChildId() == childId) {
				if (sessionInfoBean.getCurrentUserRoles().contains("EMPLOYEE") || currentDate.before(latestDate)) {
					todaysKidsRepository.delete(todaysKidsRepository.findFirstByTodaysKidsID(kid.getTodaysKidsID()));
				} else {
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Fehler bei der Abmeldung!",
							"Die Abmeldezeit ist bereits abgelaufen, bitte fÃ¼gen Sie eine Notiz hinzu.");
					RequestContext.getCurrentInstance().showMessageInDialog(message);
				}
			}
		}

	}

	/**
	 * Checks, whether the kid has birthday on this day or not.
	 *
	 * @param childId:
	 *            the child, whose birthday has to be controlled.
	 * @return the created String, based on the check.
	 */
	@SuppressWarnings("deprecation")
	public String kidHasBirthday(int childId) {
		Date kidsBirthday = new Date();
		kidsBirthday = childRepository.findFirstByChildID(childId).getBirthdate();

		if ((kidsBirthday.getDate() == calDate.getDate() && kidsBirthday.getMonth() == calDate.getMonth())) {
			return "ja";
		}

		return "nein";
	}

	/**
	 * Returns a String. "Ja" if the child gets lunch. "Nein" if it doesn't.
	 *
	 * @param hasLunch
	 *            boolean argument for the lunch
	 * @return the created string, based on the check.
	 */
	public String kidHasLunch(boolean hasLunch) {
		if (hasLunch == true) {
			return "ja";
		} else {
			return "nein";
		}

	}

	/**
	 * Sums up the Lunches, the kid consumes in nursery in a specified year. .
	 *
	 * @param childId
	 *            the child, whose lunches needs to be found.
	 * @return an Integer of how much lunches a kid had.
	 */
	@SuppressWarnings("deprecation")
	public int getAmountOfLunchYear(int childId) {
		int amount = 0;

		if (getYear() == 0) {
			setYear(Calendar.getInstance().get(Calendar.YEAR));
		}

		Collection<TodaysKids> allKids = new HashSet<TodaysKids>();
		allKids.addAll(todaysKidsRepository.findAll());

		Date beginMonth = new Date();
		Date endMonth = new Date();

		// set the first Date to 01 of the set month & year, Time: 00:00:00
		beginMonth.setYear(year - 1900);
		beginMonth.setMonth(0);
		beginMonth.setDate(01);
		beginMonth.setHours(00);
		beginMonth.setMinutes(00);
		beginMonth.setSeconds(0);

		// set the last Date to the last Day of the set month & year, Time:
		// 23:59:59
		endMonth.setYear(year - 1900);
		endMonth.setMonth(11);
		endMonth.setDate(31);
		endMonth.setHours(23);
		endMonth.setMinutes(59);
		endMonth.setSeconds(59);

		// Go through all Kids, if the kid attended that Day, add +1 to the
		// counter
		// Set the seconds +10, because Signed Date is at 00:00:00 and may
		// output an incorrect number
		for (TodaysKids tk : allKids) {
			if (tk.getChildId() == childId) {
				tk.getDate().setSeconds(10);
				if ((tk.getDate().after(beginMonth) && tk.getDate().before(endMonth))) {

					if (tk.getLunch() == true) {

						amount++;
					}
				}
			}
		}

		return amount;
	}

	/**
	 * Sums up the Lunches, the kid consumes in nursery. Only for monthly
	 * output.
	 *
	 * @param childId
	 *            the child, whose lunches needs to be found.
	 * @return an Integer of how much lunches a kid had.
	 */
	@SuppressWarnings("deprecation")
	public int getAmountOfLunch(int childId) {
		int amount = 0;

		if (getYear() == 0) {
			setYear(Calendar.getInstance().get(Calendar.YEAR));
		}

		Collection<TodaysKids> allKids = new HashSet<TodaysKids>();
		allKids.addAll(todaysKidsRepository.findAll());
		Date beginMonth = new Date();
		Date endMonth = new Date();

		// set the first Date to 01 of the set month & year, Time: 00:00:00
		beginMonth.setYear(year - 1900);
		beginMonth.setMonth(month);
		beginMonth.setDate(01);
		beginMonth.setHours(00);
		beginMonth.setMinutes(00);
		beginMonth.setSeconds(0);

		Calendar cal = Calendar.getInstance();
		cal.set(year, month, 1);

		// set the last Date to the last Day of the set month & year, Time:
		// 23:59:59
		endMonth.setYear(year - 1900);
		endMonth.setMonth(month);
		endMonth.setDate(cal.getActualMaximum(Calendar.DATE));
		endMonth.setHours(23);
		endMonth.setMinutes(59);
		endMonth.setSeconds(59);

		// Go through all Kids, if the kid attended that Day, add +1 to the
		// counter
		// Set the seconds +10, because Signed Date is at 00:00:00 and may
		// output an incorrect number
		for (TodaysKids tk : allKids) {
			if (tk.getChildId() == childId) {
				tk.getDate().setSeconds(10);
				if ((tk.getDate().after(beginMonth) && tk.getDate().before(endMonth))) {

					if (tk.getLunch() == true) {

						amount++;
					}
				}
			}
		}

		return amount;
	}

	public TodaysKidsRepository getTodaysKidsRepository() {
		return todaysKidsRepository;
	}

	public void setTodaysKidsRepository(TodaysKidsRepository todaysKidsRepository) {
		this.todaysKidsRepository = todaysKidsRepository;
	}

	/**
	 * Sums up the Days, the kid was in nursery in a specified year.
	 *
	 * @param childId
	 *            the child's id, whose days needs to be found.
	 * @return an Integer of how much days a kid has attended.
	 */
	@SuppressWarnings("deprecation")
	public int getAmountOfAttendanceYear(int childId) {
		int amount = 0;

		if (getYear() == 0) {
			setYear(Calendar.getInstance().get(Calendar.YEAR));
		}

		Collection<TodaysKids> allKids = new HashSet<TodaysKids>();
		allKids.addAll(todaysKidsRepository.findAll());

		Date beginMonth = new Date();
		Date endMonth = new Date();

		// set the first Date to 01 of the set month & year, Time: 00:00:00
		beginMonth.setYear(year - 1900);
		beginMonth.setMonth(0);
		beginMonth.setDate(01);
		beginMonth.setHours(00);
		beginMonth.setMinutes(00);
		beginMonth.setSeconds(0);

		// set the last Date to the last Day of the set month & year, Time:
		// 23:59:59
		endMonth.setYear(year - 1900);
		endMonth.setMonth(11);
		endMonth.setDate(31);
		endMonth.setHours(23);
		endMonth.setMinutes(59);
		endMonth.setSeconds(59);

		// Go through all Kids, if the kid attended that Day, add +1 to the
		// counter
		// Set the seconds +10, because Signed Date is at 00:00:00 and may
		// output an incorrect number
		for (TodaysKids tk : allKids) {
			if (tk.getChildId() == childId) {
				tk.getDate().setSeconds(10);
				if ((tk.getDate().after(beginMonth) && tk.getDate().before(endMonth))) {

					if (tk.getLunch() == true) {

						amount++;
					}
				}
			}
		}

		return amount;
	}

	/**
	 * Sums up the Days, the kid was in nursery. Only for monthly output.
	 *
	 * @param childId
	 *            the child's id, whose days needs to be found.
	 * @return an Integer of how much days a kid has attended.
	 */
	@SuppressWarnings("deprecation")
	public int getAmountOfAttendance(int childId) {
		int amount = 0;

		if (getYear() == 0) {
			setYear(Calendar.getInstance().get(Calendar.YEAR));
		}

		Collection<TodaysKids> allKids = new HashSet<TodaysKids>();
		allKids.addAll(todaysKidsRepository.findAll());

		Date beginMonth = new Date();
		Date endMonth = new Date();

		// set the first Date to 01 of the set month & year, Time: 00:00:00
		beginMonth.setYear(year - 1900);
		beginMonth.setMonth(month);
		beginMonth.setDate(01);
		beginMonth.setHours(00);
		beginMonth.setMinutes(00);
		beginMonth.setSeconds(0);

		Calendar cal = Calendar.getInstance();
		cal.set(year, month, 1);

		// set the last Date to the last Day of the set month & year, Time:
		// 23:59:59
		endMonth.setYear(year - 1900);
		endMonth.setMonth(month);
		endMonth.setDate(cal.getActualMaximum(Calendar.DATE));
		endMonth.setHours(23);
		endMonth.setMinutes(59);
		endMonth.setSeconds(59);

		// Go through all Kids, if the kid attended that Day, add +1 to the
		// counter
		// Set the seconds +10, because Signed Date is at 00:00:00 and may
		// output an incorrect number
		for (TodaysKids tk : allKids) {
			if (tk.getChildId() == childId) {
				tk.getDate().setSeconds(10);
				if ((tk.getDate().after(beginMonth) && tk.getDate().before(endMonth))) {

					if (tk.getLunch() == true) {

						amount++;
					}
				}
			}
		}

		return amount;
	}

	/**
	 * Returns all Kids from a specified Parent.
	 *
	 * @return a Collection of Child's.
	 */
	public Collection<Child> getParentsKids() {
		Collection<Child> parentsKids = new HashSet<Child>();
		parentsKids.addAll(sessionInfoBean.getCurrentUser().getMyChildren());
		return parentsKids;
	}

	/**
	 * Returns all Kids for EmployeeView which attended a specified year.
	 *
	 * @return a Collection of Child's.
	 */
	public Collection<Child> getAllKidsYear() {
		Collection<Child> allKids = new HashSet<Child>();
		allKids.addAll(childRepository.findAll());
		Collection<Child> childsToReturn = new HashSet<Child>();
		childsToReturn.addAll(allKids);
		for (Child c : allKids) {
			if (getAmountOfAttendanceYear(c.getChildID()) == 0) {
				childsToReturn.remove(c);
			}
		}

		return childsToReturn;
	}

	/**
	 * Returns all Kids for EmployeeView which attended a specified month.
	 *
	 * @return a Collection of Child's.
	 */
	public Collection<Child> getAllKidsMonth() {
		Collection<Child> allKids = new HashSet<Child>();
		allKids.addAll(childRepository.findAll());
		Collection<Child> childsToReturn = new HashSet<Child>();
		childsToReturn.addAll(allKids);
		for (Child c : allKids) {
			if (getAmountOfAttendance(c.getChildID()) == 0) {
				childsToReturn.remove(c);
			}
		}

		return childsToReturn;
	}

	/**
	 * Returns the total cost of meals for month.
	 *
	 * @return String of total cost of meals.
	 */
	public String getCostOfLunchMonth(int childId) {
		double totalCost = 0;
		totalCost = getAmountOfLunch(childId) * crecheSettingsController.getLunchPrice2();
		totalCost = Math.round(totalCost * 100.0) / 100.0;
		String price = Double.toString(totalCost) + "€";
		return price;
	}

	/**
	 * Returns the total cost of meals for year.
	 *
	 * @return String of total cost of meals.
	 */
	public String getCostOfLunchYear(int childId) {
		double totalCost = 0;
		totalCost = getAmountOfLunchYear(childId) * crecheSettingsController.getLunchPrice2();
		totalCost = Math.round(totalCost * 100.0) / 100.0;
		String price = Double.toString(totalCost) + "€";
		return price;
	}
}