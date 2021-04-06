package org.xkidea.bams.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "DETAILRECODD")
public class DetailRecord implements Serializable {

    private static final long serialVersionUID = -288504768855346786L;

    @JoinColumn(name = "SUBSIDISRYACCOUNT_ID",referencedColumnName = "ID")
    @ManyToOne
    private SubsidiaryAccount subsidiaryAccount;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "AMOUNT",columnDefinition = "Decimal(12,2) default '0.00'")
    private BigDecimal amount;

    @Basic(optional = false)
    @Column(name = "DIRECTION")
    private Integer direction;

    @Basic(optional = false)
    @Column(name = "SUMMARY")
    @Size(min = 2, max = 50, message = "{detailRecode.summary}")
    private String summary;

    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date enterTime;

    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date occurDate;

    public DetailRecord() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Date enterTime) {
        this.enterTime = enterTime;
    }

    public Date getOccurDate() {
        return occurDate;
    }

    public void setOccurDate(Date occurDate) {
        this.occurDate = occurDate;
    }

    public SubsidiaryAccount getSubsidiaryAccount() {
        return subsidiaryAccount;
    }

    public void setSubsidiaryAccount(SubsidiaryAccount subsidiaryAccount) {
        this.subsidiaryAccount = subsidiaryAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetailRecord that = (DetailRecord) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DetailRecord{" +
                "id=" + id +
                '}';
    }
}
