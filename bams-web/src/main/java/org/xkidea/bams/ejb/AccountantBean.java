package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.*;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
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

    public void updateAreasFromPerson(List<Area> areas, Person person) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaUpdate<Person> update = cb.createCriteriaUpdate(Person.class);
//
//        Root<Person> root = update.from(Person.class);
//        update
//                .set(root.get("areaList"),areas)
//                .where(cb.equal(root.get("email"),person.getEmail()));
//        em.createQuery(update).executeUpdate();

        try {
            Person p = find(person.getId());
            List<Area> a = p.getAreaList();
            System.out.println("-------------(1)--------" + a);
//            for (int i = 0; i < a.size(); i++) {
//                System.out.println("-----------(2)-------" + a.get(i));
//                p.dropArea(a.get(i));
//                System.out.println("-----------(3)-------" + p);
//                a.get(i).dropPerson(p);
//                em.merge(p);
//                em.merge(a.get(i));
//            }

            Area aa = em.find(Area.class,a.get(3).getId());
            System.out.println("-----------------P --- " + p);
            System.out.println("-----------------a--- " + aa);
            p.dropArea(aa);
            em.merge(p);

//            for (int i = 0; i < areas.size(); i++) {
//                a.add(areas.get(i));
//                a.get(i).getPersonList().add(person);
//                em.merge(a.get(i));
//            }
//            em.merge(person);
//            System.out.println("-----------(2)-------" + person.getAreaList());
        } catch (Exception ex) {
            throw new EJBException(ex);
        }

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
