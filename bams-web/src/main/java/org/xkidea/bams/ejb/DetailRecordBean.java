package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.Area;
import org.xkidea.bams.entity.DetailRecord;
import org.xkidea.bams.entity.Person;
import org.xkidea.bams.entity.SubsidiaryAccount;
import org.xkidea.bams.model.FirstBalance;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
public class DetailRecordBean extends AbstractFacade<DetailRecord> {
    private static final String BUNDLE = "bundles.Bundle";

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
        cq.where(cb.between(recordRoot.get(dateType.toString()), begin, end));
        Query query = getEntityManager().createQuery(cq);
        return ((Long) query.getSingleResult()).intValue();
    }

    public List<DetailRecord> getByEntryOrOccurDate(Date begin, Date end, boolean isEntryDate) {
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

    public List<DetailRecord> getByEntryOrOccurDate(int[] range, Person user, Date begin, Date end, boolean isEnterDate) {
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
        Person p = em.find(Person.class, user.getId());
        Query query = em.createQuery(sql)
                .setParameter("user", p)
                .setParameter("begin", begin)
                .setParameter("end", end);
        return ((Long) query.getSingleResult()).intValue();
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
                        .setParameter("area", area);
                return ((Long) query.getSingleResult()).intValue();
                // 如果指明了明细账户，则返回当前loginUser下选定明细账户下的内容
            } else {
                String sql1 = "SELECT count(detailRecord) FROM DetailRecord detailRecord ";
                String sql2 = "WHERE " + dateType.toString() + " BETWEEN :begin AND :end AND detailRecord.subsidiaryAccount = :subsidiaryAccount ORDER BY " + dateType.toString() + " ASC";
                sql.append(sql1).append(sql2);
                Query query = em.createQuery(sql.toString())
                        .setParameter("begin", begin)
                        .setParameter("end", end)
                        .setParameter("subsidiaryAccount", subsidiaryAccount);
                return ((Long) query.getSingleResult()).intValue();
            }

        }

    }

    /**
     * 分页获取数据
     * @param range
     * @param authenticatedUser
     * @param begin
     * @param end
     * @param queryByEnterDate
     * @param area
     * @param subsidiaryAccount
     * @return
     */
    public List<DetailRecord> getByEntryOrOccurDate(int[] range, Person authenticatedUser, Date begin, Date end, boolean queryByEnterDate, Area area, SubsidiaryAccount subsidiaryAccount) {
        List<DetailRecord> detailRecordList;

        // 如果未指明区域，则返回当前loginUser下的所有区域下的内容
        if (area == null) {
            detailRecordList = getByEntryOrOccurDate(range, authenticatedUser, begin, end, queryByEnterDate);
//            return detailRecordList;
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
                        .setParameter("area", area)
                        .setMaxResults(range[1] - range[0])
                        .setFirstResult(range[0])
                        .getResultList();
//                return detailRecordList;


                // 如果指明了明细账户，则返回当前loginUser下选定明细账户下的内容
            } else {
                String sql1 = "SELECT detailRecord FROM DetailRecord detailRecord ";
                String sql2 = "WHERE " + dateType.toString() + " BETWEEN :begin AND :end AND detailRecord.subsidiaryAccount = :subsidiaryAccount ORDER BY " + dateType.toString() + " ASC";
                sql.append(sql1).append(sql2);
                detailRecordList = em.createQuery(sql.toString())
                        .setParameter("begin", begin)
                        .setParameter("end", end)
                        .setParameter("subsidiaryAccount", subsidiaryAccount)
                        .setMaxResults(range[1] - range[0])
                        .setFirstResult(range[0])
                        .getResultList();
//                return detailRecordList;

            }
        }
        return detailRecordList;
    }

    /**
     * 不分页获取数据
     * @param authenticatedUser
     * @param begin
     * @param end
     * @param queryByEnterDate
     * @param area
     * @param subsidiaryAccount
     * @return
     */
    public List<DetailRecord> getByEntryOrOccurDate(Person authenticatedUser, Date begin, Date end, boolean queryByEnterDate, Area area, SubsidiaryAccount subsidiaryAccount){
        {
            List<DetailRecord> detailRecordList;

            // 如果未指明区域，则返回当前loginUser下的所有区域下的内容
            if (area == null) {
                detailRecordList = getByEntryOrOccurDate(authenticatedUser, begin, end, queryByEnterDate);
//            return detailRecordList;
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
                            .setParameter("area", area)
                            .getResultList();
//                return detailRecordList;


                    // 如果指明了明细账户，则返回当前loginUser下选定明细账户下的内容
                } else {
                    String sql1 = "SELECT detailRecord FROM DetailRecord detailRecord ";
                    String sql2 = "WHERE " + dateType.toString() + " BETWEEN :begin AND :end AND detailRecord.subsidiaryAccount = :subsidiaryAccount ORDER BY " + dateType.toString() + " ASC";
                    sql.append(sql1).append(sql2);
                    detailRecordList = em.createQuery(sql.toString())
                            .setParameter("begin", begin)
                            .setParameter("end", end)
                            .setParameter("subsidiaryAccount", subsidiaryAccount)
                            .getResultList();
//                return detailRecordList;

                }
            }
            return detailRecordList;
        }
    }

    /**
     * 取得初始余额对象
     * @param authenticatedUser
     * @param begin
     * @param area
     * @param subsidiaryAccount
     * @return
     */
    public FirstBalance getBeginningBalance(Person authenticatedUser, Date begin, Area area, SubsidiaryAccount subsidiaryAccount) {
        List<Object[]> results;

        // 如果未指明区域，则返回当前loginUser下的所有区域下的内容
        if (area == null) {
            String sql = "SELECT sum(detailRecord.amount) as TOTAIL, detailRecord.direction " +
                    "FROM DetailRecord detailRecord " +
                    "JOIN detailRecord.subsidiaryAccount subsidiaryAccount " +
                    "JOIN subsidiaryAccount.area area, IN(area.personList) person " +
                    "WHERE detailRecord.occurDate < :begin AND person = :user " +
                    "GROUP BY detailRecord.direction " +
                    "ORDER BY detailRecord.direction ASC";
            results = em.createQuery(sql)
                    .setParameter("begin", begin)
                    .setParameter("user", authenticatedUser)
                    .getResultList();
        } else {

            // 如果未指明明细账户，则返回当前loginUser下选定区域下的内容
            if (subsidiaryAccount == null) {
                String sql = "SELECT sum(detailRecord.amount) as TOTAIL, detailRecord.direction " +
                        "FROM DetailRecord detailRecord " +
                        "JOIN detailRecord.subsidiaryAccount subsidiaryAccount " +
                        "WHERE detailRecord.occurDate < :begin AND subsidiaryAccount.area = :area " +
                        "GROUP BY detailRecord.direction " +
                        "ORDER BY detailRecord.direction ASC";
                results = em.createQuery(sql)
                        .setParameter("begin", begin)
                        .setParameter("area", area)
                        .getResultList();


                // 如果指明了明细账户，则返回当前loginUser下选定明细账户下的内容
            } else {
                String sql = "SELECT sum(detailRecord.amount) as TOTAIL, detailRecord.direction " +
                        "FROM DetailRecord detailRecord " +
                        "WHERE detailRecord.occurDate < :begin AND detailRecord.subsidiaryAccount = :subsidiaryAccount " +
                        "GROUP BY detailRecord.direction " +
                        "ORDER BY detailRecord.direction ASC";
                results = em.createQuery(sql)
                        .setParameter("begin", begin)
                        .setParameter("subsidiaryAccount", subsidiaryAccount)
                        .getResultList();

            }

        }
        if (results.size() == 0) {
            return null;
        }
        FirstBalance firstBalance = new FirstBalance();
        if (results.size() == 1) {
            if ((Integer) (results.get(0)[1]) == 0) {
                firstBalance.setDebitTotal((BigDecimal) results.get(0)[0]);
                firstBalance.setCreditTotal(BigDecimal.ZERO);
            } else {
                firstBalance.setDebitTotal(BigDecimal.ZERO);
                firstBalance.setCreditTotal((BigDecimal) results.get(0)[0]);
            }
        } else {
            firstBalance.setDebitTotal((BigDecimal) results.get(0)[0]);
            firstBalance.setCreditTotal((BigDecimal) results.get(1)[0]);
        }
        return firstBalance;
    }

    /**
     * 封装账簿数据
     *
     * @param firstBalance     　期初余额
     * @param detailRecordList 　发生额集合
     * @return 包含期初余额、发生额（包含余额）、本月合计、累计的DetailRecord对象集合
     * 累计：指查询期间的借方合计及贷方合计（期末余额　＝　期初余额　＋　累计借方　－　累计贷方）。
     */
    public List<DetailRecord> setBalanceForDetailRecord(FirstBalance firstBalance, List<DetailRecord> detailRecordList) {
        List<DetailRecord> detailRecords = new ArrayList<>();
        // 保存上一条记录的年、月
        int prevYear = -1;
        int prevMonth = -1;
        // 保存当月合计数
        BigDecimal debitTotal = BigDecimal.ZERO;
        BigDecimal creditTotal = BigDecimal.ZERO;
        // 保存上一个月的累计数
        BigDecimal prevDebitTotal = BigDecimal.ZERO;
        BigDecimal prevCreditTotal = BigDecimal.ZERO;
        // 保存上一条记录的余额，初始为零
        BigDecimal previousBalance = BigDecimal.ZERO;
        // 保存记录的顺序号
        int index = 1;

        // 如果有期初余额，设置为第一条记录。
        if (firstBalance != null) {
            DetailRecord first = new DetailRecord();
            first.setSummary(ResourceBundle.getBundle(BUNDLE).getString("FirstBalance_Summary"));
            // 如果余额小于零，则设置余额方向为贷方，金额为正数。
            if (firstBalance.getBalance().compareTo(BigDecimal.ZERO) == -1) {
                first.setBalanceDirection(1);
                first.setBalance(firstBalance.getBalance().abs());
            } else {
                first.setBalanceDirection(0);
                first.setBalance(firstBalance.getBalance());
            }
            // 期初余额设置为第一条记录
            detailRecords.add(first);
            // 期初余额保存在变量中，用于下一条记录计算余额
            previousBalance = previousBalance.add(firstBalance.getBalance());
        }

        // 遍历传入的每一条记录
        for (DetailRecord d : detailRecordList) {
            //　设置日历为当前记录的业务发生时间，并取得年度及月份。
            Calendar cal = new Calendar.Builder().setInstant(d.getOccurDate()).build();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);

            // 比较当前记录年度及月份与上一条记录是否相等，如果相等
            if (prevYear == year && prevMonth == month){
                // 当前记录是否是借方发生额
                if (d.getDirection() == 0){
                    // 设置：当前余额　＝　前一条记录的余额　＋　当前记录的发生额
                    d.setBalance(previousBalance.add(d.getAmount()));
                    // 设置：当前借方发生额
                    d.setDebitAmount(d.getAmount());
                    // 设置：本月合计　＝　本月合计　＋　当前发生额
                    debitTotal = debitTotal.add(d.getAmount());
                }else{
                    // 设置：当前余额　＝　前一条记录的余额　－　当前记录的发生额
                    d.setBalance(previousBalance.subtract(d.getAmount()));
                    // 设置：当前贷方发生额
                    d.setCreditAmount(d.getAmount());
                    // 设置：本月合计　＝　本月合计　＋　当前发生额
                    creditTotal = creditTotal.add(d.getAmount());
                }
                // 当前记录的年度及月份赋值到中间变量中，用于同下一条记录比较
                prevYear = year;
                prevMonth = month;

            //  如果当前记录年度或月份与上一条记录不相等
            }else{
                // 查询的记录中，第一个月前不设置本月合计及累计
                if((prevYear != -1) && (prevMonth != -1)) {
                    // 插入并设置本月合计记录
                    DetailRecord totalThisMonth = new DetailRecord();
                    totalThisMonth.setSummary(ResourceBundle.getBundle(BUNDLE).getString("TotalThisMonth"));
                    totalThisMonth.setDebitAmount(debitTotal);
                    totalThisMonth.setCreditAmount(creditTotal);
                    // 清除余额方向
                    totalThisMonth.setBalanceDirection(-1);
                    detailRecords.add(totalThisMonth);

                    // 插入并设置本月累计记录
                    DetailRecord cumulative = new DetailRecord();
                    cumulative.setSummary(ResourceBundle.getBundle(BUNDLE).getString("Cumulative"));
                    cumulative.setDebitAmount(prevDebitTotal.add(debitTotal));
                    cumulative.setCreditAmount(prevCreditTotal.add(creditTotal));
                    // 清除余额方向
                    cumulative.setBalanceDirection(-1);
                    detailRecords.add(cumulative);

                    // 当前月份的借方累计、贷方累计保存在变量中，用于下一月份累计的计算
                    prevDebitTotal = cumulative.getDebitAmount();
                    prevCreditTotal = cumulative.getCreditAmount();

                    // 重置本月借方、贷方合计为零
                    debitTotal = BigDecimal.ZERO;
                    creditTotal = BigDecimal.ZERO;

                    // 重置下一条记录的顺序号
                    index = 1;
                }

                // 当前记录是否是借方发生额
                if (d.getDirection() == 0){
                    // 设置：当前余额　＝　前一条记录的余额　＋　当前记录的发生额
                    d.setBalance(previousBalance.add(d.getAmount()));
                    // 设置：当前借方发生额
                    d.setDebitAmount(d.getAmount());
                    // 设置：本月合计　＝　本月合计　＋　当前发生额
                    debitTotal = d.getAmount();
                }else{
                    // 设置：当前余额　＝　前一条记录的余额　－　当前记录的发生额
                    d.setBalance(previousBalance.subtract(d.getAmount()));
                    // 设置：当前贷方发生额
                    d.setCreditAmount(d.getAmount());
                    // 设置：本月合计　＝　本月合计　＋　当前发生额
                    creditTotal = d.getAmount();
                }
                // 当前记录的年度及月份赋值到中间变量中，用于同下一条记录比较
                prevYear = year;
                prevMonth = month;
            }
            //　余额保存在变量中用于下一条记录余额计算
            previousBalance = d.getBalance();
            // 如果余额小于零，则设置余额方向为贷方，金额为正数。
            if (d.getBalance().compareTo(BigDecimal.ZERO) == -1) {
                d.setBalanceDirection(1);
                d.setBalance(d.getBalance().abs());
            } else {
                d.setBalanceDirection(0);
            }
            // 设置当前记录的顺序号
            d.setId(index);
            // 为下一条记录的顺序号赋值做准备
            index++;
            // 当前记录加入到账簿数据记录中
            detailRecords.add(d);
        }

        // 插入并设置查询记录中最后一个月的本月合计记录
        DetailRecord  totalThisMonth = new DetailRecord();
        totalThisMonth.setSummary(ResourceBundle.getBundle(BUNDLE).getString("TotalThisMonth"));
        totalThisMonth.setDebitAmount(debitTotal);
        totalThisMonth.setCreditAmount(creditTotal);
        // 清除余额方向
        totalThisMonth.setBalanceDirection(-1);
        detailRecords.add(totalThisMonth);

        // 插入并设置查询记录中最后一个月的累计记录
        DetailRecord  cumulative = new DetailRecord();
        cumulative.setSummary(ResourceBundle.getBundle(BUNDLE).getString("Cumulative"));
        cumulative.setDebitAmount(prevDebitTotal.add(debitTotal));
        cumulative.setCreditAmount(prevCreditTotal.add(creditTotal));
        // 清除余额方向
        cumulative.setBalanceDirection(-1);
        detailRecords.add(cumulative);

        return detailRecords;
    }
}
