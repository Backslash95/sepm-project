package at.qe.sepm.skeleton.stefan.repositories;

import java.util.Date;

import at.qe.sepm.skeleton.stefan.models.Day;

/**
 * Repository for managing {@link Day} entities.
 *
 * @author Steve
 */
public interface DayRepository extends AbstractRepository<Day, Long>
{

	Day findFirstByDayID(int dayID);
	Day findFirstByDate(Date date);

}
