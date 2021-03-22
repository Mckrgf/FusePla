package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.middleware.model.bean.TokenEntity;

/**
 * Created by wangshizhan on 2019/12/9
 * Email:wangshizhan@supcom.com
 */
@ContractFactory(entites = TokenEntity.class)
public interface TokenAPI {

    void getAccessToken(String userName, String password);

}
