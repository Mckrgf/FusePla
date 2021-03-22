package com.supcon.mes.middleware.constant;

import android.os.Environment;

import java.io.File;

/**
 * Created by wangshizhan on 2018/4/28.
 * Email:wangshizhan@supcon.com
 */

public interface Constant {

    String FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "isupPlant" + File.separator;
    String CRASH_PATH = FILE_PATH + "log" + File.separator;
    String XJ_PATH = FILE_PATH + "xj" + File.separator;
    String YH_PATH = FILE_PATH + "yh" + File.separator;
    String WX_PATH = FILE_PATH + "wx" + File.separator;
    String WTS_PATH = FILE_PATH + "wts" + File.separator;
    String SCAN_PATH = FILE_PATH + "scan" + File.separator;
    String EAM_PATH = FILE_PATH + "eam" + File.separator;
    String PD_PATH = FILE_PATH + "pd" + File.separator;
    String HAZARD_PATH = FILE_PATH + "hazard" + File.separator;
    String IMAGE_SAVE_PATH = FILE_PATH + "pics" + File.separator;   //在线单据下载的缺陷图片存放位置
    String IMAGE_SAVE_YHPATH = YH_PATH + "pics" + File.separator;   //缺陷拍摄的图片位置
    String IMAGE_SAVE_XJPATH = XJ_PATH + "pics" + File.separator;   //巡检拍摄的图片路径
    String IMAGE_SAVE_WXPATH = WX_PATH + "pics" + File.separator;
    String IMAGE_SAVE_WTSPATH = WTS_PATH + "sign" + File.separator;//保存签名
    String IMAGE_SAVE_TICKETPATH = WTS_PATH + "ticket" + File.separator;//风险安全措施拍照
    String WTS_UPLOAD_PATH = WTS_PATH + "upload" + File.separator;//上传离线作业票需要数据的存放位置
    String WTS_DOWNLOAD_PATH = WTS_PATH + "download" + File.separator;//下载离线作业票存放位置
    String WTS_DOWNLOAD_PIC_PATH = WTS_PATH + "safe_sign" + File.separator;//下载风险安全措施跟签名的存放位置
    String WTS_SAVE_SAFE_TYPE = WTS_PATH + "safe_type" + File.separator;//保存风险安全措施跟签名的大类操作存放位置
    String PD_DOWNLOAD_PATH = PD_PATH + "download" + File.separator;//盘点离线工作数据存放位置
    String SCAN_IMAGE_LOADER = SCAN_PATH + "pics" + File.separator;//扫描
    String EAM_IMAGE_LOADER = FILE_PATH + "eam" + File.separator + "pics" + File.separator;//设备档案
    String CID = "cid";
    String CNAME = "cname";
    String XJ_GUIDE_IMGPATH = FILE_PATH + "eamInspectionGuideImage" + File.separator; //设备巡检指导图片路径
    String UPLOAD_HAZARD_PATH = HAZARD_PATH + "Local" + File.separator;//隐患离线保存
    String UPLOAD_HAZARD_UPLOAD_PATH = UPLOAD_HAZARD_PATH + "Upload" + File.separator;//隐患离线保存
    String UPLOAD_HAZARD_FILE_PATH = UPLOAD_HAZARD_UPLOAD_PATH + "File" + File.separator;//隐患离线保存
    String UPLOAD_HAZARD_ZIP_PATH = UPLOAD_HAZARD_PATH + "hzzard.zip";//隐患离线保存
    String CLIENT_TYPE_MOBILE = "mobile";
    String CLIENT_TYPE_ANDROID = "android";

    interface CAMERA_ACT {
        int ACT_GALLERY = 0;
        int ACT_CAMERA = 1;
        int ACT_CROP = 2;
        int ACT_PERMISSION = 3;
    }

    interface Widget {
        String XJHZ = "XJHZ";
        String WTS_PROGRESS = "WTS_PROGRESS";
        String WARNING_YJ = "WARNING_YJ";
        String SBDA_SUMMARY = "SBDA_SUMMARY";
        String WOM_PENDING = "WOM_PENDING";
    }


    interface SearchType {

        int CONTACT = 0;
        int APP = CONTACT + 1;
        int PENDING = APP + 1;
        int DEVICE = APP + 2;
        int XJAREA = APP + 3;

    }

    interface WxgdWorkSource {
        String faultInfoSource = "BEAM2003/01";//隐患单
        String checkRepair = "BEAM2003/06";//检修
        String bigRepair = "BEAM2003/05";//大修
        String maintenance = "BEAM2003/03";//维保
        String lubrication = "BEAM2003/02";//润滑
        String sparePart = "BEAM2003/04";//备件更换
    }

    interface WxgdWorkSource_CN {
        String allSource = "来源不限";//全部
        String faultInfoSource = "隐患单";//隐患单
        String checkRepair = "检修";//检修
        String bigRepair = "大修";//大修
        String maintenance = "维保";//维保
        String lubrication = "润滑";//润滑
        String sparePart = "备件更换";//备件更换
    }

    /**
     * 周期类型
     */
    interface PeriodType {
        String TIME_FREQUENCY = "BEAM014/01"; // 时间频率
        String RUNTIME_LENGTH = "BEAM014/02"; // 运行时长
    }

    //维修工单执行状态
    interface WxgdAction {

        String STOP = "BEAM2008/01";

        String ACTIVATE = "BEAM2008/02";

        String PAUSE = "maintenance_workType/stop";

        String START = "maintenance_workType/start";
    }

    interface Router {
        String WELCOME = "welcome";
        String MAIN = "main";
        String LOGIN = "login";
        String NETWORK_SETTING = "NETWORK_SETTING";

        String MAP_CACHE_SETTING = "MAP_CACHE_SETTING";


