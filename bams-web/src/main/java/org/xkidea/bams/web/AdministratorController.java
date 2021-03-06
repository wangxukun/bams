package org.xkidea.bams.web;

import org.xkidea.bams.ejb.AdministratorBean;
import org.xkidea.bams.entity.Accountant;
import org.xkidea.bams.entity.Administrator;
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
import java.util.Date;
import java.util.ResourceBundle;

@Named(value = "administratorController")
@SessionScoped
public class AdministratorController implements Serializable {

    private static final long serialVersionUID = -5358089989415078777L;
    private static final String BUNDLE = "bundles.Bundle";

    private Administrator current;
    private DataModel items = null;

    @EJB
    private AdministratorBean ejbFacade;
    private AbstractPaginationHelper pagination;
    private int selectedItemIndex;

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

    /**
     * 获取实例化的分页助手
     * @return 分页助手
     */
    public AbstractPaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new AbstractPaginationHelper(AbstractPaginationHelper.DEFAULT_SIZE) {
                @Override
                public int getItemsCount() {
                    // 返回数据库中Administrator实体的数量
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public PageNavigation prepareList() {
        recreateModel();
        return PageNavigation.LIST;
    }

    public PageNavigation prepareView() {
        current = (Administrator) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return PageNavigation.VIEW;
    }

    private void recreateModel() {
        items = null;
    }

    public PageNavigation prepareCreate() {
        current = new Administrator();
        selectedItemIndex = -1;
        return PageNavigation.CREATE;
    }

    public PageNavigation parentPrepareCreate() {
        current = new Administrator();
        selectedItemIndex = -1;
        return PageNavigation.CREATE_ADMINISTRATOR;
    }

    public PageNavigation prepareEdit() {
        current = (Administrator) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return PageNavigation.EDIT;
    }

    public PageNavigation update() {
        try {
            current.setPassword(MD5Util.generateMD5(current.getPassword()));
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("AdministratorUpdated"));
            return PageNavigation.VIEW;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * admin/administrator/List.xhtml下的删除键使用
     * @return
     */
    public PageNavigation destroy() {
        current = (Administrator) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreateModel();
        return PageNavigation.LIST;
    }

    /**
     * admin/administrator/View.xhtml下的删除键使用
     * @return
     */
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

    /**
     * 当当前条目被删除后，更新下一条为当前条目
     */
    private void updateCurrentItem() {
        int count = getFacade().count(); //　获取条目总数
        if (selectedItemIndex >= count) {
            selectedItemIndex = count - 1;

            // 如果变量--当前页面的第一条目 >= 总条目数
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("AdministratorDeleted"));
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
        return PageNavigation.SYSTEM_MANAGE_HOME;
    }

    public PageNavigation create() {
        try {
            current.setPassword(MD5Util.generateMD5(current.getPassword()));
            current.setDateCreated(new Date());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("AdministratorCreated"));
            return PageNavigation.VIEW;
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }
}
