package sepm.creche.ui.beans;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.faces.event.ActionEvent;

import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sepm.creche.models.Child;
import sepm.creche.models.TodaysKids;
import sepm.creche.repositories.ChildRepository;
import sepm.creche.repositories.TodaysKidsRepository;

/**
 * CalendarView Class for the DayScheduler. Provides basic methods for loading
 * the attended Days into the Parents/Employee View.
 *
 * @author Daniel, Fabian
 */
@Component
@Scope("session")
public class CalendarViewDS implements Serializable {

	/**
	 * Necessary attributes for the class.
	 */
	private static final long serialVersionUID = 1L;

	private ScheduleModel lazyModel;

	private ScheduleEvent event = new DefaultScheduleEvent();

	/**
	 * Necessary wired beans for the class.
	 */
	@Autowired
	private TodaysKidsRepository todaysKidsRepository;

	@Autowired
	private SessionInfoBean sessionInfoBean;

	@Autowired
	private ChildRepository childRepository;

	/**
	 * Constructor for the Calendar as LazyModel.
	 *
	 */
	public CalendarViewDS() {

		lazyModel = new LazyScheduleModel() {

			private static final long serialVersionUID = 1L;

			@Override
			public void loadEvents(Date start, Date end) {
				try {
					createDayEvents();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		};

	}

	public ScheduleModel getLazyModel() {
		return lazyModel;
	}

	public void setLazyModel(ScheduleModel lazyModel) {
		this.lazyModel = lazyModel;
	}

	/**
	 * Creates all Events based on whether the current User is an Employee or a
	 * parent. In Case it is an Parent, it receives their kids and adds it. In
	 * Case it is an Employee, it just received every kid.
	 *
	 * 
	 * @throws ParseException
	 *             if the built Date (as StringBuilder) wasn't parsed properly.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void createDayEvents() throws ParseException {

		if (sessionInfoBean.getCurrentUserRoles().contains("PARENT")) {

			// Get all Kids from this specific parent
			Collection<Child> parentsKids = new HashSet<Child>();
			parentsKids.addAll(sessionInfoBean.getCurrentUser().getMyChildren());
			Collection<TodaysKids> allKids = new HashSet<TodaysKids>();
			allKids.addAll(todaysKidsRepository.findAll());

			StringBuilder startDate = new StringBuilder();
			StringBuilder endDate = new StringBuilder();
			String startDateAsString;
			String endDateAsString;

			// Go through all kids, go through all Kids of all Days and check,
			// whether the Kid was there on a day or not
			// If it attended the day, create an event and save it
			for (Child c : parentsKids) {
				for (TodaysKids tk : allKids) {

					startDate.delete(0, startDate.length());
					endDate.delete(0, endDate.length());

					if (tk.getChildId() == c.getChildID()) {
						DefaultScheduleEvent toAdd = new DefaultScheduleEvent();

						startDate.append(tk.getDate());

						startDate.delete(11, 21);

						startDate.append(tk.getDropOffTime());

						startDate.append(":00");

						startDateAsString = startDate.toString();

						toAdd.setTitle(childRepository.findFirstByChildID(tk.getChildId()).getName());

						toAdd.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDateAsString));

						endDate.append(tk.getDate());
						endDate.delete(11, 20);
						endDate.append(tk.getPickUpTime());
						endDate.append(":00");
						endDateAsString = endDate.toString();

						toAdd.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDateAsString));

						toAdd.setEditable(false);
						lazyModel.addEvent(toAdd);
					}
				}
			}
		} else {
			// TODO employee Code
		}
	}

	/**
	 * Adds the specified event.
	 * 
	 * @param actionEvent:
	 *            event to add.
	 * 
	 */
	public void addEvent(ActionEvent actionEvent) {
		if (event.getId() == null)
			lazyModel.addEvent(event);
		else
			lazyModel.updateEvent(event);

		event = new DefaultScheduleEvent();
	}

}