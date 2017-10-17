package com.ctid.intelsmsapp.database;

import com.ctid.intelsmsapp.entity.Company;

import java.util.List;

/**
 * Company data-accessing interface
 * Created by Logan on 2017/10/16.
 */

public interface CompanyDao {

    void save(Company company);

    List<Company> findAll();

    void delete(Company company);

    Company findByNumber(String number);

    void deleteAll();
}