        String WEB = "web";
        String MAP = "map";
        String ABOUT = "ABOUT";

        String WRITE_TICKET = "WRITE_TICKET";
        String TICKET_DETAIL = "TICKET_DETAIL";
        String BEACON_WELCOME = "BEACON_WELCOME"; // 工程助手app
        String BEACON_HOME = "BEACON_HOME"; // 工程助手app
    }

    /**
     * 备件领用状态
     */
    interface SparePartUseStatus {
        String NO_USE = "maintenance_useState/notUsed"; // 未领用
        String USEING = "maintenance_useState/using"; // 领用中
        String UESED = "maintenance_useState/used"; // 已领用
        String PRE_USE = "maintenance_useState/04"; // 预领用
    }

    interface PicType {
        String TICKET_PIC = "ticketRecord";
        String WX_PIC = "WX_PIC";
    }

    // 隐患维修类型
    interface YHWXType {

        String DX = "大修";
        String JX = "检修";
        String RC = "日常";

        String DX_SYSCODE = "BEAM2005/03";
        String JX_SYSCODE = "BEAM2005/02";
        String RC_SYSCODE = "BEAM2005/01";

    }

    interface WxgdView {

        String DISPATCH_OPEN_URL = "/msService/maintenance/workRecord/workRecord/workDispatchEdit";
        String RECEIVE_OPEN_URL = "/msService/maintenance/workRecord/workRecord/workReceiptEdit";
        String EXECUTE_OPEN_URL = "/msService/maintenance/workRecord/workRecord/workExecuteEdit";
        String ACCEPTANCE_OPEN_URL = "/msService/maintenance/workRecord/workRecord/workCheckEdit";
    }

    interface IntentKey {

        String ACTIVITY_ROUTER = "ACTIVITY_ROUTER";
        String LOGIN_INVALID = "loginInvalid";
        String FIRST_LOGIN = "FIRST_LOGIN";
        String LOGOUT = "LOGOUT";
    }



    interface SPKey {
        String IS_ONLINE = "IS_ONLINE";
        String SAVE_PASSWORD = "SAVE_PASSWORD";
        String IS_NETWORK_SWITCH = "IS_NETWORK_SWITCH";
        String ACCESS_TOKEN = "ACCESS_TOKEN";
        String ACCESS_TOKEN_VALUE = "ACCESS_TOKEN_VALUE";
        String ACCESS_TOKEN_VALID_TIME = "ACCESS_TOKEN_VALID_TIME";
        //        String OLD_BAP_COOKIES = "OLD_BAP_COOKIES";
        String GW_SESSION_ID = "GWSessionId";
        String AUTH_SESSION_ID = "AUTH_SESSION_ID";

        String UPDATE_TIME = "UPDATE_TIME";
        String MY_APPS = "MY_APPS";
        String MY_ATTENTION = "MY_ATTENTION";
        String STAFF = "STAFF";

        String DEVICE_TOKEN = "DEVICE_TOKEN";

        String UHF_ENABLE = "UHF_ENABLE";
        String TEMP_MODE = "TEMP_MODE";
        String VIB_MODE = "VIB_MODE";

        String KEY_SWITCH_NOTIFY = "KEY_SWITCH_NOTIFY";
        String KEY_SWITCH_NOTIFY_SHAKE = "KEY_SWITCH_NOTIFY_SHAKE";
        String KEY_SWITCH_NOTIFY_SOUND = "KEY_SWITCH_NOTIFY_SOUND";
        String KEY_RINGTONE_NAME = "KEY_RINGTONE_NAME";
        String KEY_RINGTONE_URI = "KEY_RINGTONE_URI";

        String SEARCH_HISTORY = "SEARCH_HISTORY";
        String DATA_MANAGE_XJ_BASE_UPDATE_DATE = "DATA_MANAGE_XJ_BASE_UPDATE_DATE";
        String DATA_MANAGE_EAM_BASE_UPDATE_DATE = "DATA_MANAGE_EAM_BASE_UPDATE_DATE";
        String DATA_MANAGE_XJ_BASE_UPDATE_DATE_LONG = "DATA_MANAGE_XJ_BASE_UPDATE_DATE_LONG";
        String DATA_MANAGE_SYSTEM_CODE_DATA = "DATA_MANAGE_SYSTEM_CODE_DATA";

        String SAFE_MODULE_CONFIG_RADIUS = "SAFE_MODULE_CONFIG_RADIUS";

        String XJ_TASKS_CACHE = "XJ_TASKS_CACHE";
        String KEY_MAP_CACHE = "KEY_MAP_CACHE";
        String FIRST_OPEN_MAP = "FIRST_OPEN_MAP";

        String DATA_BASE_UPDATE_INFO = "DATA_BASE_UPDATE_INFO";

        String LONGITUADE="LONGITUADE";//定位信息
        String LATITUADE="LATITUADE";//定位信息
        String ADDRESS="";//定位信息，当前位置

        String PMO_WEBSOCKET_IP = "PMO_WEBSOCKET_IP";
    }


    interface Date {

        String TODAY = "今天";
        String YESTERDAY = "昨天";
        String TOMORROW = "明天";
        String THREEDAY = "后三天";
        String THREE_DAY = "三天内";
        String THIS_WEEK = "本周";
        String THIS_MONTH = "本月";
        String ALL = "全部";

        String ONE_WEEK = "一周";
        String ONE_MONTH = "一月";

        String COMMON_FORMATE = "yyyy-MM-dd HH:mm";
        String DATE_FORMATE = "yyyy-MM-dd HH:mm:ss";
    }

