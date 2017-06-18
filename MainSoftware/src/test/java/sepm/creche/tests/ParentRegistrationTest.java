package sepm.creche.tests;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import sepm.creche.Main;
import sepm.creche.generator.NewUserHandler;
import sepm.creche.generator.UserGenerator;
import sepm.creche.models.User;
import sepm.creche.models.UserRole;
import sepm.creche.repositories.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class ParentRegistrationTest {

	@Autowired
	UserRepository userRep;
	@Autowired
	UserGenerator userGen;
	@Autowired
	NewUserHandler newUserHandler;

	@Test
	public void test() {
		// check if all guidelines are met for every parent
		for (User user : userRep.findAll()) {
			Assert.assertNotNull(user.getUsername());
			Assert.assertNotNull(user.getEmail());
			Assert.assertNotNull(user.getPassword());
			Assert.assertNotNull(user.getFirstName());
			Assert.assertNotNull(user.getLastName());
			Assert.assertNotNull(user.getAddress());
			Assert.assertNotNull(user.isEnabled());
			Assert.assertNotNull(user.isInactive());
			Assert.assertNotNull(user.getPhone());
			Assert.assertNotNull(user.getCreateDate());
			Assert.assertNotNull(user.getCreateUser());
			Assert.assertTrue((new Date().compareTo(user.getCreateDate()) > 0));
			Assert.assertTrue(user.getPassword().length() > 5);
			Assert.assertTrue(user.getEmail().length() > 4);
			Assert.assertTrue(user.getEmail().contains("@"));
			Assert.assertTrue("Userrole not set!", user.getRoles().contains(UserRole.ADMIN)
					|| user.getRoles().contains(UserRole.PARENT) || user.getRoles().contains(UserRole.EMPLOYEE));
		}
	}

	@Test
	public void register() {

		User user = new User();
		user.setUsername("sepp2");
		user.setCreateUser(userRep.findFirstByUsername("user1"));
		user.setCreateDate(new Date());
		user.setAddress("Wörgl");
		user.setPassword("asdfgh22");
		user.setFirstName("Stefan");
		user.setLastName("Kaufmann");
		user.setEmail("stefan.km@sowsvongornix.at");
		user.setSex("Männlich");
		user.setPhone("+43-660-2147116");
		user.setEnabled(true);
		user.setInactive(false);
		user.setSendEmails(true);

		userGen.addNewUser(user);
		Assert.assertNotNull(userRep.findFirstByUsername("sepp2"));

		newUserHandler.setNewUser(user);
		Assert.assertEquals(user, newUserHandler.getNewUser());
	}

}
