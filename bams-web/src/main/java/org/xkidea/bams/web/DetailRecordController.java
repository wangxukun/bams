package org.xkidea.bams.web;

import org.xkidea.bams.model.AccountQuery;
import org.xkidea.bams.web.util.PageNavigation;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;

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
            current.setStartDate(new Date());
            current.setEndDate(new Date());
        }
        return current;
    }

    public PageNavigation query() {
        return PageNavigation.CREATE;
    }
}