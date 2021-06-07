package org.xkidea.bams.web;

import org.xkidea.bams.ejb.AreaBean;
import org.xkidea.bams.entity.Area;
import org.xkidea.bams.web.util.AbstractPaginationHelper;
import org.xkidea.bams.web.util.JsfUtil;
import org.xkidea.bams.web.util.PageNavigation;

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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Named(value = "areaController")
@SessionScoped
public class AreaController implements Serializable {
    private static final String BUNDLE = "bundles.Bundle";
    private static final long serialVersionUID = -693340121690214777L;

    private Area current;
    private DataModel items = null;

    // 分配的区域集合
    List<Area> areaList;

    @EJB
    private AreaBean ejbFacade;
    private AbstractPaginationHelper pagination;
    private int selectedItemIndex;

    @Inject
    UserController userController;
    @Inject
    AccountantController accountantController;

    public AreaController() {
        if (areaList == null) {
            areaList = new ArrayList<>();
        }
    }

    public Area getSelected() {
        if (current == null) {
            current = new Area();
            selectedItemIndex = -1;
        }
        return current;
    }

    private AreaBean getFacade() {
        return ejbFacade;
    }

    public PageNavigation create() {
        try {
//            current.getPersonList().add(userController.getAuthenticatedUser());
//            current.setGeneralAccount(userController.getAuthenticatedUser().getAccountList().get(0));
            getFacade().create(current, userController.getAuthenticatedUser());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("AreaCreated"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public PageNavigation prepareList() {
        recreateModel();
        return PageNavigation.LIST;
    }

    public PageNavigation home() {
        return PageNavigation.ACCOUNT_MANAGE_HOME;
    }

    private void recreateModel() {
        items = null;
    }

    public AbstractPaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new AbstractPaginationHelper(AbstractPaginationHelper.DEFAULT_SIZE) {
                @Override
                public int getItemsCount() {
//                    return getFacade().count();
                    return getFacade().getCountByCurrentUser(userController.getAuthenticatedUser());
                }

                @Override
                public DataModel createPageDataModel() {
                    /*return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(),
                    getPageFirstItem() + getPageSize()}));*/
                    return new ListDataModel(getFacade().findByCurrentUser(new int[]{getPageFirstItem(),
                            getPageFirstItem() + getPageSize()},userController.getAuthenticatedUser()));
                }
            };
        }
        return pagination;
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
            areaList = getFacade().findByCurrentUser(new int[]{
                    getPagination().getPageFirstItem(),
                    getPagination().getPageFirstItem()+getPagination().getPageSize()},
                    accountantController.getSelected());
        }
        return items;
    }

    public PageNavigation next() {
        getPagination().nextPage();
        recreateModel();
        return PageNavigation.LIST;
    }

    public PageNavigation previous() {
        getPagination().previousPage();
        recreateModel();
        return PageNavigation.LIST;
    }

    public PageNavigation prepareCreate() {
        current = new Area();
        selectedItemIndex = -1;
        return PageNavigation.CREATE;
    }

    public PageNavigation prepareView() {
        current = (Area) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return PageNavigation.VIEW;
    }

    public PageNavigation prepareEdit() {
        current = (Area) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return PageNavigation.EDIT;
    }

    public PageNavigation destroy() {
        current = (Area) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreateModel();
        return PageNavigation.LIST;
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("AreaDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
        }
    }

    public PageNavigation update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("AreaUpdated"));
            return PageNavigation.VIEW;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
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
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public SelectItem[] getItemsAvailableSelectOne() {
//        return JsfUtil.getSelectItems(ejbFacade.findAll(),true);
        return JsfUtil.getSelectItems(ejbFacade.getAreasByCurrentUser(userController.getAuthenticatedUser()),true);
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.getAreasByCurrentUser(userController.getAuthenticatedUser()),false);
    }

    @FacesConverter(forClass = Area.class)
    public static class AreaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AreaController controller = (AreaController) context.getApplication().getELResolver().getValue(context.getELContext(),null,"areaController");
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
            if (value instanceof Area) {
                Area o = (Area)value;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("value " + value + " is of type " + value.getClass().getName() + "; expected type: " + AreaController.class.getName());
            }
        }

        private String getStringKey(Integer id) {
            StringBuilder sb = new StringBuilder();
            sb.append(id);
            return sb.toString();
        }
    }

    /**
     * 为选定操作员分配区域
     * @return
     */
    public PageNavigation areasAssign() {
        System.out.println("-------------" + areaList.size());
        return PageNavigation.LIST;
    }

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }
}