    interface Transition {    //工作流相关流程
        String SAVE = "save";       //保存\保存按钮
        String CANCEL = "cancel";   //作废
        String REJECT = "reject";   //驳回
        String SUBMIT = "submit";   //提交
        String EXECUTE = "execute";   //执行
        String SAVE_LOCAL = "saveLocal";    //保存到本地
        String SUBMIT_LOCAL = "submitLocal";    //提交到本地， 进行完整性检查
        String TRANSITION_MORE = "transitionMore";  //更多
        String SUBMIT_BTN = "submitBtn"; //提交按钮
        String ROUTER_BTN = "routerBtn"; //操作按钮
        String SAVE_BTN = "saveBtn";
        String CANCEL_CN = "作废";   //作废
        String REJECT_CN = "驳回";   //驳回
    }

    interface OperateType { //操作类型， 保存 / 提交
        String SAVE = "save";   //保存
        String SUBMIT = "submit";   //提交

        String EDIT = "edit";       //编辑

        String ADD = "add";//新增

        String REMOVE = "remove";//移除

        String APPROVE = "approve"; //审批

        String NEXT = "next"; //下一步

        String QUERY_ALL = "全部";//查询所有

        String QUERY_PEND = "待办";//仅查待办

        String QUERY_TYPE = "QUERY_TYPE";//查询类型
    }


    interface TimeString {
        String START_TIME = " 00:00:00";
        String END_TIME = " 23:59:59";
    }

    interface BAP_REQUEST_PARAM {

        String START_TIME = "startTime";
        String END_TIME = "endTime";
        String WORK_TYPE = "WORK_TYPE";
        String PLAN_STATE = "PLAN_STATE";
        String PLAN_STATE_GE = "PLAN_STATE_GE";
    }

    interface SCAN_PARAM {
        String SCAN_PRODUCT = "SCAN_PRODUCT";
        String SCAN_BATCH = "SCAN_BATCH";
    }

    interface BAPQuery {
        String BAPCODE = "BAPCODE";
        String BOOLEAN = "BOOLEAN";
        String VALUE = "VALUE";
        String BILL_STATE = "BILL_STATE";
        String INVENTORY_DATE_START = "INVENTORY_DATE_START";
        String INVENTORY_DATE_END = "INVENTORY_DATE_END";
        String BATCH_TEXT = "BATCH_TEXT";
        String BATCH_NUM = "BATCH_NUM";
        String ID = "ID";
        String NAME = "NAME";
        String BATCH_CODE = "BATCH_CODE";
        String STAFF_NAME = "STAFF_NAME";
        String MODEL = "MODEL";
        String SPECIFY = "SPECIFY";
        String PRODUCT_ID = "PRODUCT_ID";
        String PRODUCT_CODE = "PRODUCT_CODE";
        String PRODUCT_NAME = "PRODUCT_NAME";
        String PRODUCT_SPECIF = "PRODUCT_SPECIF";
        String WARE_CODE = "wareCode";
        String WARE_NAME = "WARE_NAME";
        String PLACE_SET_CODE = "PLACE_SET_CODE";
        String PLACE_SET_NAME = "PLACE_SET_NAME";
        String TABLE_NO = "TABLE_NO";
        String PLAN_CODE = "PLAN_CODE";

        String DELIVER_CODE = "DELIVER_CODE";
        String PICKSITE = "PICKSITE";

        String ORDER_DATE = "ORDER_DATE";
        String FIND_TIME = "FIND_TIME";
        String YH_DATE_START = "YH_DATE_START";
        String YH_DATE_END = "YH_DATE_END";
        String YH_AREA = "YH_AREA";
        String REPAIR_TYPE = "REPAIR_TYPE";
        String PRIORITY = "PRIORITY";
        String STATUS = "STATUS";
        String FAULT_INFO_TYPE = "FAULT_INFO_TYPE";
        String EAM_NAME = "EAM_NAME";
        String EAM_CODE = "EAM_CODE";
        String EAM_STATE = "EAM_STATE";

        String XSCK_DATE_START = "XSCK_DATE_START";
        String XSCK_DATE_END = "XSCK_DATE_END";
        String OUT_STORAGE_DATE = "OUT_STORAGE_DATE";

        String TEXT = "TEXT";
        String SYSTEMCODE = "SYSTEMCODE";
        String LONG = "LONG";
        String DATETIME = "DATETIME";
        String DATE = "DATE";
        String TYPE_NORMAL = "0";
        String TYPE_JOIN = "2";
        String LIKE = "like";
        String BE = "=";
        String GE = ">=";
        String LE = "<=";
        String BG = "<>";
        String LIKE_OPT_BLUR = "%?%";
        String LIKE_OPT_Q = "?";
        String FALSE = "0";
        String TRUE = "1";

        String OPT_YH_AREA = "=includeCustSub#BEAM_AREAS";

        /*维修工单快速查询字段条件*/
        String WORK_SOURCE = "WORK_SOURCE";//工单来源
        String WXGD_REPAIR_TYPE = "WXGD_REPAIR_TYPE"; // 维修类型（维修工单列表查询）
        String WXGD_PRIORITY = "WXGD_PRIORITY"; // 优先级（维修工单列表查询）
        String WXGD_STATE = "WORK_STATE";

        /*备件参照快速查询*/
        String BEAM_SPARE_PARTS = "BEAM_SPARE_PARTS";
        String INVENTORY_DATE = "INVENTORY_DATE";
        String ONLINE_EAM_NAME = "ONLINE_EAM_NAME";
        String ONLINE_EAM_STATE = "ONLINE_EAM_STATE";


        String OPEN_TIME_START = "OPEN_TIME_START";//时间
        String OPEN_TIME_STOP = "OPEN_TIME_STOP";//时间


