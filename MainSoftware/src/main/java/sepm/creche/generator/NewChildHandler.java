package sepm.creche.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import sepm.creche.models.Child;

/**
 * Loads Information for NewUser(Parent) from Page and writes to User Object
 * which is then passed to the Generator-Class.
 * 
 * @author Stefan
 */

@Component
@Scope("session")
public class NewChildHandler {

	// wired beans
	@Autowired
	private ChildGenerator childGenerator;

	// attributes for the class
	private Child newChild;

	/**
	 * This method is the constructor of the class. Several initializations are
	 * made in this method.
	 * 
	 */
	public NewChildHandler() {

		newChild = new Child();
	}

	/**
	 * This method passes the new child to the generator class.
	 */
	@PreAuthorize("hasAuthority('EMPLOYEE')")
	public void addNewChild() {
		childGenerator.addNewChild(newChild);
		newChild = new Child();

	}

	// getter and setter for the attributes
	public Child getNewChild() {
		return newChild;
	}

	public void setNewUser(Child newChild) {
		this.newChild = newChild;
	}

}
