package com.coamctech.bxloan.manager.common.httpclient;

public class BasicParameter {

    private String name;

    private String value;

    public BasicParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}