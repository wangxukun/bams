package org.xkidea.bams.web;

import org.xkidea.bams.ejb.AdministratorBean;
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
            pagination = new AbstractPaginationHelper(10) {
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

    public PageNavigation prepareCreate() {
        current = new Administrator();
        selectedItemIndex = -1;
        return PageNavigation.CREATE;
    }

    public PageNavigation create() {
        try {
            current.setPassword(MD5Util.generateMD5(current.getPassword()));
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("AdministratorCreated"));
            return prepareCreate();
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
