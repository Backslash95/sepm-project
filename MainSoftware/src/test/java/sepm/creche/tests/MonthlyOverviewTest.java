package sepm.creche.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import junit.framework.Assert;
import sepm.creche.Main;
import sepm.creche.repositories.DayRepository;
import sepm.creche.repositories.TodaysKidsRepository;
import sepm.creche.ui.controllers.TodaysKidsController;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class MonthlyOverviewTest {

	@Autowired
	TodaysKidsRepository todaysKidsRepository;
	
	@Autowired
	DayRepository dayRepository;
	
	@Autowired
	TodaysKidsController tkc;
	
	@Test
	public void testAmountOfLunch() {
		
		int childId = 1;
		
		tkc.setMonth(4);
		tkc.setYear(2017);
		
		Assert.assertEquals(2, tkc.getAmountOfLunch(childId));
		
	}
	
	@Test
	public void testAmountOfAttendance() {
		
		int childId = 1;
		
		tkc.setMonth(4);
		tkc.setYear(2017);
		
		Assert.assertEquals(2, tkc.getAmountOfAttendance(childId));
		
	}

}
