package org.xkidea.bams.model;

import org.xkidea.bams.entity.DetailRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class AccountBook {
    private BigDecimal beginningBalance;
    private BigDecimal endingBalance;

    private List<DetailRecord> detailRecordList;

    public AccountBook() {
    }

    public BigDecimal getBeginningBalance() {
        return beginningBalance;
    }

    public void setBeginningBalance(BigDecimal beginningBalance) {
        this.beginningBalance = beginningBalance;
    }

    public BigDecimal getEndingBalance() {
        return endingBalance;
    }

    public void setEndingBalance(BigDecimal endingBalance) {
        this.endingBalance = endingBalance;
    }

    public List<DetailRecord> getDetailRecordList() {
        return detailRecordList;
    }

    public void setDetailRecordList(List<DetailRecord> detailRecordList) {
        this.detailRecordList = detailRecordList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountBook that = (AccountBook) o;
        return Objects.equals(beginningBalance, that.beginningBalance) && Objects.equals(endingBalance, that.endingBalance) && Objects.equals(detailRecordList, that.detailRecordList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beginningBalance, endingBalance, detailRecordList);
    }

    @Override
    public String toString() {
        return "AccountBook{" +
                "endingBalance=" + endingBalance +
                '}';
    }
}
