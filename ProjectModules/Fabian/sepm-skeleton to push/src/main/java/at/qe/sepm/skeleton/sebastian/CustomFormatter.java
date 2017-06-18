package at.qe.sepm.skeleton.sebastian;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomFormatter extends Formatter
{

	@Override
	public String format(LogRecord record)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(record.getLevel()).append(" - " + new Date().toString() + ":  ");
		sb.append(record.getMessage()).append(System.lineSeparator());
		return sb.toString();
	}

}
