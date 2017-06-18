package sepm.creche.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.models.GalleryPic;
import sepm.creche.repositories.GalleryPicRepository;
import sepm.creche.ui.beans.SessionInfoBean;

@Component
@Scope("session")
public class GalleryPicService
{
	public static final String defaultProfilePic = "user-silhouette.jpg";

	@Autowired
	private GalleryPicRepository galleryPicRepository;

	@Autowired
	private SessionInfoBean sessionInfobean;

	public GalleryPic loadProfilePic()
	{
		if (sessionInfobean.getCurrentUser() == null)
		{
			return null;
		}
		GalleryPic pic = galleryPicRepository.findCurrentUsersProfilePic(sessionInfobean.getCurrentUser().getUsername(),
				true);
		if (pic == null)
		{
			pic = new GalleryPic();
			pic.setPictureReference("user-silhouette.jpg");
			pic.setUserID(sessionInfobean.getCurrentUser().getUsername());
			pic.setProfilePic(true);
			pic = galleryPicRepository.save(pic);
		}

		return pic;
	}

	public void setProfilePic(GalleryPic pic)
	{
		if (loadProfilePic() != null)
		{
			remove(loadProfilePic());
		}

		pic.setProfilePic(true);
		galleryPicRepository.save(pic);
	}

	public void saveGalleryPic(GalleryPic pic)
	{
		pic.setProfilePic(false);
		galleryPicRepository.save(pic);
	}

	public GalleryPic loadProfilePicByPicName(String picname)
	{

		return galleryPicRepository.findFirstByPictureReference(picname);

	}

	public GalleryPic loadProfilePicByID(int pictureID)
	{

		return galleryPicRepository.findFirstByPictureID(pictureID);

	}

	public List<GalleryPic> loadAllPics()
	{
		return galleryPicRepository.findByIsProfilePic(false);
	}

	public void remove(GalleryPic pic)
	{

		galleryPicRepository.delete(pic);

	}

}
