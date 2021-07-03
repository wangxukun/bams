package org.xkidea.bams.model;

import java.math.BigDecimal;

public class FirstBalance {
    private BigDecimal debitTotal;
    private BigDecimal creditTotal;

    public FirstBalance() {
    }

    public FirstBalance(BigDecimal debitTotal, BigDecimal creditTotal) {
        this.debitTotal = debitTotal;
        this.creditTotal = creditTotal;
    }

    public BigDecimal getDebitTotal() {
        return debitTotal;
    }

    public void setDebitTotal(BigDecimal debitTotal) {
        this.debitTotal = debitTotal;
    }

    public BigDecimal getCreditTotal() {
        return creditTotal;
    }

    public void setCreditTotal(BigDecimal creditTotal) {
        this.creditTotal = creditTotal;
    }

    public BigDecimal getBalance() {
        return debitTotal.subtract(creditTotal);
    }
}
