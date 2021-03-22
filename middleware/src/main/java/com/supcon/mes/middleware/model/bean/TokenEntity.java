package com.supcon.mes.middleware.model.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wangshizhan on 2019/12/9
 * Email:wangshizhan@supcom.com
 */
public class TokenEntity extends TokenErrorEntity {

    /**
     * {
     *     "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1NzU5NDQ4MzksInVzZXJfbmFtZSI6ImZ1a3VuIiwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiYzE5MTcxNjAtMzE3NC00ZDU1LTk4MmMtNDA0NTBkMzY4YjNmIiwiY2xpZW50X2lkIjoibW9iaWxlLWNsaWVudCIsInNjb3BlIjpbImFsbCJdfQ.NeAwUdi_qnkREOtghJsd3o8StHy54N2r8Xtl75bBHnM",
     *     "token_type": "bearer",
     *     "expires_in": 86399,
     *     "scope": "all",
     *     "jti": "c1917160-3174-4d55-982c-40450d368b3f"
     *     "userInfo"{"userId":1001,"userName":"fukun","language":"zh_CN","staffId":1001,"staffName":"fukun","staffCode":"fukun","positionId":1000,"positionName":"A1001","positionCode":"A1001","departmentId":1001,"departmentName":"A1001","departmentCode":"A1001","companyId":1000,
     *               "companyName":"默认公司","companyCode":"defaultCompany","companyType":"UNIT","mobileFlag":null}
     * }
     */

    @SerializedName("access_token")
    public String accessToken;

    @SerializedName("token_type")
    public String tokenType;

    @SerializedName("expires_in")
    public String lastTime;

    public AccountInfo userInfo;

    public String scope;

    public String jti;

    public String getTokenType() {
        return tokenType == null?"":tokenType;
    }

    public String getScope() {
        return scope == null?"":scope;
    }

    public String getAccessToken() {
        return accessToken == null?"":accessToken;
    }
}
