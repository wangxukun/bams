package org.xkidea.bams.web.util;

public enum PageNavigation {

    CREATE("Create"),
    LIST("List"),
    EDIT("Edit"),
    VIEW("View"),
    INDEX("index"),
    SYSTEM_MANAGE_HOME("/admin/index"),
    ACCOUNT_MANAGE_HOME("/account/index");
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
