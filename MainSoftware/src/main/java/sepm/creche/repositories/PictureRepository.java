package sepm.creche.repositories;

import sepm.creche.models.Picture;

/**
 * Repository for managing {@link Picture} entities. 
 *
 * @author Stefan
 */
public interface PictureRepository extends AbstractRepository<Picture, Long> {

	/**
	 * Finds the first entry for the matching ID.
	 *
	 * @param pictureID
	 *            the Identifier for the search
	 * @return the found Picture
	 */
	Picture findFirstByPictureID(int pictureID);
	
	/**
	 * Finds the first entry for the matching ID.
	 *
	 * @param childID
	 *            the Identifier for the search
	 * @return the found Picture
	 */
	Picture findFirstByChildID(int childID);


}
