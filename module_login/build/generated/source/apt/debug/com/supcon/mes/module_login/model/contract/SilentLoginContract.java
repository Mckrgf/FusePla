package com.supcon.mes.module_login.model.contract;

import com.supcon.common.view.base.presenter.BasePresenter;
import com.supcon.common.view.contract.IBaseView;
import com.supcon.mes.module_login.model.api.SilentLoginAPI;
import com.supcon.mes.module_login.model.bean.LoginEntity;
import java.lang.String;

/**
 * @Contract created by apt
 * 注解内实体和方法是一一对应的
 * add by wangshizhan
 */
public interface SilentLoginContract {
  /**
   * @View created by apt
   */
  interface View extends IBaseView {
    /**
     * @method create by apt
     */
    void dologinSuccess(LoginEntity entity);

    /**
     * @method create by apt
     */
    void dologinFailed(String errorMsg);
  }

  /**
   * @Presenter created by apt
   */
  abstract class Presenter extends BasePresenter<SilentLoginContract.View> implements SilentLoginAPI {
  }
}
