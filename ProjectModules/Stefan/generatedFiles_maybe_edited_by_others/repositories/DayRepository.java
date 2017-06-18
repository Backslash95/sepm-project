package at.qe.sepm.skeleton.repositories;

import at.qe.sepm.skeleton.model.Day;

/**
 * Repository for managing {@link Day} entities.
 *
 * @author Steve
 */
public interface DayRepository extends AbstractRepository<Day, Long> {

	Day findFirstByDayID(int dayID);

}
