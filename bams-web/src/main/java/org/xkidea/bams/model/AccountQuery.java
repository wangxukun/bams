package org.xkidea.bams.model;

import org.xkidea.bams.entity.Area;
import org.xkidea.bams.entity.SubsidiaryAccount;

import java.io.Serializable;
import java.util.Date;

public class AccountQuery implements Serializable {
    private static final long serialVersionUID = -2342437016158734617L;
    boolean queryByEnterDate;
    Date beginDate;
    Date endDate;
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

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
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

    public String getDescription() {
        if (area == null) {
            return "总账户";
        }else{
            if (subsidiaryAccount == null) {
                return area.getDescription();
            } else {
                return subsidiaryAccount.getName();
            }
        }
    }
}
