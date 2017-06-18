package sepm.creche.generator;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sepm.creche.executors.NewUserMailNotifierThread;
import sepm.creche.logger.AuditLog;
import sepm.creche.mail.MailService;
import sepm.creche.models.GalleryPic;
import sepm.creche.models.User;
import sepm.creche.models.UserRole;
import sepm.creche.repositories.UserRepository;
import sepm.creche.services.GalleryPicService;
import sepm.creche.services.UserService;
import sepm.creche.ui.beans.SessionInfoBean;
import sepm.creche.utils.FileUploader;

/**
 * 
 * Generates a new User(Parent) for the User Database.
 * 
 * @author Stefan
 */

@Component
@Scope("session")
public class UserGenerator
{

	// wired beans
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private AuditLog auditLog;
	@Autowired
	private SessionInfoBean sessionInfo;
	@Autowired
	private MailService mailService;
	@Autowired
	private FileUploader fileUploader;
	@Autowired
	private GalleryPicService galleryPicService;

	// attributes for the class
	private String fileName;

	public UserGenerator()
	{
		fileName = "";
	}

	/**
	 * This method takes the given information from the input and saves it, when
	 * met the guidelines. Guidelines: passwords match, length of password
	 * minimal 6, user doesn't exist, e-mail contains an '@' and has a
	 * minumlength of 4(x@x.at), given first and lastname, e-mail not already
	 * taken, phone given, address given, sex given
	 * 
	 * @param newUser
	 *            the new user(parent) passed by the handler class
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void addNewUser(User newUser)
	{
		// create a Parent-User
		User user = new User();
		// filled in values
		user.setFirstName(newUser.getFirstName());
		user.setLastName(newUser.getLastName());
		user.setUsername(newUser.getUsername());
		user.setEmail(newUser.getEmail());
		user.setPhone(newUser.getPhone());
		user.setAddress(newUser.getAddress());
		user.setSex(newUser.getSex());

		// other values to be filled for a User
		user.setEnabled(true);
		user.setInactive(true); // -> at least one child has to be selected in
								// registration
		user.setSendEmails(true);
		user.setCreateDate(new Date());
		user.setCreateUser(userRepository.findFirstByUsername("admin"));
		Set<UserRole> newset = new HashSet<UserRole>();
		newset.add(UserRole.PARENT);
		user.setRoles(newset);

		String passwd = UUID.randomUUID().toString().substring(0, 6);
		PasswordEncoder encoder = new StandardPasswordEncoder();
		user.setPassword(encoder.encode(passwd));

		FacesContext facesContext = FacesContext.getCurrentInstance();
		// check if inputs are correct
		if (!userService.userExists(user) && user.getEmail().contains("@") && user.getEmail().length() > 4
				&& user.getFirstName().length() > 0 && user.getLastName().length() > 0
				&& userRepository.findByEmail(user.getEmail()) == null && newUser.getPhone().length() > 0
				&& newUser.getAddress().length() > 0 && newUser.getUsername().length() > 0
				&& newUser.getSex().length() > 0)
		{

			// save user
			userRepository.save(user);

			// log event
			auditLog.log(sessionInfo.getCurrentUser(), "New user(parent) with username " + newUser.getUsername()
					+ " and name " + newUser.getFirstName() + " " + newUser.getLastName() + " generated");

			// adding the gallerypicture
			GalleryPic pic = new GalleryPic();
			pic.setUserID(user.getUsername());
			if (fileName != "")
			{
				pic.setPictureReference(fileName);
			} else
			{
				pic.setPictureReference(GalleryPicService.defaultProfilePic);
			}
			galleryPicService.setProfilePic(pic);

			// sends confirmation email
			newUser.setPassword(passwd);
			notifyRegisteredUser(newUser);

			reinit();

			// redirect if registration was successful
			try
			{
				if (FacesContext.getCurrentInstance() != null)
					FacesContext.getCurrentInstance().getExternalContext().redirect("successRegistration.xhtml");
			} catch (IOException e)
			{
				e.printStackTrace();
			}

		} else
		{
			// display the wrong input
			if (user.getUsername().length() == 0)
			{
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bitte das Feld Benutzername ausfüllen!", ""));
			} else if (userService.userExists(user))
			{
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
						"Benutzername schon vorhanden, wählen Sie bitte einen anderen!", ""));
			} else if (user.getFirstName().length() == 0 || user.getLastName().length() == 0)
			{
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
						"Bitte die Felder Vor- und Nachname ausfüllen!", ""));
			} else if (!user.getEmail().contains("@") || user.getEmail().length() < 4)
			{
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL,
								"Ungültige E-Mail Addesse, die Mindestlänge beträgt 4 Zeichen und ein '@' Symbol muss vorhanden sein!",
								""));
			} else if (userRepository.findByEmail(user.getEmail()) != null)
			{
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
						"E-Mail Adresse schon vorhanden, bitte wählen Sie eine andere!", ""));
			} else if (newUser.getPhone().length() == 0)
			{
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bitte das Feld Telefonnummer ausfüllen!", ""));
			} else if (newUser.getAddress().length() == 0)
			{
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bitte das Feld Adresse ausfüllen!", ""));
			} else if (newUser.getSex().length() == 0)
			{
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Bitte ein Geschlecht auswählen!", ""));
			}

		}

	}

	private void notifyRegisteredUser(User user)
	{
		NewUserMailNotifierThread p = new NewUserMailNotifierThread(mailService, user);
		new Thread(p).start();
	}

	/**
	 * This method handles the fileuploader for the photo.
	 * 
	 * @param event
	 *            an event to get the uploaded file from
	 * @throws IOException
	 * 
	 */
	public void handleFileUpload(FileUploadEvent event) throws IOException
	{

		fileName = fileUploader.handleFileUpload(event);
	}

	public void reinit()
	{
		fileName = "";
	}

}
