package at.qe.sepm.skeleton.stefan.repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.qe.sepm.skeleton.stefan.models.TodaysKids;


public interface TodaysKidsRepository extends AbstractRepository<TodaysKids, Long>{

	TodaysKids findFirstByChildId(int childID);
	
	TodaysKids findFirstBySignedDate(Date signedDate);
	
	TodaysKids findFirstByTodaysKidsID(int todaysKidsID);
	
	@Query("SELECT u FROM TodaysKids u WHERE u.signedDate = :signedDate")
	Collection<TodaysKids> getKidCollection(@Param("signedDate") Date signedDate);
	
	

}
