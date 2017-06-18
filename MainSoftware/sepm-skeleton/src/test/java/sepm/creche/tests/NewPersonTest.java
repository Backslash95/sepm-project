package sepm.creche.tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import junit.framework.Assert;
import sepm.creche.Main;
import sepm.creche.generator.NewPersonHandler;
import sepm.creche.generator.PersonGenerator;
import sepm.creche.models.Child;
import sepm.creche.models.Person;
import sepm.creche.repositories.ChildRepository;
import sepm.creche.repositories.PersonRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class NewPersonTest {

	@Autowired
	NewPersonHandler newPersonHandler;
	@Autowired
	PersonRepository personRepo;
	@Autowired
	PersonGenerator personGen;
	@Autowired
	ChildRepository childRepo;

	@Test
	public void newPersonTest() {
		// NewPersonHandler newPersonHandler = new NewPersonHandler();

		Person person = new Person();
		person.setEmail("dummy@email.test");
		person.setName("dummyPerson");
		person.setPersonID(1234567890);
		person.setPhone("1234567890");

		List<Child> childList = new ArrayList<Child>();
		childList.add(childRepo.findFirstByName("Stefan"));
		personGen.setChildList(childList);

		newPersonHandler.setNewUser(person);
		Assert.assertEquals(person, newPersonHandler.getNewPerson());

		personGen.addNewPerson(person);
		Assert.assertNotNull(personRepo.findFirstByName("dummyPerson"));

	}

}
