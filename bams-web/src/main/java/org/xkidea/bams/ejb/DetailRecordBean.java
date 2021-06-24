package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.Area;
import org.xkidea.bams.entity.DetailRecord;
import org.xkidea.bams.entity.Person;
import org.xkidea.bams.entity.SubsidiaryAccount;

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
        StringBuilder dateType = new StringBuilder();
        if (isEnterDate) {
            dateType.append("detailRecord.enterTime");
        } else {
            dateType.append("detailRecord.occurDate");
        }

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<DetailRecord> recordRoot = cq.from(DetailRecord.class);
        cq.select(cb.count(recordRoot));
        cq.where(cb.between(recordRoot.get(dateType.toString()),begin,end));
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
        StringBuilder dateType = new StringBuilder();
        if (isEnterDate) {
            dateType.append("detailRecord.enterTime");
        } else {
            dateType.append("detailRecord.occurDate");
        }
        String sql = "SELECT detailRecord " +
                "FROM DetailRecord detailRecord " +
                "JOIN detailRecord.subsidiaryAccount subsidiaryAccount " +
                "JOIN subsidiaryAccount.area area, IN(area.personList) person " +
                "WHERE " + dateType.toString() + " BETWEEN :begin AND :end AND person = :user " +
                "ORDER BY " + dateType.toString() + " ASC";
        List<DetailRecord> detailRecordList = em.createQuery(sql)
                .setParameter("user", user)
                .setParameter("begin", begin)
                .setParameter("end", end)
                .getResultList();
        return detailRecordList;
    }

    public List<DetailRecord> getByEntryOrOccurDate(int[] range,Person user, Date begin, Date end, boolean isEnterDate) {
        StringBuilder dateType = new StringBuilder();
        if (isEnterDate) {
            dateType.append("detailRecord.enterTime");
        } else {
            dateType.append("detailRecord.occurDate");
        }
        String sql = "SELECT detailRecord " +
                "FROM DetailRecord detailRecord " +
                "JOIN detailRecord.subsidiaryAccount subsidiaryAccount " +
                "JOIN subsidiaryAccount.area area, IN(area.personList) person " +
                "WHERE " + dateType.toString() + " BETWEEN :begin AND :end AND person = :user " +
                "ORDER BY " + dateType.toString() + " ASC";
        List<DetailRecord> detailRecordList = em.createQuery(sql)
                .setParameter("user", user)
                .setParameter("begin", begin)
                .setParameter("end", end)
                .setMaxResults(range[1] - range[0])
                .setFirstResult(range[0])
                .getResultList();
        return detailRecordList;
    }

    public int count(Person user, Date begin, Date end, boolean isEnterDate) {
        StringBuilder dateType = new StringBuilder();
        if (isEnterDate) {
            dateType.append("detailRecord.enterTime");
        } else {
            dateType.append("detailRecord.occurDate");
        }
        String sql = "SELECT COUNT(detailRecord) " +
                "FROM DetailRecord detailRecord " +
                "JOIN detailRecord.subsidiaryAccount subsidiaryAccount " +
                "JOIN subsidiaryAccount.area area, IN(area.personList) person " +
                "WHERE " + dateType.toString() + " BETWEEN :begin AND :end AND person = :user ";
        Person p = em.find(Person.class,user.getId());
        Query query = em.createQuery(sql)
                .setParameter("user", p)
                .setParameter("begin", begin)
                .setParameter("end", end);
        return ((Long)query.getSingleResult()).intValue();
    }

    public int count(Person authenticatedUser, Date begin, Date end, boolean queryByEnterDate, Area area, SubsidiaryAccount subsidiaryAccount) {
        int totail;
        if (area == null) {
            totail = count(authenticatedUser, begin, end, queryByEnterDate);
            return totail;
        } else {

            StringBuilder dateType = new StringBuilder();
            StringBuilder sqlAS = new StringBuilder();
            StringBuilder sql = new StringBuilder();

            /*String sql1 = "SELECT count(detailRecord) FROM DetailRecord detailRecord JOIN detailRecord.subsidiaryAccount subsidiaryAccount ";
            String sqlNoSubsidiaryAccount = "JOIN subsidiaryAccount.area area ";
            String sql2 = "WHERE " + dateType.toString() + " BETWEEN :begin AND :end " + sqlAS.toString() + " ORDER BY " + dateType.toString() + " ASC";*/

            if (queryByEnterDate) {
                // 按录入时间查找
                dateType.append("detailRecord.enterTime");
            } else {
                // 按业务发生日期查找
                dateType.append("detailRecord.occurDate");
            }

            // 如果未指明明细账户，则返回当前loginUser下选定区域下的内容
            if (subsidiaryAccount == null) {
                sqlAS.append("AND area = :area ");
                String sql1 = "SELECT count(detailRecord) FROM DetailRecord detailRecord JOIN detailRecord.subsidiaryAccount subsidiaryAccount ";
                String sql2 = "WHERE " + dateType.toString() + " BETWEEN :begin AND :end AND subsidiaryAccount.area = :area ORDER BY " + dateType.toString() + " ASC";
                sql.append(sql1).append(sql2);
                Query query = em.createQuery(sql.toString())
                        .setParameter("begin", begin)
                        .setParameter("end", end)
                        .setParameter("area",area);
                return ((Long)query.getSingleResult()).intValue();
                // 如果指明了明细账户，则返回当前loginUser下选定明细账户下的内容
            } else {
                String sql1 = "SELECT count(detailRecord) FROM DetailRecord detailRecord ";
                String sql2 = "WHERE " + dateType.toString() + " BETWEEN :begin AND :end AND detailRecord.subsidiaryAccount = :subsidiaryAccount ORDER BY " + dateType.toString() + " ASC";
                sql.append(sql1).append(sql2);
                Query query = em.createQuery(sql.toString())
                        .setParameter("begin", begin)
                        .setParameter("end", end)
                        .setParameter("subsidiaryAccount",subsidiaryAccount);
                return ((Long)query.getSingleResult()).intValue();
            }

        }

    }

    public List<DetailRecord> getByEntryOrOccurDate(int[] range, Person authenticatedUser, Date begin, Date end, boolean queryByEnterDate, Area area, SubsidiaryAccount subsidiaryAccount) {
        List<DetailRecord> detailRecordList;

        // 如果未指明区域，则返回当前loginUser下的所有区域下的内容
        if (area == null) {
            detailRecordList = getByEntryOrOccurDate(range, authenticatedUser, begin, end, queryByEnterDate);
            return detailRecordList;
        } else {

            StringBuilder dateType = new StringBuilder();
            StringBuilder sqlAS = new StringBuilder();
            StringBuilder sql = new StringBuilder();


            if (queryByEnterDate) {
                // 按录入时间查找
                dateType.append("detailRecord.enterTime");
            } else {
                // 按业务发生日期查找
                dateType.append("detailRecord.occurDate");
            }

//            String sqlNoSubsidiaryAccount = "JOIN subsidiaryAccount.area area ";

            // 如果未指明明细账户，则返回当前loginUser下选定区域下的内容
            if (subsidiaryAccount == null) {
                String sql1 = "SELECT detailRecord FROM DetailRecord detailRecord JOIN detailRecord.subsidiaryAccount subsidiaryAccount ";
                String sql2 = "WHERE " + dateType.toString() + " BETWEEN :begin AND :end AND subsidiaryAccount.area = :area ORDER BY " + dateType.toString() + " ASC";
                sql.append(sql1).append(sql2);
                detailRecordList = em.createQuery(sql.toString())
                        .setParameter("begin", begin)
                        .setParameter("end", end)
                        .setParameter("area",area)
                        .setMaxResults(range[1] - range[0])
                        .setFirstResult(range[0])
                        .getResultList();
                return detailRecordList;


                // 如果指明了明细账户，则返回当前loginUser下选定明细账户下的内容
            } else {
                String sql1 = "SELECT detailRecord FROM DetailRecord detailRecord ";
                String sql2 = "WHERE " + dateType.toString() + " BETWEEN :begin AND :end AND detailRecord.subsidiaryAccount = :subsidiaryAccount ORDER BY " + dateType.toString() + " ASC";
                sql.append(sql1).append(sql2);
                detailRecordList = em.createQuery(sql.toString())
                        .setParameter("begin", begin)
                        .setParameter("end", end)
                        .setParameter("subsidiaryAccount",subsidiaryAccount)
                        .setMaxResults(range[1] - range[0])
                        .setFirstResult(range[0])
                        .getResultList();
                return detailRecordList;

            }

        }

    }
}
