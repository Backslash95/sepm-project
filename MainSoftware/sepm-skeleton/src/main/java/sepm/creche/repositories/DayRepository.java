package sepm.creche.repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sepm.creche.models.Day;
import sepm.creche.models.DayType;

/**
 * Repository for managing {@link Day} entities. This repository implements the
 * abstract repository{@link AbstractRepository}.
 *
 * @author Stefan
 */
public interface DayRepository extends AbstractRepository<Day, Long> {

	/**
	 * Finds the first entry for the matching ID.
	 *
	 * @param dayID
	 *            the Identifier for the search
	 * @return the found Day
	 */
	Day findFirstByDayID(int dayID);

	/**
	 * Finds the first entry for the matching date.
	 *
	 * @param date
	 *            the date for the search
	 * @return the found Day
	 */
	Day findFirstByDate(Date date);

	/**
	 * Finds the all days between start and end date with a given day type.
	 *
	 */
	@Query("SELECT u FROM Day u WHERE u.dayType = :dayType AND u.date < :endDate AND u.date > :startDate")
	Collection<Day> findDaysByType(@Param("dayType") DayType dayType, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate);

}
