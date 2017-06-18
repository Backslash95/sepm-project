package at.qe.sepm.skeleton.ui.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;


/**
 * Basic request scoped bean to test bean initialization.
 *
 * @author Michael Brunner <Michael.Brunner@uibk.ac.at>
 */
@Component
@Scope("request")
public class SecurityTestBean {
	
    @PreAuthorize("hasAuthority('ADMIN')")
    public void doAdminAction() {
        return;
    }

    @PreAuthorize("hasAuthority('PSLEADER')")
    public void doPSLeaderAction() {
        return;
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    public void doStudentAction() {
        return;
    }

}
