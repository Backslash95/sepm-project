package sepm.creche.tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import sepm.creche.Main;
import sepm.creche.models.Message;
import sepm.creche.services.MessageService;
import sepm.creche.services.ReminderService;
import sepm.creche.services.TaskService;
import sepm.creche.services.UserService;
import sepm.creche.ui.beans.PickListViewUser;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class ServiceTest
{

	@Autowired
	UserService userService;

	@Autowired
	ReminderService reminderService;

	@Autowired
	MessageService messageService;

	@Autowired
	TaskService taskService;

	@Autowired
	PickListViewUser pickListViewUser;

	@Test
	@WithMockUser(username = "admin", authorities =
	{ "ADMIN" })
	public void testDatainitialization()
	{
		reminderService.remind();
		taskService.findMyTasks();
		messageService.setDisableFrontEnd(true);

		messageService.setMsg("TestMsg");
		messageService.sendNewMsg("admin", "admin");

		DualListModel<String> dl = new DualListModel<String>();
		ArrayList<String> names = new ArrayList<String>();
		names.add("Admin Istrator(admin)");
		dl.setTarget(names);
		pickListViewUser.setUsernames(dl);
		messageService.sendToMultipleUsers();

		List<Message> m = messageService.getAllMsgs();
		m = messageService.getMyMsgs();
		messageService.selectMsg(m.get(0).getMsgID());
		m = messageService.getMySentMsgs();
		m = messageService.getUnreadMsgs();

	}

}