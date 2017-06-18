package sepm.creche.repositories;

import sepm.creche.models.CrecheSettings;
import sepm.creche.models.Person;

/**
 * Repository for managing {@link Person} entities. This repository implements
 * the abstract repository{@link AbstractRepository}.
 *
 * @author Daniel, Fabian
 */

public interface CrecheSettingsRepository extends AbstractRepository<CrecheSettings, Long> {
	
	CrecheSettings findFirstBySettingsId(int id);

}
