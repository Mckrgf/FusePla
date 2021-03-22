package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

/**
 * @Author xushiyun
 * @Create-time 7/8/19
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */

@Entity
public class StaffEntity extends BaseEntity{
    /**
     * {
     *       "id": 1001,
     *       "entityname": null,
     *       "name": "方佳涵",
     *       "code": "fjh",
     *       "email": "122@qq.com",
     *       "mobilePhone": null,
     *       "im": null,
     *       "sort": 2147483647,
     *       "valid": true,
     *       "mainPosition": null,
     *       "customerField1": null,
     *       "customerField2": null,
     *       "sex": null,
     *       "uuid": "9e035b47-a884-4de3-a048-4abbfe0397f2"
     *     }
     */
    @Id
    public Long id;
    public String mainPosition;
    public String name;
    @ToOne
    public DepartmentEntity department;
    public int sort;
    public String uuid;
    public String mobilePhone;
    public String sex;
    public String email;
    public String code;

    public String ip;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 944745661)
    private transient StaffEntityDao myDao;

    @Generated(hash = 1177678008)
    public StaffEntity(Long id, String mainPosition, String name, int sort,
            String uuid, String mobilePhone, String sex, String email, String code,
            String ip) {
        this.id = id;
        this.mainPosition = mainPosition;
        this.name = name;
        this.sort = sort;
        this.uuid = uuid;
        this.mobilePhone = mobilePhone;
        this.sex = sex;
        this.email = email;
        this.code = code;
        this.ip = ip;
    }

    @Generated(hash = 623314536)
    public StaffEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMainPosition() {
        return this.mainPosition;
    }

    public void setMainPosition(String mainPosition) {
        this.mainPosition = mainPosition;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return this.sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Generated(hash = 1289914797)
    private transient boolean department__refreshed;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1516423709)
    public DepartmentEntity getDepartment() {
        if (department != null || !department__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DepartmentEntityDao targetDao = daoSession.getDepartmentEntityDao();
            targetDao.refresh(department);
            department__refreshed = true;
        }
        return department;
    }

    /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
    @Generated(hash = 133484279)
    public DepartmentEntity peakDepartment() {
        return department;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1553384241)
    public void setDepartment(DepartmentEntity department) {
        synchronized (this) {
            this.department = department;
            department__refreshed = true;
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
    @Generated(hash = 881921302)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStaffEntityDao() : null;
    }

}
