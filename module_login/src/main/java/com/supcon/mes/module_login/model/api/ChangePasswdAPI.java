package com.supcon.mes.module_login.model.api;

import com.app.annotation.apt.ContractFactory;
import com.supcon.mes.module_login.model.bean.ChangePasswdResultEntity;

/**
 * @Author xushiyun
 * @Create-time 8/2/19
 * @Pageage com.supcon.mes.module_login.model.api
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
@ContractFactory(entites = ChangePasswdResultEntity.class)
public interface ChangePasswdAPI {
    void changePasswd(String oldPass, String newPass);
}
