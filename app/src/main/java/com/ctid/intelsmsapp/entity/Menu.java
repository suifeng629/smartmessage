package com.ctid.intelsmsapp.entity;

import com.orm.SugarRecord;

public class Menu extends SugarRecord {
    /**
     *
     */
    private int menuId;
    private String number;
    private int menuLevel;
    private int menuSort;
    private int menuParent;
    private String menuName;
    private String menuUrl;
    private int companyNumId;

    public Menu() {

    }

    public int getCompanyNumId() {
        return companyNumId;
    }

    public void setCompanyNumId(int companyNumId) {
        this.companyNumId = companyNumId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(int menuLevel) {
        this.menuLevel = menuLevel;
    }

    public int getMenuSort() {
        return menuSort;
    }

    public void setMenuSort(int menuSort) {
        this.menuSort = menuSort;
    }

    public int getMenuParent() {
        return menuParent;
    }

    public void setMenuParent(int menuParent) {
        this.menuParent = menuParent;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }
}
