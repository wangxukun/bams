package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.Groups;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class GroupsBean extends AbstractFacade<Groups> {
    @PersistenceContext(unitName = "bamsPU")
    private EntityManager em;

    public GroupsBean() {
        super(Groups.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void create(Groups groups) {
        em.persist(groups);
    }

    @Override
    public void remove(Groups groups) {
        em.remove(em.merge(groups));
    }
}