        String OPEN_TIME = "OPEN_TIME";//时间
        String ON_OR_OFF = "ON_OR_OFF";//开关状态
        String EAM_EXACT_CODE = "EAM_EXACT_CODE";//精确设备编码
        String EAMCODE = "EAMCODE";//设备编码
        String EAM_TYPE = "EAM_TYPE";//设备类型
        String EAM_AREA = "EAM_AREA";//区域类型
        String EAM_AREANAME = "EAM_AREANAME";//区域类型主设备
        String IS_MAIN_EQUIP = "IS_MAIN_EQUIP";//是否主设备


        String TICKET_NO = "TICKET_NO";
        String APPLY_TIME = "APPLY_TIME";
        String DY = ">";
        String XY = "<";
        String NM = "~";
        String LCI = "[";
        String RCI = "]";
        String LOI = "(";
        String ROI = ")";

        String XJ_ID = "XJ_ID";
        String XJ_START_TIME_1 = "XJ_START_TIME_1";
        String XJ_START_TIME_2 = "XJ_START_TIME_2";
        String XJ_END_TIME_1 = "XJ_END_TIME_1";
        String XJ_END_TIME_2 = "XJ_END_TIME_2";
        String XJ_TASK_STATE = "TASK_STATE";

        // WOM：工单快速查询
        String PRODUCE_BATCH_NUM = "PRODUCE_BATCH_NUM"; // 生产批号
        String RECORD_TYPE = "RECORD_TYPE";             // 待办记录
        String EXE_STATE = "EXE_STATE";                 // 执行状态
        String FORMULA_SET_PROCESS = "SET_PROCESS";     // 配方属性
        String IS_MORE_OTHER = "IS_MORE_OTHER";         // 其他活动
        String IS_FOR_TEMP = "IS_FOR_TEMP";             // 机动活动
        String IS_FOR_ADJUST = "IS_FOR_ADJUST";         // 调整活动
        String TASK_PROCESS_ID = "TASK_PROCESS_ID";     // 工序ID
        String BAT_RECORD_STATE = "BAT_RECORD_STATE";   // 配料记录


        String WARE_RECORD = "DIRECTION";

        String START_TIME = "START_TIME";
        String END_TIME = "END_TIME";

        //时间查询
        String OUT_DATE_START = "OUT_DATE_START";
        String OUT_DATE_END = "OUT_DATE_END";
        String IN_DATE_START = "IN_DATE_START";
        String IN_DATE_END = "IN_DATE_END";

        String APPLY_DATE_START = "APPLY_DATE_START";
        String APPLY_DATE_END = "APPLY_DATE_END";

        String IN_STORAGE_DATE = "IN_STORAGE_DATE";
        String WARE_ID = "wareId";
        String GOOD_ID = "goodId";
        String SPECIFICATIONS = "SPECIFICATIONS";
        String BILL_NO = "BILL_NO";
        String CODE = "CODE";
        String EAM_INSTALL_PLACE = "EAM_INSTALL_PLACE";

        String STATE = "STATE"; //设备状态
        String REGION_NAME = "REGION_NAME"; //区域位置

        String WORK_RECORD_SOURCE = "WORK_RECORD_SOURCE";
        String WORK_RECORD_STATE = "WORK_RECORD_STATE";
        String CONTENT = "CONTENT";

        //盘点筛选时间
        String PD_DATE_START = "PD_DATE_START";
        String PD_DATE_END = "PD_DATE_END";
        String TAG_NAME = "TAG_NAME";
        String INVENTORY_STATUS = "INVENTORY_STATUS";

        //停机报警更新单个列表内容时所用的请求参数key值
        String RUNGATHER_ID = "runGatherIds";
        String CLOSED_TYPE = "closedType";
        String ClOSED_REASON = "closedReason";


        String BUSI_VERSION = "BUSI_VERSION";

        //造纸参照页面查询参数
        String PAPER_MACHINE_NAME = "PAPER_MACHINE_NAME";  //机台
        String PAPER_FARM = "PAPER_FARM";  //车间
        String PAPER_LINE = "PAPER_LINE";  //产线
        String PAPER_TASK = "PAPER_TASK";  //任务单

        String ROLL_CODE_IDENTITY = "ROLL_CODE_IDENTITY";  //卷号标识
        String PAPER_ROLL_CODE = "PAPER_ROLL_CODE";  //原纸卷号
        String RACK_CODE = "RACK_CODE";  //料架编码

        String APPLY_STATUS ="APPLY_STATUS";   //停送电单据状态

        String CONSUMABLE_SPARE_CODE="SPARE_CODE";//耗材序列号
        String CONSUMABLE_SPARE_NAME="SPARE_NAME";//耗材名称

        String CONSUMEABLE_USE_START_TIME="CONSUMEABLE_USE_START_TIME";//耗材日历选择开始
        String CONSUMEABLE_USE_END_TIME="CONSUMEABLE_USE_END_TIME";//耗材日历选择开始
        String IS_ON="IS_ON";
    }


    interface SystemCode {
        //应用类型
        String APP_TYPE_NORMAL = "base_AppType/02";//原生的页面
        String APP_TYPE_H5 = "base_AppType/01";//h5自带导航栏，不需要我们给他添加title
        String APP_TYPE_LINK = "base_AppType/03";//h5不带导航栏，需要我们给他添加title
        String APP_TYPE_OTHER_APP = "base_AppType/04";
        String APP_TYPE_OTHER = "base_AppType/05";
        String APP_TYPE_MORE = "base_AppType/-1";

        //屏幕显示类型
        String SCREEN_TYPE_DEFAULT_PORTRAIT = "SCREEN_TYPE/01";//默认竖屏，可以转动
        String SCREEN_TYPE_DEFAULT_LANDSCAPE = "SCREEN_TYPE/02";//默认横屏，可以转动
        String SCREEN_TYPE_PORTRAIT = "SCREEN_TYPE/03";//保持竖屏
        String SCREEN_TYPE_LANDSCAPE = "SCREEN_TYPE/04";//保持横屏

