package org.xkidea.bams.entity;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class Customer extends Person{

    private static final long serialVersionUID = -6499599034034914483L;

    public Customer() {
        this.groupsList = new ArrayList<>();
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
