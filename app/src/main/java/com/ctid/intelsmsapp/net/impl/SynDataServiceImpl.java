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
import com.ctid.intelsmsapp.utils.FileUtil;
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
     * @param dataSynType 数据同步类型，1 自动，2 手动， 3 内置数据
     *
     * @throws Exception
     */
    @Override
    public boolean autoSysPlatDataToLocalDB(int dataSynType) throws Exception {
        boolean flag = true;
        List<ReturnDataVo> dataList = null;
        try {
            //获取平台数据文件
            if(dataSynType== SysConstants.DATA_SYN_TYPE_HAND){
                dataList = this.downLoadAllDataFromIntelSmsPlat();//手动更新
            }else{
                dataList = this.downLoadAllDataFromLocal();// 本地加载
            }

            //平台数文件入库
            if (dataList != null) {
                this.synDataToLocalDB(dataList,dataSynType);
            } else {
                flag = false;
                System.out.println("同步程序未获取到平台数据！dataSynType="+dataSynType);
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            System.out.println("同步程序发送异常：dataSynType="+dataSynType+",info=" + e.getMessage());
        }
        return flag;
    }

    /**
     * 向短信数据平台发起请求，获取全量数据
     * @return
     * @throws Exception
     */
    private List<ReturnDataVo> downLoadAllDataFromIntelSmsPlat() throws Exception {
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

    private List<ReturnDataVo> downLoadAllDataFromLocal() throws Exception {
        List<ReturnDataVo> dataList = null;
        HttpControllerServiceImpl conn = null;
        try {
            //发起请求，获取数据
            conn = new HttpControllerServiceImpl();
            String synData = FileUtil.ReadFile("raw/initdata.json");
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
     *     /**
     * 同步平台数据到本地数据库
     * @param dataList 全量数据
     * @param dataSynType 数据同步类型，1 自动，2 手动， 3 内置数据
     * @throws Exception
     */
    private void synDataToLocalDB(List<ReturnDataVo> dataList, int dataSynType) throws Exception {
        //实例化数据库操作类
        CompanyDao companyDao = new CompanyDaoImpl();
        SdkMenuDao sdkMenuDao = new SdkMenuDaoImpl();
        SmsModelDao smsModelDao = new SmsModelDaoImpl();

        //1 .如果手动更新，直接删除库，再更新
        if(dataSynType == SysConstants.DATA_SYN_TYPE_HAND){
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
        }else{ //2.加载本地数据，如果本地库不存在，保存数据入库

            //2.1判断商户数据是否存在
            List<Company> companies = Company.listAll(Company.class);
            if(companies.isEmpty()){
                //同步商户数据到本地库
                for (ReturnDataVo data : dataList) {
                    try {
                        this.saveCompany(data,companyDao);
                    } catch (Exception e) {
                        System.out.println("本地商户数据保存异常，Number=" + data.getNumber());
                    }
                }
            }

            //2.2判断菜单数据是否存在
            List<Menu> menus = Menu.listAll(Menu.class);
            if(menus.isEmpty()){
                //同步菜单数据到本地库
                for (ReturnDataVo data : dataList) {
                    try {
                        this.saveMenus(data,sdkMenuDao);
                    } catch (Exception e) {
                        System.out.println("本地菜单数据保存异常，Number=" + data.getNumber());
                    }
                }
            }

            //2.3判断模板数据是否存在
            List<Model> models = Model.listAll(Model.class);
            if(models.isEmpty()){
                //同步模板数据到本地库
                for (ReturnDataVo data : dataList) {
                    try {
                        this.saveSmsModels(data,smsModelDao);
                    } catch (Exception e) {
                        System.out.println("本地模板数据保存异常，Number=" + data.getNumber());
                    }
                }
            }

        }



    }

    /**
     * 生成商户实体
     *
     * @param data
     * @return
     */
    private void saveCompany(ReturnDataVo data, CompanyDao companyDao) {
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
    private void saveMenus(ReturnDataVo data, SdkMenuDao sdkMenuDao) {
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
    private void saveSmsModels(ReturnDataVo data, SmsModelDao smsModelDao) {
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
