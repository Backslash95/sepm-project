package at.qe.sepm.skeleton.ahmet.genericSort;
public class Person
{
	public Person(String name, int age, String address)
	{
		this.name = name;
		this.age = age;
		this.address = address;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getAge()
	{
		return age;
	}

	public void setAge(int age)
	{
		this.age = age;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	private String name;
	private int age;
	private String address;

}
