package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.Customer;
import org.xkidea.bams.entity.Groups;
import org.xkidea.bams.entity.Person;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

//    public Person getUserByEmail(String email) {
//        Query createNamedQuery = getEntityManager().createNamedQuery("Person.findByEmail");
//
//        createNamedQuery.setParameter("email", email);
//
//        if (createNamedQuery.getResultList().size() > 0) {
//            return (Person) createNamedQuery.getSingleResult();
//        } else {
//            return null;
//        }
//    }
}
