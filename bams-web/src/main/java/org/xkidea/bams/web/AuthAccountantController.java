package org.xkidea.bams.web;

import org.xkidea.bams.ejb.AccountantBean;
import org.xkidea.bams.ejb.AreaBean;
import org.xkidea.bams.entity.Accountant;
import org.xkidea.bams.entity.Area;
import org.xkidea.bams.web.util.JsfUtil;
import org.xkidea.bams.web.util.PageNavigation;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

@Named(value = "authAccountantController")
@SessionScoped
public class AuthAccountantController implements Serializable {
    private static final long serialVersionUID = -8331732837446958567L;
    private static final String BUNDLE = "bundles.Bundle";

    @Inject
    UserController userController;
    @Inject
    AccountantController accountantController;
    @EJB
    AccountantBean ejbAccountantFacade;
    @EJB
    AreaBean ejbAreaFacade;

    private List<Area> areaList;
    private List<Area> oldAreas;

    public AuthAccountantController() {
    }

    @PostConstruct
    public void init() {
        areaList = accountantController.getSelected().getAreaList();
        oldAreas = areaList;
    }

    /**
     * 为选定操作员分配区域
     *
     * @return
     */
    public PageNavigation areasAssign() {
        try {
            Accountant accountant = accountantController.getSelected();
            ejbAccountantFacade.updateAreasFromPerson(accountant.getAreaList(),accountant);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("AccountantUpdated"));
            return PageNavigation.LIST;
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }
}
