package sepm.creche.repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sepm.creche.models.TodaysKids;

/**
 * Repository for managing {@link TodaysKids} entities. This repository
 * implements the abstract repository{@link AbstractRepository}.
 *
 * @author Daniel,Fabian
 */

public interface TodaysKidsRepository extends AbstractRepository<TodaysKids, Long> {

	/**
	 * Finds the first entry for the matching Identifier (Id of the class).
	 *
	 * @param todaysKidsID
	 *            the identifier for the search
	 * @return the found entity of TodaysKids
	 */
	TodaysKids findFirstByTodaysKidsID(int todaysKidsID);

	/**
	 * Finds the first entry for the matching childID (Id of the class
	 * {@link Child}).
	 *
	 * @param childID
	 *            the identifier for the search
	 * @return the found entity of TodaysKids
	 */
	TodaysKids findFirstByChildId(int childID);

	/**
	 * Finds the first entry for the matching date.
	 *
	 * @param signedDate
	 *            the date for the search
	 * @return the found entity of TodaysKids
	 */
	TodaysKids findFirstBySignedDate(Date signedDate);

	/**
	 * Finds all entites containing the given date and writes it into a list.
	 *
	 * @param signedDate
	 *            the date for the search
	 * @return the list of all found entites of TodaysKids
	 */
	@Query("SELECT u FROM TodaysKids u WHERE u.signedDate = :signedDate")
	Collection<TodaysKids> getKidCollection(@Param("signedDate") Date signedDate);

}
