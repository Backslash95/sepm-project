package sepm.creche.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sepm.creche.models.GalleryPic;

/**
 * 
 *
 * @author aspir
 */
public interface GalleryPicRepository extends AbstractRepository<GalleryPic, Long>
{

	/**
	 * Finds the first entry for the matching ID.
	 *
	 * @param pictureID
	 *            the Identifier for the search
	 * @return the found Picture
	 */
	GalleryPic findFirstByPictureID(int pictureID);

	GalleryPic findFirstByPictureReference(String picname);

	GalleryPic findFirstByUserID(String userID);

	@Query("SELECT u FROM GalleryPic u WHERE u.isProfilePic = :isProfilePic AND u.userID = :userID")
	GalleryPic findCurrentUsersProfilePic(@Param("userID") String userID, @Param("isProfilePic") boolean isProfilePic);

	List<GalleryPic> findByIsProfilePic(boolean isProfilePic);

}
