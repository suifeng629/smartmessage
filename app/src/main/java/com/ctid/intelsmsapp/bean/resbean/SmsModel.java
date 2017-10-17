package com.ctid.intelsmsapp.bean.resbean;

import java.io.Serializable;

/**
 * 服务器json数据解析，短信模板实体
 */
public class SmsModel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5163800041360193422L;
    private int modId;
    private String number;
    private String content;
    private String reg;
    private String regCfg;
    private String regGroup;

    public int getModId() {
        return modId;
    }

    public void setModId(int modId) {
        this.modId = modId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getRegCfg() {
        return regCfg;
    }

    public void setRegCfg(String regCfg) {
        this.regCfg = regCfg;
    }

    public String getRegGroup() {
        return regGroup;
    }

    public void setRegGroup(String regGroup) {
        this.regGroup = regGroup;
    }

}
