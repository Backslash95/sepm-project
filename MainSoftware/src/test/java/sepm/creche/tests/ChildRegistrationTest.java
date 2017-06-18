package sepm.creche.tests;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import sepm.creche.Main;
import sepm.creche.generator.ChildGenerator;
import sepm.creche.generator.NewChildHandler;
import sepm.creche.models.Child;
import sepm.creche.models.Person;
import sepm.creche.models.User;
import sepm.creche.repositories.ChildRepository;
import sepm.creche.repositories.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class ChildRegistrationTest {

	@Autowired
	ChildRepository childRep;
	@Autowired
	ChildGenerator childGen;
	@Autowired
	UserRepository userRep;
	@Autowired
	NewChildHandler newChildHandler;

	@Test
	public void test() {
		// check if all guidelines are met for every child
		for (Child child : childRep.findAll()) {
			Assert.assertNotNull(child.getAddress());
			Assert.assertNotNull(child.getSex());
			Assert.assertNotNull(child.getBirthdate());
			Assert.assertNotNull(child.getName());
			Assert.assertNotNull(child.getRegisterDate());
			Assert.assertTrue((new Date().compareTo(child.getRegisterDate()) > 0));
		}
	}

	@Test
	public void register() {

		Child child = new Child();
		child.setBirthdate(new Date());
		child.setName("Steve");
		child.setSex("Männlich");
		child.setDeregistered(false);
		child.setAddress("Wörgl");
		child.setEmergencyContact("Doris");

		List<User> parentList = new ArrayList<User>();
		parentList.add(userRep.findFirstByUsername("user1"));
		childGen.setParentList(parentList);

		List<Person> personList = new ArrayList<Person>();
		Person person = new Person();
		person.setName("Sepp23");
		person.setEmail("Stefan.km@nix.at");
		person.setPhone("+43-66021471158");
		personList.add(person);
		childGen.setPersonList(personList);

		childGen.addNewChild(child);
		Assert.assertNotNull(childRep.findFirstByName("Steve"));

		newChildHandler.setNewUser(child);
		Assert.assertEquals(child, newChildHandler.getNewChild());
	}

}
