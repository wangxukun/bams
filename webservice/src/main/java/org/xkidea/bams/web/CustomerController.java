package org.xkidea.bams.web;

import org.xkidea.bams.ejb.UserBean;
import org.xkidea.bams.entity.Customer;
import org.xkidea.bams.entity.Person;
import org.xkidea.bams.web.util.AbstractPaginationHelper;
import org.xkidea.bams.web.util.JsfUtil;
import org.xkidea.bams.web.util.MD5Util;
import org.xkidea.bams.web.util.PageNavigation;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Named;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.Date;
import java.util.ResourceBundle;

@Named(value = "customerController")
@SessionScoped
public class CustomerController implements Serializable {

    private static final long serialVersionUID = 3072888272035148071L;
    private static String BUNDLE = "bundles.Bundle";

    // TODO @Inject @LoggedIn

    private Customer current;
    private DataModel items = null;

    @EJB
    UserBean ejbFacade;

    private AbstractPaginationHelper pagination;
    private int selectedItemIndex;

    public CustomerController() {
    }

    public Customer getSelected() {
        if (current == null) {
            current = new Customer();
            selectedItemIndex = -1;
        }
        return current;
    }

    public PageNavigation previous() {
        getPagination().previousPage();
        recreateModel();
        return PageNavigation.LIST;
    }

    public PageNavigation next() {
        getPagination().nextPage();
        recreateModel();
        return PageNavigation.LIST;
    }

    public UserBean getFacade() {
        return ejbFacade;
    }

    public AbstractPaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new AbstractPaginationHelper(AbstractPaginationHelper.DEFAULT_SIZE) {
                @Override
                public int getItemsCount() {
                    return ejbFacade.count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(),getPageFirstItem() + getPageSize()}));
                    //return new ListDataModel(getFacade().findAll());
                }
            };
        }
        return pagination;
    }

    private boolean isUserDuplicated(Person person) {
        return (getFacade().getUserByEmail(person.getEmail()) == null) ? false : true;
    }

    public PageNavigation home() {
        return PageNavigation.SYSTEM_MANAGE_HOME;
    }

    public PageNavigation create(){
        try {
            if (!isUserDuplicated(current)) {
                current.setPassword(MD5Util.generateMD5(current.getPassword()));
                current.setDateCreated(new Date());
                getFacade().createUser(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("CustomerCreated"));
            } else {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("DuplicatedCustomerError"));
            }
            return PageNavigation.VIEW;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e,ResourceBundle.getBundle(BUNDLE).getString("CustomerCreationError"));
            return null;
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    public PageNavigation prepareView() {
        current = (Customer) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return PageNavigation.VIEW;
    }

    public PageNavigation prepareList() {
        recreateModel();
        return PageNavigation.LIST;
    }

    public PageNavigation prepareCreate() {
        current = new Customer();
        selectedItemIndex = -1;
        return PageNavigation.CREATE;
    }

    public PageNavigation prepareEdit() {
        current = (Customer) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return PageNavigation.EDIT;
    }

    public PageNavigation update() {
        try {
            current.setPassword(MD5Util.generateMD5(current.getPassword()));
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("CustomerUpdated"));
            return PageNavigation.VIEW;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e,ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public PageNavigation destroy() {
        current = (Customer) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreateModel();
        return PageNavigation.LIST;
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("CustomerDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e,ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
        }
    }
}
