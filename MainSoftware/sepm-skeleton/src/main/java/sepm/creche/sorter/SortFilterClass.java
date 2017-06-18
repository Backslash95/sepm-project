package sepm.creche.sorter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A generic Sort and Filter Class, which is able to perform the desired
 * function on a given attribute of a collection of a certain class.
 * 
 * @author Aspir
 */
@Component
@Scope("session")
public class SortFilterClass
{
	private String filter;

	/**
	 * Prints the given collection for the specified get-method.
	 * 
	 * @param collection
	 *            the specified collection
	 * @param methodName
	 *            the get-method for the desired attribute as a String
	 */
	public <U, T extends Collection<U>> void printCollection(T collection, String methodName)
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

	/**
	 * Prints the given collection in a certain format. Prints every entry of
	 * the class.
	 * 
	 * @param collection
	 *            the specified collection
	 */
	public <U, T extends Collection<U>> void printCollectionFormatted(T collection)
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

	/**
	 * Sorts the given collection on the specified attribute either des or asc.
	 * 
	 * @param collection
	 *            the specified collection
	 * @param methodName
	 *            the get-method for the desired attribute as a String
	 * @param descending
	 *            if des or asc sort
	 */
	public <U, T extends Collection<U>> void sortCollection(T collection, String methodName, boolean descending)
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

	/**
	 * Filters all entries of a Collection on the specified attribute and
	 * filter.
	 * 
	 * @param collection
	 *            the specified collection
	 * @param methodName
	 *            the get-method for the desired attribute as a String
	 * @param filter
	 *            filter as a String
	 * @param descending
	 *            if des or asc sort
	 */
	public <U, T extends Collection<U>> Collection<U> filterCollection(T collection, String methodName, String filter,
			boolean descending)
	{
		if (collection.isEmpty())
		{
			System.out.print("Collection is empty!\n");
			return new HashSet<U>();
		}
		Class<? extends Object> className = collection.iterator().next().getClass();

		Method method = null;

		try
		{
			method = className.getMethod(methodName);
		} catch (NoSuchMethodException | SecurityException e1)
		{
			System.out.print("Sort call aborted! Specified Attribute does not exist!\n");
			return new HashSet<U>();
		}

		Collection<U> copyOfCollection = new HashSet<U>();

		for (U element : collection)
		{
			try
			{
				if (Pattern.compile(Pattern.quote(filter), Pattern.CASE_INSENSITIVE)
						.matcher(method.invoke(element).toString()).find())
				{
					copyOfCollection.add(element);
				}
			} catch (IllegalAccessException e)
			{
				System.out.print("Sorting failed: Illegal Element Access");

			} catch (IllegalArgumentException e)
			{
				System.out.print("Sorting failed: Illegal Element");
			} catch (InvocationTargetException e)
			{
				System.out.print("Sorting failed: Illegal Target Invocation");
			}
		}

		return copyOfCollection;

	}

	public String getFilter()
	{
		return filter;
	}

	public void setFilter(String filter)
	{
		this.filter = filter;
	}

}
