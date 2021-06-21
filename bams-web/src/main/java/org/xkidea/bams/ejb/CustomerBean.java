package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class CustomerBean extends AbstractFacade<Customer> {

    @PersistenceContext(unitName = "bamsPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public CustomerBean() {
        super(Customer.class);
    }

    @Override
    public void create(Customer customer) {
        Groups customerGroup = (Groups) em.createNamedQuery("Groups_findByName")
                .setParameter("name", "CUSTOMER")
                .getSingleResult();
        customer.getGroupsList().add(customerGroup);
        customerGroup.getPersonList().add(customer);
//            super.create(customer);
        em.persist(customer);
    }

    @Override
    public void remove(Customer customer) {
        Groups customerGroup = (Groups) em.createNamedQuery("Groups_findByName")
                .setParameter("name", "CUSTOMER")
                .getSingleResult();
        customerGroup.getPersonList().remove(customer);
        em.remove(em.merge(customer));
        em.merge(customerGroup);
    }

    public Person getUserByEmail(String email) {
        Query createNamedQuery = getEntityManager().createNamedQuery("Person.findByEmail");

        createNamedQuery.setParameter("email", email);

        if (createNamedQuery.getResultList().size() > 0) {
            return (Person) createNamedQuery.getSingleResult();
        } else {
            return null;
        }
    }


    /**
     * 查找当前总账户下的报账员集合
     * @param range
     * @param currentAccount
     * @return
     */
    public List<Customer> findByCurrentGeneralAccount(int[] range, GeneralAccount currentAccount) {
        CriteriaBuilder qb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> query = qb.createQuery(Customer.class);
        Root<Customer> customerRoot = query.from(Customer.class);
        query.select(customerRoot).where(qb.isMember(currentAccount,customerRoot.get("accountList")));
        TypedQuery<Customer> q = em.createQuery(query);
        List<Customer> results = q.getResultList();
        return results;
    }

    public int getCountByCurrentGeneralAccount(GeneralAccount currentAccount) {
        Long count = (Long) em.createQuery("SELECT count(a) FROM Customer a, IN (a.accountList) ac WHERE ac = :currentAccount")
                .setParameter("currentAccount",currentAccount)
                .getSingleResult();
        return count.intValue();
    }
}
