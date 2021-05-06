package org.xkidea.bams.web;

import org.xkidea.bams.ejb.TreasurerBean;
import org.xkidea.bams.entity.Treasurer;
import org.xkidea.bams.web.util.AbstractPaginationHelper;

import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
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
}
