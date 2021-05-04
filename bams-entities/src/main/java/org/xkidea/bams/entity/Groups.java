package org.xkidea.bams.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "GROUPZ")
// TODO NamedQueries
@NamedQueries({
        @NamedQuery(name = "Groups_findByName", query = "SELECT g FROM Groups g WHERE g.name = :name")
})
public class Groups implements Serializable {
    private static final long serialVersionUID = -3652979213312473763L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50, message = "{groups.name}")
    @Column(name = "NAME", unique = true)
    private String name;
    @Size(max = 300, message = "{groups.description}")
    @Column(name = "DESCRIPTION")
    private String description;
    @ManyToMany(mappedBy = "groupsList")
    @XmlTransient
    private List<Person> personList;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "groups")
    private List<Account> accountList;

    public Groups() {
        personList = new ArrayList<>();
        accountList = new ArrayList<>();
    }

    public Groups(Integer id) {
        this.id = id;
        personList = new ArrayList<>();
        accountList = new ArrayList<>();
    }

    public Groups(Integer id, String name) {
        this.id = id;
        this.name = name;
        personList = new ArrayList<>();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Groups groups = (Groups) o;
        return id.equals(groups.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Groups{" +
                "id=" + id +
                '}';
    }
}
