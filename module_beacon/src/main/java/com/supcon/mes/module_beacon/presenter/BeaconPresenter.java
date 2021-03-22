package com.supcon.mes.module_beacon.presenter;


import com.supcon.mes.module_beacon.bean.OkResponse;
import com.supcon.mes.module_beacon.model.contract.GetBeaconsContract;
import com.supcon.mes.module_beacon.model.net.ModuleBeaconHttpClient;

/**
 * @author : yaobing
 * @date : 2020/11/3 15:53
 * @desc :
 */
public class BeaconPresenter extends GetBeaconsContract.Presenter {
    @Override
    public void getBeacons() {
        mCompositeSubscription.add(
                ModuleBeaconHttpClient.getBeaconList(null)
                        .onErrorReturn(throwable -> {
                            OkResponse response = new OkResponse();
                            response.setCode(500);
                            response.setMsg(throwable.toString());
                            return response;
                        })
                        .subscribe(mapEntity -> {
                            if (mapEntity.getCode() == 500) {
                                getView().getBeaconsFailed(mapEntity.getMsg());
                            } else {
                                getView().getBeaconsSuccess(mapEntity);
                            }
                        }));
    }
}
