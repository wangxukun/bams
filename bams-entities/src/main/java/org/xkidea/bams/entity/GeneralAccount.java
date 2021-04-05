package org.xkidea.bams.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GeneralAccount extends Account{

    private static final long serialVersionUID = -2313957548533758458L;

    @ManyToMany(mappedBy = "accountList")
    @XmlTransient
    private List<Person> personList;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "generalAccount")
    private List<Area> areaList;

    public GeneralAccount() {
        this.personList = new ArrayList<>();
        this.areaList = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
