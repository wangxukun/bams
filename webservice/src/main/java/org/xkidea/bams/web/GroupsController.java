package org.xkidea.bams.web;

import org.xkidea.bams.ejb.GroupsBean;
import org.xkidea.bams.entity.Groups;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named(value = "groupsController")
@SessionScoped
public class GroupsController implements Serializable {

    private static final long serialVersionUID = 145115176099628659L;

    private Groups current;
    @EJB
    private GroupsBean ejbFacade;

    public GroupsController() {
    }

    public Groups getSelected() {
        if (current == null) {
            current = new Groups();
        }
        return current;
    }

    private GroupsBean getFacade() {
        return ejbFacade;
    }

    public void create() {
        try {
            getFacade().create(current);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
