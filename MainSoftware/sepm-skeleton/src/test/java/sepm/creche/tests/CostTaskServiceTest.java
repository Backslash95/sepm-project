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
import sepm.creche.models.Task;
import sepm.creche.repositories.ChildRepository;
import sepm.creche.repositories.DayRepository;
import sepm.creche.repositories.TaskRepository;
import sepm.creche.repositories.TodaysKidsRepository;
import sepm.creche.repositories.UserRepository;
import sepm.creche.services.CostTaskService;
import sepm.creche.ui.controllers.TodaysKidsController;

@RunWith(SpringRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class CostTaskServiceTest {

	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
	CostTaskService cts;
	
	
	@Test
	public void test() {
		cts.createCostTask();
		String name = "Mittagessen-Abrechnung";
		Assert.assertNotNull(taskRepository.findFirstByName(name));
		Assert.assertNotNull(taskRepository.findFirstByName(name).getAmountOfWorkers());
		Assert.assertNotNull(taskRepository.findFirstByName(name).getAssignedUsers());
		Assert.assertNotNull(taskRepository.findFirstByName(name).getDeadline());
		Assert.assertNotNull(taskRepository.findFirstByName(name).getDescription());
	}

}
