package com.supcon.mes.middleware.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.util.List;

/**
 * Time:    2020-05-11  14: 30
 * Author： nina
 * Des:
 */
public class MapResultEntity extends BaseEntity {

    /**
     * param : {"coordinates":[{"lon":120.13307983220338,"lat":30.18143770488222,"hei":6.68},{"lon":120.1337529614409,"lat":30.1814153375503,"hei":6.68}],"buildingPatchId":null,"floor":null}
     */

    private ParamBean param;

    public ParamBean getParam() {
        return param;
    }

    public void setParam(ParamBean param) {
        this.param = param;
    }

    public static class ParamBean {
        /**
         * coordinates : [{"lon":120.13307983220338,"lat":30.18143770488222,"hei":6.68},{"lon":120.1337529614409,"lat":30.1814153375503,"hei":6.68}]
         * buildingPatchId : null
         * floor : null
         */

        private Object buildingPatchId;
        private Object floor;
        private List<CoordinatesBean> coordinates;
        private String layerCode;

        public Object getBuildingPatchId() {
            return buildingPatchId;
        }

        public void setBuildingPatchId(Object buildingPatchId) {
            this.buildingPatchId = buildingPatchId;
        }

        public Object getFloor() {
            return floor;
        }

        public void setFloor(Object floor) {
            this.floor = floor;
        }

        public List<CoordinatesBean> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<CoordinatesBean> coordinates) {
            this.coordinates = coordinates;
        }

        public String getLayerCode() {
            return layerCode;
        }

        public void setLayerCode(String layerCode) {
            this.layerCode = layerCode;
        }

        public static class CoordinatesBean {
            /**
             * lon : 120.13307983220338
             * lat : 30.18143770488222
             * hei : 6.68
             */

            private double lon;
            private double lat;
            private double hei;

            public double getLon() {
                return lon;
            }

            public void setLon(double lon) {
                this.lon = lon;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getHei() {
                return hei;
            }

            public void setHei(double hei) {
                this.hei = hei;
            }
        }
    }
}
