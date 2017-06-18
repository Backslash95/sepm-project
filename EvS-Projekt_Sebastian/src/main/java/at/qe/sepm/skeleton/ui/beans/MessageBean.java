package at.qe.sepm.skeleton.ui.beans;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * Bean that fires different messages
 *
 * @author André Potocnik <andre.potocnik@student.uibk.ac.at>, Daniel Proksch <Daniel.Proksch@student.uibk.ac.at>
 */
@Component
@Scope("view")
public class MessageBean {
	
	/**
	 * Displays an info message on the active page
	 * 
	 * @param message the message to display as info message
	 */
	public void infoMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", message));
	}
	
	/**
	 * Displays an info message on the active page
	 * 
	 * @param message the message to display warning message
	 */
	public void warningMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", message));
	}
	
	/**
	 * Displays an info message on the active page
	 * 
	 * @param message the message to display as error message
	 */
	public void errorMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
	}
	
	/**
	 * Message is shown if a student is checked-in on a work package
	 */
	public void	infoStudentCheckedIn() {
		infoMessage("You're currently checked-in on an work package.");
	}
	
	/** Message is shown if someone enters wrong username password combination
	 * on login
	 */
    public void errorWrongUserPass() {
    	errorMessage("Your username or password is incorrect.");
    }

}
