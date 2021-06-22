package org.xkidea.bams.web;

import org.xkidea.bams.ejb.AreaBean;
import org.xkidea.bams.ejb.PersonBean;
import org.xkidea.bams.entity.Area;
import org.xkidea.bams.entity.Person;
import org.xkidea.bams.web.util.JsfUtil;
import org.xkidea.bams.web.util.PageNavigation;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Named(value = "authAccountantController")
@SessionScoped
public class AuthAccountantController implements Serializable {
    private static final long serialVersionUID = -8331732837446958567L;
    private static final String BUNDLE = "bundles.Bundle";

    @Inject
    AccountantController accountantController;
    @Inject
    CustomerController customerController;
    @Inject
    TreasurerController treasurerController;
    @EJB
    PersonBean personBean;
    @EJB
    AreaBean areaBean;

    private List<Area> oldAreas;
    private Person person;

    public AuthAccountantController() {
        oldAreas = new ArrayList<>();
    }

    public Person getSelectedTreasurer() {
        person = treasurerController.getSelected();
        oldAreas.clear();
        if (person.getAreaList() != null && person.getAreaList().size() > 0) {
            person.getAreaList().stream().forEach(area -> {
                oldAreas.add(area);
            });
        }
        return person;
    }


    public Person getSelectedAccountant() {
        person = accountantController.getSelected();
        oldAreas.clear();
        if (person.getAreaList() != null && person.getAreaList().size() > 0) {
            person.getAreaList().stream().forEach(area -> {
                oldAreas.add(area);
            });
        }
        return person;
    }

    public Person getSelectedCustomer() {
        person = customerController.getSelected();
        oldAreas.clear();
        if (person.getAreaList() != null && person.getAreaList().size() > 0) {
            person.getAreaList().stream().forEach(area -> {
                oldAreas.add(area);
            });
        }
        return person;
    }

    /**
     * 为选定操作员分配区域
     *
     * @return
     * select * from PERSON;
     * select * from AREA;
     * select * from PERSON_AREA;
     * select p.ID,p.NAME,a.ID,a.NAME from PERSON as p,AREA as a,PERSON_AREA as pa where p.ID=pa.PERSON_ID and a.ID=pa.AREA_ID;
     */
    public PageNavigation areasAssign() {
        try {
            personBean.updateAreasOfPerson(person, person.getAreaList(),oldAreas);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("AssignAreas"));
            return PageNavigation.LIST;
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public PageNavigation areasAssignOfTreasurer() {
        // TODO 为出纳员分配区域，区域所属的总账也要相应的改变为出纳员所属的总账。
        //  对区域拥有权限的所有Persons的所属总账，也要以分配区域的出纳员保持一致。
        try {
            personBean.updateAreasOfPerson(person, person.getAreaList(),oldAreas);
            //　设置出纳员中每个新增的区域的GENERALACCOUNT_ID与出纳员所属ACCOUNT_ID一致
            for (Area a : person.getAreaList()) {
                Area temp = areaBean.find(a.getId());
                temp.setGeneralAccount(person.getAccountList().get(0));
                for (Person p : temp.getPersonList()) {
                    if (!"TREASURER".equals(p.getGroupsList().get(0).getName())){
                        p.setAccountList(person.getAccountList());
                    }
                }
                areaBean.edit(temp);
            }
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("AssignAreas"));
            return PageNavigation.LIST;
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

}
