package org.xkidea.bams.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SubsidiaryAccount extends Account{

    private static final long serialVersionUID = 905403751337815776L;

    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    @ManyToOne
    private Area area;

    public SubsidiaryAccount() {
    }

    public SubsidiaryAccount(Area area) {
        this.area = area;
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
