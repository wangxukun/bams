package org.xkidea.bams.web.util;

public enum PageNavigation {

    CREATE("Create"),
    List("List"),
    EDIT("Edit"),
    VIEW("View"),
    INDEX("index");
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
