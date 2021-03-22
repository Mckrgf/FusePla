package com.supcon.mes.middleware.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.common.com_http.NullEntity;

/**
 * Created by wangshizhan on 2017/12/27.
 * Email:wangshizhan@supcon.com
 */
@ContractFactory(entites = {NullEntity.class})
public interface LogoutAPI {
    void logout();
}
