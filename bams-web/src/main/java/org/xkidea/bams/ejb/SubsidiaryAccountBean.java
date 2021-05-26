package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.GeneralAccount;
import org.xkidea.bams.entity.SortAccount;
import org.xkidea.bams.entity.SubsidiaryAccount;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

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

    @Override
    public void create(SubsidiaryAccount subsidiaryAccount) {
        SortAccount sortAccount = (SortAccount) em.createNamedQuery("SortAccount_findByName")
                .setParameter("name","SUBSIDIARYACCOUNT")
                .getSingleResult();
        subsidiaryAccount.setBalance(BigDecimal.ZERO);
        subsidiaryAccount.setSortAccount(sortAccount);
        sortAccount.getAccountList().add(subsidiaryAccount);
        em.persist(subsidiaryAccount);
    }

    /*public List<SubsidiaryAccount> findByGeneralAccount(int[] range, GeneralAccount generalAccount) {

    }*/
}
