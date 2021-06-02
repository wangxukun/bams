package org.xkidea.bams.model;

import org.xkidea.bams.entity.Area;
import org.xkidea.bams.entity.SubsidiaryAccount;

import java.io.Serializable;

public class AccountQuery implements Serializable {
    private static final long serialVersionUID = -2342437016158734617L;
    String queryCategory = "1";
    String startDate;
    String endDate;
    Area area;
    SubsidiaryAccount subsidiaryAccount;

    public AccountQuery() {
    }

    public String getQueryCategory() {
        return queryCategory;
    }

    public void setQueryCategory(String queryCategory) {
        this.queryCategory = queryCategory;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public SubsidiaryAccount getSubsidiaryAccount() {
        return subsidiaryAccount;
    }

    public void setSubsidiaryAccount(SubsidiaryAccount subsidiaryAccount) {
        this.subsidiaryAccount = subsidiaryAccount;
    }
}
