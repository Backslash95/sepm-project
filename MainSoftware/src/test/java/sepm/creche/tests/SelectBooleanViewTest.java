package sepm.creche.tests;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import sepm.creche.Main;
import sepm.creche.logger.AuditLog;
import sepm.creche.mail.MailService;
import sepm.creche.models.User;
import sepm.creche.repositories.UserRepository;
import sepm.creche.ui.beans.SelectBooleanView;
import sepm.creche.ui.beans.SessionInfoBean;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class SelectBooleanViewTest {
	
	@Autowired
	SessionInfoBean sessionInfoBean;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MailService mailService;
	
	@Autowired
	AuditLog auditLog;
	
	
	private SelectBooleanView view;
	
	@Before
	public void init(){
		view = new SelectBooleanView();
		view.setSessionInfoBean(sessionInfoBean);
		view.setUserRepository(userRepository);
		view.setMailService(mailService);
		view.setAuditLog(auditLog);
	}
	
	@Test
	@WithMockUser(username = "user1", authorities =
	{ "EMPLOYEE" })
	public void SelectBooleanViewTest() {
		
		view.setValue(true);		
		view.submit();
		
		User user = sessionInfoBean.getCurrentUser();
		Assert.assertTrue(user.getSendEmails());
		
		view.setValue(false);
		view.submit();
		
		user = sessionInfoBean.getCurrentUser();
		Assert.assertFalse(user.getSendEmails());
		Assert.assertNotNull("user null",user);
	}

}
