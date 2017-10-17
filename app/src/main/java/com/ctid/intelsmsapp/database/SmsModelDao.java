package com.ctid.intelsmsapp.database;

import com.ctid.intelsmsapp.entity.Model;

import java.util.List;

/**
 * Sms model data-accessing interface
 * Created by Logan on 2017/10/16.
 */

public interface SmsModelDao {

    void save(Model smsModel);

    List<Model> findAll();

    void delete(Model model);

    void deleteAll();

}
