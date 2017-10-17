package com.ctid.intelsmsapp.database;

import com.ctid.intelsmsapp.entity.Company;
import com.ctid.intelsmsapp.entity.Menu;

import java.util.List;

/**
 * Sdk Menu data-accessing interface
 * Created by Logan on 2017/10/16.
 */

public interface SdkMenuDao {

    void save(Menu menu);

    List<Menu> findAll();

    void delete(Menu menu);

    void deleteAll();

    List<Menu> findMenusByCompany(Company company);
}
