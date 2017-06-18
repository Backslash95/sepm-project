package at.qe.sepm.skeleton.stefan.repositories;

import at.qe.sepm.skeleton.stefan.models.TodaysKids;

public interface TodaysKidsRepository extends AbstractRepository<TodaysKids, Long>{

	TodaysKids findFirstByTodaysKidsID(int childID);
	
}
