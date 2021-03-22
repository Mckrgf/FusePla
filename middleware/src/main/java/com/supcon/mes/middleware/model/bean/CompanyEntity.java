package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by wangshizhan on 2019/12/4
 * Email:wangshizhan@supcom.com
 */
@Entity
public class CompanyEntity extends BaseEntity {

    /**
     * {
     *       "id": 1000,
     *       "name": "默认公司",
     *       "code": "defaultCompany",
     *       "parent": null,
     *       "sort": 0,
     *       "description": null,
     *       "entityname": null,
     *       "customerField1": null,
     *       "customerField2": null,
     *       "children": null,
     *       "valid": true,
     *       "fullname": null,
     *       "type": "UNIT",
     *       "uuid": "02f3f2bd-dfa5-4498-bd77-909d1ce693fd"
     *     }
     */
    @Id
    public Long id;
    public String name;
    public String code;
    public String fullname;
    @Generated(hash = 1940563811)
    public CompanyEntity(Long id, String name, String code, String fullname) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.fullname = fullname;
    }
    @Generated(hash = 1762635696)
    public CompanyEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getFullname() {
        return this.fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

}
