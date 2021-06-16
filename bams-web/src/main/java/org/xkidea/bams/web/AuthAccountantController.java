package org.xkidea.bams.web;

import org.xkidea.bams.ejb.PersonBean;
import org.xkidea.bams.entity.Accountant;
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
    @EJB
    PersonBean personBean;

    private List<Area> oldAreas;
    private Person person;

    public AuthAccountantController() {
        oldAreas = new ArrayList<>();
    }


    public Person getSelectedPerson() {
        person = accountantController.getSelected();
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

}
