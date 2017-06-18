package sepm.creche.ui.controllers;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sepm.creche.logger.AuditLog;
import sepm.creche.models.GalleryPic;
import sepm.creche.models.User;
import sepm.creche.repositories.UserRepository;
import sepm.creche.services.GalleryPicService;
import sepm.creche.ui.beans.SessionInfoBean;
import sepm.creche.utils.FileUploader;

/**
 * The controller for editing the Parent
 * 
 * @author Stefan
 */

@Component
@Scope("session")
public class EditParentController {

	// wired beans
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuditLog auditLog;
	@Autowired
	private SessionInfoBean sessionInfoBean;
	@Autowired
	private FileUploader fileUploader;
	@Autowired
	private GalleryPicService galleryPicService;

	// attributes for the class
	private User currentUser;
	private boolean value;
	public static final int passWordMinLegth = 6;
	private String checkpassword;
	private String fileName;
	private String oldpassword;

	public EditParentController() {
		currentUser = new User();
		fileName = "";
		oldpassword = "";
		checkpassword = "";
	}

	/**
	 * This method takes the given information from the input and saves it for
	 * the selected user, when met the guidelines.
	 * 
	 */

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void saveData() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		// check if inputs are valid
		if (currentUser.getEmail().contains("@") && currentUser.getEmail().length() > 4
				&& currentUser.getFirstName().length() > 0 && currentUser.getLastName().length() > 0
				&& (userRepository.findByEmail(currentUser.getEmail()) == null
						|| (userRepository.findByEmail(currentUser.getEmail())).equals(currentUser))
				&& currentUser.getPhone().length() > 0 && currentUser.getAddress().length() > 0
				&& currentUser.getSex().length() > 0 && checkpassword.equals(currentUser.getPassword())
				&& ((currentUser.getPassword().length() > EditParentController.passWordMinLegth)
						|| (currentUser.getPassword().length() == 0 && checkpassword.length() == 0))) {

			if (currentUser.getPassword().length() == 0) {
				currentUser.setPassword(oldpassword);
			}

			PasswordEncoder encoder = new StandardPasswordEncoder();
			currentUser.setSendEmails(value);

			currentUser.setPassword(encoder.encode(currentUser.getPassword()));
			userRepository.save(currentUser);

			// adding the gallerypicture
			GalleryPic pic = new GalleryPic();
			if (fileName != "") {
				pic.setPictureReference(fileName);
				pic.setUserID(currentUser.getUsername());
				galleryPicService.setProfilePic(pic);
			}

			// log message
			String toggle = "OFF";
			if (value) {
				toggle = "ON";
			}

			// log event
			auditLog.log(currentUser,
					"user " + currentUser.getFirstName() + " " + currentUser.getLastName() + " was edited");
			auditLog.log(currentUser, "User toggled sending of emails to: " + toggle);

			reinit();

			if (FacesContext.getCurrentInstance() != null) {
				Flash flash = facesContext.getExternalContext().getFlash();
				flash.setKeepMessages(true);
				flash.setRedirect(true);
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Änderungen wurden erfolgreich gespeichert!", ""));
				try {
					FacesContext.getCurrentInstance().getExternalContext().redirect("../../../secured/welcome.xhtml");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else {
			// display the wrong input
			if (currentUser.getFirstName().length() == 0 || currentUser.getLastName().length() == 0) {
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
						"Bitte die Felder Vor- und Nachname ausfüllen!", ""));
			} else if (!currentUser.getEmail().contains("@") || currentUser.getEmail().length() < 4) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Ungültige E-Mail Addesse, die Mindestlänge beträgt 4 Zeichen und ein '@' Symbol muss vorhanden sein!",
								""));
			} else if (!(userRepository.findByEmail(currentUser.getEmail()).equals(currentUser))) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"E-Mail Adresse schon bei einem anderen Benutzer vorhanden, bitte wählen Sie eine andere!",
								""));
			} else if (currentUser.getPhone().length() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bitte das Feld Telefonnummer ausfüllen!", ""));
			} else if (currentUser.getAddress().length() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bitte das Feld Adresse ausfüllen!", ""));
			} else if (currentUser.getSex().length() == 0) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bitte ein Geschlecht auswählen!", ""));
			} else if (currentUser.getPassword().length() <= EditParentController.passWordMinLegth) {
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
						"Das gewählte Passwort ist zu kurz, die Mindestlänge beträgt 7 Zeichen!", ""));
			} else if (!checkpassword.equals(currentUser.getPassword())) {
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Passwörter stimmen nicht überein!", ""));
			}
		}

	}

	/**
	 * This method handles the fileuploader for the photo.
	 * 
	 * @param event
	 *            an event to get the uploaded file from
	 * @throws IOException
	 * 
	 */
	public void handleFileUpload(FileUploadEvent event) throws IOException {

		fileName = fileUploader.handleFileUpload(event);
	}

	public void reinit() {
		fileName = "";
		checkpassword = "";
		oldpassword = "";
	}

	// getters and setters
	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
		oldpassword = currentUser.getPassword();
		try {
			if (FacesContext.getCurrentInstance() != null)
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/secured/restricted/ParentData/editParent.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean getValue() {
		return getCurrentUser().getSendEmails();
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public String getCheckpassword() {
		return checkpassword;
	}

	public void setCheckpassword(String checkpassword) {
		this.checkpassword = checkpassword;
	}

}
