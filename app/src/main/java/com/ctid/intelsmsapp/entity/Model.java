package com.ctid.intelsmsapp.entity;

import com.orm.SugarRecord;

public class Model extends SugarRecord {

    private int modId;
    private String number;
    private String content;
    private String reg;
    private String regCfg;
    private String regGroup;
    private int companyNumId;

    public Model() {

    }

    public int getCompanyNumId() {
        return companyNumId;
    }

    public void setCompanyNumId(int companyNumId) {
        this.companyNumId = companyNumId;
    }

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
