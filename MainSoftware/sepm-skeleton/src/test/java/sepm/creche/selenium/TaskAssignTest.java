package sepm.creche.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TaskAssignTest
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

		browser.findElement(By.xpath("//*[@id='mainView:newTaskPage1:taskTable:stateSort']/span[1]")).click();
		Thread.sleep(1000);

		browser.findElement(By.xpath("// *[@id='mainView:newTaskPage1:taskTable:0:assignButton1']/span[2]")).click();
		Thread.sleep(1000);
		browser.findElement(By.xpath("// *[@id='mainView:newTaskPage1:pickList2']/div[2]/div/button[2]/span[1]"))
				.click();
		Thread.sleep(1000);
		browser.findElement(By.xpath("// *[@id='mainView:newTaskPage1:assignTasksButton1']/span[2]")).click();

		Thread.sleep(4000);

	}

	@After
	public void tearDown()
	{
		browser.close();
	}
}