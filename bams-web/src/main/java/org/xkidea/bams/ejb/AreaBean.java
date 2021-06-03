package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.Area;
import org.xkidea.bams.entity.GeneralAccount;
import org.xkidea.bams.entity.Person;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class AreaBean extends AbstractFacade<Area> {

    private static final Logger logger = Logger.getLogger(AreaBean.class.getCanonicalName());
    @PersistenceContext(unitName = "bamsPU")
    private EntityManager em;

    public AreaBean() {
        super(Area.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public void create(Area area, Person person) {
        Person p= (Person) em.createNamedQuery("Person.findByEmail")
                .setParameter("email",person.getEmail())
                .getSingleResult();
        area.getPersonList().add(p);
        area.setGeneralAccount(p.getAccountList().get(0));
        p.getAreaList().add(area);
        em.persist(area);
    }

    public int countByGeneralAccount(GeneralAccount generalAccount) {
        CriteriaBuilder qb = em.getCriteriaBuilder();
        CriteriaQuery query = qb.createQuery();
        Root<Area> area = query.from(Area.class);
        query.select(qb.count(area));
        query.where(qb.equal(area.get("generalAccount"),generalAccount));
        Query q = em.createQuery(query);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * 返回当前User有权限的所有区域集合
     * @param currentUser
     * @return
     */
    public List<Area> getAreasByCurrentUser(Person currentUser) {
        List<Area> areas = em.createQuery("SELECT a FROM Area a, IN (a.personList) p WHERE p = :currentUser")
                .setParameter("currentUser",currentUser)
                .getResultList();
        return areas;
    }

    public List<Area> findByCurrentUser(int[] range,Person currentUser) {
        List<Area> areas = em.createQuery("SELECT a FROM Area a, IN (a.personList) p WHERE p = :currentUser")
                .setParameter("currentUser",currentUser)
                .setMaxResults(range[1] - range[0])
                .setFirstResult(range[0])
                .getResultList();
        return areas;
    }

    public int getCountByCurrentUser(Person currentUser) {
        Long count = (Long) em.createQuery("SELECT count(a) FROM Area a, IN (a.personList) p WHERE p = :currentUser")
                .setParameter("currentUser",currentUser)
                .getSingleResult();
        return count.intValue();
    }
}
