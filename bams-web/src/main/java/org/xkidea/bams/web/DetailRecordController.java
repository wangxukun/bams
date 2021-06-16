package org.xkidea.bams.web;

import org.xkidea.bams.ejb.DetailRecordBean;
import org.xkidea.bams.ejb.SubsidiaryAccountBean;
import org.xkidea.bams.entity.DetailRecord;
import org.xkidea.bams.entity.SubsidiaryAccount;
import org.xkidea.bams.model.AccountQuery;
import org.xkidea.bams.web.util.JsfUtil;
import org.xkidea.bams.web.util.PageNavigation;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.io.Serializable;

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

    public DetailRecordController() {
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

    public SelectItem[] getSubsidiaryAccountItemAvailableSelectOne(){
        SelectItem [] result;
        result = ejbSubAccountFacade.getSubsidiaryAccountItemsAvailableSelectOneByArea(currentQuery.getArea());
        currentQuery.setArea(null);
        return result;
    }

    public PageNavigation create() {
        // TODO .....
        System.out.println("-----ID-------(1)---------" + currentDetailRecord.getId());
        System.out.println("-----SUMMARY-------(2)---------" + currentDetailRecord.getSummary());
        System.out.println("-----AMOUNT-------(3)---------" + currentDetailRecord.getAmount());
        System.out.println("-----DIRECTION-------(4)---------" + currentDetailRecord.getDirection());
        System.out.println("-----OCCURDATE-------(5)---------" + currentDetailRecord.getOccurDate());
        System.out.println("-----SubsidiaryAccount-------(6)---------" + currentDetailRecord.getSubsidiaryAccount());
        System.out.println("-----ENTERTIME-------(7)---------" + currentDetailRecord.getEnterTime());

        return PageNavigation.LIST;
    }

}