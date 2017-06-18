package at.qe.sepm.skeleton.persistence;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.repositories.UserRepository;

/**
 * Stores and manages current Database in a file. Can save to and load from
 * file.
 * 
 * @author ASPIR
 *
 */
@Component
@Scope("singleton")
public class DataManager
{
	public DataManager()
	{
		storageService = new StorageService();
		storageController = new StorageController<StorageService>();
	}

	@Autowired
	private UserRepository userRepository;

	StorageService storageService;

	StorageController<StorageService> storageController;

	/**
	 * Write to Object and then export Object to File. Errors handeled locally
	 * through Sysout.
	 */

	@Scheduled(cron = "0 0 * * * *") // every hour;
	// Sec,Min,Hour,Day of month, Month,
	// Day
	// of Week
	public void scheduledBackUp()
	{

		writeDataBaseToObj();
		storeDataBaseToFile();
		System.out.println("Backup of All DBs has been created!");
	}

	public void store()
	{
		storageService = new StorageService();
		scheduledBackUp();
	}

	/**
	 * On creation loads from File to obj and then tries to write do Database
	 * via Repositories.
	 */

	@PostConstruct
	public void load()
	{
		storageService = new StorageService();
		loadDataBaseToObj();
		if (storageService != null)
		{
			writeIntoDataBase();
			System.out.println("Successfully restored DBs from file!");

		} else
		{
			System.out.println("Aborting restore from file...");
		}

	}

	public void writeDataBaseToObj()
	{
		storageService.setUserDB(userRepository.findAll());

	}

	public void storeDataBaseToFile()
	{
		storageController.serializeObj(storageService);
	}

	public void loadDataBaseToObj()
	{
		storageService = storageController.deSerializeObj();
	}

	/**
	 * Checks if Entities exist in the DB, if not adds them to DB via
	 * Repository.
	 */

	public void writeIntoDataBase()
	{

		Collection<User> userDBinFile = storageService.getUserDB();

		for (User u : userDBinFile)
		{

			if (u != null && userRepository.findFirstByUsername(u.getUsername()) == null)
			{
				// System.out.println("Found NEW User ADDING " +
				// u.getUsername());
				userRepository.save(u);
			}

		}

	}

	public void addMessage(String summary)
	{
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
