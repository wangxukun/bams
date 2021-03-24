package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.Administrator;
import org.xkidea.bams.entity.Groups;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AdministratorBean extends AbstractFacade<Administrator> {
    @PersistenceContext(unitName = "bamsPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public AdministratorBean() {
        super(Administrator.class);
    }

    @Override
    public void create(Administrator admin) {
        Groups adminGroup = (Groups) em.createNamedQuery("Groups_findByName")
                .setParameter("name","ADMINISTRATOR")
                .getSingleResult();
        admin.getGroupsList().add(adminGroup);
        adminGroup.getPersonList().add(admin);
        em.persist(admin);
        // TODO Java ee 7
       // em.merge(adminGroup);
    }

    @Override
    public void remove(Administrator admin) {
        Groups adminGroup = (Groups) em.createNamedQuery("Groups_findByName")
                .setParameter("name","ADMINISTRATOR")
                .getSingleResult();
        adminGroup.getPersonList().remove(admin);
        em.remove(em.merge(admin));
        em.merge(adminGroup);
    }
}
