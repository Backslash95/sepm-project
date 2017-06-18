package sepm.creche.tests;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import sepm.creche.Main;
import sepm.creche.mail.MailService;
import sepm.creche.models.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class MailServiceTest
{
	@Autowired
	MailService mailService;

	@Test
	@WithMockUser(username = "admin", authorities =
	{ "ADMIN" })
	public void sendEmailTest()
	{

		mailService.sendEmail("aspir@live.de", "TestMail", "Testing", new File("FakePdf"));

	}

	@Test
	@WithMockUser(username = "admin", authorities =
	{ "ADMIN" })
	public void sendEmailTaskTest()
	{

		mailService.sendEmailTask("aspir@live.de", new File("FakePdf"));

	}

	@Test
	@WithMockUser(username = "admin", authorities =
	{ "ADMIN" })
	public void sendEmailAcquaintancesConfirmedTest()
	{

		mailService.sendEmailAcquaintancesConfirmed("aspir@live.de");

	}

	@Test
	@WithMockUser(username = "admin", authorities =
	{ "ADMIN" })
	public void sendRegistrationEmail()
	{
		User u = new User();
		u.setUsername("mockUpUser");
		u.setEmail("aspir@live.de");

		mailService.sendRegistrationEmail(u);

	}

}
