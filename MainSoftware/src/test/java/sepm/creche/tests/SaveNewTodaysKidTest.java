package sepm.creche.tests;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import junit.framework.Assert;
import sepm.creche.Main;
import sepm.creche.repositories.DayRepository;
import sepm.creche.repositories.TodaysKidsRepository;
import sepm.creche.ui.controllers.TodaysKidsController;

@RunWith(SpringRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class SaveNewTodaysKidTest
{

	@Autowired
	TodaysKidsRepository todaysKidsRepository;

	@Autowired
	DayRepository dayRepository;

	@Autowired
	TodaysKidsController tkc;

	@Test
	@WithMockUser(username = "admin", authorities = { "ADMIN" })
	public void saveNewKidTest() throws ParseException
	{

		int dayId = 9;
		tkc.setCalDate(dayRepository.findFirstByDayID(dayId).getDate());

		tkc.getNewKid().setDropOffTime("08:00");
		tkc.getNewKid().setPickUpTime("14:00");
		tkc.getNewKid().setPickUpPerson("Steve Egger");
		tkc.setDropOffTimeToDisplay("08:00");
		tkc.getDropOffTimeToDisplay();
		tkc.setPickUpTimeToDisplay("13:30");
		tkc.getPickUpTimeToDisplay();
		tkc.setMondayDate(new Date());
		tkc.getMondayDate();
		tkc.setTuesdayDate(new Date());
		tkc.getTuesdayDate();
		tkc.setWednesdayDate(new Date());
		tkc.getWednesdayDate();
		tkc.setThursdayDate(new Date());
		tkc.getThursdayDate();
		tkc.setFridayDate(new Date());
		tkc.getFridayDate();
		tkc.setSaturdayDate(new Date());
		tkc.getSaturdayDate();
		tkc.setLunch(true);
		tkc.setMonday(true);
		tkc.setTuesday(true);
		tkc.setWednesday(true);
		tkc.setThursday(true);
		tkc.setFriday(true);
		tkc.setSaturday(true);
		tkc.setMonth(new Date().getMonth());
		Assert.assertNotNull(tkc.getMonth());
		Assert.assertEquals(true, tkc.getLunch());
		Assert.assertEquals(true, tkc.isMonday());
		Assert.assertEquals(true, tkc.isTuesday());
		Assert.assertEquals(true, tkc.isWednesday());
		Assert.assertEquals(true, tkc.isThursday());
		Assert.assertEquals(true, tkc.isFriday());
		Assert.assertEquals(true, tkc.isSaturday());
		tkc.setTuesday(false);
		tkc.setWednesday(false);
		tkc.setThursday(false);
		tkc.setFriday(false);
		tkc.setSaturday(false);
		tkc.setYear(new Date().getYear());
		Assert.assertNotNull(tkc.getYear());
		tkc.setChildId(1); //Equals Josef Huber afaik
		Assert.assertNotNull(tkc.getChildId());
		//Assert.assertNotNull(todaysKidsRepository.findFirstBySignedDate(dayRepository.findFirstByDayID(dayId).getDate()));
		tkc.setCalDate(new Date());
		Date date = new Date();
		date.setDate(1);
		tkc.setCalDate(date);
		date.setDate(2);
		tkc.setCalDate(date);
		date.setDate(3);
		tkc.setCalDate(date);
		date.setDate(4);
		tkc.setCalDate(date);
		date.setDate(5);
		tkc.setCalDate(date);
		date.setDate(6);
		tkc.setCalDate(date);
		date.setDate(7);
		tkc.setCalDate(date);//runs setCalDate, which runs every single case in setDatesFromCalDate
		Assert.assertNotNull(tkc.getName(1));
		Assert.assertNotNull(tkc.getSignedKids());
		Assert.assertNotNull(tkc.getSignedKidsParent());
		tkc.setYear(date.getYear());
		Assert.assertNotNull(tkc.getYear());
		Assert.assertNotNull(tkc.getKids());
		Assert.assertNotNull(tkc.getAcquaintances());
		tkc.setAcquaintance("Admin Istrator");
		Assert.assertNotNull(tkc.getAcquaintance());
		tkc.editKidsNote("Er ist krank");
		Assert.assertNotNull(tkc.getCalDate());
		//Assert.assertEquals(false, tkc.kidHasBirthday(1));
		Assert.assertEquals("ja", tkc.kidHasLunch(true));
		Assert.assertNotNull(tkc.getAmountOfLunchYear(1));
		Assert.assertNotNull(tkc.getAmountOfLunch(1));
		Assert.assertNotNull(tkc.getAmountOfAttendanceYear(1));
		Assert.assertNotNull(tkc.getAmountOfAttendance(1));
		Assert.assertNotNull(tkc.getTodaysKidsRepository());
		Assert.assertNotNull(tkc.getParentsKids());
		Assert.assertNotNull(tkc.getAllKidsYear());
		Assert.assertNotNull(tkc.getAllKidsMonth());
		Assert.assertNotNull(tkc.getCostOfLunchMonth(1));
		Assert.assertNotNull(tkc.getCostOfLunchYear(1));
		Assert.assertNotNull(tkc.kidHasBirthday(1));
	}
	
}
