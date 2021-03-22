package com.supcon.mes.module_map.presenter;

import android.support.annotation.Nullable;

import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.util.HttpErrorReturnUtil;
import com.supcon.mes.module_map.model.bean.DownLoadFileEntity;
import com.supcon.mes.module_map.model.bean.MapCacheBaseLayerBeanEntity;
import com.supcon.mes.module_map.model.bean.MapCacheConfigEntity;
import com.supcon.mes.module_map.model.contract.GetMapConfigContract;
import com.supcon.mes.module_map.model.network.MapHttpClient;
import com.supcon.mes.module_map.util.DisposeUtil;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

/**
 * Created by wangshizhan on 2017/8/7.
 */

public class GetMapConfigPresenter extends GetMapConfigContract.Presenter {


    @Override
    public void getBaseConfigInfoForMobile() {
        mCompositeSubscription.add(
                MapHttpClient.getBaseConfigInfoForMobile()
                        .onErrorReturn(throwable -> {
                            BAP5CommonEntity<CommonEntity<MapCacheConfigEntity>> loginEntity = new BAP5CommonEntity<CommonEntity<MapCacheConfigEntity>>();
                            loginEntity.success = false;
                            loginEntity.msg = HttpErrorReturnUtil.getErrorInfo(throwable);
                            return loginEntity;
                        })
                        .subscribe(new Consumer<BAP5CommonEntity<CommonEntity<MapCacheConfigEntity>>>() {
                            @Override
                            public void accept(BAP5CommonEntity<CommonEntity<MapCacheConfigEntity>> mapEntity) throws Exception {
                                if (mapEntity.success) {
                                    getView().getBaseConfigInfoForMobileSuccess(mapEntity.data);
                                } else {
                                    getView().getBaseConfigInfoForMobileFailed(mapEntity.msg);
                                }
                            }
                        }));
    }

    @Override
    public void downLoadFile(MapCacheBaseLayerBeanEntity entityMap) {
        if (entityMap == null) {
            return;
        }
        //地图包相对路径
        //  /greenDill/static/GISModel/+包名.zip就是下载地址，下载完之后要进行解压缩
        Disposable disposable = MapHttpClient.downloadFile(entityMap.getPackageName() + ".zip")
                .onErrorReturn(new Function<Throwable, ResponseBody>() {
                    @Override
                    public ResponseBody apply(Throwable throwable) throws Exception {
                        ResponseBody responseBody = new ResponseBody() {
                            @Nullable
                            @Override
                            public MediaType contentType() {
                                return null;
                            }

                            @Override
                            public long contentLength() {
                                return 0;
                            }

                            @Override
                            public BufferedSource source() {
                                return null;
                            }
                        };
                        return responseBody;
                    }
                })
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) {
                        if (responseBody.contentLength() != 0) {
                            DownLoadFileEntity entity = new DownLoadFileEntity();
                            entity.setEntity(entityMap);
                            entity.setResponseBody(responseBody);
                            getView().downLoadFileSuccess(entity);
                        } else {
                            getView().downLoadFileFailed(entityMap.getPackageName());
                        }
                    }
                });
        mCompositeSubscription.add(disposable);

        DisposeUtil.getInstance().addKey(entityMap.getPackageName(), disposable);
    }

}
