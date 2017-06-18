package sepm.creche.ui.controllers;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.models.GalleryPic;
import sepm.creche.models.User;
import sepm.creche.services.GalleryPicService;
import sepm.creche.ui.beans.SessionInfoBean;

/**
 * The controller for the ParentData.
 * 
 * @author Stefan
 */

@Component
@Scope("session")
public class ParentDataController {

	@Autowired
	private SessionInfoBean sessionInfo;
	@Autowired
	private GalleryPicService galleryPicService;

	// attributes for the class
	private User currentUser;

	/**
	 * This method is the constructor of the class.
	 */
	public ParentDataController() {
		currentUser = new User();
	}

	@PostConstruct
	public void init() {
		currentUser = sessionInfo.getCurrentUser();
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public String loadProfilePictrue() {
		GalleryPic pic = galleryPicService.loadProfilePic();
		return pic.getPictureReference();
	}

}