        //系统编码
        String WTS_ENSURETYPE = "WTS_ensureType";

        String WTS_ENSURETYPE_LOCATION = "WTS_ensureType/location";
        String WTS_ENSURETYPE_PHOTOGRAPH = "WTS_ensureType/photograph";
        String WTS_ENSURETYPE_CHECKCONFIRM = "WTS_ensureType/checkConfirm";
        String WTS_ENSURETYPE_INPUTTEXT = "WTS_ensureType/inputText";
        /**
         * 这个是自己加的
         */
        String WTS_ENSURETYPE_NECESSARY = "WTS_ensureType/necessary";

        String WTS_SAFETYTYPE_CONSTRUCTIONRISK = "WTS_safetyType/constructionRisk";//施工作业风险

        String WTS_SAFETYTYPE_PROCESSRISK = "WTS_safetyType/processRisk";//生产工艺风险

        String WTS_SAFETYTYPE_SAFEMEASURE = "WTS_safetyType/safeMeasure";//安全措施

        String WTS_SAFETYTYPE = "WTS_safetyType";//

        String WTS_STDTYPE = "WTS_stdType";   //气体分析标准选项
        String WTS_STDTYPE_NUMBER = WTS_STDTYPE + "/number";// 数值
        String WTS_STDTYPE_BOOLEAN = WTS_STDTYPE + "/boolean";// 是否
        String WTS_STDTYPE_TEXT = WTS_STDTYPE + "/text";// 文本

        String WTS_WORK_TYPE = "WTS_workType";
        String WTS_LEVELONE = "WTS_levelOne";

        String WTS_LEVELTWO = "WTS_levelTwo";


        String WTS_LEVELTHREE = "WTS_levelThree";

        String WTS_FIREWAY = "WTS_fireWay";

        String WTS_PROCESSING = "WTS_processing";

        String WTS_RESULT = "WTS_checkResult";

        String WTS_RESULT_OVER = WTS_RESULT + "workEnd";  //工作完成

        String WTS_RESULT_NORMAL = WTS_RESULT + "reNormal";  //  现场环境恢复正常

        String WTS_RESULT_SAFE = WTS_RESULT + "siteSafety";   //工作现场处于安全状态

        String WTS_LEVELONE_FIRSTLEVEL = "WTS_levelOne/firstLevel";
        String WTS_LEVELONE_SECONDLEVEL = "WTS_levelOne/secondLevel";
        String WTS_LEVELONE_SPECIALLEVEL = "WTS_levelOne/specialLevel";

        String WTS_FIREWAY_CRUSH = "WTS_fireWay/crush";//破碎
        String WTS_FIREWAY_ELECTRODRILL = "WTS_fireWay/electrodrill";//电钻
        String WTS_FIREWAY_ELECTROWEIDING = "WTS_fireWay/electrowelding";//电焊
        String WTS_FIREWAY_FLAME = "WTS_fireWay/flame";//明火
        String WTS_FIREWAY_GASCUTTING = "WTS_fireWay/gasCutting";//气割
        String WTS_FIREWAY_GLAZINGMACHINE = "WTS_fireWay/glazingMachine";//磨光机
        String WTS_FIREWAY_HAMMERING = "WTS_fireWay/hammering";//锤击

        String WTS_RESULT_QUALIFIED = "WTS_result/qualified";//合格
        String WTS_RESULT_UMQUALIFIED = "WTS_result/unqualified";//不合格

        String WTS_PROCESSING_CLOSE = "WTS_processing/close";//正常
        String WTS_PROCESSING_DELAY = "WTS_processing/delay";//延期
        String WTS_PROCESSING_STOP = "WTS_processing/stop";//中止

        //安全检查
        String SESSCM_HANDLEMODE = "SESSCM_handleMode";
        String SESSCM_TASKSTATE = "SESSCM_taskState";
        String SESSCM_CHECKRESULT = "SESSCM_checkResult";
        String SESSCM_RISKLEVEL = "SESSCM_riskLevel";
        String SESSCM_RISKTYPE = "SESSCM_riskType";

        //隐患
        String SESHRM_RISKTYPE = "SESHRM_riskType";
        String SESHRM_RISKRESOURCE = "SESHRM_riskResource";
        String SESHRM_RISKLEVEL = "SESHRM_riskLevel";
        String SESHRM_WORKRECORD = "SESHRM_workRecord";
        String SESHRM_RISKMODE = "SESHRM_riskMode";
        String SESHRM_DEVICERISKTYPE = "SESHRM_deviceRiskType";
        String SESHRM_WORKRECORDFLOW = "SESHRM_workRecordFlow";
        String SESHRM_CONFIRMRESULT = "SESHRM_confirmResult";
        String SESHRM_ACCIDENTTYPE = "SESHRM_accidentType";

        //巡检
        String PATROL_valueType = "PATROL_valueType";//值类型
        String PATROL_editType = "PATROL_editType"; //编辑方式
        String PATROL_routeType = "PATROL_routeType";//巡检类型
        String PATROL_planType = "PATROL_planType"; //巡检模式
        String PATROL_taskState = "PATROL_taskState";//巡检任务状态
        String PATROL_payCardType = "PATROL_payCardType";//刷卡类型
        String PATROL_signInType = "PATROL_signInType";//手工签到原因
        String PATROL_realValue = "PATROL_realValue";//结论
        String PATROL_passReason = "PATROL_passReason";//跳过原因
        String PATROL_taskDetailState = "PATROL_taskDetailState";//任务明细状态

        //应急指挥
        String SESECD_processState = "SESECD_processState";
        String SESECD_alarmLevel = "SESECD_alarmLevel";
        String OIL_TYPE = "OIL_TYPE";
        String SESECD_actionState = "SESECD_actionState";


