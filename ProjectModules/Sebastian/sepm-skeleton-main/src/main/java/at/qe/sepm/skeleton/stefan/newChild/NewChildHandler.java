package at.qe.sepm.skeleton.stefan.newChild;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.stefan.models.Child;

/**
 * Loads Information for NewUser(Parent) from Page and writes to User Object
 * which is then passed to the Generator-Class.
 * 
 */

@Component
@Scope("session")
public class NewChildHandler {

	@Autowired
	private ChildGenerator childGenerator;
	private Child newChild;

	public NewChildHandler() {

		newChild = new Child();
	}

	// TODO: @PreAuthorize("hasAuthority('EMPLOYEE')") -> remove comment when
	// testing is done
	public void addNewChild() {
		childGenerator.addNewChild(newChild);
		newChild = new Child();

	}

	public Child getNewChild() {
		return newChild;
	}

	public void setNewUser(Child newChild) {
		this.newChild = newChild;
	}

}
