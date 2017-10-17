package com.ctid.intelsmsapp.database.impl;

import com.ctid.intelsmsapp.database.SdkMenuDao;
import com.ctid.intelsmsapp.entity.Menu;

import java.util.List;

/**
 * 菜单数据数据库操作实现类
 * Created by jiangyanjun on 2017/10/16.
 */

public class SdkMenuDaoImpl implements SdkMenuDao {

    @Override
    public void save(Menu menu) {
        menu.save();
    }

    @Override
    public List<Menu> findAll() {
        return Menu.listAll(Menu.class);
    }

    @Override
    public void delete(Menu menu) {
        menu.delete();
    }


    @Override
    public void deleteAll() {
        Menu.deleteAll(Menu.class);
    }
}
