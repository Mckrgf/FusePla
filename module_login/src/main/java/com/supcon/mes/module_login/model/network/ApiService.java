package com.supcon.mes.module_login.model.network;

import com.supcon.common.com_http.NullEntity;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/3/23.
 */

public interface ApiService {


    /**
     * 修改密码
     * @param oldPassword
     * @param newpassword
     * @return
     */
    @POST("/foundation/userset/saveUsersrtInfo.action?a=-1&")
    Flowable<NullEntity> changePasswd(@Query("oldpassword") String oldPassword, @Query("newpassword") String newpassword);
}
