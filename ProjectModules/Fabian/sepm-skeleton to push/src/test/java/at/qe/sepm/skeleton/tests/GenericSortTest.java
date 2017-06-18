package at.qe.sepm.skeleton.tests;

import java.util.ArrayList;

import org.junit.Test;

import at.qe.sepm.skeleton.ahmet.genericSort.Person;
import at.qe.sepm.skeleton.ahmet.genericSort.SortClass;

public class GenericSortTest
{
	private SortClass sort = new SortClass();

	@Test
	public void testSortMethodWithEmptyList()
	{

		ArrayList<Person> emptyList = new ArrayList<Person>();
		sort.sortCollection(emptyList, "getName", false);
		sort.printCollection(emptyList, "getName");

	}

	@Test
	public void testSortMethodWithWrongAttribute()
	{

		ArrayList<Person> nonEmptyList = new ArrayList<Person>();
		nonEmptyList.add(new Person("Hans M�ller", 21, "Blumen-Weg 10"));
		nonEmptyList.add(new Person("Adelheid Tanzer", 21, "Markt-Graben-Weg 5"));
		sort.sortCollection(nonEmptyList, "doesNotExist", false);
		sort.printCollection(nonEmptyList, "doesNotExist");

	}

	@Test
	public void testSortMethodWithEmptyListAndWrongAttribute()
	{
		ArrayList<Person> emptyList = new ArrayList<Person>();
		sort.sortCollection(emptyList, "doesNotExist", false);
		sort.printCollection(emptyList, "doesNotExist");

	}

}
