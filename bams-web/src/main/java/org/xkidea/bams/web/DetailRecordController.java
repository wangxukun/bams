package org.xkidea.bams.web;

import org.xkidea.bams.ejb.DetailRecordBean;
import org.xkidea.bams.ejb.SubsidiaryAccountBean;
import org.xkidea.bams.entity.DetailRecord;
import org.xkidea.bams.entity.SubsidiaryAccount;
import org.xkidea.bams.model.AccountQuery;
import org.xkidea.bams.web.util.AbstractPaginationHelper;
import org.xkidea.bams.web.util.JsfUtil;
import org.xkidea.bams.web.util.PageNavigation;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

@Named(value = "detailRecordController")
@SessionScoped
public class DetailRecordController implements Serializable {
    private static final long serialVersionUID = -7176059911038677633L;
    private static final String BUNDLE = "bundles.Bundle";

    private AccountQuery currentQuery;
    private DetailRecord currentDetailRecord;

    private String strOccurDate;

    @EJB
    DetailRecordBean ejbFacade;
    @EJB
    SubsidiaryAccountBean ejbSubAccountFacade;
    private AbstractPaginationHelper pagination;
    private int selectedItemIndex;


    public DetailRecordController() {
    }

    public DetailRecordBean getFacade() {
        return this.ejbFacade;
    }

    public String getStrOccurDate() {
        return strOccurDate;
    }

    public void setStrOccurDate(String strOccurDate) {
        this.strOccurDate = strOccurDate;
    }

    public AccountQuery getSelectedQuery() {
        if (currentQuery == null) {
            currentQuery = new AccountQuery();
            currentQuery.setStartDate(JsfUtil.getFirstDayOfThisYear());
            currentQuery.setEndDate(JsfUtil.getToday());
        }
        return currentQuery;
    }

    public DetailRecord getSelectedSubsidiaryAccount() {
        if (currentDetailRecord == null) {
            currentDetailRecord = new DetailRecord();
            setStrOccurDate(JsfUtil.getToday());
        }
        return currentDetailRecord;
    }

    public PageNavigation query() {
        return PageNavigation.CREATE;
    }

    public SelectItem[] getSubsidiaryAccountItemAvailableSelectOne() {
        SelectItem[] result;
        result = ejbSubAccountFacade.getSubsidiaryAccountItemsAvailableSelectOneByArea(currentQuery.getArea());
//        currentQuery.setArea(null);
        return result;
    }

    public PageNavigation create() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
            currentDetailRecord.setOccurDate(sdf.parse(this.getStrOccurDate()));
            currentDetailRecord.setEnterTime(new Date());
            getFacade().create(currentDetailRecord);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("DetailRecordCreated"));
            return PageNavigation.LIST;
        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public PageNavigation home() {
        return PageNavigation.LIST;
    }

    public AbstractPaginationHelper getPagination() {
        if (pagination == null) {
            Calendar begin = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            Date today = new Date();
            begin.setTime(today);
            begin.set(Calendar.HOUR,0);
            begin.set(Calendar.MINUTE,0);
            begin.set(Calendar.SECOND,0);
            end.setTime(today);
            end.set(Calendar.HOUR,23);
            end.set(Calendar.MINUTE,59);
            end.set(Calendar.SECOND,59);

            pagination = new AbstractPaginationHelper(AbstractPaginationHelper.DEFAULT_SIZE) {
                @Override
                public int getItemsCount() {

                    return ejbFacade.count(begin.getTime(),end.getTime());
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(ejbFacade.getByEntryOrOccurDate(begin.getTime(),end.getTime(),true));
                }
            };
        }
        return pagination;
    }
}