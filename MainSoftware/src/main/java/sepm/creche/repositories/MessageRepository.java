package sepm.creche.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sepm.creche.models.Message;

public interface MessageRepository extends AbstractRepository<Message, Long>
{
	List<Message> findByUserIDFrom(String userID);

	List<Message> findByUserIDTo(String userID);

	@Query("SELECT m FROM Message m WHERE m.alreadyRead = false and m.userIDTo=:userID")
	List<Message> findUnreadMsgs(@Param("userID") String userID);

	Message findByMsgID(int msgID);
}
