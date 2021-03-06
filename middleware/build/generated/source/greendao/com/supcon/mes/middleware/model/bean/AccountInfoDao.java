package com.supcon.mes.middleware.model.bean;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ACCOUNT_INFO".
*/
public class AccountInfoDao extends AbstractDao<AccountInfo, Long> {

    public static final String TABLENAME = "ACCOUNT_INFO";

    /**
     * Properties of entity AccountInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", false, "ID");
        public final static Property UserId = new Property(1, long.class, "userId", true, "_id");
        public final static Property UserName = new Property(2, String.class, "userName", false, "USER_NAME");
        public final static Property StaffId = new Property(3, long.class, "staffId", false, "STAFF_ID");
        public final static Property StaffCode = new Property(4, String.class, "staffCode", false, "STAFF_CODE");
        public final static Property StaffName = new Property(5, String.class, "staffName", false, "STAFF_NAME");
        public final static Property DepartmentId = new Property(6, long.class, "departmentId", false, "DEPARTMENT_ID");
        public final static Property DepartmentCode = new Property(7, String.class, "departmentCode", false, "DEPARTMENT_CODE");
        public final static Property DepartmentName = new Property(8, String.class, "departmentName", false, "DEPARTMENT_NAME");
        public final static Property CompanyId = new Property(9, long.class, "companyId", false, "COMPANY_ID");
        public final static Property CompanyName = new Property(10, String.class, "companyName", false, "COMPANY_NAME");
        public final static Property CompanyCode = new Property(11, String.class, "companyCode", false, "COMPANY_CODE");
        public final static Property CompanyType = new Property(12, String.class, "companyType", false, "COMPANY_TYPE");
        public final static Property PositionId = new Property(13, long.class, "positionId", false, "POSITION_ID");
        public final static Property PositionName = new Property(14, String.class, "positionName", false, "POSITION_NAME");
        public final static Property PositionCode = new Property(15, String.class, "positionCode", false, "POSITION_CODE");
        public final static Property Password = new Property(16, String.class, "password", false, "PASSWORD");
        public final static Property Ip = new Property(17, String.class, "ip", false, "IP");
        public final static Property Date = new Property(18, String.class, "date", false, "DATE");
        public final static Property ImageId = new Property(19, long.class, "imageId", false, "IMAGE_ID");
        public final static Property RoleIds = new Property(20, String.class, "roleIds", false, "ROLE_IDS");
        public final static Property RoleNames = new Property(21, String.class, "roleNames", false, "ROLE_NAMES");
        public final static Property Uuid = new Property(22, String.class, "uuid", false, "UUID");
        public final static Property LoginTime = new Property(23, String.class, "loginTime", false, "LOGIN_TIME");
        public final static Property FirstDepartmentId = new Property(24, long.class, "firstDepartmentId", false, "FIRST_DEPARTMENT_ID");
        public final static Property FirstDepartmentName = new Property(25, String.class, "firstDepartmentName", false, "FIRST_DEPARTMENT_NAME");
    }

    private DaoSession daoSession;


    public AccountInfoDao(DaoConfig config) {
        super(config);
    }
    
    public AccountInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ACCOUNT_INFO\" (" + //
                "\"ID\" INTEGER NOT NULL ," + // 0: id
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 1: userId
                "\"USER_NAME\" TEXT," + // 2: userName
                "\"STAFF_ID\" INTEGER NOT NULL ," + // 3: staffId
                "\"STAFF_CODE\" TEXT," + // 4: staffCode
                "\"STAFF_NAME\" TEXT," + // 5: staffName
                "\"DEPARTMENT_ID\" INTEGER NOT NULL ," + // 6: departmentId
                "\"DEPARTMENT_CODE\" TEXT," + // 7: departmentCode
                "\"DEPARTMENT_NAME\" TEXT," + // 8: departmentName
                "\"COMPANY_ID\" INTEGER NOT NULL ," + // 9: companyId
                "\"COMPANY_NAME\" TEXT," + // 10: companyName
                "\"COMPANY_CODE\" TEXT," + // 11: companyCode
                "\"COMPANY_TYPE\" TEXT," + // 12: companyType
                "\"POSITION_ID\" INTEGER NOT NULL ," + // 13: positionId
                "\"POSITION_NAME\" TEXT," + // 14: positionName
                "\"POSITION_CODE\" TEXT," + // 15: positionCode
                "\"PASSWORD\" TEXT," + // 16: password
                "\"IP\" TEXT," + // 17: ip
                "\"DATE\" TEXT," + // 18: date
                "\"IMAGE_ID\" INTEGER NOT NULL ," + // 19: imageId
                "\"ROLE_IDS\" TEXT," + // 20: roleIds
                "\"ROLE_NAMES\" TEXT," + // 21: roleNames
                "\"UUID\" TEXT," + // 22: uuid
                "\"LOGIN_TIME\" TEXT," + // 23: loginTime
                "\"FIRST_DEPARTMENT_ID\" INTEGER NOT NULL ," + // 24: firstDepartmentId
                "\"FIRST_DEPARTMENT_NAME\" TEXT);"); // 25: firstDepartmentName
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ACCOUNT_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AccountInfo entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getUserId());
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(3, userName);
        }
        stmt.bindLong(4, entity.getStaffId());
 
        String staffCode = entity.getStaffCode();
        if (staffCode != null) {
            stmt.bindString(5, staffCode);
        }
 
        String staffName = entity.getStaffName();
        if (staffName != null) {
            stmt.bindString(6, staffName);
        }
        stmt.bindLong(7, entity.getDepartmentId());
 
        String departmentCode = entity.getDepartmentCode();
        if (departmentCode != null) {
            stmt.bindString(8, departmentCode);
        }
 
        String departmentName = entity.getDepartmentName();
        if (departmentName != null) {
            stmt.bindString(9, departmentName);
        }
        stmt.bindLong(10, entity.getCompanyId());
 
        String companyName = entity.getCompanyName();
        if (companyName != null) {
            stmt.bindString(11, companyName);
        }
 
        String companyCode = entity.getCompanyCode();
        if (companyCode != null) {
            stmt.bindString(12, companyCode);
        }
 
        String companyType = entity.getCompanyType();
        if (companyType != null) {
            stmt.bindString(13, companyType);
        }
        stmt.bindLong(14, entity.getPositionId());
 
        String positionName = entity.getPositionName();
        if (positionName != null) {
            stmt.bindString(15, positionName);
        }
 
        String positionCode = entity.getPositionCode();
        if (positionCode != null) {
            stmt.bindString(16, positionCode);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(17, password);
        }
 
        String ip = entity.getIp();
        if (ip != null) {
            stmt.bindString(18, ip);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(19, date);
        }
        stmt.bindLong(20, entity.getImageId());
 
        String roleIds = entity.getRoleIds();
        if (roleIds != null) {
            stmt.bindString(21, roleIds);
        }
 
        String roleNames = entity.getRoleNames();
        if (roleNames != null) {
            stmt.bindString(22, roleNames);
        }
 
        String uuid = entity.getUuid();
        if (uuid != null) {
            stmt.bindString(23, uuid);
        }
 
        String loginTime = entity.getLoginTime();
        if (loginTime != null) {
            stmt.bindString(24, loginTime);
        }
        stmt.bindLong(25, entity.getFirstDepartmentId());
 
        String firstDepartmentName = entity.getFirstDepartmentName();
        if (firstDepartmentName != null) {
            stmt.bindString(26, firstDepartmentName);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AccountInfo entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getUserId());
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(3, userName);
        }
        stmt.bindLong(4, entity.getStaffId());
 
        String staffCode = entity.getStaffCode();
        if (staffCode != null) {
            stmt.bindString(5, staffCode);
        }
 
        String staffName = entity.getStaffName();
        if (staffName != null) {
            stmt.bindString(6, staffName);
        }
        stmt.bindLong(7, entity.getDepartmentId());
 
        String departmentCode = entity.getDepartmentCode();
        if (departmentCode != null) {
            stmt.bindString(8, departmentCode);
        }
 
        String departmentName = entity.getDepartmentName();
        if (departmentName != null) {
            stmt.bindString(9, departmentName);
        }
        stmt.bindLong(10, entity.getCompanyId());
 
        String companyName = entity.getCompanyName();
        if (companyName != null) {
            stmt.bindString(11, companyName);
        }
 
        String companyCode = entity.getCompanyCode();
        if (companyCode != null) {
            stmt.bindString(12, companyCode);
        }
 
        String companyType = entity.getCompanyType();
        if (companyType != null) {
            stmt.bindString(13, companyType);
        }
        stmt.bindLong(14, entity.getPositionId());
 
        String positionName = entity.getPositionName();
        if (positionName != null) {
            stmt.bindString(15, positionName);
        }
 
        String positionCode = entity.getPositionCode();
        if (positionCode != null) {
            stmt.bindString(16, positionCode);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(17, password);
        }
 
        String ip = entity.getIp();
        if (ip != null) {
            stmt.bindString(18, ip);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(19, date);
        }
        stmt.bindLong(20, entity.getImageId());
 
        String roleIds = entity.getRoleIds();
        if (roleIds != null) {
            stmt.bindString(21, roleIds);
        }
 
        String roleNames = entity.getRoleNames();
        if (roleNames != null) {
            stmt.bindString(22, roleNames);
        }
 
        String uuid = entity.getUuid();
        if (uuid != null) {
            stmt.bindString(23, uuid);
        }
 
        String loginTime = entity.getLoginTime();
        if (loginTime != null) {
            stmt.bindString(24, loginTime);
        }
        stmt.bindLong(25, entity.getFirstDepartmentId());
 
        String firstDepartmentName = entity.getFirstDepartmentName();
        if (firstDepartmentName != null) {
            stmt.bindString(26, firstDepartmentName);
        }
    }

    @Override
    protected final void attachEntity(AccountInfo entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 1);
    }    

    @Override
    public AccountInfo readEntity(Cursor cursor, int offset) {
        AccountInfo entity = new AccountInfo( //
            cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // userId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // userName
            cursor.getLong(offset + 3), // staffId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // staffCode
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // staffName
            cursor.getLong(offset + 6), // departmentId
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // departmentCode
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // departmentName
            cursor.getLong(offset + 9), // companyId
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // companyName
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // companyCode
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // companyType
            cursor.getLong(offset + 13), // positionId
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // positionName
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // positionCode
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // password
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // ip
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // date
            cursor.getLong(offset + 19), // imageId
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // roleIds
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // roleNames
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // uuid
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // loginTime
            cursor.getLong(offset + 24), // firstDepartmentId
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25) // firstDepartmentName
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AccountInfo entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setUserId(cursor.getLong(offset + 1));
        entity.setUserName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStaffId(cursor.getLong(offset + 3));
        entity.setStaffCode(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setStaffName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDepartmentId(cursor.getLong(offset + 6));
        entity.setDepartmentCode(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDepartmentName(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setCompanyId(cursor.getLong(offset + 9));
        entity.setCompanyName(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setCompanyCode(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setCompanyType(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setPositionId(cursor.getLong(offset + 13));
        entity.setPositionName(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setPositionCode(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setPassword(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setIp(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setDate(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setImageId(cursor.getLong(offset + 19));
        entity.setRoleIds(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setRoleNames(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setUuid(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setLoginTime(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setFirstDepartmentId(cursor.getLong(offset + 24));
        entity.setFirstDepartmentName(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(AccountInfo entity, long rowId) {
        entity.setUserId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(AccountInfo entity) {
        if(entity != null) {
            return entity.getUserId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(AccountInfo entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getContactEntityDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getDepartmentEntityDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T2", daoSession.getCompanyEntityDao().getAllColumns());
            builder.append(" FROM ACCOUNT_INFO T");
            builder.append(" LEFT JOIN CONTACT_ENTITY T0 ON T.\"STAFF_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN DEPARTMENT_ENTITY T1 ON T.\"DEPARTMENT_ID\"=T1.\"_id\"");
            builder.append(" LEFT JOIN COMPANY_ENTITY T2 ON T.\"COMPANY_ID\"=T2.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected AccountInfo loadCurrentDeep(Cursor cursor, boolean lock) {
        AccountInfo entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        ContactEntity staff = loadCurrentOther(daoSession.getContactEntityDao(), cursor, offset);
         if(staff != null) {
            entity.setStaff(staff);
        }
        offset += daoSession.getContactEntityDao().getAllColumns().length;

        DepartmentEntity department = loadCurrentOther(daoSession.getDepartmentEntityDao(), cursor, offset);
         if(department != null) {
            entity.setDepartment(department);
        }
        offset += daoSession.getDepartmentEntityDao().getAllColumns().length;

        CompanyEntity company = loadCurrentOther(daoSession.getCompanyEntityDao(), cursor, offset);
         if(company != null) {
            entity.setCompany(company);
        }

        return entity;    
    }

    public AccountInfo loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<AccountInfo> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<AccountInfo> list = new ArrayList<AccountInfo>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<AccountInfo> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<AccountInfo> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
