package org.xkidea.bams.web;

import org.xkidea.bams.ejb.AccountantBean;
import org.xkidea.bams.entity.Accountant;
import org.xkidea.bams.web.util.AbstractPaginationHelper;
import org.xkidea.bams.web.util.JsfUtil;
import org.xkidea.bams.web.util.MD5Util;
import org.xkidea.bams.web.util.PageNavigation;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;

@Named(value = "accountantController")
@SessionScoped
public class AccountantController implements Serializable {

    private static final long serialVersionUID = -5956167013948244397L;
    private static final String BUNDLE = "bundles.Bundle";

    private Accountant current;
    private DataModel items = null;

    @EJB
    private AccountantBean ejbFacade;
    private AbstractPaginationHelper pagination;
    private int selectedItemIndex;


    @Inject
    UserController userController;

    public AccountantController() {
    }

    public Accountant getSelected() {
        if (current == null) {
            current = new Accountant();
        }
        return current;
    }

    private AccountantBean getFacade() {
        return ejbFacade;
    }

    public AbstractPaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new AbstractPaginationHelper(AbstractPaginationHelper.DEFAULT_SIZE) {
                @Override
                public int getItemsCount() {
//                    return getFacade().count();
                    return getFacade().getCountByCurrentGeneralAccount(userController.getAuthenticatedUser().getAccountList().get(0));
                }

                @Override
                public DataModel createPageDataModel() {
                    /*return new ListDataModel(getFacade().findRange(new int[]{
                            getPageFirstItem(),
                            getPageFirstItem() + getPageSize()
                    }));*/
                    return new ListDataModel(getFacade().findByCurrentGeneralAccount(new int[]{
                            getPageFirstItem(),
                            getPageFirstItem() + getPageSize()
                    },userController.getAuthenticatedUser().getAccountList().get(0)));
                }
            };
        }
        return pagination;
    }

    public PageNavigation create() {
        try {
            current.setPassword(MD5Util.generateMD5(current.getPassword()));
            current.setDateCreated(new Date());

            current.getAccountList().add(userController.getAuthenticatedUser().getAccountList().get(0));
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("AccountantCreated"));
            return PageNavigation.VIEW;
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public PageNavigation prepareCreate() {
        current = new Accountant();
        selectedItemIndex = -1;
        return PageNavigation.CREATE;
    }

    public PageNavigation prepareList() {
        recreateModel();
        return PageNavigation.LIST;
    }

    public PageNavigation prepareView() {
        current = (Accountant) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem()+getItems().getRowIndex();
        return PageNavigation.VIEW;
    }

    public PageNavigation prepareEdit() {
        current = (Accountant) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem()+getItems().getRowIndex();
        return PageNavigation.EDIT;
    }

    public PageNavigation update() {
        try {
            current.setPassword(MD5Util.generateMD5(current.getPassword()));
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("AccountantUpdated"));
            return PageNavigation.VIEW;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public PageNavigation destroy() {
        current = (Accountant) getItems().getRowData();
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

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("AccountantDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
        }
    }

    public PageNavigation next(){
        pagination.nextPage();
        recreateModel();
        return PageNavigation.LIST;
    }

    public PageNavigation previous() {
        getPagination().previousPage();
        recreateModel();
        return PageNavigation.LIST;
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
            current = getFacade().findRange(new int[]{selectedItemIndex,selectedItemIndex + 1}).get(0);
        }
    }

    public PageNavigation home(){
        return PageNavigation.SYSTEM_MANAGE_HOME;
    }

    private void recreateModel() {
        items = null;
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    public PageNavigation prepareAreasAssign() {
        current = (Accountant) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return PageNavigation.ACCOUNTANT_AREAS_ASSIGN;
    }
}
