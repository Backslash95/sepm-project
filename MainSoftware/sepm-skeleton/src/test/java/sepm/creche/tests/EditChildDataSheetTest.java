package sepm.creche.tests;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import sepm.creche.Main;
import sepm.creche.models.Child;
import sepm.creche.models.Person;
import sepm.creche.models.Picture;
import sepm.creche.models.User;
import sepm.creche.repositories.ChildRepository;
import sepm.creche.repositories.PictureRepository;
import sepm.creche.repositories.UserRepository;
import sepm.creche.ui.controllers.EditChildDataSheetController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class EditChildDataSheetTest {

	@Autowired
	ChildRepository childRep;
	@Autowired
	EditChildDataSheetController ecdc;
	@Autowired
	UserRepository userRep;
	@Autowired
	PictureRepository picRepo;

	@Test
	public void saveTest() {
		Child child = new Child();
		child.setBirthdate(new Date());
		child.setName("Steve");
		child.setSex("Männlich");
		child.setDeregistered(false);
		child.setAddress("Wörgl");
		child.setEmergencyContact("Doris");

		Set<User> parentSet = new HashSet<User>();
		parentSet.add(userRep.findFirstByUsername("user1"));
		child.setMyParents(parentSet);

		Set<Person> personSet = new HashSet<Person>();
		Person person = new Person();
		person.setName("Sepp23");
		person.setEmail("stefan.km@gornix.at");
		person.setPhone("+43-66021471158");
		personSet.add(person);
		child.setMyRelatives(personSet);

		Picture pic = new Picture();
		pic.setChildID(child.getChildID());
		pic.setPictureReference("user-silhouette.jpg");
		picRepo.save(pic);

		ecdc.setSelectedChild(child);
		ecdc.setPersonList(personSet);
		ecdc.setFileName("");
		ecdc.saveData();
		Assert.assertEquals("", ecdc.getFileName());
	}

}
