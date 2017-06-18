package sepm.creche.ui.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.logger.AuditLog;
import sepm.creche.mail.MailService;
import sepm.creche.models.User;
import sepm.creche.repositories.UserRepository;

/**
 * This Class provides a switch to toggle if emails are sent to an account
 * 
 * @author Sebastian Grabher
 */
@Component
@Scope("view")
public class SelectBooleanView
{

	@Autowired
	private AuditLog auditLog;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SessionInfoBean sessionInfoBean;

	@Autowired
	private MailService mailService;

	private boolean value;

	public boolean getValue()
	{
		return sessionInfoBean.getCurrentUser().getSendEmails();
	}

	public void setValue(boolean value)
	{
		this.value = value;
	}

	public void submit()
	{
		User user = sessionInfoBean.getCurrentUser();
		user.setSendEmails(value);
		userRepository.save(user);

		mailService.sendRegistrationEmail(user);

		// log message
		String toggle = "OFF";
		if (value)
		{
			toggle = "ON";
		}
		auditLog.log(user, "User toggled sending of emails to: " + toggle);
	}

	public void setSessionInfoBean(SessionInfoBean sessionInfoBean){
		this.sessionInfoBean = sessionInfoBean;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void setAuditLog(AuditLog auditLog) {
		this.auditLog = auditLog;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
	
	
	
	
}