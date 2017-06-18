package sepm.creche.logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * UI Controller for the audit log. This class handles the log messages which
 * are stored in a file
 * 
 * @author Sebastian Grabher
 *
 */

@Component
@Scope("view")
public class LogController {
	private final int DATE_LENGTH = 31;

	public ArrayList<LogMessage> readFile() {
		ArrayList<LogMessage> everything = new ArrayList<LogMessage>();
		BufferedReader br;
		String timestamp;
		String userName;
		String tempString1;
		String tempString2;
		String logString;
		int logCharacterDelay;
		LogMessage logMessage;

		try {
			br = new BufferedReader(new FileReader("files/Audit.log"));
			String line = br.readLine();

			timestamp = "";
			userName = "";
			tempString1 = "";
			tempString2 = "";
			logString = "";
			logCharacterDelay = 0;

			while (line != null) {
				timestamp = line.substring(0, DATE_LENGTH);
				tempString1 = "USER:test " + line.substring(DATE_LENGTH);
				tempString2 = tempString1.substring(0, 4);
				if (tempString2.equals("USER")) {
					userName = tempString1.substring(5).split(" ")[0];
					logCharacterDelay = userName.length() + 5;
				}
				logString = tempString1.substring(logCharacterDelay);

				logMessage = new LogMessage(timestamp, userName, logString);
				everything.add(0, logMessage);
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		print(everything);
		return everything;
	}

	private void print(ArrayList<LogMessage> everything) {
		for (LogMessage logMessage : everything) {
			System.out.println(logMessage.getTimestamp());
			System.out.println(logMessage.getUser());
			System.out.println(logMessage.getMessage());
			System.out.println();
		}
	}

	public Collection<LogMessage> getLogs() {

		return readFile();
	}

}
