package com.supcon.mes.module_webview.ui.listener;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.view.OrientationEventListener;

/**
 * Time:    2020/6/8  19: 20
 * Author： nina
 * Des:
 */
public class OrientationWatchDog {

    private static final String TAG = OrientationWatchDog.class.getSimpleName();

    private Context mContext;
    //系统的屏幕方向改变监听
    private OrientationEventListener mLandOrientationListener;

    int mOrientation;

    /**
     * 屏幕方向
     */
    private enum Orientation {
        /**
         * 竖屏
         */
        Port,
        /**
         * 横屏
         */
        Land
    }


    public OrientationWatchDog(Context context) {
        mContext = context;
    }

    /**
     * 开始监听
     */
    public void startWatch() {
        if (mLandOrientationListener == null) {
            mLandOrientationListener = new OrientationEventListener(mContext,
                    SensorManager.SENSOR_DELAY_NORMAL) {
                @Override
                public void onOrientationChanged(int orientation) {

                    //判null
                    if(mContext == null || ((Activity)mContext).isFinishing()){
                        return;
                    }
                    //记录用户手机上一次放置的位置
                    int mLastOrientation = mOrientation;

                    if(orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                        //手机平放时，检测不到有效的角度

                        //重置为原始位置 -1
                        mOrientation = -1;
                        return;
                    }


                    //如果用户关闭了手机的屏幕旋转功能，不再开启代码自动旋转了，直接return
                    try {
                        /**
                         * 1 手机已开启屏幕旋转功能
                         * 0 手机未开启屏幕旋转功能
                         */
                        if(Settings.System.getInt(mContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION) == 0){
                            return;
                        }
                    } catch (Settings.SettingNotFoundException e) {
                        e.printStackTrace();
                    }

                    /**
                     * 只检测是否有四个角度的改变
                     */
                    if(orientation > 350 || orientation< 10) {
                        //0度，用户竖直拿着手机
                        mOrientation = 0;

                    } else if(orientation > 80 && orientation < 100) {
                        //90度，用户右侧横屏拿着手机
                        mOrientation = 90;

                    } else if(orientation > 170 && orientation < 190) {
                        //180度，用户反向竖直拿着手机
                        mOrientation = 180;

                    } else if(orientation > 260 && orientation < 280) {
                        //270度，用户左侧横屏拿着手机
                        mOrientation = 270;
                    }

                    //当检测到用户手机位置距离上一次记录的手机位置发生了改变，开启屏幕自动旋转
                    if(mLastOrientation != mOrientation){
                        ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    }
                }
            };
        }

        mLandOrientationListener.enable();
    }

    /**
     * 结束监听
     */
    public void stopWatch() {
        if (mLandOrientationListener != null) {
            mLandOrientationListener.disable();
        }
    }

    /**
     * 销毁监听
     */
    public void destroy() {
        stopWatch();
        mLandOrientationListener = null;
    }

}
