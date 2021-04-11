package org.xkidea.bams.web;

import org.xkidea.bams.ejb.GeneralAccountBean;
import org.xkidea.bams.entity.GeneralAccount;
import org.xkidea.bams.web.util.AbstractPaginationHelper;
import org.xkidea.bams.web.util.JsfUtil;
import org.xkidea.bams.web.util.PageNavigation;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;

@Named(value = "generalAccountController")
@SessionScoped
public class GeneralAccountController implements Serializable {
    private static final long serialVersionUID = -7736554891786448740L;
    private static final String BUNDLE = "bundles.Bundle";

    private GeneralAccount current;
    private DataModel items = null;

    @EJB
    private GeneralAccountBean ejbFacade;
    private AbstractPaginationHelper pagination;
    private int selectedItemIndex;

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

    public AbstractPaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new AbstractPaginationHelper(10) {
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

    public PageNavigation prepareList() {
        recreateModel();
        return PageNavigation.LIST;
    }

    public PageNavigation prepareView() {
        current = (GeneralAccount) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return PageNavigation.VIEW;
    }

    private void recreateModel() {
        items = null;
    }

    public PageNavigation prepareCreate() {
        current = new GeneralAccount();
        selectedItemIndex = -1;
        return PageNavigation.CREATE;
    }

    public PageNavigation prepareEdit() {
        current = (GeneralAccount) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return PageNavigation.EDIT;
    }

    public PageNavigation update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("GeneralAccountUpdated"));
            return PageNavigation.VIEW;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public PageNavigation destroy() {
        current = (GeneralAccount) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreateModel();
        return PageNavigation.LIST;
    }

    public PageNavigation destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return PageNavigation.VIEW;
        } else {
            recreateModel();
            return PageNavigation.LIST;
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            selectedItemIndex = count - 1;

            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("GeneralAccountDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
        }
    }

    public PageNavigation previous() {
        getPagination().previousPage();
        recreateModel();
        return PageNavigation.LIST;
    }

    public PageNavigation next(){
        pagination.nextPage();
        recreateModel();
        return PageNavigation.LIST;
    }

    public PageNavigation home(){
        return PageNavigation.ACCOUNT_MANAGE_HOME;
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
