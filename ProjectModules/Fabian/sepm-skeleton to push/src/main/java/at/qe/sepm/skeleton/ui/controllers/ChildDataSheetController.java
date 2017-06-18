package at.qe.sepm.skeleton.ui.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.stefan.models.Child;
import at.qe.sepm.skeleton.stefan.models.Person;
import at.qe.sepm.skeleton.stefan.models.User;
import at.qe.sepm.skeleton.stefan.repositories.ChildRepository;

@Component
@Scope("session")
public class ChildDataSheetController {

	@Autowired
	private ChildRepository childRepository;

	private int selectedChildID;
	private Child selectedChild;
	private Collection<User> parentCollection;
	private Collection<Person> personCollection;

	public ChildDataSheetController() {
		selectedChildID = 0;
		selectedChild = new Child();
		parentCollection = new HashSet<User>();
		personCollection = new HashSet<Person>();
	}

	public Collection<User> loadParentList() {
		parentCollection = new HashSet<User>();
		if (selectedChild.getUserSetChild() != null)
			parentCollection.addAll(selectedChild.getUserSetChild());

		return parentCollection;
	}

	public Collection<Person> loadPersonList() {
		personCollection = new HashSet<Person>();
		if (selectedChild.getPersonSet() != null)
			personCollection.addAll(selectedChild.getPersonSet());

		return personCollection;
	}

	public int getSelectedChildID() {
		return selectedChildID;
	}

	public void setSelectedChildID(int selectedChildID) {
		this.selectedChildID = selectedChildID;
		selectedChild = childRepository.findFirstByChildID(selectedChildID);
		try {
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/stefan/KinderStammblatt/selectedChild.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Child getSelectedChild() {
		return selectedChild;
	}

	public void setSelectedChild(Child selectedChild) {
		this.selectedChild = selectedChild;
	}

}
