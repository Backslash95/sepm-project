package sepm.creche.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import junit.framework.Assert;
import sepm.creche.Main;
import sepm.creche.models.User;
import sepm.creche.repositories.ChildRepository;
import sepm.creche.repositories.UserRepository;
import sepm.creche.ui.beans.SessionInfoBean;
import sepm.creche.ui.controllers.EditParentController;
import sepm.creche.ui.controllers.ParentDataController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class ParentDataTest {

	@Autowired
	ChildRepository childRep;
	@Autowired
	ParentDataController pdc;
	@Autowired
	UserRepository userRep;
	@Autowired
	EditParentController epdc;
	@Autowired
	SessionInfoBean session;

	@Test
	public void ControllerTest() {
		User usr = userRep.findFirstByUsername("user1");
		pdc.setCurrentUser(usr);
		Assert.assertEquals(usr, pdc.getCurrentUser());
	}

	@Test
	@WithMockUser(username = "admin", authorities = { "ADMIN" })
	public void register() {

		User user = userRep.findFirstByUsername("admin");
		user.setSex("Männlich");
		epdc.setCurrentUser(user);
		epdc.setCheckpassword(user.getPassword());
		epdc.saveData();

		Assert.assertEquals(user, epdc.getCurrentUser());
	}
}
