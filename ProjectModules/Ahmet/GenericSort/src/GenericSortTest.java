import java.util.ArrayList;

import org.junit.Test;

public class GenericSortTest
{

	@Test
	public void testSortMethodWithEmptyList()
	{
		ArrayList<Person> emptyList = new ArrayList<Person>();
		SortClass.sortCollection(emptyList, "getName", false);
		SortClass.printCollection(emptyList, "getName");

	}

	@Test
	public void testSortMethodWithWrongAttribute()
	{

		ArrayList<Person> nonEmptyList = new ArrayList<Person>();
		nonEmptyList.add(new Person("Hans Müller", 21, "Blumen-Weg 10"));
		nonEmptyList.add(new Person("Adelheid Tanzer", 21, "Markt-Graben-Weg 5"));
		SortClass.sortCollection(nonEmptyList, "doesNotExist", false);
		SortClass.printCollection(nonEmptyList, "doesNotExist");

	}

	@Test
	public void testSortMethodWithEmptyListAndWrongAttribute()
	{
		ArrayList<Person> emptyList = new ArrayList<Person>();
		SortClass.sortCollection(emptyList, "doesNotExist", false);
		SortClass.printCollection(emptyList, "doesNotExist");

	}

}
