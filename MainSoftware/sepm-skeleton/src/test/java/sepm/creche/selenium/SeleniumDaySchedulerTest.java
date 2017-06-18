package sepm.creche.selenium;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import sepm.creche.Main;
import sepm.creche.models.Day;
import sepm.creche.models.DayType;
import sepm.creche.repositories.DayRepository;
import sepm.creche.ui.controllers.CalendarController;
import sepm.creche.ui.controllers.CrecheSettingsController;
import sepm.creche.ui.controllers.TodaysKidsController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class SeleniumDaySchedulerTest
{

	private WebDriver browser;
	
	@Autowired
	private TodaysKidsController todaysKidsController;

	@Autowired
	private DayRepository dayRepository;
	
	@Autowired
	private CrecheSettingsController crecheSettingsController;
	
	private int deadLine;
	

	@Before
	public void setup()
	{
		browser = new FirefoxDriver();
	}

	
	@SuppressWarnings("deprecation")
	@Test
	public void startTest() throws InterruptedException
	{	
		todaysKidsController= new TodaysKidsController();
		deadLine = crecheSettingsController.getDeadline2();
		
		
		
		for (int i = 1; i<=31; i++){
			Date dates = new Date();
			Day days = new Day();
			dates.setDate(i);
			days.setDate(dates);
			days.setDescription("Selenium Test");
			days.setDayType(DayType.OCCUPATION);
			days.setMaxOccupation(15);
			days.setDayOfTheWeek(days.getDayOfTheWeek());
			dayRepository.save(days);
		}
		
		for (int i = 1; i<=31; i++){
			Date dates = new Date();
			Day days = new Day();
			dates.setMonth(dates.getMonth()+1);
			dates.setDate(i);
			days.setDate(dates);
			days.setDescription("Selenium Test");
			days.setDayType(DayType.OCCUPATION);
			days.setMaxOccupation(15);
			days.setDayOfTheWeek(days.getDayOfTheWeek());
			dayRepository.save(days);
		}
		Thread.sleep(1000);
		browser.get("http://localhost:8080/");
		Thread.sleep(1000);

		// browser.findElement(By.id("login")).click();

		// Will throw exception if elements not found
		browser.findElement(By.id("username")).sendKeys("admin");
		browser.findElement(By.id("password")).sendKeys("passwd");

		browser.findElement(By.id("loginButton")).click();
		Thread.sleep(1000);
		// browser.findElement(By.id("account")).click();
		browser.get("http://localhost:8080/secured/DaySchedulerParent.xhtml");
		Thread.sleep(1000);
		
		browser.findElement(By.xpath("//*[@id='daySchedulerParent']/ul/li[3]/a")).click();
		//browser.findElement(By.id("registerChildsNew:date")).click();
		browser.findElement(By.xpath("//*[@id='daySchedulerParent:registerChildsNew:date_input']")).click();
		Thread.sleep(1000);
		browser.findElement(By.xpath("//*[@id='ui-datepicker-div']/table/tbody/tr[5]/td[1]/a")).click();
		Thread.sleep(1000);
		browser.findElement(By.xpath("//*[@id='daySchedulerParent:registerChildsNew:registerDates:0:saturday']/div[2]/span")).click();
		Thread.sleep(1000);
		browser.findElement(By.xpath("//*[@id='daySchedulerParent:registerChildsNew:child_label']")).click();
		Thread.sleep(1000);
		browser.findElement(By.xpath("//*[@id='daySchedulerParent:registerChildsNew:child_1']")).click();
		Thread.sleep(1000);
		browser.findElement(By.xpath("//*[@id='daySchedulerParent:registerChildsNew:dropOffTime']")).click();
		Thread.sleep(1000);
		browser.findElement(By.xpath("//*[@id='daySchedulerParent:registerChildsNew:dropOffTime']")).sendKeys("0800");
		Thread.sleep(1000);
		browser.findElement(By.xpath("//*[@id='daySchedulerParent:registerChildsNew:pickUpTime']")).click();
		Thread.sleep(1000);
		browser.findElement(By.xpath("//*[@id='daySchedulerParent:registerChildsNew:pickUpTime']")).sendKeys("1330");
		Thread.sleep(1000);
		browser.findElement(By.xpath("//*[@id='daySchedulerParent:registerChildsNew:lunch']/div[2]/span")).click();
		Thread.sleep(1000);
		browser.findElement(By.xpath("//*[@id='daySchedulerParent:registerChildsNew:pickUpPerson_label']")).click();
		Thread.sleep(1000);
		browser.findElement(By.xpath("//*[@id='daySchedulerParent:registerChildsNew:pickUpPerson_1']")).click();
		Thread.sleep(1000);
		browser.findElement(By.xpath("//*[@id='daySchedulerParent:registerChildsNew:saveChild']/span[2]")).click();
		Thread.sleep(1000);
	}

	@After
	public void tearDown()
	{
		browser.close();
	}
}