package sepm.creche.repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sepm.creche.models.Reminder;

public interface ReminderRepository extends AbstractRepository<Reminder, Long>
{

	@Query("SELECT u FROM Reminder u WHERE u.date <= :endDate AND u.date >= :startDate")
	Collection<Reminder> findTodaysReminders(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
