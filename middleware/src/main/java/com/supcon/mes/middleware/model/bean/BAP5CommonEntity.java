package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2019/11/12
 * Email:wangshizhan@supcom.com
 */
public class BAP5CommonEntity<T> extends BaseEntity {

    public int code;
    public boolean success;
    public T data;
    public String msg;

}
