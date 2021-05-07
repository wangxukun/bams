package org.xkidea.bams.web;

import org.xkidea.bams.ejb.TreasurerBean;
import org.xkidea.bams.entity.Treasurer;
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

@Named(value = "treasurerController")
@SessionScoped
public class TreasurerController implements Serializable {

    private static final long serialVersionUID = 3822388479554114531L;
    private static final String BUNDLE = "bundles.Bundle";

    private Treasurer current;
    private DataModel items = null;

    @EJB
    private TreasurerBean ejbFacade;
    private AbstractPaginationHelper pagination;
    private int selectedItemIndex;

    public TreasurerController() {
    }

    public Treasurer getSelected() {
        if (current == null) {
            current = new Treasurer();
        }
        return current;
    }

    private TreasurerBean getFacade() {
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
                    return new ListDataModel(getFacade().findRange(new int[]{
                            getPageFirstItem(),
                            getPageFirstItem() + getPageSize()
                    }));
                }
            };
        }
        return pagination;
    }

    public PageNavigation create() {
        try {
            current.setPassword(MD5Util.generateMD5(current.getPassword()));
            current.setDateCreated(new Date());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("TreasurerCreated"));
            return PageNavigation.VIEW;
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
}
