package sepm.creche.tests;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import junit.framework.Assert;
import sepm.creche.Main;
import sepm.creche.models.TodaysKids;
import sepm.creche.repositories.DayRepository;
import sepm.creche.repositories.TodaysKidsRepository;
import sepm.creche.ui.controllers.TodaysKidsController;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class TodaysKidsViewTest {

	@Autowired
	TodaysKidsRepository todaysKidsRepository;
	
	@Autowired
	DayRepository dayRepository;
	
	@Autowired
	TodaysKidsController tkc;
	
	
	@Test
	public void employeeViewTest() {
		
		int dayId = 3;
			
		tkc.setCalDate(dayRepository.findFirstByDayID(dayId).getDate());
		
		Collection<TodaysKids> signedKids = new HashSet<TodaysKids>();
		
		signedKids.addAll(tkc.getSignedKids());
		
		for(TodaysKids tk : signedKids){
			Assert.assertEquals("childId does not match", 4, tk.getChildId());
		}
		
	}
	
	@Test
	@WithMockUser(username = "admin", authorities =
	{ "PARENT" })
	public void parentViewTest() {
		
		int dayId = 2;
			
		tkc.setCalDate(dayRepository.findFirstByDayID(dayId).getDate());
		
		Collection<TodaysKids> signedKids = new HashSet<TodaysKids>();
		
		signedKids.addAll(tkc.getSignedKidsParent());
		
		for(TodaysKids tk : signedKids){
			Assert.assertEquals("childId does not match", 1, tk.getChildId());
		}
		
	}

}
