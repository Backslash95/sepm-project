package sepm.creche.repositories;

import java.util.List;

import sepm.creche.models.Comment;

public interface CommentRepository extends AbstractRepository<Comment, Long>
{
	// Comment findFirstByCommentID(int commentID);

	List<Comment> findByPicture(String picturename);
}
