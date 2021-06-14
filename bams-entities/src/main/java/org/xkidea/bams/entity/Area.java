package org.xkidea.bams.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "AREA")
@NamedQueries({
        @NamedQuery(name = "Area.findByGeneralAccount", query = "SELECT a FROM Area a WHERE a.generalAccount = :generalAccount")
})
public class Area implements Serializable {

    private static final long serialVersionUID = -856821028349714874L;
    @ManyToOne
    private GeneralAccount generalAccount;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "area")
    private List<SubsidiaryAccount> subsidiaryAccountList;

    @ManyToMany(mappedBy = "areaList",cascade = CascadeType.ALL)
    @XmlTransient
    private List<Person> personList;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Size(min=2, max=50, message="{area.name}")
    @Column(name = "NAME")
    private String name;
    @Size(max = 300, message = "{groups.description}")
    @Column(name = "DESCRIPTION")
    private String description;

    public Area() {
        this.subsidiaryAccountList = new ArrayList<>();
        this.personList = new ArrayList<>();
    }

    public Area(Integer id) {
        this.id = id;
        this.subsidiaryAccountList = new ArrayList<>();
        this.personList = new ArrayList<>();
    }

    public Area(Integer id, @Size(min = 2, max = 50, message = "{area.name}") String name) {
        this.id = id;
        this.name = name;
        this.subsidiaryAccountList = new ArrayList<>();
        this.personList = new ArrayList<>();
    }

    public GeneralAccount getGeneralAccount() {
        return generalAccount;
    }

    public void setGeneralAccount(GeneralAccount generalAccount) {
        this.generalAccount = generalAccount;
    }

    public List<SubsidiaryAccount> getSubsidiaryAccountList() {
        return subsidiaryAccountList;
    }

    public void setSubsidiaryAccountList(List<SubsidiaryAccount> subsidiaryAccountList) {
        this.subsidiaryAccountList = subsidiaryAccountList;
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

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public void addPerson(Person person) {
        this.getPersonList().add(person);
    }

    public void dropPerson(Person person) {
        this.getPersonList().remove(person);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Area)) {
            return false;
        }
        Area other = (Area) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return this.name;
    }
}
