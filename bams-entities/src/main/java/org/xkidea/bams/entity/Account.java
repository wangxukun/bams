package org.xkidea.bams.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ACCOUNT")
// TODO @NamedQueries
public class Account implements Serializable {

    private static final long serialVersionUID = 4907786332920001037L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    protected Integer id;
    @Basic(optional = false)
    @Size(min=2, max=50, message="{person.name}")
    @Column(name = "NAME")
    protected String name;
    @Basic(optional = false)
    @Column(name = "BALANCE",columnDefinition = "Decimal(12,2) default '0.00'")
    protected BigDecimal balance;
    @Basic(optional = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_CREATED")
    protected Date dateCreated;

    public Account() {
    }

    public Account(Integer id, @Size(min = 2, max = 50, message = "{person.name}") String name, BigDecimal balance, @NotNull Date dateCreated) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.dateCreated = dateCreated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id.equals(account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                '}';
    }
}