        String QX_TYPE = "BEAM029";
        String YH_STATE = "BEAM2004";
        String YH_WX_TYPE = "BEAM2005"; // 维修类型
        String YH_SOURCE = "BEAM2006";
        String YH_PRIORITY = "BEAM2007"; //优先级
        //维修工单
        String WXGD_SOURCE = "BEAM2003";// 工单来源
        String CHECK_RESULT = "BEAM033"; // 验收结论
        String WXGD_STATE = "BEAM049";

        String BASESET_EAMSTATE = "BaseSet_eamState"; //设备状态

        // 生产工单管理
        String REJECT_REASON = "WOM_rejectReason";          // 拒签原因
        String RETIREMENT_STATE = "WOM_retirementState";    // 退废状态

        String maintenance_workRecordSource = "maintenance_workRecordSource";
        String EAM_workRecordState = "EAM_workRecordState";
        String EAM_oilType = "EAM_oilType";
        String MAINTENANCE_CHECK_RESULT = "maintenance_checkResult";

        String EQPTOperation_closedType = "EQPTOperation_closedType";

        String EAM_SPECIAL_DEVICE = "BaseSet_specialType";  //特种设备类型
        String EAM_CHECK_TYPE = "BaseSet_wayType"; //检查方式
        String EAM_SIDE_TYPE = "BaseSet_sideType"; //内外检
        String EAM_PRAESE_LEVEL = "BaseSet_ventRts"; //压力等级
        String EAM_SAFE_LEVEL = "BaseSet_saveRtg"; //安全等级等级
        String EAM_PRECISION = "BaseSet_precision"; //精确度
        String EAM_MEATYPE = "BaseSet_meatype"; //检定类型
        String EAM_MEACLASS = "BaseSet_meaClass"; //类别
        String EAM_PURPOSE = "BaseSet_eamPurpose"; //设备用途
        String EAM_MEASURETYPE = "BaseSet_measureType";

        String MAINTENANCE_USESTATE = "maintenance_useState"; ///领用状态

        //连续生产
        String TASK_TASKSCOPE = "Task_taskScope";//任务范围

        String Consumable_offReason="Consumable_offReason";
    }

    /**
     * 我的流程(处理过的)单据状态
     */
    interface TableStatus_CH {
        String PRE_DISPATCH = "待派工";
        String PRE_EXECUTE = "待执行";
        String PRE_ACCEPT = "待验收";
        String PRE_NOTIFY = "待通知";
        String END = "已结束";
        String CANCEL = "作废";

        String EDIT = "编辑";
        String DISPATCH = "派工";
        String EXECUTE = "执行";
        String EXECUTE_NOTIFY = "执行,通知";
        String NOTIFY = "通知";
        String ACCEPT = "验收";
        String RECALL = "撤回";
        String REVIEW = "审核";
        String CONFIRM = "接单(确认)";
        String OVER = "结束";

        String TAKE_EFFECT = "生效";

        String FIREWORK = "firework";  //动火作业
        String LIMITSPACE = "limitSpace"; //受限空间作业(只有在受限的)
        String LIMITSPACEWORK = "limitSpaceWork"; //受限空间作业
        String BLOCKWORK = "blockWork"; //盲板抽堵作业
        String HEIGHTWORK = "heightWork"; //高处作业
        String LIFTWORK = "liftWork"; //吊装作业
        String ELECTRICITYWORK = "electricity"; //临时用电作业
        String SOILWORK = "soilWork"; //动土作业
        String BREAKWORK = "breakWork"; //断路作业


        String TICKET_EDIT = "Edit";

        String TICKET_GAS = "Gas";

        String TICKET_RISK = "Risk";

        String TICKET_APPROVAL = "Approval";

        String TICKET_DEAL = "Deal";

        String TICKET_CLOSE = "Close";

        String WTS_PROCESSING_DELAY = "WTS_processing/delay";//延期
        String WTS_PROCESSING_CLOSE = "WTS_processing/close";//正常
        String WTS_PROCESSING_STOP = "WTS_processing/stop";//中止
    }

    /**
     * 模块授权：传入各自模块的url，此处只截取url的模块开头第一部分
     */
    interface ModuleAuthorization {
        String BEAM2 = "/BEAM2/";       // 设备模块
        String mobileEAM = "/mobileEAM/";  // 移动设备巡检
        String WOM = "/WOM/";        // 工单
    }

    interface ModuleCode {
        String BEAM2 = "BEAM2";       // 设备模块
        String mobileEAM = "mobileEAM";  // 移动设备巡检
        String WOM = "WOM";        // 工单
    }

    interface WebUrl {
        String HAS_TITLE = "HAS_TITLE";
        String SCREEN_ORIENT = "SCREEN_ORIENT";
        String WEB_TITLE = "WEB_TITLE";

