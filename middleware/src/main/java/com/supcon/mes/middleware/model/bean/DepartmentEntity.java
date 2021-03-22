package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by wangshizhan on 2018/7/12
 * Email:wangshizhan@supcom.com
 */
@Entity
public class DepartmentEntity extends BaseEntity implements Cloneable{

    /**
     * {
     *             "id": 1002,
     *             "name": "开发部1部",
     *             "manager": null,
     *             "code": "kfb01",
     *             "parent": null,
     *             "valid": true,
     *             "category": null,
     *             "company": null,
     *             "sort": 0,
     *             "entityName": null,
     *             "customerField1": null,
     *             "customerField2": "false",
     *             "children": null,
     *             "uuid": "26142841-1f71-4109-9e6b-47795797d740",
     *             "description": null,
     *             "parentId": 1000,
     *             "layRec": "1000-1002"
     *         }
     */

    @Id
    public Long id;
    public String name;
    public String code;
    public String searchPinyin;
    public String layRec;
    public Long parentId;
    public int sort;
    public String manager;
    public String fullPathName;
    public long cid;

    @Transient
    public ContactEntity staffInfo;

    @ToOne(joinProperty = "parentId")
    public DepartmentEntity parentDepartment;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 983847591)
    private transient DepartmentEntityDao myDao;

    @Override
    public DepartmentEntity clone() throws CloneNotSupportedException {
        return (DepartmentEntity) super.clone();
    }

    @Generated(hash = 380393756)
    public DepartmentEntity(Long id, String name, String code, String searchPinyin,
            String layRec, Long parentId, int sort, String manager,
            String fullPathName, long cid) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.searchPinyin = searchPinyin;
        this.layRec = layRec;
        this.parentId = parentId;
        this.sort = sort;
        this.manager = manager;
        this.fullPathName = fullPathName;
        this.cid = cid;
    }

    @Generated(hash = 768875561)
    public DepartmentEntity() {
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

    public String getSearchPinyin() {
        return this.searchPinyin;
    }

    public void setSearchPinyin(String searchPinyin) {
        this.searchPinyin = searchPinyin;
    }

    public String getLayRec() {
        return this.layRec;
    }

    public void setLayRec(String layRec) {
        this.layRec = layRec;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public int getSort() {
        return this.sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getManager() {
        return this.manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getFullPathName() {
        return this.fullPathName;
    }

    public void setFullPathName(String fullPathName) {
        this.fullPathName = fullPathName;
    }

    public long getCid() {
        return this.cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    @Generated(hash = 246542235)
    private transient Long parentDepartment__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1027856876)
    public DepartmentEntity getParentDepartment() {
        Long __key = this.parentId;
        if (parentDepartment__resolvedKey == null
                || !parentDepartment__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DepartmentEntityDao targetDao = daoSession.getDepartmentEntityDao();
            DepartmentEntity parentDepartmentNew = targetDao.load(__key);
            synchronized (this) {
                parentDepartment = parentDepartmentNew;
                parentDepartment__resolvedKey = __key;
            }
        }
        return parentDepartment;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1775711843)
    public void setParentDepartment(DepartmentEntity parentDepartment) {
        synchronized (this) {
            this.parentDepartment = parentDepartment;
            parentId = parentDepartment == null ? null : parentDepartment.getId();
            parentDepartment__resolvedKey = parentId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2098133619)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDepartmentEntityDao() : null;
    }
 
}
