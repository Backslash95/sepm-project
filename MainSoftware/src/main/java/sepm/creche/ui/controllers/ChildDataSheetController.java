package sepm.creche.ui.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sepm.creche.logger.AuditLog;
import sepm.creche.models.Child;
import sepm.creche.models.Person;
import sepm.creche.models.Picture;
import sepm.creche.models.User;
import sepm.creche.models.UserRole;
import sepm.creche.repositories.ChildRepository;
import sepm.creche.repositories.PersonRepository;
import sepm.creche.repositories.PictureRepository;
import sepm.creche.repositories.UserRepository;
import sepm.creche.ui.beans.SessionInfoBean;

/**
 * The controller for the ChildDataSheet.
 * 
 * @author Stefan
 */

@Component
@Scope("session")
public class ChildDataSheetController
{

	// wired beans
	@Autowired
	private ChildRepository childRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PictureRepository pictureRepostiroy;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private AuditLog auditLog;
	@Autowired
	private SessionInfoBean sessionInfo;

	// attributes for the class
	private int selectedChildID;
	private Child selectedChild;
	private Collection<User> parentCollection;
	private Collection<Person> personCollection;
	private Collection<User> checkParentCollection;
	private Collection<Child> checkChildCollection;
	private boolean allChildsInactiveFlag;
	private Picture pictureOfChild;
	private Collection<Person> relativesToDelete;

	/**
	 * This method is the constructor of the class. Several initializations are
	 * made in this method.
	 */
	public ChildDataSheetController()
	{
		selectedChildID = 0;
		selectedChild = new Child();
		parentCollection = new HashSet<User>();
		personCollection = new HashSet<Person>();
		checkParentCollection = new HashSet<User>();
		checkChildCollection = new HashSet<Child>();
		allChildsInactiveFlag = true;
		relativesToDelete = new HashSet<Person>();
	}

	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void deleteRelatives(Collection<Child> children)
	{

		// get all relatives to delete and delete relations between selected
		// children and relatives on children side
		if (children != null)
		{
			for (Child child : children)
			{
				if (child.getMyRelatives() != null)
				{
					relativesToDelete.addAll(child.getMyRelatives());
					child.setMyRelatives(null);
					childRepository.save(child);
				}
			}
		}

		// delete relations on relative side and delete the person
		for (Person person : relativesToDelete)
		{
			person.setKnownChildren(null);
			person = personRepository.save(person);
			personRepository.delete(person);

			// log event
			auditLog.log(sessionInfo.getCurrentUser(), "Acquaintance with name " + person.getName() + " deleted");
		}

	}

	public String loadImagePath()
	{
		pictureOfChild = pictureRepostiroy.findFirstByChildID(selectedChildID);
		if (pictureOfChild != null)
		{
			return pictureOfChild.getPictureReference();
		} else
		{
			return "";
		}
	}

	/**
	 * This method prints a string on the GUI.
	 * 
	 * @param summary
	 *            the string to be printed with
	 * @param detail
	 *            second string to be printed
	 */
	public void addMessage(String summary, String detail)
	{
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		if (FacesContext.getCurrentInstance() != null)
			FacesContext.getCurrentInstance().addMessage(null, message);
	}

	/**
	 * This method deregisters a child, when clicked on the specific button with
	 * confirmation and sets the related parent to inactive, when all children
	 * are inactive.
	 */
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
	public void deregister()
	{
		if (selectedChild.getSignOutDate() == null)
		{
			selectedChild.setDeregistered(true);
			selectedChild.setSignOutDate(new Date());
			selectedChild = childRepository.save(selectedChild);

			// log event
			auditLog.log(sessionInfo.getCurrentUser(),
					"Child with name " + selectedChild.getName() + " is set to inacitve");

			// check if parent has no more active children
			if (selectedChild.getMyParents() != null)
			{
				checkParentCollection.addAll(selectedChild.getMyParents());
				for (User parent : checkParentCollection)
				{
					// don't set admin to inactive
					if (!(parent.getRoles().contains(UserRole.ADMIN)))
					{
						allChildsInactiveFlag = true;
						if (parent.getMyChildren() != null)
						{
							checkChildCollection.addAll(parent.getMyChildren());
							for (Child child : checkChildCollection)
							{
								if (child.isDeregistered() == false)
								{
									allChildsInactiveFlag = false;
								}
							}

							// set the related parents to inactive, if all
							// children are inactive
							if (allChildsInactiveFlag == true)
							{
								parent.setInactive(true);
								Collection<UserRole> roles = parent.getRoles();
								roles.remove(UserRole.PARENT);
								roles.add(UserRole.INACTIVEPARENT);
								parent.setRoles(roles);

								// log event
								auditLog.log(sessionInfo.getCurrentUser(),
										"user with username " + parent.getUsername() + " and name "
												+ parent.getFirstName() + " " + parent.getLastName()
												+ " set to inactive");

								deleteRelatives(checkChildCollection);
								userRepository.save(parent);
							}
						}
					}
				}
			}
			reinit();
			addMessage("", "Sie haben das Kind erfolgreich abgemeledet!");
		} else
		{
			addMessage("", "Das Kind wurde bereits abgemeldet!");
		}
	}

	/**
	 * This method reinitializes the needed attributes of the class, when used
	 * once.
	 */
	public void reinit()
	{
		checkParentCollection = new HashSet<User>();
		checkChildCollection = new HashSet<Child>();
		allChildsInactiveFlag = true;
	}

	/**
	 * This method loads the related parents of the selected child.
	 * 
	 * @return list of all found parents
	 */
	public Collection<User> loadParentList()
	{
		parentCollection = new HashSet<User>();
		if (selectedChild.getMyParents() != null)
			parentCollection.addAll(selectedChild.getMyParents());

		return parentCollection;
	}

	/**
	 * This method loads the related persons (acquaintances) of the selected
	 * child.
	 * 
	 * @return list of all found persons (acquaintances)
	 */
	public Collection<Person> loadPersonList()
	{
		selectedChild = childRepository.findFirstByChildID(selectedChildID);
		personCollection = new HashSet<Person>();
		if (!selectedChild.getMyRelatives().isEmpty())
		{
			for (Person p : selectedChild.getMyRelatives())
			{
				if (p.isActivated())
					personCollection.add(p);
			}
		}

		return personCollection;
	}

	// getter for the childID
	public int getSelectedChildID()
	{
		return selectedChildID;
	}

	/**
	 * This method finds the selected child by the given childID (which is set
	 * when clicked on the button in the list of all related children to the
	 * user). Also a redirect takes place.
	 * 
	 * @param selectedChild
	 *            the ID, which is set and used to find the selcted child
	 */
	public void setSelectedChildID(int selectedChildID)
	{
		this.selectedChildID = selectedChildID;
		selectedChild = childRepository.findFirstByChildID(selectedChildID);
		try
		{
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/secured/restricted/KinderStammblatt/selectedChild.xhtml");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void setSelectedChildIDWithoutRedirect(int selectedChildID)
	{
		this.selectedChildID = selectedChildID;
		selectedChild = childRepository.findFirstByChildID(selectedChildID);
	}

	// getters and setters
	public Child getSelectedChild()
	{
		return selectedChild;
	}

	public void setSelectedChild(Child selectedChild)
	{
		this.selectedChild = selectedChild;
	}

}
