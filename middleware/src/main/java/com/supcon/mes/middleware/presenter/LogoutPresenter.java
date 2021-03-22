package com.supcon.mes.middleware.presenter;


import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.supcon.common.view.App;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.MBapConstant;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.PLAApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.TokenEntity;
import com.supcon.mes.middleware.model.contract.LogoutContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.middleware.util.CookieTempUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by wangshizhan on 2019/10/28
 * Email:wangshizhan@supcom.com
 */
public class LogoutPresenter extends LogoutContract.Presenter {

    @SuppressLint("CheckResult")
    @Override
    public void logout() {
        LogUtil.i("dologout");
//        Flowable.timer(500, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        doLogout();
//                    }
//                });

        String tokenCache = SharedPreferencesUtils.getParam(PLAApplication.getAppContext(), Constant.SPKey.ACCESS_TOKEN, "");

        if (TextUtils.isEmpty(tokenCache)) {
            return;
        }
        TokenEntity tokenEntity = GsonUtil.gsonToBean(tokenCache, TokenEntity.class);
        Map<String, Object> map = new HashMap<>();
        map.put(("authenticationCode"), tokenEntity.accessToken);

        mCompositeSubscription.add(
                MiddlewareHttpClient.kickOut(tokenEntity.accessToken)
                        .onErrorReturn(new Function<Throwable, BAP5CommonEntity>() {
                            @Override
                            public BAP5CommonEntity apply(Throwable throwable) throws Exception {
                                BAP5CommonEntity commonEntity = new BAP5CommonEntity();
                                commonEntity.success = false;
                                commonEntity.msg = throwable.toString();
                                return commonEntity;
                            }
                        })
                        .subscribe(new Consumer<BAP5CommonEntity>() {
                            @Override
                            public void accept(BAP5CommonEntity bap5CommonEntity) throws Exception {
                                doLogout();
                                if(bap5CommonEntity.success){
                                    getView().logoutSuccess();
                                }
                                else{
                                    getView().logoutSuccess();
                                }
                            }
                        }));

    }

    private void doLogout() {
        LogoutPresenter.this.getView().logoutSuccess();
        SharedPreferencesUtils.setParam(MBapApp.getAppContext(), MBapConstant.SPKey.LOGIN, false);
        SharedPreferencesUtils.setParam(MBapApp.getAppContext(), MBapConstant.SPKey.JSESSIONID, "");
        SharedPreferencesUtils.setParam(MBapApp.getAppContext(), MBapConstant.SPKey.CASTGC, "");
        SharedPreferencesUtils.setParam(MBapApp.getAppContext(), Constant.SPKey.GW_SESSION_ID, "");
        MBapApp.setIsLogin(false);

        //用户信息清空
        PLAApplication.setAccountInfo(null);
        PLAApplication.setStaffEntity(null);

        CookieTempUtil.clearCookie(App.getAppContext());
    }

}
