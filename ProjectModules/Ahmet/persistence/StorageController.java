package at.qe.sepm.skeleton.persistence;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author ASPIR
 *
 * @param <objToSerialize>
 *            for the Type of Object to be stored in File.
 */
public class StorageController<objToSerialize> implements Serializable
{
	private static final long serialVersionUID = 1L;

	public void serializeObj(objToSerialize idx)
	{
		try
		{
			FileOutputStream fileOut = new FileOutputStream("dataBase.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(idx);
			out.close();
			fileOut.close();
			System.out.println("Data stored in dataBase.ser!");
		} catch (IOException i)
		{
			i.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public objToSerialize deSerializeObj()
	{
		objToSerialize idx = null;
		try
		{
			FileInputStream fileIn = new FileInputStream("dataBase.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			idx = (objToSerialize) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i)
		{
			System.out.println("Could not load from File!");
			return null;
		} catch (ClassNotFoundException c)
		{
			System.out.println("File does not match current state of Classes! Remove File!");
			return null;
		}
		return idx;
	}

}
