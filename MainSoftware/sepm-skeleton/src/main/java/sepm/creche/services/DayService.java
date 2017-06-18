package sepm.creche.services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.models.Day;
import sepm.creche.models.DayType;
import sepm.creche.repositories.DayRepository;

@Component
@Scope("session")
public class DayService
{

	@Autowired
	private DayRepository dayRepository;

	public void remove(Day entity)
	{
		dayRepository.delete(entity);
	}

	public Day save(Day entity)
	{
		return dayRepository.save(entity);
	}

	public Collection<Day> findHolidays(Date startDate, Date endDate)
	{
		return dayRepository.findDaysByType(DayType.HOLIDAY, startDate, endDate);
	}

	public Collection<Day> findRegularDays(Date startDate, Date endDate)
	{
		return dayRepository.findDaysByType(DayType.OCCUPATION, startDate, endDate);
	}

	public Day getDayWithID(int dayID)
	{
		return dayRepository.findFirstByDayID(dayID);
	}

}
