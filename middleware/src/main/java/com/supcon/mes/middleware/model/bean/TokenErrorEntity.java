package com.supcon.mes.middleware.model.bean;

import com.google.gson.annotations.SerializedName;
import com.supcon.common.com_http.BaseEntity;

/**
 * Created by wangshizhan on 2019/12/9
 * Email:wangshizhan@supcom.com
 */
public class TokenErrorEntity extends BaseEntity {
    /**
     * {
     *     "timestamp": 1575872422443,
     *     "status": 401,
     *     "error": "Unauthorized",
     *     "error_description": "超过最大并发数（5）",
     *     "message": "Unauthorized",
     *     "path": "/auth/oauth/token"
     * }
     */

    public int status;
    public long timestamp;
    public String error;
    @SerializedName("error_description")
    public String errorDescription;
    public String message;
    public String path;
}
