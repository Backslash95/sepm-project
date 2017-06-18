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
import sepm.creche.logger.AuditLog;
import sepm.creche.logger.LogController;
import sepm.creche.models.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class AuditLogTest {
	
	@Autowired
	private AuditLog auditLog;
	
	@SuppressWarnings("deprecation")
	@Test
	public void logTest1() {
		Exception exc = null;
		try {
			auditLog.log("test log");
		} catch (Exception e) {
			fail();
		}
		Assert.assertNull("Logging failed",exc);
	}
	
	@Test
	public void logTest2() {
		Exception exc = null;
		try {
			User dummyUser = new User();
			dummyUser.setUsername("dummy");
			auditLog.log(dummyUser, "test log with user");
		} catch (Exception e) {
			exc = e;
		}
		Assert.assertNull("Logging with user failed: ",exc);
	}

}
