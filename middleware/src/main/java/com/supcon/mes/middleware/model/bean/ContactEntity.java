package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.PLAApplication;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

/**
 * Created by wangshizhan on 2019/12/3
 * Email:wangshizhan@supcom.com
 */
@Entity
public class ContactEntity extends BaseEntity {

    /**
     * {
     *         "picturePath": null,
     *         "departmentCodes": [{"code":"test11","fullPathName":"部门测试","cid":1000}],
     *         "roomNumber": null,
     *         "code": "fjh",
     *         "virtualMobile": null,
     *         "jobTitle": null,
     *         "sex": "SEX_NATURE/MALE",
     *         "mobile": "15067111456",
     *         "memo": null,
     *         "sort": 2147483647,
     *         "positionName": "开发",
     *         "extensionNumber": null,
     *         "name": "方佳涵",
     *         "mneCode": "fangjiahan",
     *         "lineNumber": null,
     *         "email": "122@qq.com",
     *         "staffId": 1001
     *       }
     */
    public void setDepartmentCodes(List<DepartmentEntity> departments) {
        this.departments = departments;
        if(departments != null && departments.size()!=0){
            DepartmentEntity department = departments.get(0);
            if(department!=null){
                departmentCode = department.code;
                fullPathName = department.fullPathName;
                companyId  = department.cid;

            }
        }
    }



    /** To-one relationship, resolved on first access. */
    @Keep
    public DepartmentEntity getDepartment() {
        String __key = this.departmentCode;

        if(departmentCode==null){
           return null;
        }

        if (department__resolvedKey == null || !department__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            DepartmentEntityDao targetDao;
            if (daoSession == null) {
                targetDao = PLAApplication.dao().getDepartmentEntityDao();
            }
            else {
                targetDao = daoSession.getDepartmentEntityDao();
            }
            try {
                DepartmentEntity departmentNew = targetDao.queryBuilder().where(DepartmentEntityDao.Properties.Code.eq(departmentCode)).unique();
                synchronized (this) {
                    department = departmentNew;
                    department__resolvedKey = __key;
                }
            }
            catch (Exception e){

            }

        }
        return department;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Keep
    public void setDepartment(DepartmentEntity department) {
        synchronized (this) {
            this.department = department;
            departmentCode = department == null ? null : department.getCode();
            department__resolvedKey = departmentCode;
        }
    }


    @Id
    public Long staffId;
    public String code;
    public String name;
    public String positionName;

    public Long userId;

    public long companyId;

    public String departmentId;
    public String departmentCode;
    public String departmentName;
    public String fullPathName;

    @Transient
    public List<DepartmentEntity> departments;

    public String jobTitle;
    public String mneCode;
    public String picturePath;
    public String sex;
    public String mobile;
    public String email;

    public String memo;

    public String searchPinyin;

    public long sort;
    public long documentId;
    public long updateTime;
    public String ip = PLAApplication.getIp();

    @ToOne(joinProperty = "companyId")
    public CompanyEntity company;

    @Transient
    public DepartmentEntity department;


    private transient String department__resolvedKey;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1284084573)
    private transient ContactEntityDao myDao;


    @Generated(hash = 971199512)
    public ContactEntity(Long staffId, String code, String name, String positionName, Long userId, long companyId, String departmentId,
            String departmentCode, String departmentName, String fullPathName, String jobTitle, String mneCode, String picturePath, String sex,
            String mobile, String email, String memo, String searchPinyin, long sort, long documentId, long updateTime, String ip) {
        this.staffId = staffId;
        this.code = code;
        this.name = name;
        this.positionName = positionName;
        this.userId = userId;
        this.companyId = companyId;
        this.departmentId = departmentId;
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
        this.fullPathName = fullPathName;
        this.jobTitle = jobTitle;
        this.mneCode = mneCode;
        this.picturePath = picturePath;
        this.sex = sex;
        this.mobile = mobile;
        this.email = email;
        this.memo = memo;
        this.searchPinyin = searchPinyin;
        this.sort = sort;
        this.documentId = documentId;
        this.updateTime = updateTime;
        this.ip = ip;
    }



    @Generated(hash = 393979869)
    public ContactEntity() {
    }

    @Generated(hash = 1496811699)
    private transient Long company__resolvedKey;






    public Long getStaffId() {
        return this.staffId;
    }



    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }



    public String getCode() {
        return this.code;
    }



    public void setCode(String code) {
        this.code = code;
    }



    public String getName() {
        return this.name;
    }



    public void setName(String name) {
        this.name = name;
    }



    public String getPositionName() {
        return this.positionName;
    }



    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }



    public long getCompanyId() {
        return this.companyId;
    }



    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }



    public String getDepartmentId() {
        return this.departmentId;
    }



    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }



    public String getDepartmentCode() {
        return this.departmentCode;
    }



    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }



    public String getDepartmentName() {
        return this.departmentName;
    }



    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }



    public String getFullPathName() {
        return this.fullPathName;
    }



    public void setFullPathName(String fullPathName) {
        this.fullPathName = fullPathName;
    }



    public String getJobTitle() {
        return this.jobTitle;
    }



    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }



    public String getMneCode() {
        return this.mneCode;
    }



    public void setMneCode(String mneCode) {
        this.mneCode = mneCode;
    }



    public String getPicturePath() {
        return this.picturePath;
    }



    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }



    public String getSex() {
        return this.sex;
    }



    public void setSex(String sex) {
        this.sex = sex;
    }



    public String getMobile() {
        return this.mobile;
    }



    public void setMobile(String mobile) {
        this.mobile = mobile;
    }



    public String getEmail() {
        return this.email;
    }



    public void setEmail(String email) {
        this.email = email;
    }



    public String getMemo() {
        return this.memo;
    }



    public void setMemo(String memo) {
        this.memo = memo;
    }



    public String getSearchPinyin() {
        return this.searchPinyin;
    }



    public void setSearchPinyin(String searchPinyin) {
        this.searchPinyin = searchPinyin;
    }



    public long getSort() {
        return this.sort;
    }



    public void setSort(long sort) {
        this.sort = sort;
    }



    public long getUpdateTime() {
        return this.updateTime;
    }



    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }



    /** To-one relationship, resolved on first access. */
    @Keep
    public CompanyEntity getCompany() {
        long __key = this.companyId;
        if (company__resolvedKey == null || !company__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            CompanyEntityDao targetDao;
            if (daoSession == null) {
                targetDao = PLAApplication.dao().getCompanyEntityDao();
            }
            else {
                targetDao = daoSession.getCompanyEntityDao();
            }
            CompanyEntity companyNew = targetDao.load(__key);
            synchronized (this) {
                company = companyNew;
                company__resolvedKey = __key;
            }
        }
        return company;
    }



    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1533425291)
    public void setCompany(@NotNull CompanyEntity company) {
        if (company == null) {
            throw new DaoException("To-one property 'companyId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.company = company;
            companyId = company.getId();
            company__resolvedKey = companyId;
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



    public long getDocumentId() {
        return this.documentId;
    }



    public void setDocumentId(long documentId) {
        this.documentId = documentId;
    }



    public String getIp() {
        return this.ip;
    }



    public void setIp(String ip) {
        this.ip = ip;
    }



    public Long getUserId() {
        return this.userId;
    }



    public void setUserId(Long userId) {
        this.userId = userId;
    }



    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 866991334)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getContactEntityDao() : null;
    }
}
