package org.xkidea.bams.entity;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class Treasurer extends Person{

    private static final long serialVersionUID = 4152855527602764061L;

    public Treasurer() {
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
