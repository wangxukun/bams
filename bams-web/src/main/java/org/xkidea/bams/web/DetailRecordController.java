package org.xkidea.bams.web;

import org.xkidea.bams.ejb.DetailRecordBean;
import org.xkidea.bams.ejb.SubsidiaryAccountBean;
import org.xkidea.bams.entity.DetailRecord;
import org.xkidea.bams.model.AccountQuery;
import org.xkidea.bams.web.util.AbstractPaginationHelper;
import org.xkidea.bams.web.util.JsfUtil;
import org.xkidea.bams.web.util.PageNavigation;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

@Named(value = "detailRecordController")
@SessionScoped
public class DetailRecordController implements Serializable {
    private static final long serialVersionUID = -7176059911038677633L;
    private static final String BUNDLE = "bundles.Bundle";

    private AccountQuery currentQuery;
    private DetailRecord currentDetailRecord;
    private DataModel items = null;

    private String strOccurDate;

    @EJB
    DetailRecordBean ejbFacade;
    @EJB
    SubsidiaryAccountBean ejbSubAccountFacade;
    private AbstractPaginationHelper pagination;
    @Inject
    private UserController userController;
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

    public DetailRecord getSelectedDetailRecord() {
        if (currentDetailRecord == null) {
            currentDetailRecord = new DetailRecord();
            setStrOccurDate(JsfUtil.getToday());
        }
        return currentDetailRecord;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreateCurrentDetailRecord() {
        currentDetailRecord = null;
    }

    private void recreateCurrentQuery() {
        currentQuery = null;
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    public void query() {
        System.out.println("---q---queryEnterDate-----" + currentQuery.isQueryByEnterDate());
        System.out.println("---q---startDate-----" + currentQuery.getStartDate());
        System.out.println("---q---endDate-----" + currentQuery.getEndDate());
        System.out.println("---q---area-----" + currentQuery.getArea());
        System.out.println("---q---subsidiaryAccount-----" + currentQuery.getSubsidiaryAccount());
        items = getPaginationByQueryResult().createPageDataModel();
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
//            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("DetailRecordCreated"));
            recreateCurrentQuery();
            recreateModel();
            return PageNavigation.LIST;
        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public PageNavigation next() {
        pagination.nextPage();
        recreateModel();
        recreateCurrentQuery();
        recreateCurrentDetailRecord();
        return PageNavigation.LIST;
    }

    public PageNavigation previous() {
        pagination.previousPage();
        recreateModel();
        recreateCurrentQuery();
        recreateCurrentDetailRecord();
        return PageNavigation.LIST;
    }

    public PageNavigation home() {
        return PageNavigation.LIST;
    }

    public PageNavigation prepareEdit() {
        currentDetailRecord = (DetailRecord) getItems().getRowData();
        currentQuery.setArea(currentDetailRecord.getSubsidiaryAccount().getArea());
        currentQuery.setSubsidiaryAccount(currentDetailRecord.getSubsidiaryAccount());
        DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
        strOccurDate = df.format(currentDetailRecord.getOccurDate());

        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return PageNavigation.EDIT;
    }

    public PageNavigation update() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
            currentDetailRecord.setOccurDate(sdf.parse(this.getStrOccurDate()));
            currentDetailRecord.setEnterTime(new Date());
            getFacade().edit(currentDetailRecord);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle(BUNDLE).getString("DetailRecordUpdated"));
            recreateCurrentQuery();
            recreateModel();
            return PageNavigation.LIST;
        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle(BUNDLE).getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public PageNavigation prepareList() {
        recreateModel();
        recreateCurrentQuery();
        recreateCurrentDetailRecord();
        return PageNavigation.LIST;
    }

    public PageNavigation destroy() {
        recreateModel();
        return PageNavigation.LIST;
    }

    public PageNavigation prepareCreate() {
        recreateCurrentQuery();
        recreateCurrentDetailRecord();
        return PageNavigation.CREATE;
    }

    public AbstractPaginationHelper getPagination() {
        if (pagination == null) {
            Calendar begin = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            Date today = new Date();
            begin.setTime(today);
            begin.set(Calendar.HOUR_OF_DAY, 0);
            begin.set(Calendar.MINUTE, 0);
            begin.set(Calendar.SECOND, 0);
            end.setTime(today);
            end.set(Calendar.HOUR_OF_DAY, 23);
            end.set(Calendar.MINUTE, 59);
            end.set(Calendar.SECOND, 59);

            pagination = new AbstractPaginationHelper(AbstractPaginationHelper.DEFAULT_SIZE) {
                @Override
                public int getItemsCount() {
                    int count = ejbFacade.count(userController.getAuthenticatedUser(), begin.getTime(), end.getTime(), true);
                    return count;
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(ejbFacade.getByEntryOrOccurDate(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, userController.getAuthenticatedUser(), begin.getTime(), end.getTime(), true));
                }
            };
        }
        return pagination;
    }

    public AbstractPaginationHelper getPaginationByQueryResult() {
        // TODO QueryDetailRecord...
        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        Date beginDate, endDate;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
        try {
            beginDate = sdf.parse(currentQuery.getStartDate());
            endDate = sdf.parse(currentQuery.getEndDate());

            begin.setTime(beginDate);
            end.setTime(endDate);

            begin.set(Calendar.HOUR_OF_DAY, 0);
            begin.set(Calendar.MINUTE, 0);
            begin.set(Calendar.SECOND, 0);

            end.set(Calendar.HOUR_OF_DAY, 23);
            end.set(Calendar.MINUTE, 59);
            end.set(Calendar.SECOND, 59);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        pagination = new AbstractPaginationHelper(AbstractPaginationHelper.DEFAULT_SIZE) {
            @Override
            public int getItemsCount() {
                int count = ejbFacade.count(userController.getAuthenticatedUser(), begin.getTime(), end.getTime(), true, currentQuery.getArea(), currentQuery.getSubsidiaryAccount());
                System.out.println("----------count------" + count);
                return count;
            }

            @Override
            public DataModel createPageDataModel() {
                return new ListDataModel(ejbFacade.getByEntryOrOccurDate(
                        new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, userController.getAuthenticatedUser(),
                        begin.getTime(),
                        end.getTime(),
                        currentQuery.isQueryByEnterDate(), currentQuery.getArea(), currentQuery.getSubsidiaryAccount()));
            }
        };
        return pagination;
    }
}