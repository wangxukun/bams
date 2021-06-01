package org.xkidea.bams.model;

import java.io.Serializable;
import java.util.Date;

public class AccountQuery implements Serializable {
    private static final long serialVersionUID = -2342437016158734617L;

    private Integer queryCategory = 1;
    private Date startDate ;
    private Date endDate ;

    public AccountQuery() {
    }

    public Integer getQueryCategory() {
        return queryCategory;
    }

    public void setQueryCategory(Integer queryCategory) {
        this.queryCategory = queryCategory;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
