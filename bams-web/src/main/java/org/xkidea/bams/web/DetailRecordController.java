package org.xkidea.bams.web;

import org.xkidea.bams.model.AccountQuery;
import org.xkidea.bams.web.util.JsfUtil;
import org.xkidea.bams.web.util.PageNavigation;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named(value = "detailRecordController")
@SessionScoped
public class DetailRecordController implements Serializable {
    private static final long serialVersionUID = -7176059911038677633L;
    private static final String BUNDLE = "bundles.Bundle";

    private AccountQuery current;

    public DetailRecordController() {
    }

    public AccountQuery getSelected() {
        if (current == null) {
            current = new AccountQuery();
            current.setStartDate(JsfUtil.getFirstDayOfThisYear());
            current.setEndDate(JsfUtil.getToday());
        }
        return current;
    }

    public PageNavigation query() {
        return PageNavigation.CREATE;
    }
}