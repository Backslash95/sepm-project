package sepm.creche.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.models.User;

/**
 * This Class provides a logger which are stored in a file
 * 
 * @author Sebastian Grabher
 */

@Component
@Scope("singleton")
public class AuditLog {
	private Logger logger = Logger.getLogger("AuditLog");
	private FileHandler fileHandler;
	private Formatter formatter;
	private final String LOGFILE_PATH = "files/Audit.log";

	// initiliazes the logger
	private void init() {
		try {
			// This block configure the logger with handler and formatter
			fileHandler = new FileHandler(LOGFILE_PATH, true);
			logger.addHandler(fileHandler);
			formatter = new CustomFormatter();
			fileHandler.setFormatter(formatter);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Logger getLog() {
		if (fileHandler == null) {
			init();
		}
		return logger;
	}

	// logs a message
	public void log(String s) {
		getLog();
		logger.info(":  " + s);
	}

	// logs a message and the user which caused the log message
	public void log(User user, String s) {
		getLog();
		String userName = null;
		if (user != null)
			userName = user.getUsername();
		if (userName == null) {
			userName = "";
			if (s == null)
				s = "";
		}
		logger.info(" USER:" + userName + ":  " + s);
	}

}
