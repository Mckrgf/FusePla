package com.supcon.mes.middleware.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.supcon.mes.middleware.PLAApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by wangshizhan on 2017/8/18.
 * Email:wangshizhan@supcon.com
 */

public class Util {
    /**
     * 格式化string
     *
     * @param str
     * @return
     */
    public static String strFormat(Object str) {
        if (str != null && !TextUtils.isEmpty(str.toString())) {
            return str.toString();
        }
        return "--";
    }
    /**
     * 浮点型保留两位小数
     *
     * @param d
     * @return
     */
    public static String bigTo2(Float d) {
        if (d == null || d == 0) {
            return "";
        }
        BigDecimal d1 = new BigDecimal(Double.toString(d));
        BigDecimal d2 = new BigDecimal(Integer.toString(1));
        // 四舍五入,保留2位小数
        return d1.divide(d2, 2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * num转string
     *
     * @param l
     * @return
     */
    public static String numToStr(Number l) {
        if (l == null) {
            return "";
        }
        String str = String.valueOf(l);

        return str;
    }
    
    /**
     * 格式化string
     *
     * @param str
     * @return
     */
    public static String strFormat2(Object str) {
        if (str != null && !TextUtils.isEmpty(str.toString())) {
            return str.toString();
        }
        return "";
    }
    
    public static int strToInt(String str) {
        if (str == null || TextUtils.isEmpty(str) || !TextUtils.isDigitsOnly(str)) {
            return 0;
        }
        try {
            Integer integer = Integer.valueOf(str);
            return integer;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
 * 浮点型保留两位小数
 *
 * @param d
 * @return
 */
public static String big(Float d) {
    if (d == null || d == 0) {
        return "";
    }
    BigDecimal d1 = new BigDecimal(Double.toString(d));
    BigDecimal d2 = new BigDecimal(Integer.toString(1));
    // 四舍五入,保留2位小数
    return d1.divide(d2, 2, BigDecimal.ROUND_HALF_UP).toString();
}
    
    /**
     * 浮点型保留两位小数
     *
     * @param d
     * @return
     */
    public static String big0(Float d) {
        if (d == null || d == 0) {
            return "0";
        }
        BigDecimal d1 = new BigDecimal(Double.toString(d));
        BigDecimal d2 = new BigDecimal(Integer.toString(1));
        // 四舍五入,保留2位小数
        return d1.divide(d2, 2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 浮点型保留两位小数
     *
     * @param d
     * @return
     */
    public static String big2(Float d) {
        if (d == null || d == 0) {
            return "";
        }
        BigDecimal d1 = new BigDecimal(Double.toString(d));
        BigDecimal d2 = new BigDecimal(Integer.toString(1));
        // 四舍五入,保留2位小数
        return d1.divide(d2, 2, BigDecimal.ROUND_HALF_UP).toString();
    }
    public static String big6(Float d) {
        if (d == null || d == 0) {
            return "";
        }

        String s2=d.toString();
        if (s2.contains("E")){
            s2=new BigDecimal(d).toString();
        }
        String s1="";
        if (s2.contains(".")){
            String s=s2.substring(s2.indexOf(".")+1);
            if (s.length()==0){
                s1=d+"000000";
            } else if (s.length()==1){
                s1=d+"00000";
            }else if (s.length()==2){
                s1=d+"0000";
            }else if (s.length()==3){
                s1=d+"000";
            }else if (s.length()==4){
                s1=d+"00";
            }else if (s.length()==5) {
                s1=d+"0";
            }else if (s.length()==6) {
                s1=s2;
            }else if (s.length()>6){
                s1=String.format("%.6f",d);
            }
        }else {
            s1=s2+".000000";
        }
        return s1;
    }

    /**
     * 浮点型保留6位小数
     * @param d
     * @return
     */
    public static String big6(String d) {
        if (TextUtils.isEmpty(d)) {
            return "";
        }
        float quality=Float.valueOf(d);
        if (quality==0){
            return "";
        }
        String s1="";
        if (d.contains(".")){
            String s=d.substring(d.indexOf(".")+1);
            if (s.length()==0){
                s1=d+"000000";
            } else if (s.length()==1){
                s1=d+"00000";
            }else if (s.length()==2){
                s1=d+"0000";
            }else if (s.length()==3){
                s1=d+"000";
            }else if (s.length()==4){
                s1=d+"00";
            }else if (s.length()==5) {
                s1=d+"0";
            }else if (s.length()==6) {
                s1=d;
            }else if (s.length()>6){
                s1=String.format("%.6f",d);
            }
        }else {
            s1=d+".000000";
        }
        return s1;

//        BigDecimal bigDecimal =  BigDecimal.valueOf(d);
//        return bigDecimal.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
    }
    
    /**
     * 从SD读取对应文件转String输出
     *
     * @param filePath
     * @return
     */
    public static final View EMPTY_VIEW = new View(PLAApplication.getAppContext());
    
    public static String getFileFromSD(String filePath) {
        StringBuilder result = new StringBuilder();
        
        try {
            FileInputStream f = new FileInputStream(filePath);
            BufferedReader bis = new BufferedReader(new InputStreamReader(f));
            String line = "";
            while ((line = bis.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
        
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath
     * @return boolean
     */
    public static boolean fileIsExists(String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static String list2Str(List<String> list){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(",");
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static Map<String, Object> gsonToMaps(String gsonString) {
        Map<String, Object> map = null;
        Gson gson = new Gson();
        map = (gson.fromJson(gsonString, new TypeToken<Map<String, Object>>() {
        }.getType()));
        return map;
    }
    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
    
    public static int countStr(String str1, String str2) {
        int index = 0; //定义变量。记录每一次找到的key的位置。
        int count = 0; //定义变量，记录出现的次数。
        
        while ((index = str1.indexOf(str2, index)) != -1) {
            index = index + str2.length();
            count++;
        }
        return count;
    }
    /**
     * 从asset路径下读取对应文件转String输出
     *
     * @param mContext
     * @return
     */
    public static String getJson(Context mContext, String fileName) {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        AssetManager am = mContext.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    am.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
    }

    /**
     * 从asset路径下读取图片文件
     *
     * @param
     * @return
     */
    public static Bitmap getAssetsBitmap(Context context, String assetsName){
        AssetManager asm = context.getAssets();
        InputStream inputStream = null;
        Bitmap bitmap=null;
        try {
            inputStream = asm.open(assetsName);
            Drawable d =Drawable.createFromStream(inputStream, null);
            bitmap= ((BitmapDrawable) d).getBitmap();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bitmap;
    }



    /**
     * 隐藏虚拟按键，并且全屏
     *
     * @param activity
     */
    public static void hideBottomUIMenu(Activity activity) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    
    public static void setHideVirtualKey(Window window) {
        //保持布局状态
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions |= 0x00001000;
        } else {
            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        window.getDecorView().setSystemUiVisibility(uiOptions);
    }
    
    
    public static View getDecorView(Dialog dialog) {
        
        Object o = new Object();
        
        Field[] fields = dialog.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                if (field.getName().equals("mDecor")) {
                    field.setAccessible(true);
                    return (View) field.get(o);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;
        
    }
    
    public static void toggleKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInputFromWindow(editText.getWindowToken(), 0, 0);//显示、隐藏
    }
    
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
    
    
    public static StateListDrawable getSelector(Drawable normalDraw, Drawable pressedDraw) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{
                android.R.attr.state_selected,
                android.R.attr.state_pressed,
                android.R.attr.state_focused}, pressedDraw);
        stateListDrawable.addState(new int[]{}, normalDraw);
        return stateListDrawable;
    }


    public static void setPaddingAll(View v, int paddingInDp) {
        v.setPadding(
                dpToPx(v.getContext(), paddingInDp),
                dpToPx(v.getContext(), paddingInDp),
                dpToPx(v.getContext(), paddingInDp),
                dpToPx(v.getContext(), paddingInDp));

    }
    
    public static void setPaddingRight(View v, int paddingInDp) {
        v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), dpToPx(v.getContext(), paddingInDp), v.getPaddingBottom());
    }
    
    public static int dpToPx(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    
    /**
     * 构建表单
     *
     * @param zipFile 压缩文件
     * @return 返回表单
     */
    public static List<MultipartBody.Part> createZipFileForm(File zipFile) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        RequestBody zipRequestBody = RequestBody.create(MediaType.parse("application/zip"), zipFile);
        builder.addFormDataPart("zipFile", zipFile.getName(), zipRequestBody);
        return builder.build().parts();
    }
    
    public static void setMarginLeft(View v, int l) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            final int ol = ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).leftMargin;
            final int or = ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).rightMargin;
            final int ot = ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).topMargin;
            final int ob = ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).bottomMargin;
            p.setMargins(l == -1 ? ol : l, ot, or, ob);
            v.requestLayout();
        }
    }

    /**
     * 浮点型保留两位小数
     *
     * @param d
     * @return
     */
    public static float big2Float(Float d) {
        if (d == null || d == 0) {
            return 0;
        }
        BigDecimal d1 = new BigDecimal(Double.toString(d));
        BigDecimal d2 = new BigDecimal(Integer.toString(1));
        return d1.divide(d2, 2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 保留小数点后两位
     * @param d
     * @return
     */
    public static String big2Long(Long d) {
        if (d == null || d == 0) {
            return String.valueOf(0);
        }
        DecimalFormat d1 = new DecimalFormat("0.00");
        return d1.format(d);
    }

    public static String big2Floats(Float d) {
        if (d == null || d == 0) {
            return String.valueOf(0);
        }
        DecimalFormat d1 = new DecimalFormat("0.00");
        return d1.format(d);
    }

    public static float strToFloat(String str) {
        if (str == null || TextUtils.isEmpty(str)) {
            return 0;
        }
        try {
            Float aFloat = Float.valueOf(str);
            return aFloat;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取前n天日期、后n天日期
     * @param targetDate 目标日期 就是你要从哪天开始计算
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @param dateFormat 时间格式
     * @return
     */
    public static String getOldDate(long targetDate, int distanceDay, String dateFormat) {
        SimpleDateFormat dft = new SimpleDateFormat(dateFormat);
        Date beginDate = new Date(targetDate);
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }

    public static boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    /**
     *  android 7.0系统解决拍照的问题
     * */
    public static void initPhotoError(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
}
