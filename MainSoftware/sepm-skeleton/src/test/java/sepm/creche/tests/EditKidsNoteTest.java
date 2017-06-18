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
public class EditKidsNoteTest {

	@Autowired
	TodaysKidsRepository todaysKidsRepository;
	
	@Autowired
	DayRepository dayRepository;
	
	@Autowired
	TodaysKidsController tkc;
	
	@Test
	public void editTest() {
		
		int dayId = 7;
		tkc.setChildId(4);
		tkc.setCalDate(dayRepository.findFirstByDayID(dayId).getDate());
		tkc.editKidsNote("Daniel ist krank und kann nicht kommen.");
		
		Assert.assertEquals("Daniel ist krank und kann nicht kommen.", todaysKidsRepository.findFirstBySignedDate(dayRepository.findFirstByDayID(dayId).getDate()).getNotes());
	}

}
