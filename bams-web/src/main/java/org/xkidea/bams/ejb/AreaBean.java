package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.Area;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class AreaBean extends AbstractFacade<Area> {
    @PersistenceContext(unitName = "bamsPU")
    private EntityManager em;

    public AreaBean() {
        super(Area.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
