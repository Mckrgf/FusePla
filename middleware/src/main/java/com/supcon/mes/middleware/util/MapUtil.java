package com.supcon.mes.middleware.util;

import android.os.Bundle;
import android.text.TextUtils;

import com.supcon.common.BaseConstant;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.common.view.util.LogUtil;
import com.supcon.mes.middleware.model.bean.MapConfigEntity;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Time:    2020-05-11  09: 52
 * Author： nina
 * Des:
 *
 * type: point（点） ； line（线）；  polygon（面）
 * mode: edit（编辑模式） ； look（查看模式）
 * spatialId: 当前坐标的空间数据库id, 查看模式下【必传】，编辑模式下【选传】
 * layerCode: 图层编码, 查看模式下【必传】，编辑模式下【选传】
 */
public class MapUtil {

    //http://192.168.95.164:8080/msService/SESGISConfig/themeConfig/mobileMapSelect/index
    static String urlPrefix = "/msService/SESGISConfig/themeConfig/mobileMapSelect/index";

    /**
     * http://192.168.95.164:8080/msService/SESGISConfig/themeConfig/mobileMapSelect/index?type=point&mode=edit
     * @return
     */
    public static Bundle getBundle(MapConfigEntity entity) {
        StringBuffer urlBuffer = new StringBuffer();
        urlBuffer.append(urlPrefix);
        if (entity != null && entity.getMode() != null) {

            if (StringUtil.equalsIgnoreCase(entity.getMode(), MapConfigEntity.MODE_EDIT)) {
                //type 必填
                urlBuffer.append("?mode=edit");
                if (entity.getType() != null) {
                    urlBuffer.append("&type=" + entity.getType());
                }

            } else if (StringUtil.equalsIgnoreCase(entity.getMode(), MapConfigEntity.MODE_LOOK)){
                urlBuffer.append("?mode=look");

                if (entity.getType() != null) {
                    urlBuffer.append("&spatialId=" + entity.getSpatialId());
                }

                if (entity.getType() != null) {
                    urlBuffer.append("&layerCode=" + entity.getLayerCode());
                }
            }
        }

        LogUtil.e(urlBuffer.toString());
        Bundle bundle = UrlUtil.getWebAppBundle(urlBuffer.toString(), true);
        bundle.putBoolean(BaseConstant.WEB_IS_LIST, false);

        return bundle;
    }

    public static <T extends BaseEntity> Map<String, T> getSystemCode(List<T> entities) {
        Map<String, T> map = new LinkedHashMap<>(0);
        for (T entity : entities) {
            String name = getFieldValue(entity.getClass(), entity, "value");
            if (!TextUtils.isEmpty(name)) {
                map.put(name, entity);
                continue;
            }
            String value = getFieldValue(entity.getClass(), entity, "id");
            if (!TextUtils.isEmpty(value)) {
                map.put(value, entity);
            }
        }
        return map;
    }

    public static String getFieldValue(Class<?> clazz, Object obj, String fieldName) {
        String result = "";

        try {
            for (Field field : clazz.getFields()) {
                if (field.getName().equals(fieldName)) {
                    return String.valueOf(field.get(obj));
                }
            }
//            Field field = clazz.getField(fieldName);
//            result = field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }
}
