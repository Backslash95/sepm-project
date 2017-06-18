package at.qe.sepm.skeleton.ui.controllers;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.stefan.models.Child;
import at.qe.sepm.skeleton.stefan.models.User;
import at.qe.sepm.skeleton.ui.beans.SessionInfoBean;

@Component
@Scope("view")
public class ChildUIManager {

	@Autowired
	private SessionInfoBean session;

	private Collection<Child> collectionChild;

	public ChildUIManager() {
		collectionChild = new HashSet<Child>();
	}

	public Collection<Child> loadChildList() {
		collectionChild = new HashSet<Child>();
		User currentUsr = session.getCurrentUser();

		if (currentUsr != null) {
			if (currentUsr.getChildSetUser() != null) {
				collectionChild.addAll(currentUsr.getChildSetUser());
			}
		}

		return collectionChild;
	}

}
