package org.xkidea.bams;

import org.xkidea.bams.ejb.AbstractFacade;
import org.xkidea.bams.entity.Accountant;
import org.xkidea.bams.entity.Groups;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AccountantBean extends AbstractFacade<Accountant> {
    @PersistenceContext(unitName = "bamsPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public AccountantBean() {
        super(Accountant.class);
    }

    @Override
    public void create(Accountant accountant) {
        Groups accountantGroup = (Groups) em.createNamedQuery("Groups_findByName")
                .setParameter("name","ACCOUNTANT")
                .getSingleResult();
        accountant.getGroupsList().add(accountantGroup);
        accountantGroup.getPersonList().add(accountant);
        em.persist(accountant);
    }

    @Override
    public void remove(Accountant accountant) {
        Groups accountantGroup = (Groups) em.createNamedQuery("Groups_findByName")
                .setParameter("name","ACCOUNTANT")
                .getSingleResult();
        accountantGroup.getPersonList().remove(accountant);
        em.remove(em.merge(accountant));
        em.merge(accountantGroup);
    }
}
