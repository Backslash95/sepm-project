package at.qe.sepm.skeleton.ui.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.configs.MailServiceConfig;
import at.qe.sepm.skeleton.services.MailService;

/**
 * Bean for mail services
 *
 * @author André Potocnik <andre.potocnik@student.uibk.ac.at>
 */
@Component
@Scope("request")
public class MailManagerBean {
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private MailServiceConfig mailServiceConfig;
	
	/* Recipient of test mail */
	private String testReceiver;
	
    /**
     * Get mail configuration parameter
     * 
     * @param desired parameter
     * @return the parameter which is set at mail config
     * 
     */ 
	public String printMailConfig(String field) {
		switch (field) {
			case "host" : 
				return mailServiceConfig.getHost();
		
			case "port" : 
				return mailServiceConfig.getPort().toString();
		
			case "protocol" : 
				return mailServiceConfig.getProtocol();
		
			case "username" : 
				return mailServiceConfig.getUsername();
		
			case "password" : 
				return mailServiceConfig.getPassword();
		}
		return null;
	}
	
	/**
	 * Send a test Mail with predefined attributes
	 * 
	 * @param to recipient
	 */
	public void sendTestMail(String to) {
		String from = mailServiceConfig.getUsername();
		String subject = "Mail test";
		String text = "This is a test message!";
				
		mailService.sendMail(from, to, subject, text);
	}

	public String getTestReceiver() {
		return testReceiver;
	}
	
	public void setTestReceiver(String testReceiver) {
		this.testReceiver = testReceiver;
	}
}
