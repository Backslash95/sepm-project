package sepm.creche.tests;

import java.util.Collection;
import java.util.HashSet;

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
import sepm.creche.models.User;
import sepm.creche.repositories.ChildRepository;
import sepm.creche.repositories.UserRepository;
import sepm.creche.ui.controllers.ChildDataSheetController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
public class ChildDataSheetTest
{

	@Autowired
	ChildRepository childRep;

	@Autowired
	ChildDataSheetController cdc;

	@Autowired
	UserRepository userRep;

	@Test
	public void deregisterTest()
	{

		User usr = userRep.findFirstByUsername("user4");
		for (Child child : usr.getMyChildren())
		{
			cdc.setSelectedChild(child);
			cdc.deregister();
			Assert.assertTrue(child.isDeregistered());
		}
	}

	@Test
	public void deleteRelativesTest()
	{
		User usr = userRep.findFirstByUsername("user2");
		Collection<Child> children = usr.getMyChildren();
		cdc.deleteRelatives(children);
		for (Child child : children)
		{
			Assert.assertNull(child.getMyRelatives());
		}
	}

	@Test
	public void loadingParentListTest()
	{

		Child child1 = childRep.findFirstByChildID(1);

		Collection<User> tempColl = new HashSet<User>();
		cdc.setSelectedChild(child1);
		tempColl.addAll(cdc.loadParentList());
		for (User usr : tempColl)
		{
			Assert.assertTrue(child1.getMyParents().contains(usr));
		}

	}

	@Test
	public void loadingPersonListTest()
	{

		Child child1 = childRep.findFirstByChildID(1);
		Collection<Person> tempColl = new HashSet<Person>();
		cdc.setSelectedChildIDWithoutRedirect(child1.getChildID());

		if (!cdc.loadPersonList().isEmpty())
		{
			tempColl.addAll(cdc.loadPersonList());
			child1 = childRep.findFirstByChildID(1);

			for (Person person : tempColl)
			{
				Assert.assertTrue(child1.getMyRelatives().contains(person));
			}
		}

	}

}
