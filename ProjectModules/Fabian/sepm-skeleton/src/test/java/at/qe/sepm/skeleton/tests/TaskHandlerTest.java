package at.qe.sepm.skeleton.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import at.qe.sepm.skeleton.ahmet.taskHandler.TaskHandler;
import at.qe.sepm.skeleton.stefan.models.Task;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TaskHandlerTest
{

	@Test
	public void test()
	{
		TaskHandler taskHandler = new TaskHandler();
		Task newTask = new Task();

		newTask.setAmountOfWorkers(-1);
		newTask.setDeadline(null);
		newTask.setName(null);

		taskHandler.setNewTask(newTask);
		taskHandler.saveNewTask();

	}

}
