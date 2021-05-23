package org.xkidea.bams.web;

import org.xkidea.bams.ejb.SubsidiaryAccountBean;
import org.xkidea.bams.entity.SubsidiaryAccount;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named(value = "subsidiaryAccountController")
@SessionScoped
public class SubsidiaryAccountController implements Serializable {

    private static final long serialVersionUID = 1541234262527479393L;
    private static final String BUNDLE = "bundles.Bundle";

    private SubsidiaryAccount current;

    @EJB
    private SubsidiaryAccountBean ejbFacade;

    public SubsidiaryAccount getSelected() {
        if (current == null) {
            current = new SubsidiaryAccount();
        }
        return current;
    }

    private SubsidiaryAccountBean getFacade() {
        return ejbFacade;
    }
}
