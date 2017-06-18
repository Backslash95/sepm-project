package sepm.creche.ui.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.models.Person;
import sepm.creche.repositories.PersonRepository;

/**
 * The bean for the checkbox view. This class is used for getting the chosen
 * persons (acquaintances) of the GUI.
 * 
 * @author Stefan
 */

@Component
@Scope("session")
public class CheckboxView {

	// wired beans
	@Autowired
	private PersonRepository personRepository;

	// attributes for the view
	private List<String> selectedNames;
	private List<String> names;

	/**
	 * This method is the constructor of the class. Several initializations are
	 * made in this method.
	 */
	public CheckboxView() {
		selectedNames = new ArrayList<String>();
		names = new ArrayList<String>();
	}

	/**
	 * This method initializes the view. It fills the list of which the user can
	 * choose from at the GUI. Also the annotation indicates, that the Method is
	 * called automatically after creating the bean.
	 */
	@PostConstruct
	public void init() {
		names = new ArrayList<String>();

		// fill in names from all existing acquaintances
		List<Person> allPersons = personRepository.findAll();
		if (allPersons.size() > 0) {
			for (Person person : allPersons) {
				names.add(person.getName());
			}
		}
	}

	/**
	 * This method reinitializes the view, when used once.
	 */
	public void reinit() {
		selectedNames = new ArrayList<String>();
		init();
	}

	// getters and setters
	public List<String> getNames() {
		return names;
	}

	public List<String> getSelectedNames() {
		return selectedNames;
	}

	public void setSelectedNames(List<String> selectedNames) {
		this.selectedNames = selectedNames;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

}