import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLog {
	private static Logger logger = Logger.getLogger("MyLog");  
    private static FileHandler fh;  
	
	public static void main(String[] args) {  

		MyLog.log("first test log");
		MyLog.log("log log logedy log");
		
		
	}
	
	private static void init(){
	    try {  
	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("C:/Users/Grabher Sebastian/DesktopMyLogFile.log");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  
	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	}
	
	private static Logger getLog(){
		if (fh == null) {
			init();
		}
		return logger;
	}
	
	public static void log(String s){
		getLog();
		logger.info(s);
	}
}
