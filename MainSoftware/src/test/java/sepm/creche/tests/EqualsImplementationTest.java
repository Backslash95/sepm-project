package sepm.creche.tests;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import sepm.creche.models.User;
import sepm.creche.models.UserRole;

/**
 * Tests to ensure that each entity's implementation of equals conforms to the
 * contract. See {@linkplain http://www.jqno.nl/equalsverifier/} for more
 * information.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Softwaredevelopment and Project Management" offered by the University
 * of Innsbruck.
 */
public class EqualsImplementationTest
{

	@Test
	public void testUserEqualsContract()
	{
		User user1 = new User();
		user1.setUsername("user1");
		User user2 = new User();
		user2.setUsername("user2");
		EqualsVerifier.forClass(User.class).withPrefabValues(User.class, user1, user2)
				.suppress(Warning.STRICT_INHERITANCE, Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
	}

	@Test
	public void testUserRoleEqualsContract()
	{
		EqualsVerifier.forClass(UserRole.class).verify();
	}

}
