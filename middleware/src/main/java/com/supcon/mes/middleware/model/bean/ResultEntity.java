package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2017/8/15.
 */

public class ResultEntity extends BaseEntity {

    public boolean success;

    public String errMsg;

public static final ResultEntity createSuccess() {
    ResultEntity resultEntity = new ResultEntity();
    resultEntity.success = true;
    return resultEntity;
}

    public static final ResultEntity createSuccess(String mes) {
        ResultEntity resultEntity = createSuccess();
        resultEntity.errMsg = mes;
        return resultEntity;
    }

    public static final ResultEntity createError() {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.success = false;
        return resultEntity;
    }

    public static final ResultEntity createError(String errMsg) {
        ResultEntity resultEntity = createError();
        resultEntity.errMsg = errMsg;
        return resultEntity;
    }
}
