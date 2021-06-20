package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.DetailRecord;
import org.xkidea.bams.entity.Person;

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


    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public DetailRecordBean() {
        super(DetailRecord.class);
    }

    public int count(Date begin, Date end, boolean isEnterDate) {
        StringBuilder dateTye = new StringBuilder();
        if (isEnterDate) {
            dateTye.append("detailRecord.enterTime");
        } else {
            dateTye.append("detailRecord.occurDate");
        }

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<DetailRecord> recordRoot = cq.from(DetailRecord.class);
        cq.select(cb.count(recordRoot));
        cq.where(cb.between(recordRoot.get(dateTye.toString()),begin,end));
        Query query = getEntityManager().createQuery(cq);
        return ((Long)query.getSingleResult()).intValue();
    }

    public List<DetailRecord> getByEntryOrOccurDate(Date begin,Date end,boolean isEntryDate) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<DetailRecord> recordRoot = cq.from(DetailRecord.class);
        cq.select(recordRoot);
        if (isEntryDate) {
            cq.where(cb.between(recordRoot.get("enterTime"), begin, end));
        } else {
            cq.where(cb.between(recordRoot.get("occurDate"), begin, end));
        }
        Query query = getEntityManager().createQuery(cq);
        return (List<DetailRecord>) query.getResultList();
    }

    public List<DetailRecord> getByEntryOrOccurDate(Person user, Date begin, Date end, boolean isEnterDate) {
        // TODO ORDER BY enterTime DESC/ASC
        StringBuilder dateTye = new StringBuilder();
        if (isEnterDate) {
            dateTye.append("detailRecord.enterTime");
        } else {
            dateTye.append("detailRecord.occurDate");
        }
        String sql = "SELECT detailRecord " +
                "FROM DetailRecord detailRecord " +
                "JOIN detailRecord.subsidiaryAccount subsidiaryAccount " +
                "JOIN subsidiaryAccount.area area, IN(area.personList) person " +
                "WHERE " + dateTye.toString() + " BETWEEN :begin AND :end AND person = :user ";
        Person p = em.find(Person.class,user.getId());
        List<DetailRecord> detailRecordList = em.createQuery(sql)
                .setParameter("user", p)
                .setParameter("begin", begin)
                .setParameter("end", end)
                .getResultList();
        return detailRecordList;
    }

    public int count(Person user, Date begin, Date end, boolean isEnterDate) {
        StringBuilder dateTye = new StringBuilder();
        if (isEnterDate) {
            dateTye.append("detailRecord.enterTime");
        } else {
            dateTye.append("detailRecord.occurDate");
        }
        String sql = "SELECT COUNT(detailRecord) " +
                "FROM DetailRecord detailRecord " +
                "JOIN detailRecord.subsidiaryAccount subsidiaryAccount " +
                "JOIN subsidiaryAccount.area area, IN(area.personList) person " +
                "WHERE " + dateTye.toString() + " BETWEEN :begin AND :end AND person = :user ";
        Person p = em.find(Person.class,user.getId());
        Query query = em.createQuery(sql)
                .setParameter("user", p)
                .setParameter("begin", begin)
                .setParameter("end", end);
        return ((Long)query.getSingleResult()).intValue();
    }

}
