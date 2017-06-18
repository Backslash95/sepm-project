package at.qe.sepm.skeleton.ui.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * Basic request scoped bean to test bean initialization.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Softwaredevelopment and Project Management" offered by the University
 * of Innsbruck.
 */
@Component
@Scope("request")
public class SecurityTestBean {

    @PreAuthorize("hasAuthority('ADMIN')")
    public void doAdminAction() {
        return;
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    public void doManagerAction() {
        return;
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public void doEmployeeAction() {
        return;
    }

}
