package org.xkidea.bams.web;

import org.xkidea.bams.ejb.SortAccountBean;
import org.xkidea.bams.entity.SortAccount;
import org.xkidea.bams.web.util.AbstractPaginationHelper;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.inject.Named;
import java.io.Serializable;

@Named(value = "sortAccountController")
@SessionScoped
public class SortAccountController implements Serializable {
    private static final long serialVersionUID = -2701722893952713776L;
    private static final String BUNDLE = "bundles.Bundle";

    private SortAccount current;
    private DataModel items = null;

    @EJB
    private SortAccountBean ejbFacade;
    private AbstractPaginationHelper pagination;
    private int selectedItemIndex;

    public SortAccountController() {
    }

    public SortAccount getSelected() {
        if (current == null) {
            current = new SortAccount();
            selectedItemIndex = -1;
        }
        return current;
    }

    private SortAccountBean getFacade() {
        return ejbFacade;
    }
}
