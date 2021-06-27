package org.xkidea.bams.model;

import java.math.BigDecimal;

public class DebitCreditTotailRow {
    private int direction;
    private BigDecimal totail;

    public DebitCreditTotailRow() {
    }

    public DebitCreditTotailRow(int direction, BigDecimal totail) {
        this.direction = direction;
        this.totail = totail;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public BigDecimal getTotail() {
        return totail;
    }

    public void setTotail(BigDecimal totail) {
        this.totail = totail;
    }

    @Override
    public String toString() {
        return "DebitCreditTotailRow{" +
                "direction=" + direction +
                ", totail=" + totail +
                '}';
    }
}
