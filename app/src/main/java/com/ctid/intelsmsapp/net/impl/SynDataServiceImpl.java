package com.ctid.intelsmsapp.net.impl;

import com.ctid.intelsmsapp.bean.resbean.ReturnDataVo;
import com.ctid.intelsmsapp.bean.resbean.SdkMenu;
import com.ctid.intelsmsapp.bean.resbean.SmsModel;
import com.ctid.intelsmsapp.database.CompanyDao;
import com.ctid.intelsmsapp.database.SdkMenuDao;
import com.ctid.intelsmsapp.database.SmsModelDao;
import com.ctid.intelsmsapp.database.impl.CompanyDaoImpl;
import com.ctid.intelsmsapp.database.impl.SdkMenuDaoImpl;
import com.ctid.intelsmsapp.database.impl.SmsModelDaoImpl;
import com.ctid.intelsmsapp.entity.Company;
import com.ctid.intelsmsapp.entity.Menu;
import com.ctid.intelsmsapp.entity.Model;
import com.ctid.intelsmsapp.enums.SysConstants;
import com.ctid.intelsmsapp.net.ISynDataService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据同步实现类
 * Created by jiangyanjun on 2017/10/16.
 */
public class SynDataServiceImpl implements ISynDataService {
    /**
     * 同步平台数据到本地数据库
     *
     * @throws Exception
     */
    @Override
    public boolean autoSysPlatDataToLocalDB() throws Exception {
        boolean flag = true;
        List<ReturnDataVo> dataList = null;
        try {
            dataList = this.downLoadAllDataFromIntelSmsPlat();
            if (dataList != null) {
                this.synDataToLocalDB(dataList);
            } else {
                flag = false;
                System.out.println("同步程序未获取到平台数据！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            System.out.println("同步程序发送异常：" + e.getMessage());
        }
        return flag;
    }

    /**
     * 向短信数据平台发起请求，获取全量数据
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<ReturnDataVo> downLoadAllDataFromIntelSmsPlat() throws Exception {
        List<ReturnDataVo> dataList = null;
        HttpControllerServiceImpl conn = null;
        try {
            //发起请求，获取数据
            conn = new HttpControllerServiceImpl();
            String synData = conn.executeHttpsPost(SysConstants.INTEL_SMS_SERVERS_URL, null);
            System.out.println("同步数据请求结果为：" + synData);

            //String json数据转换为List<ReturnDataVo>
            Gson gson = new Gson();
            dataList = gson.fromJson(synData, new TypeToken<List<ReturnDataVo>>() {
            }.getType());
        } catch (Exception e) {
            //	LogUtil.d("http请求异常信息："+e.getMessage());
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * 同步平台数据到本地数据库
     *
     * @param dataList 平台数据
     * @throws Exception
     */
    @Override
    public void synDataToLocalDB(List<ReturnDataVo> dataList) throws Exception {
        //实例化数据库操作类
        CompanyDao companyDao = new CompanyDaoImpl();
        SdkMenuDao sdkMenuDao = new SdkMenuDaoImpl();
        SmsModelDao smsModelDao = new SmsModelDaoImpl();

        //由于未全量数据，同步器先删除本地数据
        companyDao.deleteAll();
        sdkMenuDao.deleteAll();
        smsModelDao.deleteAll();

        //同步数据到本地库
        for (ReturnDataVo data : dataList) {
            try {
                this.saveCompany(data,companyDao);
                this.saveMenus(data,sdkMenuDao);
                this.saveSmsModels(data,smsModelDao);

            } catch (Exception e) {
                System.out.println("数据保存异常，Number=" + data.getNumber());
            }
        }


    }

    /**
     * 生成商户实体
     *
     * @param data
     * @return
     */
    public void saveCompany(ReturnDataVo data, CompanyDao companyDao) {
        Company company = new Company();
        company.setIcon(data.getIcon());
        company.setNumber(data.getNumber());
        company.setNumId(data.getNumId());
        company.setTitle(data.getTitle());
        companyDao.save(company);
    }

    /**
     * 生成菜单对象List
     *
     * @param data
     * @return
     */
    public void saveMenus(ReturnDataVo data, SdkMenuDao sdkMenuDao) {
        List<SdkMenu> reqMenus = data.getMenu();
        List<Menu> menuList = null;
        if (reqMenus != null) {
            menuList = new ArrayList<Menu>();
            for (SdkMenu s : reqMenus) {
                try {
                    Menu menu = new Menu();
                    menu.setCompanyNumId(data.getNumId());
                    menu.setMenuId(s.getMenuId());
                    menu.setMenuLevel(s.getMenuLevel());
                    menu.setMenuName(s.getMenuName());
                    menu.setMenuParent(s.getMenuParent());
                    menu.setMenuSort(s.getMenuSort());
                    menu.setMenuUrl(s.getMenuUrl());
                    menu.setNumber(s.getNumber());
                    sdkMenuDao.save(menu);
//                    menuList.add(menu);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("生成Menu异常：Number=" + data.getNumber() + ",MenuName=" + s.getMenuName() + ",getMenuId=" + s.getMenuId());
                }
            }

        }
    }

    /**
     * 生成模板对象List
     * @param data
     * @return
     */
    public void saveSmsModels(ReturnDataVo data, SmsModelDao smsModelDao) {
        List<SmsModel> reqSmsModels = data.getModel();
        List<Model> menuList = null;
        if (reqSmsModels != null) {
            menuList = new ArrayList<Model>();
            for (SmsModel m : reqSmsModels) {
                try {
                    Model model = new Model();
                    model.setCompanyNumId(data.getNumId());
                    model.setNumber(m.getNumber());
                    model.setContent(m.getContent());
                    model.setModId(m.getModId());
                    model.setReg(m.getReg());
                    model.setRegCfg(m.getRegCfg());
                    model.setRegGroup(m.getRegGroup());
                    smsModelDao.save(model);
//                    menuList.add(model);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("生成model异常，：Number=" + data.getNumber() + ",getContent=" + m.getContent() + ",getNumber=" + m.getNumber() + "cfg=" + m.getRegCfg());
                }
            }
        }

    }
}
