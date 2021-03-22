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

/**
 * Created by wangshizhan on 2017/12/14.
 * Email:wangshizhan@supcon.com
 */

@Entity
public class AccountInfo extends BaseEntity {

    /**
     * {"userId":1001,"userName":"fukun","language":"zh_CN",
     * "staffId":1001,"staffName":"fukun","staffCode":"fukun",
     * "positionId":1000,"positionName":"A1001","positionCode":"A1001",
     * "departmentId":1001,"departmentName":"A1001","departmentCode":"A1001",
     * "companyId":1000,"companyName":"默认公司","companyCode":"defaultCompany","companyType":"UNIT",
     * "mobileFlag":null}
     */

    public long id;

    @Id
    public long userId;    //用户id
    public String userName; //用户名

    public long staffId;   //用户的人员id
    public String staffCode;   //用户的人员编码
    public String staffName; //用户的真实姓名
    @ToOne(joinProperty = "staffId")
    public ContactEntity staff; //公司

    public long departmentId;  //用户部门id
    public String departmentCode;  //用户部门code
    public String departmentName;  //用户部门id
    @ToOne(joinProperty = "departmentId")
    public DepartmentEntity department; //公司

    public long companyId;   //公司id
    public String companyName;//公司名称
    public String companyCode;//公司编码
    public String companyType;//公司类型
    @ToOne(joinProperty = "companyId")
    public CompanyEntity company; //公司
    
    public long positionId;    //岗位id
    public String positionName;    //岗位名称
    public String positionCode;


    public String password = "";
    public String ip = "";
    public String date = "";
    public long imageId;		//头像img id
//    public String imagePath;		//头像地址
    public String roleIds;		//角色Id
    public String roleNames;	//角色名字
    public String uuid;		//uuid
    public String loginTime;
    public long firstDepartmentId;      //第一部门id
    public String firstDepartmentName;  //第一部门名称
//    public String mobile;
//    public String email;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 2580956)
    private transient AccountInfoDao myDao;
    @Generated(hash = 1677442345)
    public AccountInfo(long id, long userId, String userName, long staffId, String staffCode,
            String staffName, long departmentId, String departmentCode, String departmentName,
            long companyId, String companyName, String companyCode, String companyType, long positionId,
            String positionName, String positionCode, String password, String ip, String date,
            long imageId, String roleIds, String roleNames, String uuid, String loginTime,
            long firstDepartmentId, String firstDepartmentName) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.staffId = staffId;
        this.staffCode = staffCode;
        this.staffName = staffName;
        this.departmentId = departmentId;
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
        this.companyId = companyId;
        this.companyName = companyName;
        this.companyCode = companyCode;
        this.companyType = companyType;
        this.positionId = positionId;
        this.positionName = positionName;
        this.positionCode = positionCode;
        this.password = password;
        this.ip = ip;
        this.date = date;
        this.imageId = imageId;
        this.roleIds = roleIds;
        this.roleNames = roleNames;
        this.uuid = uuid;
        this.loginTime = loginTime;
        this.firstDepartmentId = firstDepartmentId;
        this.firstDepartmentName = firstDepartmentName;
    }
    @Generated(hash = 1230968834)
    public AccountInfo() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getUserId() {
        return this.userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public long getStaffId() {
        return this.staffId;
    }
    public void setStaffId(long staffId) {
        this.staffId = staffId;
    }
    public String getStaffCode() {
        return this.staffCode;
    }
    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }
    public String getStaffName() {
        return this.staffName;
    }
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
    public long getDepartmentId() {
        return this.departmentId;
    }
    public void setDepartmentId(long departmentId) {
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
    public long getCompanyId() {
        return this.companyId;
    }
    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }
    public String getCompanyName() {
        return this.companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getCompanyCode() {
        return this.companyCode;
    }
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
    public String getCompanyType() {
        return this.companyType;
    }
    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }
    public long getPositionId() {
        return this.positionId;
    }
    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }
    public String getPositionName() {
        return this.positionName;
    }
    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
    public String getPositionCode() {
        return this.positionCode;
    }
    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getIp() {
        return this.ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public long getImageId() {
        return this.imageId;
    }
    public void setImageId(long imageId) {
        this.imageId = imageId;
    }
    public String getRoleIds() {
        return this.roleIds;
    }
    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }
    public String getRoleNames() {
        return this.roleNames;
    }
    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }
    public String getUuid() {
        return this.uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getLoginTime() {
        return this.loginTime;
    }
    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
    public long getFirstDepartmentId() {
        return this.firstDepartmentId;
    }
    public void setFirstDepartmentId(long firstDepartmentId) {
        this.firstDepartmentId = firstDepartmentId;
    }
    public String getFirstDepartmentName() {
        return this.firstDepartmentName;
    }
    public void setFirstDepartmentName(String firstDepartmentName) {
        this.firstDepartmentName = firstDepartmentName;
    }
    @Generated(hash = 210054657)
    private transient Long staff__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Keep
    public ContactEntity getStaff() {
        long __key = this.staffId;
        if (staff__resolvedKey == null || !staff__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            ContactEntityDao targetDao;
            if (daoSession == null) {
                targetDao = PLAApplication.dao().getContactEntityDao();
            }
            else {
                targetDao = daoSession.getContactEntityDao();
            }
            ContactEntity staffNew = targetDao.load(__key);
            synchronized (this) {
                staff = staffNew;
                staff__resolvedKey = __key;
            }
        }
        return staff;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1857965088)
    public void setStaff(@NotNull ContactEntity staff) {
        if (staff == null) {
            throw new DaoException(
                    "To-one property 'staffId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.staff = staff;
            staffId = staff.getStaffId();
            staff__resolvedKey = staffId;
        }
    }
    @Generated(hash = 340684718)
    private transient Long department__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Keep
    public DepartmentEntity getDepartment() {
        long __key = this.departmentId;
        if (department__resolvedKey == null || !department__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            DepartmentEntityDao targetDao;
            if (daoSession == null) {
                targetDao = PLAApplication.dao().getDepartmentEntityDao();
            }
            else {
                targetDao = daoSession.getDepartmentEntityDao();
            }
            DepartmentEntity departmentNew = targetDao.load(__key);
            synchronized (this) {
                department = departmentNew;
                department__resolvedKey = __key;
            }
        }
        return department;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 908944104)
    public void setDepartment(@NotNull DepartmentEntity department) {
        if (department == null) {
            throw new DaoException(
                    "To-one property 'departmentId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.department = department;
            departmentId = department.getId();
            department__resolvedKey = departmentId;
        }
    }
    @Generated(hash = 1496811699)
    private transient Long company__resolvedKey;
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
            throw new DaoException(
                    "To-one property 'companyId' has not-null constraint; cannot set to-one to null");
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
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1149363988)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAccountInfoDao() : null;
    }

   
    
}
