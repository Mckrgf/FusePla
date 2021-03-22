package com.supcon.mes.module_map.ui;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.util.SharedPreferencesUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.MBapApp;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.StatusBarUtils;
import com.supcon.mes.mbap.view.CustomDialog;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonEntity;
import com.supcon.mes.middleware.ui.view.ExpandableListViewForScrollView;
import com.supcon.mes.middleware.util.FileUtil;
import com.supcon.mes.middleware.util.PicUtil;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_map.R;
import com.supcon.mes.module_map.model.api.GetMapConfigAPI;
import com.supcon.mes.module_map.model.bean.DownLoadFileEntity;
import com.supcon.mes.module_map.model.bean.MapCacheBaseLayerBeanEntity;
import com.supcon.mes.module_map.model.bean.MapCacheBaseLayerGroupEntity;
import com.supcon.mes.module_map.model.bean.MapCacheConfigEntity;
import com.supcon.mes.module_map.model.contract.GetMapConfigContract;
import com.supcon.mes.module_map.presenter.GetMapConfigPresenter;
import com.supcon.mes.module_map.ui.adapter.MapCachepacketAdapter;
import com.supcon.mes.module_map.util.DisposeUtil;
import com.supcon.mes.module_webview.util.map.FileHandleUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Presenter(value = GetMapConfigPresenter.class)
@Router(Constant.Router.MAP_CACHE_SETTING)
public class MapCacheSettingActivity extends BasePresenterActivity implements GetMapConfigContract.View {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("rightBtn")
    CustomImageButton rightBtn;
    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("lv_content")
    ExpandableListViewForScrollView lv_content;
    ArrayList<MapCacheBaseLayerGroupEntity> infoList = new ArrayList<>();
    MapCachepacketAdapter adapter;


