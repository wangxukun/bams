package org.xkidea.bams.web;

import org.xkidea.bams.ejb.DetailRecordBean;
import org.xkidea.bams.ejb.SubsidiaryAccountBean;
import org.xkidea.bams.entity.Person;
import org.xkidea.bams.model.AccountQuery;
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
import java.util.Calendar;
import java.util.Date;

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
//            currentQuery.setBeginDate(new Date());
            currentQuery.setEndDate(new Date());
            currentQuery.setQueryByEnterDate(false);
        }
        System.out.println("=========(2)====== currentQuery === " + currentQuery);
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
        items = getPaginationByQueryResult().createPageDataModel();
    }

    public PageNavigation prepareList() {
        recreateModel();
        recreateCurrentQuery();
        pagination = null;
        System.out.println("=========(1)====== currentQuery === " + currentQuery);
        return PageNavigation.QUERY_ACCOUNT_LIST;
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

                begin.setTime(beginDate);
                end.setTime(endDate);

                begin.set(Calendar.HOUR_OF_DAY, 0);
                begin.set(Calendar.MINUTE, 0);
                begin.set(Calendar.SECOND, 0);

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
                        return null;
                    }
                    return new ListDataModel(ejbFacade.getByEntryOrOccurDate(
                            new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, userController.getAuthenticatedUser(),
                            begin.getTime(),
                            end.getTime(),
                            currentQuery.isQueryByEnterDate(), currentQuery.getArea(), currentQuery.getSubsidiaryAccount()));
                }
            };
        }
        return pagination;
    }

    public AbstractPaginationHelper getPaginationByQueryResult() {
        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        Date beginDate, endDate;
        try {
            beginDate = currentQuery.getBeginDate();
            endDate = currentQuery.getEndDate();

            begin.setTime(beginDate);
            end.setTime(endDate);

            begin.set(Calendar.HOUR_OF_DAY, 0);
            begin.set(Calendar.MINUTE, 0);
            begin.set(Calendar.SECOND, 0);

            end.set(Calendar.HOUR_OF_DAY, 23);
            end.set(Calendar.MINUTE, 59);
            end.set(Calendar.SECOND, 59);

        } catch (Exception e) {
            e.printStackTrace();
        }


        pagination = new AbstractPaginationHelper(AbstractPaginationHelper.DEFAULT_SIZE) {
            @Override
            public int getItemsCount() {
                int count = ejbFacade.count(userController.getAuthenticatedUser(), begin.getTime(), end.getTime(), currentQuery.isQueryByEnterDate(), currentQuery.getArea(), currentQuery.getSubsidiaryAccount());
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
