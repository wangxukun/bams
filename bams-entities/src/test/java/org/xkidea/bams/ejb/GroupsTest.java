package org.xkidea.bams.ejb;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xkidea.bams.entity.Groups;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import static org.junit.Assert.assertEquals;

public class GroupsTest {
    private EJBContainer ec;
    private Context ctx;

    public GroupsTest() {
    }

    @Before
    public void setUp(){
        ec = EJBContainer.createEJBContainer();
        ctx = ec.getContext();
    }

    @After
    public void tearDown() {
        if (ec != null) {
            ec.close();
        }
    }

    @Test
    public void testGroupsBean() throws Exception {
        Groups groups = new Groups();
        groups.setName("ADMINS");
        groups.setDescription("Administrator of the Bams");

        GroupsBean instance = (GroupsBean) ctx.lookup("java:global/classes/GroupsBean");
        instance.create(groups);
        String expResult = "hello world";
        String result = instance.sayHello();
        assertEquals(expResult,result);
    }
}
