package at.qe.sepm.skeleton.ui.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import at.qe.sepm.skeleton.stefan.models.Child;
import at.qe.sepm.skeleton.stefan.models.Person;
import at.qe.sepm.skeleton.stefan.models.User;
import at.qe.sepm.skeleton.stefan.repositories.ChildRepository;
import at.qe.sepm.skeleton.stefan.repositories.PersonRepository;
import at.qe.sepm.skeleton.stefan.repositories.UserRepository;
import at.qe.sepm.skeleton.ui.beans.CheckboxView;
import at.qe.sepm.skeleton.ui.beans.PickListViewUser;

@Component
@Scope("session")
public class EditChildDataSheetController {

	@Autowired
	private ChildRepository childRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ChildDataSheetController childDataSheetController;
	@Autowired
	private PickListViewUser pickList;
	@Autowired
	private CheckboxView checkboxView;
	@Autowired
	private PersonRepository personRepository;

	// attributes for Generator
	private List<User> parentList;
	private List<Person> personList;

	private Child selectedChild;
	private Child tempChild;

	public EditChildDataSheetController() {
		parentList = new ArrayList<User>();
		personList = new ArrayList<Person>();
		tempChild = new Child();

	}

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void saveData() {
		selectedChild.setName(tempChild.getName());
		childRepository.save(selectedChild);
	}

	public void load() {
		tempChild.setName(selectedChild.getName());
	}

	public void reinit() {
		parentList = new ArrayList<User>();
		personList = new ArrayList<Person>();
		pickList.reinit();
	}

	public Child getSelectedChild() {
		return selectedChild;
	}

	public void setSelectedChild(Child selectedChild) {
		this.selectedChild = selectedChild;
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/stefan/KinderStammblatt/editChild.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		load();
	}

	public List<User> getParentList() {
		return parentList;
	}

	public void setParentList(List<User> parentList) {
		this.parentList = parentList;
	}

	public List<Person> getPersonList() {
		return personList;
	}

	public void setPersonList(List<Person> personList) {
		this.personList = personList;
	}

	public Child getTempChild() {
		return tempChild;
	}

	public void setTempChild(Child tempChild) {
		this.tempChild = tempChild;
	}

}
