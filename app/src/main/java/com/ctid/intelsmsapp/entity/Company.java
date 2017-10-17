package com.ctid.intelsmsapp.entity;

import com.orm.SugarRecord;

public class Company extends SugarRecord {

    private int numId;
    private String number;
    private String title;
    private String icon;

    public Company() {

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getNumId() {
        return numId;
    }

    public void setNumId(int numId) {
        this.numId = numId;
    }

    public String toString() {
        return title + "-" + numId;
    }
}
