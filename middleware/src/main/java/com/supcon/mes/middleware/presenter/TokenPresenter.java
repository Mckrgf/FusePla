package com.supcon.mes.middleware.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.network.Api;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.PLAApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.AccountInfo;
import com.supcon.mes.middleware.model.bean.CompanyEntity;
import com.supcon.mes.middleware.model.bean.StaffEntity;
import com.supcon.mes.middleware.model.bean.TokenEntity;
import com.supcon.mes.middleware.model.contract.TokenContract;
import com.supcon.mes.middleware.model.network.MiddlewareHttpClient;
import com.supcon.mes.middleware.util.CookieTempUtil;
import com.supcon.mes.middleware.util.ResponseUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import okio.Buffer;
import okhttp3.Response;
import retrofit2.HttpException;

/**
 * Created by wangshizhan on 2019/12/9
 * Email:wangshizhan@supcom.com
 */
public class TokenPresenter extends TokenContract.Presenter {

    @Override
    public void getAccessToken(String userName, String password) {

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("grant_type", "password");
        queryMap.put("username", userName);
        queryMap.put("password", password);
        queryMap.put("scope", "all");

        mCompositeSubscription.add(MiddlewareHttpClient.getToken(queryMap)
                .onErrorReturn(throwable -> {
                    TokenEntity tokenEntity = new TokenEntity();

                    if(throwable instanceof HttpException){
                        try {

                            ResponseBody responseBody = ((HttpException) throwable).response().errorBody();

                            Buffer buffer = ResponseUtil.getBuffer(responseBody);
                            String content = ResponseUtil.readContent(responseBody, buffer);
                            tokenEntity = GsonUtil.gsonToBean(content, TokenEntity.class);
                        }
                        catch (Exception e){

                        }
                    }

                    tokenEntity.message = throwable.toString();

                    return tokenEntity;
                })
                .subscribe(new Consumer<TokenEntity>() {
                    @Override
                    public void accept(TokenEntity tokenEntity) throws Exception {
                        if(!TextUtils.isEmpty(tokenEntity.accessToken)){

                            AccountInfo accountInfo = tokenEntity.userInfo;

                            StaffEntity me = new StaffEntity();
                            me.setCode(accountInfo.staffCode);
                            me.setName(accountInfo.staffName);
                            me.setId(accountInfo.staffId);

                            PLAApplication.dao().getStaffEntityDao().insertOrReplace(me);

                            CompanyEntity companyEntity = new CompanyEntity();
                            companyEntity.id = accountInfo.companyId;
                            companyEntity.name = accountInfo.companyName;
                            companyEntity.code = accountInfo.companyCode;

                            PLAApplication.dao().getCompanyEntityDao().insertOrReplace(companyEntity);

                            accountInfo.setIp(PLAApplication.getIp());

                            PLAApplication.setAccountInfo(accountInfo);

                            long expireTime = System.currentTimeMillis()+Long.parseLong(tokenEntity.lastTime)*1000;
                            SharedPreferencesUtils.setParam(PLAApplication.getAppContext(), Constant.SPKey.ACCESS_TOKEN_VALID_TIME, expireTime);

                            getView().getAccessTokenSuccess(tokenEntity);
                            SharedPreferencesUtils.setParam(PLAApplication.getAppContext(), Constant.SPKey.ACCESS_TOKEN, tokenEntity.toString());

                            CookieTempUtil.saveCookie(PLAApplication.getAppContext(), "Authorization="+ tokenEntity.getAccessToken()+";");
                            CookieTempUtil.syncCookie(MBapApp.getAppContext(), Api.getInstance().getBaseUrl());
                        }
                        else{
                            if ("Bad credentials".equals(tokenEntity.errorDescription)){
                                getView().getAccessTokenFailed("密码错误！");
                            }else if ("UserDetailsService returned null, which is an interface contract violation".equals(tokenEntity.errorDescription)){
                                getView().getAccessTokenFailed("用户名错误！");
                            }else {
                                getView().getAccessTokenFailed(tokenEntity.errorDescription);
                            }
                        }
                    }
                }));
    }

    public boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }


}
