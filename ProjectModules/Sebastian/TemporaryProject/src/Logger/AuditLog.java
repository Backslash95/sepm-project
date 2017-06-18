package Logger;
import java.io.IOException;
import java.util.logging.Formatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class AuditLog {
	private static Logger logger = Logger.getLogger("MyLog");  
    private static FileHandler fileHandler;  
	private static Formatter formatter;
	private static final String LOGFILE_PATH = "D:/Daten/Drive/Uni/4. Semester/Softwareentwicklung/ps1_team2/Projektcode/Sebastian/TemporaryProject/output/MyLogFile.log";
    
	public static void main(String[] args) {  

		AuditLog.log("first test log");
		AuditLog.log("log log logedy log");
		
		
	}
	
	private static void init(){
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
	
	private static Logger getLog(){
		if (fileHandler == null) {
			init();
		}
		return logger;
	}
	
	public static void log(String s){
		getLog();
		logger.info(s);
	}
  
}
