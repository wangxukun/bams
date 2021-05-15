package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.SortAccount;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * CRUD账户类别SortAccount
 */
@Stateless
public class SortAccountBean extends AbstractFacade<SortAccount> {
    @PersistenceContext(unitName = "bamsPU")
    private EntityManager em;

    public SortAccountBean() {
        super(SortAccount.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
