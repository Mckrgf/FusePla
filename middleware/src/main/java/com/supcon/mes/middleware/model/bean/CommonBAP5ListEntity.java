package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

public class CommonBAP5ListEntity<T extends BaseEntity> extends BaseEntity {
    public int code;
    public String msg;
    public boolean success;
    public CommonBAPListEntity<T> data;
}
