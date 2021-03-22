package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * Time:    2020-05-11  10: 01
 * Author： nina
 * Des:
 *
 * type: point（点）; line（线）polygon（面）
 * mode: edit（编辑模式） ； look（查看模式）//查看的时候不能选点，只能浏览看看；编辑的时候返回点
 * spatialId: 当前坐标的空间数据库id, 查看模式下【必传】，编辑模式下【选传】
 * layerCode: 图层编码, 查看模式下【必传】，编辑模式下【选传】
 */
public class MapConfigEntity extends BaseEntity {
    public static String MODE_EDIT = "edit";
    public static String MODE_LOOK = "look";

    public static String TYPE_POINT = "point";
    public static String TYPE_LINE = "line";
    public static String TYPE_POLYGON = "polygon";

    String type;//查看模式下必填
    String mode;//必填
    String spatialId;
    String layerCode;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getSpatialId() {
        return spatialId;
    }

    public void setSpatialId(String spatialId) {
        this.spatialId = spatialId;
    }

    public String getLayerCode() {
        return layerCode;
    }

    public void setLayerCode(String layerCode) {
        this.layerCode = layerCode;
    }
}
