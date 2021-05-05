package org.xkidea.bams.web;

import org.xkidea.bams.ejb.TreasurerBean;
import org.xkidea.bams.entity.Treasurer;
import org.xkidea.bams.web.util.AbstractPaginationHelper;

import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.inject.Named;
import java.io.Serializable;

@Named(value = "treasurerController")
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

    // TODO .....................
}
