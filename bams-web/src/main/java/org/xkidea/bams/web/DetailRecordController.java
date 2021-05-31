package org.xkidea.bams.web;

import org.xkidea.bams.model.AccountQuery;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named(value = "detailRecordController")
@SessionScoped
public class DetailRecordController implements Serializable {
    private static final long serialVersionUID = -7176059911038677633L;
    private static final String BUNDLE = "bundles.Bundle";

    private AccountQuery accountQuery;

    public DetailRecordController() {
    }

    public AccountQuery getAccountQuery() {
        return accountQuery;
    }

    public void setAccountQuery(AccountQuery accountQuery) {
        this.accountQuery = accountQuery;
    }
}
