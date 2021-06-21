package org.xkidea.bams.web.util;

public enum PageNavigation {

    CREATE("Create"),
    LIST("List"),
    EDIT("Edit"),
    VIEW("View"),
    INDEX("index"),
    SYSTEM_MANAGE_HOME("/admin/index"),
    ACCOUNT_MANAGE_HOME("/account/index"),
    ACCOUNT_AUTHORIZATION("/authorization/treasurer/Authorization"),
    ACCOUNTANT_AREAS_ASSIGN("/authorization/accountant/assign/List"),
    CUSTOMER_AREAS_ASSIGN("/authorization/customer/assign/List"),
    SETTING_HOME("/setting/index");
    private String text;

    PageNavigation(final String s) {
        this.text = s;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
