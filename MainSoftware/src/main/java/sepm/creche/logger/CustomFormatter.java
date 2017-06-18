package sepm.creche.logger;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * This Class sets up a custom format for the log messages
 * 
 * @author Sebastian Grabher
 */

public class CustomFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		StringBuilder sb = new StringBuilder();
		sb.append(new Date().toString());
		sb.append(record.getMessage()).append(System.lineSeparator());
		return sb.toString();
	}

}
