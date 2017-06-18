package sepm.creche.tests;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import sepm.creche.Main;
import sepm.creche.models.Task;
import sepm.creche.models.TaskState;
import sepm.creche.models.User;
import sepm.creche.repositories.TaskRepository;
import sepm.creche.repositories.UserRepository;
import sepm.creche.services.ReminderService;
import sepm.creche.services.UserService;
import sepm.creche.ui.controllers.CalendarController;
import sepm.creche.ui.controllers.ParentPickListController;
import sepm.creche.ui.controllers.TaskHandler;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class TaskHandlerTest
{

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	ParentPickListController parentPickListController;

	@Autowired
	TaskHandler taskHandler;

	@Autowired
	ReminderService reminderService;

	@Test
	@WithMockUser(username = "admin", authorities =
	{ "ADMIN" })
	public void CreateSaveTaskTest()
	{
		Task newTask = new Task();

		newTask.setAmountOfWorkers(1);
		Date dummyDate = new Date();
		dummyDate.setTime(dummyDate.getTime() + CalendarController.dayInMilSecs);
		newTask.setDeadline(dummyDate);
		newTask.setName("DummyTask");

		taskHandler.setSingleDaySelected(true);
		taskHandler.setNewTask(newTask);
		newTask = taskHandler.saveNewTask();

		newTask = taskRepository.findFirstByTaskID(newTask.getTaskID());
		Assert.assertNotNull("No such Entry!", newTask);
		Assert.assertTrue("Status is not OPEN!", newTask.getTaskState() == TaskState.OPEN);
		Assert.assertTrue("Key is not Valid!", newTask.getTaskID() > 0);
		Assert.assertTrue("Amount of Workers can't be 0!", newTask.getAmountOfWorkers() > 0);

		assignTaskTest();
		miscTest();

	}

	@Test
	@WithMockUser(username = "admin", authorities =
	{ "ADMIN" })
	public void parentsWithoutTaskTest()
	{

		User user = userRepository.findFirstByUsername("user3");

		Assert.assertTrue("This parent does not show up in the Collection!",
				userService.parentsWithoutTask().contains(user));
	}

	public void assignTaskTest()
	{
		DualListModel<String> dl = new DualListModel<String>();
		ArrayList<String> names = new ArrayList<String>();
		names.add("Admin Istrator(admin)");
		dl.setTarget(names);
		parentPickListController.setCurrentUsernames(dl);
		taskHandler.setSelectedTask(taskRepository.findFirstByName("DummyTask"));
		taskHandler.assignTask();

		Assert.assertTrue("No Entry in User -> TaskSet!", taskHandler.getSelectedTask().getUserSet().size() >= 1);
		for (User u : taskHandler.getSelectedTask().getUserSet())
		{
			Assert.assertNotNull("User Null!", u);

			Assert.assertTrue("No Entry in Task -> UserSet!", u.getMyTasks().size() >= 1);
		}

	}

	public void miscTest()
	{
		taskHandler.loadTask(1);
		taskHandler.loadTask2(1);
		taskHandler.removeTask();
		taskHandler.getOpenTasks();

		taskHandler.getTimeSpanInDays();
		taskHandler.setTimeSpanInDays(1);
		taskHandler.getStartDate();
		taskHandler.setStartDate(new Date());
		taskHandler.getEndDate();
		taskHandler.setEndDate(new Date());
		taskHandler.getDialogEnabled();
		taskHandler.setDialogEnabled(false);
		taskHandler.isDialogEnabled2();
		taskHandler.setDialogEnabled2(false);
		taskHandler.isDialogEnabled3();
		taskHandler.setDialogEnabled3(false);
		parentPickListController.showTargetList(1);
	}

}
