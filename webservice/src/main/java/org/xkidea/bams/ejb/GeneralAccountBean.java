package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.GeneralAccount;
import org.xkidea.bams.entity.Groups;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GeneralAccountBean extends AbstractFacade<GeneralAccount>{

    @PersistenceContext(unitName = "bamsPU")
    private EntityManager em;

    public GeneralAccountBean() {
        super(GeneralAccount.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void create(GeneralAccount generalAccount) {
        Groups generalAccountGroup = (Groups) em.createNamedQuery("Groups_findByName")
                .setParameter("name","GENERALACCOUNT")
                .getSingleResult();
        generalAccount.setGroups(generalAccountGroup);
        generalAccountGroup.getAccountList().add(generalAccount);
        em.persist(generalAccount);
    }

    @Override
    public void remove(GeneralAccount generalAccount) {
        Groups generalAccountGroup = (Groups) em.createNamedQuery("Groups_findByName")
                .setParameter("name","GENERALACCOUNT")
                .getSingleResult();
        generalAccountGroup.getAccountList().remove(generalAccount);
        em.remove(em.merge(generalAccount));
        em.merge(generalAccount);
    }
}
