package com.supcon.mes.module_login.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_login.model.bean.LoginEntity;

/**
 * Created by wangshizhan on 2017/12/28.
 * Email:wangshizhan@supcon.com
 */
@ContractFactory(entites = {LoginEntity.class})
public interface LoginAPI {
    void dologin(String username, String pwd);
}

