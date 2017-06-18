package sepm.creche.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import sepm.creche.Main;
import sepm.creche.models.Day;
import sepm.creche.models.DayType;
import sepm.creche.repositories.DayRepository;
import sepm.creche.ui.controllers.CalendarController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class CalendarControllerTest
{

	@Autowired
	DayRepository dayRepository;

	@Autowired
	CalendarController calendarController;

	@Test
	@WithMockUser(username = "admin", authorities =
	{ "ADMIN" })
	public void addDayTest()
	{

		Date offsetDate1 = new Date();
		Date offsetDate2 = new Date();

		offsetDate1.setTime(offsetDate1.getTime() - CalendarController.dayInMilSecs);
		offsetDate2.setTime(offsetDate2.getTime() + CalendarController.dayInMilSecs);

		ScheduleEvent event = new DefaultScheduleEvent("dummyEvent", new Date(), new Date(), true);

		Date week1Later = new Date();
		week1Later.setTime(week1Later.getTime() + 7 * CalendarController.dayInMilSecs);
		ScheduleEvent event2 = new DefaultScheduleEvent("dummyEvent", week1Later, week1Later, true);

		Date offsetDate3 = new Date();
		Date offsetDate4 = new Date();

		offsetDate3
				.setTime(offsetDate3.getTime() + 7 * CalendarController.dayInMilSecs - CalendarController.dayInMilSecs);
		offsetDate4
				.setTime(offsetDate4.getTime() + 7 * CalendarController.dayInMilSecs + CalendarController.dayInMilSecs);

		calendarController.setEvent(event);
		calendarController.addDay(false);

		calendarController.getDay().setMaxOccupation(100);
		calendarController.setEvent(event2);
		calendarController.addDay(true);

		Collection<Day> holidays = dayRepository.findDaysByType(DayType.HOLIDAY, offsetDate1, offsetDate2);
		Collection<Day> regDays = dayRepository.findDaysByType(DayType.OCCUPATION, offsetDate3, offsetDate4);

		Assert.assertTrue("Event not in Database-HOLIDAY!", holidays.size() > 0);
		Assert.assertTrue("Event not in Database-REGDAY!", regDays.size() > 0);

		for (Day d : holidays)
		{
			Assert.assertTrue("Desired Eevent not in Database-HOLIDAY!", d.getDescription().contains("dummyEvent"));
		}

		for (Day d : regDays)
		{
			System.out.println(d.getDescription());
			Assert.assertTrue("Desired Eevent not in Database-REGDAY!", d.getDescription().contains("100"));

		}

		loadDaysTest();
		removeDaysTest();

	}

	public void loadDaysTest()
	{

		Date offsetDate1 = new Date();
		Date offsetDate4 = new Date();
		offsetDate4
				.setTime(offsetDate4.getTime() + 7 * CalendarController.dayInMilSecs + CalendarController.dayInMilSecs);

		offsetDate1.setTime(offsetDate1.getTime() - CalendarController.dayInMilSecs);

		calendarController.loadDaysFromDb(offsetDate1, offsetDate4);

		for (ScheduleEvent se : calendarController.getLazyEventModel().getEvents())
		{

			Assert.assertTrue("Event cant be loaded back into LazyModel!",
					se.getTitle().contains("dummyEvent") || se.getTitle().contains("100"));
		}

	}

	public void removeDaysTest()
	{

		List<ScheduleEvent> sc = new ArrayList<ScheduleEvent>(calendarController.getLazyEventModel().getEvents());
		calendarController.setEvent(sc.get(0));
		calendarController.removeEvent(null);

		calendarController.setEvent(sc.get(1));
		calendarController.removeEvent(null);

		Date offsetDate1 = new Date();
		Date offsetDate4 = new Date();
		offsetDate4
				.setTime(offsetDate4.getTime() + 7 * CalendarController.dayInMilSecs + CalendarController.dayInMilSecs);

		offsetDate1.setTime(offsetDate1.getTime() - CalendarController.dayInMilSecs);

		calendarController.getLazyEventModel().clear();
		calendarController.loadDaysFromDb(offsetDate1, offsetDate4);

		Assert.assertTrue("Events not deleted!", calendarController.getLazyEventModel().getEvents().size() == 0);

	}

}
