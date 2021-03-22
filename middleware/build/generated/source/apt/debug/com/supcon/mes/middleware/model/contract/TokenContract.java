package com.supcon.mes.middleware.model.contract;

import com.supcon.common.view.base.presenter.BasePresenter;
import com.supcon.common.view.contract.IBaseView;
import com.supcon.mes.middleware.model.api.TokenAPI;
import com.supcon.mes.middleware.model.bean.TokenEntity;
import java.lang.String;

/**
 * @Contract created by apt
 * 注解内实体和方法是一一对应的
 * add by wangshizhan
 */
public interface TokenContract {
  /**
   * @View created by apt
   */
  interface View extends IBaseView {
    /**
     * @method create by apt
     */
    void getAccessTokenSuccess(TokenEntity entity);

    /**
     * @method create by apt
     */
    void getAccessTokenFailed(String errorMsg);
  }

  /**
   * @Presenter created by apt
   */
  abstract class Presenter extends BasePresenter<TokenContract.View> implements TokenAPI {
  }
}
