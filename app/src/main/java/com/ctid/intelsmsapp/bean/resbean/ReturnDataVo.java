package com.ctid.intelsmsapp.bean.resbean;

import java.io.Serializable;
import java.util.List;

/**
 * 获取服务器json数据源，对应实体类
 */
public class ReturnDataVo implements Serializable {

    private static final long serialVersionUID = 2101626743503255556L;
    private int numId;
    private String number;
    private String title;
    private String icon;
    private List<SmsModel> model;
    private List<SdkMenu> menu;

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

    public List<SmsModel> getModel() {
        return model;
    }

    public void setModel(List<SmsModel> model) {
        this.model = model;
    }

    public List<SdkMenu> getMenu() {
        return menu;
    }

    public void setMenu(List<SdkMenu> menu) {
        this.menu = menu;
    }

    public int getNumId() {
        return numId;
    }

    public void setNumId(int numId) {
        this.numId = numId;
    }
}
