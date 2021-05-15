package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.CategoryAccount;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * CRUD账户分组CategoryAccount
 */
@Stateless
public class CategoryAccountBean extends AbstractFacade<CategoryAccount> {
    @PersistenceContext(unitName = "bamsPU")
    private EntityManager em;

    public CategoryAccountBean() {
        super(CategoryAccount.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
