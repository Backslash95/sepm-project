package sepm.creche.sorter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

/**
 * A custom comparator used for a generic sort.
 * 
 * @author Aspir
 */
public class CollectionComparator<T> implements Comparator<T>
{
	private Method attributeCaller;
	private boolean descending;

	/**
	 * Constructor needs 2 arguments.
	 * 
	 * @param attributeCaller
	 *            the specified attribute to perfom the sort on
	 * @param descending
	 *            if descending is true
	 */
	public CollectionComparator(Method attributeCaller, boolean descending)
	{
		this.attributeCaller = attributeCaller;
		this.descending = descending;
	}

	@Override
	public int compare(T o1, T o2)
	{

		try
		{
			if (descending)
			{
				return (-1) * (attributeCaller.invoke(o1).toString()).compareTo(attributeCaller.invoke(o2).toString());
			} else
			{
				return (attributeCaller.invoke(o1).toString()).compareTo(attributeCaller.invoke(o2).toString());
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

		return 0;
	}

}
