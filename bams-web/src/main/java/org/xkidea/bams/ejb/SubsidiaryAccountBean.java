package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.SubsidiaryAccount;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class SubsidiaryAccountBean extends AbstractFacade<SubsidiaryAccount> {

    @PersistenceContext(unitName = "bamsPU")
    private EntityManager em;

    public SubsidiaryAccountBean() {
        super(SubsidiaryAccount.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
