package at.qe.sepm.skeleton.sebastian.logger;
import java.io.IOException;
import java.util.logging.Formatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("singleton")
public class AuditLog {
	private  Logger logger = Logger.getLogger("AuditLog");  
    private  FileHandler fileHandler;  
	private  Formatter formatter;
	private  final String LOGFILE_PATH = "files/Audit.log";
   
	
	
	private  void init(){
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
	
	private  Logger getLog(){
		if (fileHandler == null) {
			init();
		}
		return logger;
	}
	
	public  void log(String s){
		getLog();
		logger.info(s);
	}
  
}
