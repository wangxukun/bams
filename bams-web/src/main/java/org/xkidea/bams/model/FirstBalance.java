package org.xkidea.bams.model;

import java.math.BigDecimal;

public class FirstBalance {
    private BigDecimal debitTotail;
    private BigDecimal creditTotail;

    public FirstBalance() {
    }

    public FirstBalance(BigDecimal debitTotail, BigDecimal creditTotail) {
        this.debitTotail = debitTotail;
        this.creditTotail = creditTotail;
    }

    public BigDecimal getDebitTotail() {
        return debitTotail;
    }

    public void setDebitTotail(BigDecimal debitTotail) {
        this.debitTotail = debitTotail;
    }

    public BigDecimal getCreditTotail() {
        return creditTotail;
    }

    public void setCreditTotail(BigDecimal creditTotail) {
        this.creditTotail = creditTotail;
    }

    public BigDecimal getBalance() {
        return debitTotail.subtract(creditTotail);
    }
}
