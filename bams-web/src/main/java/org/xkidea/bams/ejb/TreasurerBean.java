package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.Groups;
import org.xkidea.bams.entity.Treasurer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class TreasurerBean extends AbstractFacade<Treasurer>{
    @PersistenceContext(unitName = "bamsPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public TreasurerBean() {
        super(Treasurer.class);
    }

    @Override
    public void create(Treasurer treasurer) {
        Groups treasurerGroup = (Groups) em.createNamedQuery("Groups_findByName")
                .setParameter("name", "TREASURER")
                .getSingleResult();
        treasurer.getGroupsList().add(treasurerGroup);
        treasurerGroup.getPersonList().add(treasurer);
        em.persist(treasurer);
    }

    @Override
    public void remove(Treasurer treasurer) {
        Groups treasurerGroup = (Groups) em.createNamedQuery("Groups_findByName")
                .setParameter("name", "TREASURER")
                .getSingleResult();
        treasurerGroup.getPersonList().remove(treasurer);
        em.remove(em.merge(treasurer));
        em.merge(treasurerGroup);
    }
}
