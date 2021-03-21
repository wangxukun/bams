package org.xkidea.bams.entity;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class Administrator extends Person{

    private static final long serialVersionUID = 3851594655487537518L;

    public Administrator() {
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
