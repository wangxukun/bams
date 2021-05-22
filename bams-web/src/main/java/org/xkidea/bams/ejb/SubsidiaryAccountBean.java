package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.Area;
import org.xkidea.bams.entity.CategoryAccount;
import org.xkidea.bams.entity.SubsidiaryAccount;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class SubsidiaryAccountBean extends AbstractFacade<SubsidiaryAccount> {

    @EJB
    private AreaBean areaBean;
    @EJB
    private CategoryAccountBean categoryAccountBean;



    @PersistenceContext(unitName = "bamsPU")
    private EntityManager em;

    public SubsidiaryAccountBean() {
        super(SubsidiaryAccount.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Area> getAreas() {
        return areaBean.findAll();
    }

    public List<CategoryAccount> getCategoryAccounts() {
        return categoryAccountBean.findAll();
    }
}
