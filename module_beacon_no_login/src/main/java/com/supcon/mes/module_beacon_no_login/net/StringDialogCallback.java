/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.supcon.mes.module_beacon_no_login.net;

import android.app.ProgressDialog;
import android.content.Context;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.request.base.Request;
import com.supcon.supbeacon.R;

import okhttp3.Response;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2016/9/11
 * 描    述：返回字符串类型的数据
 * 修订历史：
 * ================================================
 */
public abstract class StringDialogCallback extends AbsCallback<String> {

    private boolean hasDialog = false;
    private ProgressDialog dialog;
    private Context context;


    private StringConvert convert;

    public StringDialogCallback(Context context, boolean hasDialog) {
        this.context = context;
        this.hasDialog = hasDialog;
        convert = new StringConvert();
    }

    @Override
    public void onStart(Request<String, ? extends Request> request) {
        super.onStart(request);
        if (hasDialog) {
            showProgress();
        }
    }
    @Override
    public void onFinish() {
        super.onFinish();
        closeProgress();
    }

    @Override
    public String convertResponse(Response response) throws Throwable {
        String s = convert.convertResponse(response);
        response.close();
        return s;
    }

    private void showProgress() {
        if (null == dialog) {
            dialog = new ProgressDialog(context);
            dialog.setMessage(context.getResources().getString(R.string.loading));
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void closeProgress() {
        if (null!=dialog&&dialog.isShowing()) {
            dialog.cancel();
        }
    }
}
