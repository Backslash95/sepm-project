package sepm.creche.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.models.Comment;
import sepm.creche.repositories.CommentRepository;
import sepm.creche.ui.beans.SessionInfoBean;

@Component
@Scope("view")
public class CommentService
{
	@Autowired
	private CommentRepository commentRepository;

	private String commentString;

	@Autowired
	private SessionInfoBean sessionInfoBean;

	public CommentService()
	{
		setComment(new Comment());
	}

	private Comment comment;

	public void addComments(String picname)
	{
		if (commentString.isEmpty())
			return;
		Comment c = new Comment();
		c.setComment(commentString);
		c.setPicture(picname);
		c.setUsername(sessionInfoBean.getCurrentUser().getUsername());
		c.setDate(new Date());
		commentRepository.save(c);
		setComment(new Comment());
		commentString = "";
	}

	public Comment getComment()
	{
		return comment;
	}

	public void setComment(Comment comment)
	{
		this.comment = comment;
	}

	public void lis(String a)
	{
		System.out.println(a);
	}

	public String getCommentString()
	{
		return commentString;
	}

	public void setCommentString(String commentString)
	{
		this.commentString = commentString;
	}

	public List<Comment> loadComments(String picname)
	{
		List<Comment> commentList = new ArrayList<Comment>();
		commentList.addAll(commentRepository.findByPicture(picname));

		Collections.sort(commentList, new Comparator<Comment>()
		{
			public int compare(Comment m1, Comment m2)
			{
				return m2.getDate().compareTo(m1.getDate());
			}
		});

		return commentList;
	}
}
