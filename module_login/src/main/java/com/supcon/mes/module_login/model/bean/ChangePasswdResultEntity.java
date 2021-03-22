package com.supcon.mes.module_login.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * @Author xushiyun
 * @Create-time 8/2/19
 * @Pageage com.supcon.mes.module_login.model.bean
 * @Project eamtest
 * @Email ciruy.victory@gmail.com
 * @Related-classes
 * @Desc
 */
public class ChangePasswdResultEntity extends BaseEntity {
    
    /**
     * dealSuccessFlag : false
     * Message : 原密码不正确，请重新输入！
     */
    
    public boolean dealSuccessFlag;
    public String Message;
    
    public boolean isDealSuccessFlag() {
        return dealSuccessFlag;
    }
    
    public void setDealSuccessFlag(boolean dealSuccessFlag) {
        this.dealSuccessFlag = dealSuccessFlag;
    }
    
    public String getMessage() {
        return Message;
    }
    
    public void setMessage(String Message) {
        this.Message = Message;
    }
}
