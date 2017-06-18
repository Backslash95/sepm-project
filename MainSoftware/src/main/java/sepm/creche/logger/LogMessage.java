package sepm.creche.logger;

/**
 * This Class sets up a log message
 * 
 * @author Sebastian Grabher
 */
public class LogMessage {

	private String timestamp = "";
	private String user = "null";
	private String message = "";

	public LogMessage(String timestamp, String user, String message) {
		this.timestamp = timestamp;
		if (message != null)
			this.message = message;
		if (message != null)
			this.user = user;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
