package sepm.creche.ui.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sepm.creche.models.Child;
import sepm.creche.repositories.ChildRepository;

/**
 * This Class provides a checkbox view to select children
 * 
 * @author Sebastian Grabher
 */
@Component
@Scope("view")
public class ChildCheckboxView
{

	@Autowired
	private ChildRepository childRepository;

	private List<String> selectedNames;
	private List<String> names;

	private boolean buttonEnabled;

	@PostConstruct
	public void init()
	{
		names = new ArrayList<String>();

		// fill in names from all existing children
		List<Child> allChildren = childRepository.findAll();
		if (allChildren.size() > 0)
		{
			for (Child child : allChildren)
			{
				names.add(child.getName());
			}
		}
	}

	public List<String> getNames()
	{

		return names;
	}

	public List<String> getSelectedNames()
	{

		return selectedNames;
	}

	public void setSelectedNames(List<String> selectedNames)
	{
		this.selectedNames = selectedNames;
	}

	public void setNames(List<String> names)
	{
		this.names = names;
	}

	public boolean isButtonEnabled()
	{
		return buttonEnabled;
	}

	public void setButtonEnabled(boolean buttonEnabled)
	{
		this.buttonEnabled = buttonEnabled;
	}

}