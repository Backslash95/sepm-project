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
import sepm.creche.ui.beans.CalendarViewDS;
import sepm.creche.ui.controllers.TodaysKidsController;

@RunWith(SpringRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class CalendarViewDSTest {

	@Autowired
	CalendarViewDS cv;

	@Test
	@WithMockUser(username = "admin", authorities = { "PARENT" })
	public void test() throws ParseException {
		int countOne = 0;
		int countTwo = -1;

		countOne = cv.getLazyModel().getEventCount();
		cv.createDayEvents();
		countTwo = cv.getLazyModel().getEventCount();

		boolean check = false;

		if (countOne < countTwo) {
			check = true;
		}
		//check if events are created
		Assert.assertEquals(true, check);
	}

}
