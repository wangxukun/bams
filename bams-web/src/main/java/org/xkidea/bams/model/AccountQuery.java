package org.xkidea.bams.model;

import org.xkidea.bams.entity.Area;
import org.xkidea.bams.entity.SubsidiaryAccount;

import java.io.Serializable;

public class AccountQuery implements Serializable {
    private static final long serialVersionUID = -2342437016158734617L;
    boolean queryByEnterDate;
    String startDate;
    String endDate;
    Area area;
    SubsidiaryAccount subsidiaryAccount;

    public AccountQuery() {
    }

    public boolean isQueryByEnterDate() {
        return queryByEnterDate;
    }

    public void setQueryByEnterDate(boolean queryByEnterDate) {
        this.queryByEnterDate = queryByEnterDate;
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
