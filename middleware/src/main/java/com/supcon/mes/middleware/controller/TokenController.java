package com.supcon.mes.middleware.controller;

import android.content.Context;
import android.text.TextUtils;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.controller.BaseDataController;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.mes.mbap.beans.LoginEvent;
import com.supcon.mes.middleware.PLAApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.api.TokenAPI;
import com.supcon.mes.middleware.model.bean.TokenEntity;
import com.supcon.mes.middleware.model.contract.TokenContract;
import com.supcon.mes.middleware.model.event.LoginInvalidEvent;
import com.supcon.mes.middleware.presenter.TokenPresenter;
import com.supcon.mes.middleware.util.ErrorMsgHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by wangshizhan on 2019/12/9
 * Email:wangshizhan@supcom.com
 */
@Presenter(TokenPresenter.class)
public class TokenController extends BaseDataController implements TokenContract.View {

    private static long expireTime ;
    private boolean isLogining = false;

    public TokenController(Context context) {
        super(context);
    }

    @Override
    public void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginValid(LoginInvalidEvent event) {

        if(!isLogining){
            getToken();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initData() {
        super.initData();

        expireTime = SharedPreferencesUtils.getParam(context, Constant.SPKey.ACCESS_TOKEN_VALID_TIME, 0L);
        long currentTile =  System.currentTimeMillis();
        if(expireTime == 0 || expireTime < currentTile && !isLogining){
            getToken();
        }

    }

    private void getToken(){
        isLogining = true;
        presenterRouter.create(TokenAPI.class).getAccessToken(PLAApplication.getUserName(), PLAApplication.getPassword());
    }

    @Override
    public void getAccessTokenSuccess(TokenEntity entity) {
        isLogining = false;
        if(!TextUtils.isEmpty(entity.lastTime) && TextUtils.isDigitsOnly(entity.lastTime)){
            expireTime = System.currentTimeMillis()+Long.parseLong(entity.lastTime)*1000;
            SharedPreferencesUtils.setParam(context, Constant.SPKey.ACCESS_TOKEN_VALID_TIME, expireTime);
            EventBus.getDefault().post(new LoginEvent());
        }

    }

    @Override
    public void getAccessTokenFailed(String errorMsg) {
        LogUtil.e(ErrorMsgHelper.msgParse(errorMsg));
        isLogining = false;
    }
}
