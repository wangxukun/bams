package org.xkidea.bams.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "SORTACCOUNT")
@NamedQueries({
        @NamedQuery(name = "SortAccount_findByName",query = "SELECT s FROM SortAccount s WHERE s.name = :name")
})
public class SortAccount implements Serializable {
    private static final long serialVersionUID = -7963011345643047629L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50, message = "{sort_account.name}")
    @Column(name = "NAME", unique = true)
    private String name;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "sortAccount")
    private List<Account> accountList;

    public SortAccount() {
        accountList = new ArrayList<>();
    }

    public SortAccount(Integer id) {
        this.id = id;
        accountList = new ArrayList<>();
    }

    public SortAccount(Integer id, @NotNull @Size(min = 1, max = 50, message = "{sort_account.name}") String name) {
        this.id = id;
        this.name = name;
        accountList = new ArrayList<>();
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

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SortAccount sortAccount = (SortAccount) obj;
        return id.equals(sortAccount.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SortAccount{" + "id=" + id + '}';
    }
}
