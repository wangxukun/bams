package org.xkidea.bams.web;

import org.xkidea.bams.ejb.GeneralAccountBean;
import org.xkidea.bams.ejb.TreasurerBean;
import org.xkidea.bams.entity.GeneralAccount;
import org.xkidea.bams.entity.Person;
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

    // 授权的基本账户ID
    private int accountId;

    @EJB
    private TreasurerBean ejbFacade;
    @EJB
    private GeneralAccountBean ejbGAFacade;
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

    public PageNavigation prepareCreate() {
        current = new Treasurer();
        selectedItemIndex = -1;
        return PageNavigation.CREATE;
    }

    public PageNavigation prepareList() {
        recreateModel();
        return PageNavigation.LIST;
    }

    public PageNavigation prepareView() {
        current = (Treasurer) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem()+getItems().getRowIndex();
        return PageNavigation.VIEW;
    }

    public PageNavigation prepareEdit() {
        current = (Treasurer) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem()+getItems().getRowIndex();
        return PageNavigation.EDIT;
    }

    /**
     * 导航到选定用户的基本账户授权页面
     * @return
     */
    public PageNavigation prepareAccountAuthorization() {
        accountId = -1;
        current = (Treasurer) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return PageNavigation.ACCOUNT_AUTHORIZATION;
    }

    /**
     * 为选定操作员授权基本账户
     * @return
     */
    public PageNavigation accountAuthorization() {
        try {
            GeneralAccount generalAccount = ejbGAFacade.find(accountId);
            current.getAccountList().add(generalAccount);
            generalAccount.getPersonList().add(current);
            ejbGAFacade.edit(generalAccount);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("TreasurerAccountAuthorizationUpdated"));
            return PageNavigation.LIST;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public PageNavigation update() {
        try {
            current.setPassword(MD5Util.generateMD5(current.getPassword()));
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("TreasurerUpdated"));
            return PageNavigation.VIEW;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public PageNavigation destroy() {
        current = (Treasurer) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("TreasurerDeleted"));
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

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
