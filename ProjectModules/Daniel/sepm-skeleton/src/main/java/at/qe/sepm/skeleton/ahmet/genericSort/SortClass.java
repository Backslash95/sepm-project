package at.qe.sepm.skeleton.ahmet.genericSort;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortClass
{

	public static void testSort()
	{

		ArrayList<Person> persons = new ArrayList<Person>();
		persons.add(new Person("Hans M�ller", 21, "Blumen-Weg 10"));
		persons.add(new Person("Adelheid Tanzer", 36, "Markt-Graben-Weg 5"));
		persons.add(new Person("Rene Burger", 28, "Feld-Stra�e 80"));
		persons.add(new Person("Bertha Bauer", 40, "Herz-Erzog-Gasse 20"));

		/* Print unsorted Collection */
		printCollection(persons, "getName");
		System.out.print("-----------------------------\n");

		/* Duplicate Collection */
		List<Person> list = new ArrayList<Person>(persons);

		/* Duplicate Collection */
		ArrayList<Person> persons1 = new ArrayList<Person>(persons);

		printCollection(persons1, "getName");
		printCollection(persons1, "getAddress");
		System.out.print("-----------------------------\n");

		Field[] fields1 = Person.class.getDeclaredFields();
		ArrayList<Field> fields = new ArrayList<Field>(Arrays.asList(fields1));
		printCollection(fields, "getName");
		System.out.print("-----------------------------\n");

		sortCollection(list, "getName", false);
		printCollection(list, "getName");
		printCollection(list, "getAddress");
		System.out.print("-----------------------------\n");

		sortCollection(list, "getAge", false);
		printCollection(list, "getName");
		printCollection(list, "getAddress");
		System.out.print("-----------------------------\n");

		sortCollection(list, "getAddress", false);
		printCollection(list, "getName");
		printCollection(list, "getAddress");
		System.out.print("-----------------------------\n");

		printCollectionFormatted(persons);

	}

	public static <U, T extends Collection<U>> void printCollection(T collection, String methodName)
	{
		if (collection.isEmpty())
		{
			System.out.print("Collection is empty!\n");
			return;
		}
		Class<? extends Object> className = collection.iterator().next().getClass();

		Method method = null;
		try
		{
			method = className.getMethod(methodName);
		} catch (NoSuchMethodException | SecurityException e1)
		{
			System.out.print("Print call aborted! Specified Attribute does not exist!\n");
			return;
		}

		for (U element : collection)
		{
			try
			{
				System.out.println(method.invoke(element));
			} catch (IllegalAccessException e)
			{
				System.out.print("Print failed: Illegal Element Access");
			} catch (IllegalArgumentException e)
			{
				System.out.print("Print failed: Illegal Element");
			} catch (InvocationTargetException e)
			{
				System.out.print("Print failed: Illegal Target Invocation");
			}
		}
	}

	public static <U, T extends Collection<U>> void printCollectionFormatted(T collection)
	{
		if (collection.isEmpty())
		{
			System.out.print("Collection is empty!\n");
			return;
		}
		Class<? extends Object> className = collection.iterator().next().getClass();

		Field[] fields1 = className.getDeclaredFields();
		ArrayList<Field> fields = new ArrayList<Field>(Arrays.asList(fields1));

		for (U element : collection)
		{
			for (Field attribute : fields)
			{
				try
				{
					attribute.setAccessible(true);
					System.out.print(attribute.getName() + ": " + attribute.get(element) + " | ");
				} catch (IllegalArgumentException | IllegalAccessException e)
				{
					System.out.print("Sort call aborted! Specified Attribute does not exist!\n");
					e.printStackTrace();
				}

			}

			System.out.print("\n");

		}

	}

	public static <U, T extends Collection<U>> void sortCollection(T collection, String methodName, boolean descending)
	{
		if (collection.isEmpty())
		{
			System.out.print("Collection is empty!\n");
			return;
		}
		Class<? extends Object> className = collection.iterator().next().getClass();

		Method method = null;

		try
		{
			method = className.getMethod(methodName);
		} catch (NoSuchMethodException | SecurityException e1)
		{
			System.out.print("Sort call aborted! Specified Attribute does not exist!\n");
			return;
		}

		Comparator<U> comparator = new CollectionComparator<U>(method, descending);

		Collections.sort((List<U>) collection, comparator);

	}

}