    @Override
    protected int getLayoutID() {
        return R.layout.ac_map_cache_setting;
    }
    
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> {
            List<MapCacheBaseLayerBeanEntity> listIsDownLoading = getIsDownloadIngList();
            if (listIsDownLoading != null && listIsDownLoading.size() > 0) {
                showCancelDialog(getString(R.string.map_cache_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //所有的下载的列表都要 取消
                        DisposeUtil.getInstance().cacelAll();

                        for (MapCacheBaseLayerBeanEntity entity : listIsDownLoading) {
                            String filePathAndName = FileHandleUtil.getFilePathAndName(entity.getPackageName() + ".zip");
                            FileUtil.deleteFile(new File(filePathAndName));
                        }

                        back();
                    }
                });
            } else {
                back();
            }

        });

        lv_content.setOnItemLongClickListener((parent, view, position, id) -> false);

        lv_content.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            MapCacheBaseLayerBeanEntity entity = adapter.getData().get(groupPosition).get(childPosition);
            if (entity.getState() != MapCacheBaseLayerBeanEntity.STATE_HAS_DOWNLOAD) {

                //取消的按钮
                if (entity.getState() == MapCacheBaseLayerBeanEntity.STATE_DOWNLOAD_ING) {
                    //因为不支持断点续传，取消后就必须重新下载;弹出提示框，未下载完成，确定要取消？取消后只能重新下载
                    showCancelDialog(getString(R.string.map_cache_cancel), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DisposeUtil.getInstance().cacelByKey(entity.getPackageName());

                            String filePathAndName = FileHandleUtil.getFilePathAndName(entity.getPackageName() + ".zip");
                            FileUtil.deleteFile(new File(filePathAndName));

                            //
                            if (groupPosition > 0) {
                                entity.setState(MapCacheBaseLayerBeanEntity.STATE_DOWNLOADABLE);
                            } else {
                                entity.setState(MapCacheBaseLayerBeanEntity.STATE_NEED_UPDATE);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    //开始下载，进度条更新
                    //如果中间返回了，再进来怎么弄
                    presenterRouter.create(GetMapConfigAPI.class).downLoadFile(entity);

                    entity.setState(MapCacheBaseLayerBeanEntity.STATE_DOWNLOAD_ING);
                    adapter.notifyDataSetChanged();
                }
            }

            return true;
        });
    }

    @Override
    public void back() {
        //
        saveToLocal();

        super.back();
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.map_cache));

        presenterRouter.create(GetMapConfigAPI.class).getBaseConfigInfoForMobile();

        try {
            adapter = new MapCachepacketAdapter(context, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        lv_content.setAdapter(adapter);
    }


    @Override
    public void getBaseConfigInfoForMobileSuccess(CommonEntity entity)  {
        if (entity != null && entity.success) {
            MapCacheConfigEntity mapConfigEntity = (MapCacheConfigEntity) entity.result;
            if (mapConfigEntity != null) {
                //列表展示，包括本地的与服务端返回的，都可以删除
                updateList(mapConfigEntity.getBaseLayers());
            }
        }
    }

    @Override
    public void getBaseConfigInfoForMobileFailed(String errorMsg) {
        ToastUtils.show(context, errorMsg);

        updateList(null);
    }

    private void updateList(List<MapCacheBaseLayerBeanEntity> entities) {
        infoList = getComparedList(entities);
        try {
            adapter.setData(infoList);

            for (int i = 0; i < infoList.size(); i++) {
                lv_content.expandGroup(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void downLoadFileSuccess(DownLoadFileEntity entity) {

        MapCacheBaseLayerBeanEntity beanEntity =  entity.getEntity();

        if (beanEntity != null) {

            String filePath = FileHandleUtil.filePath;
            String fileName = beanEntity.getPackageName() + ".zip";
            File file = PicUtil.writeToDisk(fileName, filePath, entity.getResponseBody());

            //解压缩：//如果原来的地方有这个文件夹 就删除整个文件夹，然后解压缩
            //删除zip，解压缩成功后删除原来的zip
            FileHandleUtil.unZipFile(file.getPath(), filePath, beanEntity.getPackageName());

            //更新UI
            if (infoList.size() > 1) {
                List<MapCacheBaseLayerBeanEntity> unDownloadList = infoList.get(1);
                if (unDownloadList != null && unDownloadList.size() > 0) {
                    for (MapCacheBaseLayerBeanEntity mapCacheBaseLayerBeanEntity : unDownloadList) {
                        if (StringUtil.equalsIgnoreCase(mapCacheBaseLayerBeanEntity.getPackageName(), beanEntity.getPackageName())) {
                            unDownloadList.remove(mapCacheBaseLayerBeanEntity);
                            infoList.get(0).add(beanEntity);
                            break;
                        }
                    }
                }
            }

            beanEntity.setState(MapCacheBaseLayerBeanEntity.STATE_HAS_DOWNLOAD);
            saveToLocal();

            adapter.notifyDataSetChanged();
        }
    }

    private void saveToLocal() {
        if (infoList != null  && infoList.size()> 0 && infoList.get(0) != null) {
            List<MapCacheBaseLayerBeanEntity> children = infoList.get(0);
            if (children != null && children.size() > 0) {
                String mapcacheJson = GsonUtil.gsonString(children);
                SharedPreferencesUtils.setParam(MBapApp.getAppContext(), Constant.SPKey.KEY_MAP_CACHE, mapcacheJson);
            }
        }
    }

    @Override
    public void downLoadFileFailed(String errorMsg) {
//        ToastUtils.show(context, getString(R.string.download_attachment_failed), Toast.LENGTH_SHORT);

        if (!StringUtil.isBlank(errorMsg)) {
            DisposeUtil.getInstance().cacelByKey(errorMsg);

            if (infoList.size() > 1) {
                MapCacheBaseLayerGroupEntity entities = infoList.get(1);
                for (MapCacheBaseLayerBeanEntity entity : entities) {
                    if (StringUtil.equalsIgnoreCase(entity.getPackageName(), errorMsg)) {
                        entity.setState(MapCacheBaseLayerBeanEntity.STATE_DOWNLOADABLE);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

        }
    }

    /**
     * 比较本地的数据与服务端返回的数据
     *
     */
    private ArrayList<MapCacheBaseLayerGroupEntity> getComparedList(List<MapCacheBaseLayerBeanEntity> serverList) {
        ArrayList<MapCacheBaseLayerGroupEntity> groupList = new ArrayList<>();

        MapCacheBaseLayerGroupEntity groupHasDownload = new MapCacheBaseLayerGroupEntity();
        groupHasDownload.setTitle(getString(R.string.map_cache_has_download));

        MapCacheBaseLayerGroupEntity groupUnDownLoad = new MapCacheBaseLayerGroupEntity();
        groupUnDownLoad.setTitle(getString(R.string.map_cache_un_download));

        groupList.add(groupHasDownload);
        groupList.add(groupUnDownLoad);


        List<MapCacheBaseLayerBeanEntity> localList = getLocalList();
        if (localList != null  && serverList != null) {
            Map<String, MapCacheBaseLayerBeanEntity> mapLocalList = new HashMap<>();
            for (MapCacheBaseLayerBeanEntity entity : localList) {
                mapLocalList.put(entity.getPackageName(), entity);
            }

            Map<String, MapCacheBaseLayerBeanEntity> mapServerList = new HashMap<>();
            for (MapCacheBaseLayerBeanEntity entity : serverList) {
                mapServerList.put(entity.getPackageName(), entity);
            }

            //
            for (MapCacheBaseLayerBeanEntity entity : serverList) {
                if (mapLocalList.containsKey(entity.getPackageName())) {
                    if (StringUtil.equalsIgnoreCase(entity.getVersion(), mapLocalList.get(entity.getPackageName()).getVersion())) {
                        entity.setState(MapCacheBaseLayerBeanEntity.STATE_HAS_DOWNLOAD);
                        groupHasDownload.add(entity);
                    } else {
                        entity.setState(MapCacheBaseLayerBeanEntity.STATE_NEED_UPDATE);
                        groupHasDownload.add(entity);
                    }
                } else{
                    entity.setState(MapCacheBaseLayerBeanEntity.STATE_DOWNLOADABLE);
                    groupUnDownLoad.add(entity);

                }
            }

            for (MapCacheBaseLayerBeanEntity entity : localList) {
                if (mapServerList.containsKey(entity.getPackageName())) {
                    localList.remove(entity);
                } else {
                    entity.setState(MapCacheBaseLayerBeanEntity.STATE_HAS_DOWNLOAD);
                    groupHasDownload.add(entity);
                }
            }

            serverList.addAll(localList);

        } else if (serverList != null){
            for (MapCacheBaseLayerBeanEntity entity : serverList) {
                entity.setState(MapCacheBaseLayerBeanEntity.STATE_DOWNLOADABLE);
                groupUnDownLoad.add(entity);
            }
        } else if (localList != null) {
            for (MapCacheBaseLayerBeanEntity entity : localList) {
                entity.setState(MapCacheBaseLayerBeanEntity.STATE_HAS_DOWNLOAD);
                groupHasDownload.add(entity);
            }
        }

        return groupList;
    }

    /**
     * 从缓存中取出来，如果有的话就
     */
    private List<MapCacheBaseLayerBeanEntity> getLocalList() {
        String mapcacheJson = SharedPreferencesUtils.getParam(MBapApp.getAppContext(), Constant.SPKey.KEY_MAP_CACHE, "");

        if (StringUtil.isBlank(mapcacheJson)) {
            return null;
        }

        List<MapCacheBaseLayerBeanEntity> beans = GsonUtil.jsonToList(mapcacheJson, MapCacheBaseLayerBeanEntity.class);

        if (beans == null || beans.size() < 1) {
            return null;
        }

        //
        String PATH_ON_SD_CARD_OF_APP = FileHandleUtil.filePath;
        File f = new File(PATH_ON_SD_CARD_OF_APP);
        if (!f.exists()) {
            f.mkdirs();
        }

        Map<String, String> fileNameKey = new HashMap<>();
        //读取本地的,判断 mapcache存不存在，如果存在，遍历文件夹存储为List<String>
        if (f.exists()) {
            File[] fileList = f.listFiles();
            for (File file : fileList) {
                if (file.isDirectory() || file.isFile()) {
                    fileNameKey.put(file.getName(), file.getName());
                }
            }
        }

        for (MapCacheBaseLayerBeanEntity bean : beans) {
            if (!fileNameKey.containsKey(bean.getPackageName())) {
                beans.remove(bean);
            }
        }

        return beans;
    }

    private void showCancelDialog(String title, View.OnClickListener confirmListener) {
        new CustomDialog(context)
                .twoButtonAlertDialog(title)
                .bindView(R.id.redBtn, getString(R.string.confirm))
                .bindView(R.id.grayBtn, getString(R.string.cancel))
                .bindClickListener(R.id.redBtn, confirmListener, true)
                .bindClickListener(R.id.grayBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.e("cacel");
                    }
                }, true)
                .show();
    }

    private List<MapCacheBaseLayerBeanEntity> getIsDownloadIngList() {
        List<MapCacheBaseLayerBeanEntity> list = new ArrayList<>();
        for (MapCacheBaseLayerGroupEntity groupEntity : infoList) {
            List<MapCacheBaseLayerBeanEntity> children = groupEntity;
            if (children != null && children.size() > 0) {
                for (MapCacheBaseLayerBeanEntity child : children) {
                    if (child.getState() == MapCacheBaseLayerBeanEntity.STATE_DOWNLOAD_ING) {
                        list.add(child);
                    }
                }
            }
        }
        return list;
    }

}
