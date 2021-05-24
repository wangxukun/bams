package org.xkidea.bams.web;

import org.xkidea.bams.ejb.SubsidiaryAccountBean;
import org.xkidea.bams.entity.SubsidiaryAccount;
import org.xkidea.bams.web.util.AbstractPaginationHelper;
import org.xkidea.bams.web.util.JsfUtil;
import org.xkidea.bams.web.util.PageNavigation;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;

@Named(value = "subsidiaryAccountController")
@SessionScoped
public class SubsidiaryAccountController implements Serializable {

    private static final long serialVersionUID = 1541234262527479393L;
    private static final String BUNDLE = "bundles.Bundle";

    private SubsidiaryAccount current;
    private DataModel items = null;

    @EJB
    private SubsidiaryAccountBean ejbFacade;
    private AbstractPaginationHelper pagination;

    public SubsidiaryAccount getSelected() {
        if (current == null) {
            current = new SubsidiaryAccount();
        }
        return current;
    }

    private SubsidiaryAccountBean getFacade() {
        return ejbFacade;
    }

    public AbstractPaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new AbstractPaginationHelper(AbstractPaginationHelper.DEFAULT_SIZE) {
                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(),getPageFirstItem()+getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    public PageNavigation home(){
        return PageNavigation.ACCOUNT_MANAGE_HOME;
    }

    public PageNavigation create() {
        try {
            current.setDateCreated(new Date());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("SubsidiaryAccountCreated"));
            return PageNavigation.LIST;
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public PageNavigation prepareList() {
        recreateModel();
        return PageNavigation.LIST;
    }

    private void recreateModel() {
        items = null;
    }
}
