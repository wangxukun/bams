package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.Groups;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class GroupsBean {
    @PersistenceContext(unitName = "bamsPU")
    private EntityManager em;

    public void create(Groups groups) {
        em.persist(groups);
    }
    private String hello = "hello world";

    public String sayHello() {
        return hello;
    }
}