        String TD_LIST = "/BEAMEle/onOrOff/onoroff/eleOffList.action?__pc__=QkVBTUVsZV8xLjAuMF9vbk9yT2ZmX2VsZU9mZkxpc3Rfc2VsZnw_&workFlowMenuCode=BEAMEle_1.0.0_onOrOff_eleOffList&openType=page&clientType=android";
        String SD_LIST = "/BEAMEle/onOrOff/onoroff/eleOnList.action?__pc__=QkVBTUVsZV8xLjAuMF9vbk9yT2ZmX2VsZU9uTGlzdF9zZWxmfA__&workFlowMenuCode=BEAMEle_1.0.0_onOrOff_eleOnList&openType=page&clientType=android";

//        String P_LIST_CLIENT = "&clientType=android";
//        String P_LIST_DH = "&__pc__=SFNFMl84LjAuMC4wX2ZpcmVXb3JrX2NvbmZvcm1MaXN0X3NlbGZ8"+P_LIST_CLIENT;
//        String P_LIST_DT = "&__pc__=SFNFMl84LjAuMC4wX3NvaWxXb3JrX2NvbmZvcm1MaXN0X3NlbGZ8"+P_LIST_CLIENT;
//        String P_LIST_SXKJ = "&__pc__=SFNFMl84LjAuMC4wX2xpbWl0U3BhY2VXb3JrX2NvbmZvcm1MaXN0X3NlbGZ8"+P_LIST_CLIENT;
//        String P_LIST_LSYD = "&__pc__=SFNFMl84LjAuMC4wX2VsZWN0cmljaXR5V29ya19jb25mb3JtTGlzdF9zZWxmfA__"+P_LIST_CLIENT;
//        String P_LIST_GCZY = "&__pc__=SFNFMl84LjAuMC4wX2hlaWdodFdvcmtfY29uZm9ybUxpc3Rfc2VsZnw_"+P_LIST_CLIENT;
//        String P_LIST_DZ = "&__pc__=SFNFMl84LjAuMC4wX2xpZnRXb3JrX2NvbmZvcm1MaXN0X3NlbGZ8"+P_LIST_CLIENT;
//        String P_LIST_CDMB = "&__pc__=SFNFMl84LjAuMC4wX2Jsb2NrV29ya19jb25mb3JtTGlzdF9zZWxmfA__"+P_LIST_CLIENT;
//        String P_LIST_DL = "&__pc__=SFNFMl84LjAuMC4wX2JyZWFrV29ya19jb25mb3JtTGlzdF9zZWxmfA__"+P_LIST_CLIENT;
//
//
//        String P_DH_LIST = "/HSE2/fireWork/fireWork/conformList.action?workFlowMenuCode=HSE2_8.0.0.0_fireWork_conformList"+P_LIST_DH;
//        String P_DT_LIST = "/HSE2/soilWork/soilWork/conformList.action?workFlowMenuCode=HSE2_8.0.0.0_soilWork_conformList"+P_LIST_DT;
//        String P_SXKJ_LIST = "/HSE2/limitSpaceWork/limitSpaceWork/conformList.action?workFlowMenuCode=HSE2_8.0.0.0_limitSpaceWork_conformList"+P_LIST_SXKJ;
//        String P_LSYD_LIST = "/HSE2/electricityWork/electricityWork/conformList.action?workFlowMenuCode=HSE2_8.0.0.0_electricityWork_conformList"+P_LIST_LSYD;
//        String P_GC_LIST = "/HSE2/heightWork/heightWork/conformList.action?workFlowMenuCode=HSE2_8.0.0.0_heightWork_conformList"+P_LIST_GCZY;
//        String P_DZ_LIST = "/HSE2/liftWork/liftWork/conformList.action?workFlowMenuCode=HSE2_8.0.0.0_liftWork_conformList"+P_LIST_DZ;
//        String P_CDMB_LIST = "/HSE2/blockWork/blockWork/conformList.action?workFlowMenuCode=HSE2_8.0.0.0_blockWork_conformList"+P_LIST_CDMB;
//        String P_DL_LIST = "/HSE2/breakWork/breakWork/conformList.action?workFlowMenuCode=HSE2_8.0.0.0_breakWork_conformList"+P_LIST_DL;
    }

    interface MobileWeb {

        String TSD_LIST = "/BEAMEle/onOrOff/onoroff/eleOffList.action";

    }


    /**
     * 八大票种
     */
    interface TicketType {
        String blockWork = "maintenance_workTicketType/blockWork"; // 抽堵盲板作业
        String breakWork = "maintenance_workTicketType/breakWork"; // 断路安全作业
        String electricityWork = "maintenance_workTicketType/electricityWork"; // 临时用电作业
        String fireWork = "maintenance_workTicketType/fireWork"; // 动火安全作业
        String heightWork = "maintenance_workTicketType/heightWork"; // 高处安全作业
        String liftWork = "maintenance_workTicketType/liftWork"; // 吊装安全作业
        String limitSpaceWork = "maintenance_workTicketType/limitSpaceWork"; // 受限空间作业
        String soilWork = "maintenance_workTicketType/soilWork"; // 动土安全作业
    }

    interface TicketType_CN {
        String WTS_workType = "WTS_workType";//作业票类型

        String FireWork = "WTS_workType/fireWork";
        String BlockWork = "WTS_workType/blockWork";
        String BreakWork = "WTS_workType/breakWork";
        String ElectricityWork = "WTS_workType/electricityWork";
        String HeightWork = "WTS_workType/heightWork";
        String LiftWork = "WTS_workType/liftWork";
        String LimitSpaceWork = "WTS_workType/limitSpaceWork";
        String SoilWork = "WTS_workType/soilWork";

        String FireViewCode = "WTS_1.0.0_workTicket_fireworkEdit";
        String BlockViewCode = "WTS_1.0.0_workTicket_blockWorkEdit";
        String BreakViewCode = "WTS_1.0.0_workTicket_breakWorkEdit";
        String LiftViewCode = "WTS_1.0.0_workTicket_liftWorkEdit";
        String LimitSpaceViewCode = "WTS_1.0.0_workTicket_limitSpaceWorkEdit";
        String HeightViewCode = "WTS_1.0.0_workTicket_heightWorkEdit";
        String ElectricityViewCode = "WTS_1.0.0_workTicket_electricityEdit";
        String SoilViewCode = "WTS_1.0.0_workTicket_soilWorkEdit";

        String blockWork = "抽堵盲板作业"; // 抽堵盲板作业
        String breakWork = "断路安全作业"; // 断路安全作业
        String electricityWork = "临时用电作业"; // 临时用电作业
        String fireWork = "动火安全作业"; // 动火安全作业
        String heightWork = "高处安全作业"; // 高处安全作业
        String liftWork = "吊装安全作业"; // 吊装安全作业
        String limitSpaceWork = "受限空间作业"; // 受限空间作业
        String soilWork = "动土安全作业"; // 动土安全作业
    }

