package at.qe.sepm.skeleton.ui.beans;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.model.Project;
import at.qe.sepm.skeleton.model.User;
import at.qe.sepm.skeleton.ui.controllers.UserListController;
/**
 * 
 * 
 * @author andre
 *
 */
@Component
@Scope("request")
public class StudentDataGrid implements Serializable {

	@Autowired
	private UserListController userListController;
	
	@Autowired
	private SessionInfoBean sessionInfoBean;
	
	@Autowired
	private Project project;
	
	
	private static final long serialVersionUID = 1L;

	private List<User> students;

	private User selectedStudent;
		
	public void init() {
		project = sessionInfoBean.getCurrentUser().getProject();
		
	}
}
