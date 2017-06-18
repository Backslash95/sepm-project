package sepm.creche.tests;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.common.collect.Iterables;

import sepm.creche.Main;
import sepm.creche.logger.AuditLog;
import sepm.creche.logger.LogController;
import sepm.creche.logger.LogMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class LogControllerTest {
	
	private LogController dummyLogController;
	
	@Autowired
	private AuditLog auditLog;
	
	@Before
	public void init(){
		dummyLogController = new LogController();
	}
	
	@Test
	public void getLogsTest(){
		auditLog.log("testlog");
		
		Collection<LogMessage> dummyLogMessages;
		
		dummyLogMessages = dummyLogController.getLogs();
		
		Assert.assertNotNull("Reading log file failed", dummyLogMessages);
		Assert.assertNotEquals("Reading first log message failed (is \"\")", "", Iterables.get(dummyLogMessages, 0));
		Assert.assertNotNull("Reading log message failed (is null)", Iterables.get(dummyLogMessages,0));

	}

}
