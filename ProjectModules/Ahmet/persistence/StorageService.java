package at.qe.sepm.skeleton.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import at.qe.sepm.skeleton.model.User;

/**
 * Storageservice to save DB in a file.
 */

public class StorageService implements Serializable
{
	private static final long serialVersionUID = 1L;

	public StorageService()
	{
		userDB = new ArrayList<User>();

	}

	private Collection<User> userDB;

	public Collection<User> getUserDB()
	{
		return userDB;
	}

	public void setUserDB(Collection<User> userDB)
	{
		this.userDB = userDB;
	}

}
