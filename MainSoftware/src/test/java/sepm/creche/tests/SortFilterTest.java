package sepm.creche.tests;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import sepm.creche.sorter.CollectionComparator;
import sepm.creche.sorter.DummyPerson;
import sepm.creche.sorter.SortFilterClass;

public class SortFilterTest
{
	private SortFilterClass sort = new SortFilterClass();

	@Test
	public void testSortMethodWithEmptyList()
	{

		ArrayList<DummyPerson> emptyList = new ArrayList<DummyPerson>();
		sort.sortCollection(emptyList, "getName", false);
		sort.printCollection(emptyList, "getName");

	}

	@Test
	public void testSortMethodWithWrongAttribute()
	{

		ArrayList<DummyPerson> nonEmptyList = new ArrayList<DummyPerson>();
		nonEmptyList.add(new DummyPerson("Hans M�ller", 21, "Blumen-Weg 10"));
		nonEmptyList.add(new DummyPerson("Adelheid Tanzer", 21, "Markt-Graben-Weg 5"));
		sort.sortCollection(nonEmptyList, "doesNotExist", false);
		sort.printCollection(nonEmptyList, "doesNotExist");
		sort.printCollection(nonEmptyList, "getName");

	}

	@Test
	public void testSortMethodWithEmptyListAndWrongAttribute()
	{
		ArrayList<DummyPerson> emptyList = new ArrayList<DummyPerson>();
		sort.sortCollection(emptyList, "doesNotExist", false);
		sort.printCollection(emptyList, "doesNotExist");

	}

	@Test
	public void testSortMethod()
	{
		ArrayList<DummyPerson> nonEmptyList = new ArrayList<DummyPerson>();
		nonEmptyList.add(new DummyPerson("Hans M�ller", 21, "Blumen-Weg 10"));
		nonEmptyList.add(new DummyPerson("Adelheid Tanzer", 21, "Markt-Graben-Weg 5"));

		sort.filterCollection(nonEmptyList, "getName", "Tanz", true);
		sort.printCollectionFormatted(nonEmptyList);
		sort.printCollectionFormatted(new ArrayList<DummyPerson>());
		sort.filterCollection(new ArrayList<DummyPerson>(), "getName", "Tanz", true);
		sort.filterCollection(new ArrayList<Integer>(), "noSuchMethod", "Tanz", true);
	}

	@Test
	public void testComparator() throws NoSuchMethodException, SecurityException
	{
		ArrayList<DummyPerson> nonEmptyList = new ArrayList<DummyPerson>();
		nonEmptyList.add(new DummyPerson("Hans M�ller", 21, "Blumen-Weg 10"));
		nonEmptyList.add(new DummyPerson("Adelheid Tanzer", 21, "Markt-Graben-Weg 5"));

		CollectionComparator<DummyPerson> c = new CollectionComparator<DummyPerson>(
				DummyPerson.class.getMethod("getName"), false);

		CollectionComparator<DummyPerson> c1 = new CollectionComparator<DummyPerson>(
				DummyPerson.class.getMethod("getName"), true);

		Assert.assertTrue("Comparator failed!", c.compare(nonEmptyList.get(0), nonEmptyList.get(1)) >= 1);
		Assert.assertTrue("Comparator failed!", c.compare(nonEmptyList.get(1), nonEmptyList.get(0)) < 0);
		Assert.assertTrue("Comparator failed!", c1.compare(nonEmptyList.get(0), nonEmptyList.get(1)) < 0);
		Assert.assertTrue("Comparator failed!", c1.compare(nonEmptyList.get(1), nonEmptyList.get(0)) >= 1);

	}

}
