package com.supcon.mes.module_map.model.contract;

import  com.supcon.mes.module_map.model.bean.DownLoadFileEntity;
import com.supcon.common.view.base.presenter.BasePresenter;
import com.supcon.common.view.contract.IBaseView;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.module_map.model.api.GetMapConfigAPI;
import java.lang.String;

/**
 * @Contract created by apt
 * 注解内实体和方法是一一对应的
 * add by wangshizhan
 */
public interface GetMapConfigContract {
  /**
   * @View created by apt
   */
  interface View extends IBaseView {
    /**
     * @method create by apt
     */
    void getBaseConfigInfoForMobileSuccess(CommonEntity entity);

    /**
     * @method create by apt
     */
    void getBaseConfigInfoForMobileFailed(String errorMsg);

    /**
     * @method create by apt
     */
    void downLoadFileSuccess(DownLoadFileEntity entity);

    /**
     * @method create by apt
     */
    void downLoadFileFailed(String errorMsg);
  }

  /**
   * @Presenter created by apt
   */
  abstract class Presenter extends BasePresenter<GetMapConfigContract.View> implements GetMapConfigAPI {
  }
}
