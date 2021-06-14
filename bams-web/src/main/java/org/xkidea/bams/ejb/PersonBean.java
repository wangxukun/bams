package org.xkidea.bams.ejb;

import org.xkidea.bams.entity.Area;
import org.xkidea.bams.entity.Person;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

@Stateless
public class PersonBean extends AbstractFacade<Person> {
    @PersistenceContext(unitName = "bamsPU")
    private EntityManager em;

    @EJB
    AreaBean areaBean;

    public PersonBean() {
        super(Person.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public void updateAreasOfPerson(Person person, Area area) {
        // TODO......
        dropArea(person,area);
    }

    public void addArea(Person person, Area area) {

        try {
            Person p = find(person.getId());
            Area a = areaBean.find(area.getId());
            p.addArea(a);
            a.addPerson(p);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }

    }

    public void dropArea(Person person, Area area) {

        try {
            Person p = find(person.getId());
            Area a = areaBean.find(area.getId());
            p.dropArea(a);
            a.dropPerson(p);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }

    }

}
