package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.Area;
import org.xkidea.bams.entity.GeneralAccount;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Level;
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

    public List<Area> findByGeneralAccount(int[] range,GeneralAccount generalAccount){
        CriteriaBuilder qb = em.getCriteriaBuilder();
        CriteriaQuery<Area> query = qb.createQuery(Area.class);
        Root<Area> area = query.from(Area.class);
        query.where(qb.equal(area.get("generalAccount"),generalAccount));

        List<Area> result = this.findRange(range,query);

        logger.log(Level.FINEST,"Area List size: {0}",result.size());

        return result;
    }

    public List<Area> getAreasByGeneralAccount(GeneralAccount generalAccount) {
        Query createNamedQuery = getEntityManager().createNamedQuery("Area.findByGeneralAccount");
        createNamedQuery.setParameter("generalAccount",generalAccount);

        return (List<Area>) createNamedQuery.getResultList();
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
}
