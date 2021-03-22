package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2020/4/11
 * Email:wangshizhan@supcom.com
 */
public class ObjectEntity extends BaseEntity {

    public Long id;

    public String name;
    public String code;


    public ObjectEntity(Long id){
        this.id = id;
    }

}
