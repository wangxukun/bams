package org.xkidea.bams.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SubsidiaryAccount extends Account{

    private static final long serialVersionUID = 905403751337815776L;

    @JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    @ManyToOne
    private Area area;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "subsidiaryAccount")
    private List<DetailRecord> detailRecordList;

    public SubsidiaryAccount() {
        detailRecordList = new ArrayList<>();
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public List<DetailRecord> getDetailRecordList() {
        return detailRecordList;
    }

    public void setDetailRecordList(List<DetailRecord> detailRecordList) {
        this.detailRecordList = detailRecordList;
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