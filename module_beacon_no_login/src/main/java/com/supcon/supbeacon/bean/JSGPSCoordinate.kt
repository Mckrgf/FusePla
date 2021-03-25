package com.supcon.supbeacon.bean

import java.io.Serializable

/**
 * @author : yaobing
 * @date : 2020/8/7 14:18
 * @desc :  js返回的坐标数据
 */
class JSGPSCoordinate {
    /**
     * param : {"coordinates":[{"lon":120.13366876567463,"lat":30.18183882796643,"hei":3.9}],"buildingPatchId":null,"floor":null}
     */
    var param: ParamBean? = null

    class ParamBean {
        /**
         * coordinates : [{"lon":120.13366876567463,"lat":30.18183882796643,"hei":3.9}]
         * buildingPatchId : null
         * floor : null
         */
        var buildingPatchId: Any? = null
        var floor: Any? = null
        var coordinates: List<CoordinatesBean>? = null

        class CoordinatesBean :Serializable{
            /**
             * lon : 120.13366876567463
             * lat : 30.18183882796643
             * hei : 3.9
             */
            var lon = 0.0
            var lat = 0.0
            var hei = 0.0

        }
    }
}