    //如果分渠道不分包的话，这里一个统一的xiaomi的key
    //小米推送使用的key
    String MiPushAppId = "2882303761518264187";
    String MiPushAppKey = "5241826432187";

    //页面之间传递的参数
    public static String INTENT_EXTRA_ID = "INTENT_EXTRA_ID";
    public static String INTENT_EXTRA_OBJECT = "INTENT_EXTRA_OBJECT";
    public static String INTENT_EXTRA_OBJECT_CHAT = "INTENT_EXTRA_OBJECT_CHAT";
    public static String INTENT_EXTRA_INT = "INTENT_EXTRA_INT";
    public static String INTENT_EXTRA_BOOLEAN = "INTENT_EXTRA_BOOLEAN";


    interface MapKey {

        //销售出库
        String XSCK_GOODNAME = "material_1_0_0_saleOut_saleOutList_LISTPT_ASSO_b302e56f_2262_499b_b85d_79aa786ffc54";//物料名称
        String XSCK_GOODCODE = "material_1_0_0_saleOut_saleOutList_LISTPT_ASSO_88b7cb47_1f77_4a1c_996b_a024fb79e8b3";//物料编码
        String XSCK_OUTNUM = "material2_7_5_0_0_saleOut_saleOutList_LISTPT_ASSO_9217f145_ddf5_4237_9d8c_b29a0542dc70";//出库数量

        //生产入库
        String SCRK_GOODNAME = "material_1_0_0_produceInSingles_productInSingleList_LISTPT_ASSO_4033a30c_c097_427b_a736_c7e3482c8989";//物料名称
        String SCRK_GOODCODE = "material_1_0_0_produceInSingles_productInSingleList_LISTPT_ASSO_10dbfcb5_8017_4a56_ac51_54918cd9b306";//物料编码
        String SCRK_INNUM = "material_1_0_0_produceInSingles_productInSingleList_LISTPT_ASSO_82287119_18f8_4d6c_b98b_d06106b2ff6f";//入库数量


        //生产出库
        String SCCK_GOODNAME = "material_1_0_0_produceOutSingle_produceOutSingleList_LISTPT_ASSO_a8a3ea60_cc71_4786_a9a0_47b98a6b95ef";//物料名称
        String SCCK_GOODCODE = "material_1_0_0_produceOutSingle_produceOutSingleList_LISTPT_ASSO_8894d1d1_3f85_495c_a79d_ad945c919b8e";//物料编码
        String SCCK_OUTNUM = "material_1_0_0_produceOutSingle_produceOutSingleList_LISTPT_ASSO_446083aa_4352_47f3_8512_83dcb05dbed5";//出库数量


        //其他出库
        String QTCK_GOODNAME = "material_1_0_0_otherOutSingle_otherOutList_LISTPT_ASSO_d04bf1cf_694e_4de2_b03d_dd4699db2c62";//物料名称
        String QTCK_GOODCODE = "material_1_0_otherOutSingle_list_LISTPT_ASSO_58f13e05_f571_431d_9ecf_9a006a96bc06";//物料编码
        String QTCK_OUTNUM = "material_1_0_0_otherOutSingle_otherOutList_LISTPT_ASSO_e01bbc56_5932_433b_90c1_89840a0229af";//出库数量

        //其他入库
        String QTRK_GOODNAME = "material_1_0_0_otherInSingle_inSingleList_LISTPT_ASSO_d5365083_a4db_4467_8876_4971e47259bb";//物料名称
        String QTRK_GOODCODE = "material_1_0_0_otherInSingle_inSingleList_LISTPT_ASSO_6171cf81_2361_4b77_a655_b2b9e1c02dd2";//物料编码
        String QTRK_INNUM = "material_1_0_0_otherInSingle_inSingleList_LISTPT_ASSO_90dcba1f_273d_471a_a86e_c857f340a16b";//入库数量

        //采购入库
        String CGRK_GOODNAME = "material_1_0_0_purchaseInSingles_purchaseInSingleList_LISTPT_ASSO_e595958e_ca44_42f1_a969_563974ccb760";//物料名称
        String CGRK_INNUM = "material_1_0_0_purchaseInSingles_purchaseInSingleList_LISTPT_ASSO_5176ad7a_3c9e_49ff_9402_b78003b65315";//物料数量
        String CGRK_GOODCODE = "material_1_0_0_purchaseInSingles_purchaseInSingleList_LISTPT_ASSO_aa6d5623_9b21_4a5b_8feb_fa1fd63990f7";//物料编码

        //领用出库
        String LLCK_GOODNAME = "material_1_0_acquireOutSingle_useOutList_LISTPT_ASSO_f6c8df8b_4193_4f52_b7d6_11094728ca11";//物料名称
        String LLCK_GOODCODE = "material_1_0_acquireOutSingle_useOutList_LISTPT_ASSO_1e1ebe55_359b_4d02_8390_d6dadc6945f5";//物料编码
        String LLCK_OUTNUM = "material_1_0_acquireOutSingle_useOutList_LISTPT_ASSO_1c6bac5e_2fad_416e_9040_72e0a28a1caf";//出库数量
        String LLCK_PARAMA = "charparama";//接收方

    }

    interface Direction {
        String receive = "directionReceive";
        String send = "directionSend";
    }

    interface State {
        String checkState1 = "无需检验";
        String checkState2 = "待检";
        String checkState3 = "合格";
        String checkState4 = "不合格";

        String passState1 = "无需放行";
        String passState2 = "待放行";
        String passState3 = "合格待放行";
        String passState5 = "已放行";
    }

}
