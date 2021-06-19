package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.DetailRecord;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@Stateless
public class DetailRecordBean extends AbstractFacade<DetailRecord>{
    @PersistenceContext(unitName = "bamsPU")
    private EntityManager em;

    private CriteriaBuilder cb;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public DetailRecordBean() {
        super(DetailRecord.class);
        this.cb = getEntityManager().getCriteriaBuilder();
    }

    public int count(Date begin, Date end) {
        CriteriaQuery cq = this.cb.createQuery();
        Root<DetailRecord> recordRoot = cq.from(DetailRecord.class);
        cq.select(this.cb.count(recordRoot));
        cq.where(this.cb.between(recordRoot.get("enterTime"),begin,end));
        Query query = getEntityManager().createQuery(cq);
        return ((Long)query.getSingleResult()).intValue();
    }

    public List<DetailRecord> getByEntryOrOccurDate(Date begin,Date end,boolean isEntryDate) {
        CriteriaQuery cq = cb.createQuery();
        Root<DetailRecord> recordRoot = cq.from(DetailRecord.class);
        cq.select(cb.count(recordRoot));
        if (isEntryDate) {
            cq.where(cb.between(recordRoot.get("enterTime"), begin, end));
        } else {
            cq.where(cb.between(recordRoot.get("occurDate"), begin, end));
        }
        Query query = getEntityManager().createQuery(cq);
        return query.getResultList();
    }
}
