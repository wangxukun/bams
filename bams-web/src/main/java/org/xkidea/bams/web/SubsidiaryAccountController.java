package org.xkidea.bams.web;

import org.xkidea.bams.ejb.SubsidiaryAccountBean;
import org.xkidea.bams.entity.SubsidiaryAccount;
import org.xkidea.bams.web.util.AbstractPaginationHelper;
import org.xkidea.bams.web.util.JsfUtil;
import org.xkidea.bams.web.util.PageNavigation;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
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
    private int selectedItemIndex;

    @Inject
    UserController userController;

    @PostConstruct
    public void init() {
//        current = new SubsidiaryAccount();
//        current.setArea(userController.getAuthenticatedUser().getAreaList().get(1));
    }

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
//                    return getFacade().count();
                    return getFacade().countByGeneralAccount(userController.getAuthenticatedUser().getAccountList().get(0));
                }

                @Override
                public DataModel createPageDataModel() {
//                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(),getPageFirstItem()+getPageSize()}));
                    return new ListDataModel(getFacade().findByGeneralAccountAreas(new int[]{getPageFirstItem(),getPageFirstItem()+getPageSize()},userController.getAuthenticatedUser().getAccountList().get(0)));
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
            recreateModel();
            return prepareList();
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public PageNavigation update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("SubsidiaryAccountUpdated"));
            return PageNavigation.VIEW;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public PageNavigation destroy() {
        current = (SubsidiaryAccount) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreateModel();
        return PageNavigation.LIST;
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("SubsidiaryAccountDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
        }
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

    public PageNavigation prepareList() {
        recreateModel();
        return PageNavigation.LIST;
    }

    public PageNavigation prepareCreate() {
        current = new SubsidiaryAccount();
        selectedItemIndex = -1;
        return PageNavigation.CREATE;
    }

    public PageNavigation prepareView() {
        current = (SubsidiaryAccount) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return PageNavigation.VIEW;
    }

    public PageNavigation prepareEdit() {
        current = (SubsidiaryAccount) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return PageNavigation.EDIT;
    }

    private void recreateModel() {
        items = null;
    }

    public PageNavigation first() {
        pagination.firstPage();
        recreateModel();
        return PageNavigation.LIST;
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

    public PageNavigation last() {
        pagination.lastPage();
        recreateModel();
        return  PageNavigation.LIST;
    }

    @FacesConverter(forClass = SubsidiaryAccount.class)
    public static class SubsidiaryAccountConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SubsidiaryAccountController controller = (SubsidiaryAccountController) context.getApplication().getELResolver().getValue(context.getELContext(),null,"subsidiaryAccountController");
            System.out.println("----------------------------(4444)-------" + controller.ejbFacade.find(getKey(value)));
            return controller.ejbFacade.find(getKey(value));
        }

        private Integer getKey(String value) {
            Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Object value) {
            if (value == null) {
                return null;
            }
            if (value instanceof SubsidiaryAccount) {
                SubsidiaryAccount o = (SubsidiaryAccount)value;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("value " + value + " is of type " + value.getClass().getName() + "; expected type: " + SubsidiaryAccountConverter.class.getName());
            }
        }

        private String getStringKey(Integer id) {
            StringBuilder sb = new StringBuilder();
            sb.append(id);
            return sb.toString();
        }
    }

}
