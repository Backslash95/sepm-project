package sepm.creche.ui.controllers;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.logger.AuditLog;
import sepm.creche.models.Day;
import sepm.creche.models.DayType;
import sepm.creche.services.DayService;
import sepm.creche.ui.beans.SessionInfoBean;

/**
 * Service for loading in calendar entries and displaying them. Also provides a
 * function to manipulate the entries with according background processing of
 * the database.
 * 
 * @author Aspir
 */

@Component
@Scope("session")
public class CalendarController implements Serializable
{
	public static final long dayInMilSecs = 24 * 3600 * 1000;

	private static final long serialVersionUID = 1L;

	private ScheduleModel lazyEventModel;
	private Day day;

	@Autowired
	private DayService dayService;

	@Autowired
	private AuditLog auditLog;

	@Autowired
	private SessionInfoBean sessionInfoBean;

	private ScheduleEvent event;

	/**
	 * For the primefaces element "Schedule" a lazy model can be implemented so
	 * that not every entry of the database is loaded into the current view.
	 * This is done by implementing the class LazyScheduleModel and the method
	 * loadEvents.
	 *
	 */
	public CalendarController()
	{
		day = new Day();
		day.setMaxOccupation(15);
		event = new DefaultScheduleEvent();

		lazyEventModel = new LazyScheduleModel()
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void loadEvents(Date start, Date end)
			{
				loadDaysFromDb(start, end);
			}
		};

	}

	@SuppressWarnings("deprecation")
	public boolean lazyModelContains(ScheduleEvent event)
	{

		for (ScheduleEvent e : lazyEventModel.getEvents())
		{
			if (e.getStartDate().getMonth() == event.getStartDate().getMonth()
					&& e.getStartDate().getDate() == event.getStartDate().getDate())
			{
				return true;
			}
		}

		return false;

	}

	public Date getInitialDate()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);

		return calendar.getTime();
	}

	public ScheduleEvent getEvent()
	{
		return event;
	}

	public void setEvent(ScheduleEvent event)
	{
		this.event = event;
	}

	/**
	 * Adds the calendar event into the database as a day-entity. This entry is
	 * used to mark an off-day.
	 *
	 * @param event
	 *            the selected event
	 */
	public void addDay(boolean isRegularDay)
	{
		DayType dtype;
		String descPre;
		int maxOccupation;
		String msg;

		if (isRegularDay)
		{
			dtype = DayType.OCCUPATION;
			descPre = "max. Belegung: " + day.getMaxOccupation();
			maxOccupation = day.getMaxOccupation();
			msg = new StringBuilder("Regular day with maxOccupation " + maxOccupation).toString();

		} else
		{
			dtype = DayType.HOLIDAY;
			descPre = "Frei: " + event.getTitle();
			maxOccupation = 0;
			msg = new StringBuilder("Holiday " + event.getTitle()).toString();
		}

		Date offsetDate = new Date();
		offsetDate.setTime(event.getStartDate().getTime());
		long initialTime = offsetDate.getTime();

		for (int i = 0; i <= countDays(event.getStartDate(), event.getEndDate()); i++)
		{
			offsetDate.setTime(initialTime + i * dayInMilSecs);
			if (dayAlreadySet(offsetDate))
				continue;
			Day newDay = new Day();
			newDay.setDayType(dtype);
			newDay.setDate(offsetDate);
			newDay.setDescription(descPre);
			newDay.setMaxOccupation(maxOccupation);
			dayService.save(newDay);

			auditLog.log(sessionInfoBean.getCurrentUser(), msg + " " + newDay.getDate() + " created");
		}

		event = new DefaultScheduleEvent();
		day = new Day();
		day.setMaxOccupation(15);
	}

	@SuppressWarnings("deprecation")
	private boolean dayAlreadySet(Date selectedDate)
	{
		for (ScheduleEvent e : lazyEventModel.getEvents())
		{
			if (e.getStartDate().getMonth() == selectedDate.getMonth()
					&& e.getStartDate().getDate() == selectedDate.getDate())
			{
				addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Tag ist bereits belegt!", ""));
				return true;
			}
		}

		return false;

	}

	public void onEventSelect(SelectEvent selectEvent)
	{
		setEvent((ScheduleEvent) selectEvent.getObject());
	}

	public void onDateSelect(SelectEvent selectEvent)
	{
		setEvent(new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject()));
	}

	private void addMessage(FacesMessage message)
	{
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	/**
	 * Loads every "day" in the given time-span. Then they are added to the
	 * view-model.
	 *
	 * @param startDate
	 * @param endDate
	 *            the time-span for the entries
	 */
	@SuppressWarnings("deprecation")
	public void loadDaysFromDb(Date startDate, Date endDate)
	{

		Collection<Day> holidays = dayService.findHolidays(startDate, endDate);
		Collection<Day> occupations = dayService.findRegularDays(startDate, endDate);

		if (holidays == null)
			return;

		for (Day d : holidays)
		{
			Date offsetDate = d.getDate();
			offsetDate.setHours(1);
			DefaultScheduleEvent se = new DefaultScheduleEvent(d.getDescription(), d.getDate(), offsetDate, true);
			se.setData(new Integer(d.getDayID()));
			lazyEventModel.addEvent(se);

		}

		if (occupations == null)
			return;

		for (Day d : occupations)
		{
			Date offsetDate = d.getDate();
			offsetDate.setHours(1);
			DefaultScheduleEvent se = new DefaultScheduleEvent(d.getDescription(), d.getDate(), offsetDate, true);
			se.setData(new Integer(d.getDayID()));
			lazyEventModel.addEvent(se);
		}

	}

	public Day getDay()
	{
		return day;
	}

	public void setDay(Day day)
	{
		this.day = day;
	}

	public ScheduleModel getLazyEventModel()
	{
		return lazyEventModel;
	}

	public void setLazyEventModel(ScheduleModel lazyEventModel2)
	{
		this.lazyEventModel = lazyEventModel2;
	}

	private long countDays(Date startDate, Date endDate)
	{
		return (endDate.getTime() - startDate.getTime()) / (1000 * 3600 * 24);
	}

	public void removeEvent(ActionEvent actionEvent)
	{
		Day toRemove = dayService.getDayWithID(((Integer) event.getData()).intValue());
		dayService.remove(toRemove);
		auditLog.log(sessionInfoBean.getCurrentUser(), toRemove.getDayType() + " " + toRemove.getDate() + " removed");
	}
}
