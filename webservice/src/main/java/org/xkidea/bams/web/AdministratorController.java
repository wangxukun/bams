package org.xkidea.bams.web;

import org.xkidea.bams.ejb.AdministratorBean;
import org.xkidea.bams.entity.Administrator;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named(value = "administratorController")
@SessionScoped
public class AdministratorController implements Serializable {

    private static final long serialVersionUID = -5358089989415078777L;
    private static final String BUNDLE = "bundles.Bundle";

    private Administrator current;

    @EJB
    private AdministratorBean ejbFacade;

    public AdministratorController() {
    }

    public Administrator getSelected() {
        if (current == null) {
            current = new Administrator();
        }
        return current;
    }

    private AdministratorBean getFacade() {
        return ejbFacade;
    }

    public void create() {
        try {
            System.out.println("---- " + current.getName());
            getFacade().create(current);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
