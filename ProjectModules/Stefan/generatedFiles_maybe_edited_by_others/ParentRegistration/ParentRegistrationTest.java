package at.qe.sepm.skeleton.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import at.qe.sepm.skeleton.Main;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.model.UserRole;
import at.qe.sepm.skeleton.repositories.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class ParentRegistrationTest {

	@Autowired
	UserRepository userRep;

	User user1 = new User();

	@Test
	public void test() {
		for (User user : userRep.findAll()) {
			Assert.assertNotNull(user.getUsername());
			Assert.assertNotNull(user.getEmail());
			Assert.assertNotNull(user.getPassword());
			Assert.assertNotNull(user.getFirstName());
			Assert.assertNotNull(user.getLastName());
			Assert.assertNotNull(user.getAddress());
			Assert.assertNotNull(user.getPhone());
			Assert.assertTrue("Userrole not set!", user.getRoles().contains(UserRole.ADMIN)
					|| user.getRoles().contains(UserRole.PARENT) || user.getRoles().contains(UserRole.EMPLOYEE));
		}
	}

}
