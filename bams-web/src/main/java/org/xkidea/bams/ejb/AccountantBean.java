package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.Accountant;
import org.xkidea.bams.entity.GeneralAccount;
import org.xkidea.bams.entity.Groups;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

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

    /**
     * 查找当前总账户下的会计员集合
     * @param range
     * @param currentAccount
     * @return
     */
    public List<Accountant> findByCurrentGeneralAccount(int[] range, GeneralAccount currentAccount) {
        CriteriaBuilder qb = em.getCriteriaBuilder();
        CriteriaQuery<Accountant> query = qb.createQuery(Accountant.class);
        Root<Accountant> accountantRoot = query.from(Accountant.class);
        query.select(accountantRoot).where(qb.isMember(currentAccount,accountantRoot.get("accountList")));
        TypedQuery<Accountant> q = em.createQuery(query);
        List<Accountant> results = q.getResultList();
        return results;
    }

    public int getCountByCurrentGeneralAccount(GeneralAccount currentAccount) {
        Long count = (Long) em.createQuery("SELECT count(a) FROM Accountant a, IN (a.accountList) ac WHERE ac = :currentAccount")
                .setParameter("currentAccount",currentAccount)
                .getSingleResult();
        return count.intValue();
    }
}
