package sepm.creche.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import sepm.creche.Main;
import sepm.creche.models.Person;
import sepm.creche.repositories.PersonRepository;
import sepm.creche.ui.beans.CheckboxView;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class CheckboxViewTest {

	@Autowired
	PersonRepository personRep;

	@Autowired
	CheckboxView cbv;

	@Test
	public void initTest() {

		// needed attributes
		Collection<Person> personSet = new HashSet<Person>();
		List<String> namesFromBean = new ArrayList<String>();

		// get all persons
		personSet.addAll(personRep.findAll());

		// get names from bean
		cbv.init();
		if (cbv.getNames() != null) {
			namesFromBean.addAll(cbv.getNames());

			// compare names
			for (Person person : personSet) {
				Assert.assertTrue(namesFromBean.contains(person.getName()));
			}
		}
	}

}
