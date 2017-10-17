package com.ctid.intelsmsapp.database.impl;

import com.ctid.intelsmsapp.database.CompanyDao;
import com.ctid.intelsmsapp.entity.Company;

import java.util.List;

/**
 * Company data-accessing object implementation
 * Created by Logan on 2017/10/16.
 */

public class CompanyDaoImpl implements CompanyDao {

    @Override
    public void save(Company company) {
        company.save();
    }

    @Override
    public List<Company> findAll() {
        return Company.listAll(Company.class);
    }

    @Override
    public void delete(Company company) {
        company.delete();
    }

    @Override
    public Company findByNumber(String number) {

        return Company.find(Company.class, "number = ?", number).get(0);
    }

    @Override
    public void deleteAll() {
        Company.deleteAll(Company.class);
    }
}
