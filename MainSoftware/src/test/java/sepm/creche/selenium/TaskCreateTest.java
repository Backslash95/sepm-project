package sepm.creche.selenium;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TaskCreateTest
{

	private WebDriver browser;

	@Before
	public void setup()
	{
		browser = new FirefoxDriver();
	}

	@Test
	public void startTest() throws InterruptedException
	{
		browser.get("http://localhost:8080/");
		Thread.sleep(1000);

		// browser.findElement(By.id("login")).click();

		// Will throw exception if elements not found
		browser.findElement(By.id("username")).sendKeys("admin");
		browser.findElement(By.id("password")).sendKeys("passwd");

		// browser.findElement(By.id("login_form")).click();
		browser.findElement(By.xpath("// *[@id='login_form']/button/span")).click();

		Thread.sleep(1000);
		// browser.findElement(By.id("account")).click();
		browser.get("http://localhost:8080/secured/other/Employee/tasks.xhtml");
		Thread.sleep(1000);
		browser.findElement(By.id("mainView:newTaskPage4:createTaskButton")).click();
		Thread.sleep(1000);
		browser.findElement(By.id("mainView:newTaskPage4:selectTaskType1")).click();
		Thread.sleep(1000);
		browser.findElement(By.id("mainView:newTaskPage2:desc")).sendKeys("Jause" + new Date().toGMTString());
		browser.findElement(By.id("mainView:newTaskPage2:descLong"))
				.sendKeys("Jause bringen, sonst verhungern die armen Kinder!");
		browser.findElement(By.id("mainView:newTaskPage2:workers")).clear();
		browser.findElement(By.id("mainView:newTaskPage2:workers")).sendKeys("3");

		browser.findElement(By.id("mainView:newTaskPage2:dueto_input")).click();
		browser.findElement(By.xpath("//*[@id='ui-datepicker-div']/table/tbody/tr[5]/td[5]/a")).click();

		browser.findElement(By.id("mainView:newTaskPage2:timeSpan")).clear();
		browser.findElement(By.id("mainView:newTaskPage2:timeSpan")).sendKeys("5");
		Thread.sleep(1000);
		browser.findElement(By.xpath("//*[@id='mainView:newTaskPage2:submitCreateTask']/span[2]")).click();

		Thread.sleep(1000);
		browser.findElement(By.xpath("//*[@id='mainView']/ul/li[2]/a")).click();
		Thread.sleep(3000);

	}

	@After
	public void tearDown()
	{
		browser.close();
	}
}