package at.qe.sepm.skeleton.ui.controllers;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.stefan.models.Child;
import at.qe.sepm.skeleton.stefan.models.User;
import at.qe.sepm.skeleton.stefan.repositories.ChildRepository;

@Component
@Scope("view")
public class ContactListController {

	@Autowired
	private ChildRepository childRepository;

	private Collection<User> collectionUser;

	public ContactListController() {
		collectionUser = new HashSet<User>();
	}

	public Collection<User> loadParentContactList() {

		for (Child child : childRepository.findAll()) {
			collectionUser.addAll(child.getUserSetChild());
		}

		return collectionUser;
	}

	public String convertChildTable(Collection<Child> childCollection) {
		StringBuilder childnames = new StringBuilder();
		for (Child child : childCollection) {
			childnames.append(child.getName());
			childnames.append(",");
		}
		// get rid off last comma
		childnames.deleteCharAt(childnames.length() - 1);
		return childnames.toString();
	}
}
