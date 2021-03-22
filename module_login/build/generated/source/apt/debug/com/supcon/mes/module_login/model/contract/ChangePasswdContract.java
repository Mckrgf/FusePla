package com.supcon.mes.module_login.model.contract;

import com.supcon.common.view.base.presenter.BasePresenter;
import com.supcon.common.view.contract.IBaseView;
import com.supcon.mes.module_login.model.api.ChangePasswdAPI;
import com.supcon.mes.module_login.model.bean.ChangePasswdResultEntity;
import java.lang.String;

/**
 * @Contract created by apt
 * 注解内实体和方法是一一对应的
 * add by wangshizhan
 */
public interface ChangePasswdContract {
  /**
   * @View created by apt
   */
  interface View extends IBaseView {
    /**
     * @method create by apt
     */
    void changePasswdSuccess(ChangePasswdResultEntity entity);

    /**
     * @method create by apt
     */
    void changePasswdFailed(String errorMsg);
  }

  /**
   * @Presenter created by apt
   */
  abstract class Presenter extends BasePresenter<ChangePasswdContract.View> implements ChangePasswdAPI {
  }
}
