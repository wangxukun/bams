package org.xkidea.bams.web;

import org.xkidea.bams.ejb.GeneralAccountBean;
import org.xkidea.bams.entity.GeneralAccount;
import org.xkidea.bams.web.util.JsfUtil;
import org.xkidea.bams.web.util.PageNavigation;
import sun.reflect.annotation.ExceptionProxy;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;

@Named(value = "generalAccountController")
@RequestScoped
public class GeneralAccountController implements Serializable {
    private static final long serialVersionUID = -7736554891786448740L;

    private static final String BUNDLE = "bundles.Bundle";

    private GeneralAccount current;

    @EJB
    private GeneralAccountBean ejbFacade;

    public GeneralAccountController() {
    }

    public GeneralAccount getSelected() {
        if (current == null) {
            current = new GeneralAccount();
        }
        return current;
    }

    private GeneralAccountBean getFacade() {
        return ejbFacade;
    }

    public PageNavigation create() {
        try {
            current.setDateCreated(new Date());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("GeneralAccountCreated"));
            return PageNavigation.VIEW;
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }
}
