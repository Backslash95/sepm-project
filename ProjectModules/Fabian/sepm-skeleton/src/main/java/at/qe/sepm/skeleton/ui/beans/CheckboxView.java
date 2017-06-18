package at.qe.sepm.skeleton.ui.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import at.qe.sepm.skeleton.stefan.models.Person;
import at.qe.sepm.skeleton.stefan.repositories.PersonRepository;

@Component
@Scope("session")
public class CheckboxView {

	@Autowired
	private PersonRepository personRepository;

	private List<String> selectedNames;
	private List<String> names;

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