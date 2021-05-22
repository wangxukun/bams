package org.xkidea.bams.web;

import org.xkidea.bams.ejb.SubsidiaryAccountBean;
import org.xkidea.bams.entity.Area;
import org.xkidea.bams.entity.CategoryAccount;
import org.xkidea.bams.entity.SubsidiaryAccount;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value = "subsidiaryAccountController")
@SessionScoped
public class SubsidiaryAccountController implements Serializable {

    private static final long serialVersionUID = 1541234262527479393L;
    private static final String BUNDLE = "bundles.Bundle";

    private SubsidiaryAccount current;
    private int areaId;
    private int categoryAccountId;
    private List<Area> areaList;
    private List<CategoryAccount> categoryAccountList;

    @EJB
    private SubsidiaryAccountBean ejbFacade;

    public SubsidiaryAccount getSelected() {
        if (current == null) {
            current = new SubsidiaryAccount();
        }
        return current;
    }

    private SubsidiaryAccountBean getFacade() {
        return ejbFacade;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getCategoryAccountId() {
        return categoryAccountId;
    }

    public void setCategoryAccountId(int categoryAccountId) {
        this.categoryAccountId = categoryAccountId;
    }

    public List<Area> getAreaList() {
        return ejbFacade.getAreas();
    }

    public List<CategoryAccount> getCategoryAccountList() {
        return ejbFacade.getCategoryAccounts();
    }
}
