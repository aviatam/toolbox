package com.toolbox.selenium;

/**
 * Created by: aviat
 * Date: 5/6/2015
 */
public enum BrowserType {

    CHROME("chrome"),
    HTML_UNIT_DRIVER("HTML Unit Driver"),
    FIREFOX("firefox"),
    IE("internet explorer"),
    UNKNOWN("Unknown");

    private String name;

    BrowserType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
