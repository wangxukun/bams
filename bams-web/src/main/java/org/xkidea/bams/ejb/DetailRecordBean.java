package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.DetailRecord;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
