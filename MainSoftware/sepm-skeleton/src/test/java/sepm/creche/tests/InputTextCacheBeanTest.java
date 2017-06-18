package sepm.creche.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import junit.framework.Assert;
import sepm.creche.Main;
import sepm.creche.ui.beans.InputTextCacheBean;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class InputTextCacheBeanTest {

	InputTextCacheBean bean = new InputTextCacheBean();
	
	@Test
	public void inputTextCacheBeanTest() {
		bean.setFirstName("test1");
		bean.setLastName("test2");
		bean.setPassword("test3");
		bean.setUsername("test4");
		Assert.assertEquals("test1", bean.getFirstName());
		Assert.assertEquals("test2", bean.getLastName());
		Assert.assertEquals("test3", bean.getPassword());
		Assert.assertEquals("test4", bean.getUsername());
	}

}
