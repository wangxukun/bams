package org.xkidea.bams.web;

import org.xkidea.bams.ejb.DetailRecordBean;
import org.xkidea.bams.ejb.SubsidiaryAccountBean;
import org.xkidea.bams.entity.DetailRecord;
import org.xkidea.bams.entity.Person;
import org.xkidea.bams.model.AccountQuery;
import org.xkidea.bams.model.FirstBalance;
import org.xkidea.bams.web.util.AbstractPaginationHelper;
import org.xkidea.bams.web.util.PageNavigation;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Named(value = "accountQueryController")
@SessionScoped
public class AccountQueryController implements Serializable {
    private static final long serialVersionUID = 1506050027387898478L;

    private AccountQuery currentQuery;
    private DataModel items = null;
    private AbstractPaginationHelper pagination;

    @EJB
    SubsidiaryAccountBean ejbSubAccountFacade;
    @EJB
    DetailRecordBean ejbFacade;

    @Inject
    UserController userController;

    // 保存每次查询的期初余额对象
    FirstBalance beginningBalance;
    // 保存每次查询的发生额
    List<DetailRecord> detailRecordList;
    // 保存每次查询的所有记录（期初余额、发生额、本月合计、累计）
    List<DetailRecord> pageData;


    public AccountQueryController() {
    }

    public DetailRecordBean getFacade() {
        return this.ejbFacade;
    }

    public AccountQuery getSelectedQuery() {
        if (currentQuery == null) {
            currentQuery = new AccountQuery() {
                @Override
                protected Person getCurrentUser() {
                    return userController.getAuthenticatedUser();
                }
            };
            currentQuery.setEndDate(new Date());
            currentQuery.setQueryByEnterDate(false);
        }
        return currentQuery;
    }

    private void recreateModel() {
        items = null;
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

    public SelectItem[] getSubsidiaryAccountItemAvailableSelectOne() {
        SelectItem[] result;
        result = ejbSubAccountFacade.getSubsidiaryAccountItemsAvailableSelectOneByArea(currentQuery.getArea());
        return result;
    }

    public void query() {
        pagination = null;
        beginningBalance = null;
        detailRecordList = null;
        pageData = null;
        items = getPagination().createPageDataModel();
    }

    public PageNavigation prepareList() {
        recreateModel();
        recreateCurrentQuery();
        pagination = null;
        pageData = null;
        return PageNavigation.QUERY_ACCOUNT_LIST;
    }

    public PageNavigation first() {
        pagination.firstPage();
        recreateModel();
        return PageNavigation.LIST;
    }

    public PageNavigation last() {
        pagination.lastPage();
        recreateModel();
        return PageNavigation.LIST;
    }

    public PageNavigation previous() {
        pagination.previousPage();
        recreateModel();
        return PageNavigation.LIST;
    }

    public PageNavigation next() {
        pagination.nextPage();
        recreateModel();
        return PageNavigation.LIST;
    }

    public AbstractPaginationHelper getPagination() {
        if (pagination == null) {
            Calendar begin = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            Date beginDate, endDate;
            try {
                beginDate = currentQuery.getBeginDate();
                endDate = currentQuery.getEndDate();

                if (beginDate != null) {
                    begin.setTime(beginDate);
                    begin.set(Calendar.HOUR_OF_DAY, 0);
                    begin.set(Calendar.MINUTE, 0);
                    begin.set(Calendar.SECOND, 0);
                }

                end.setTime(endDate);
                end.set(Calendar.HOUR_OF_DAY, 23);
                end.set(Calendar.MINUTE, 59);
                end.set(Calendar.SECOND, 59);

            } catch (Exception e) {
                e.printStackTrace();
            }
            pagination = new AbstractPaginationHelper(AbstractPaginationHelper.DEFAULT_SIZE) {
                @Override
                public int getItemsCount() {
                    if (currentQuery.getBeginDate() == null || currentQuery.getEndDate() == null) {
                        return 0;
                    }
                    int count = ejbFacade.count(userController.getAuthenticatedUser(), begin.getTime(), end.getTime(), currentQuery.isQueryByEnterDate(), currentQuery.getArea(), currentQuery.getSubsidiaryAccount());
                    return count;
                }

                @Override
                public DataModel createPageDataModel() {
                    if (currentQuery.getBeginDate() == null || currentQuery.getEndDate() == null) {
                        pagination = null;
                        return null;
                    }
                    if (pageData == null) {
                        beginningBalance = ejbFacade.getBeginningBalance(
                                userController.getAuthenticatedUser(),
                                begin.getTime(),
                                currentQuery.getArea(),
                                currentQuery.getSubsidiaryAccount());
                        detailRecordList = ejbFacade.getByEntryOrOccurDate(
                                userController.getAuthenticatedUser(),
                                begin.getTime(),
                                end.getTime(),
                                currentQuery.isQueryByEnterDate(), currentQuery.getArea(), currentQuery.getSubsidiaryAccount());
                        pageData = ejbFacade.setBalanceForDetailRecord(beginningBalance, detailRecordList);
                    }
                    int lastItem = (getPageFirstItem() + getPageSize()) < (pageData.size()) ? (getPageFirstItem() + getPageSize()) : (pageData.size());
                    return new ListDataModel(pageData.subList(getPageFirstItem(), lastItem));
                }
            };
        }
        return pagination;
    }

/*    public AbstractPaginationHelper getPagination2(){
        {
            if (pagination == null) {
                Calendar begin = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                Date beginDate, endDate;
                try {
                    beginDate = currentQuery.getBeginDate();
                    endDate = currentQuery.getEndDate();

                    if (beginDate != null) {
                        begin.setTime(beginDate);
                        begin.set(Calendar.HOUR_OF_DAY, 0);
                        begin.set(Calendar.MINUTE, 0);
                        begin.set(Calendar.SECOND, 0);
                    }

                    end.setTime(endDate);
                    end.set(Calendar.HOUR_OF_DAY, 23);
                    end.set(Calendar.MINUTE, 59);
                    end.set(Calendar.SECOND, 59);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                pagination = new AbstractPaginationHelper(AbstractPaginationHelper.DEFAULT_SIZE) {
                    @Override
                    public int getItemsCount() {
                        if (currentQuery.getBeginDate() == null || currentQuery.getEndDate() == null) {
                            return 0;
                        }
                        int count = ejbFacade.count(userController.getAuthenticatedUser(), begin.getTime(), end.getTime(), currentQuery.isQueryByEnterDate(), currentQuery.getArea(), currentQuery.getSubsidiaryAccount());
                        return count;
                    }

                    @Override
                    public DataModel createPageDataModel() {
                        if (currentQuery.getBeginDate() == null || currentQuery.getEndDate() == null) {
                            pagination = null;
                            return null;
                        }
                        FirstBalance beginningBalance = ejbFacade.getBeginningBalance(
                                userController.getAuthenticatedUser(),
                                begin.getTime(),
                                currentQuery.getArea(),
                                currentQuery.getSubsidiaryAccount());
                        List<DetailRecord> detailRecordList = ejbFacade.getByEntryOrOccurDate(
                                new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, userController.getAuthenticatedUser(),
                                begin.getTime(),
                                end.getTime(),
                                currentQuery.isQueryByEnterDate(), currentQuery.getArea(), currentQuery.getSubsidiaryAccount());
                        List<DetailRecord> pageData = ejbFacade.setBalanceForDetailRecord(beginningBalance,detailRecordList);
                        return new ListDataModel(pageData);
                    }
                };
            }
            return pagination;
        }
    }*/
}
