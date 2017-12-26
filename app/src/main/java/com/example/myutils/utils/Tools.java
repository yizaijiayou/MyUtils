package com.example.myutils.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myutils.R;
import com.example.myutils.utils.systemUtils.L;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.TELEPHONY_SERVICE;


/**
 * 项目名称： LegalHigh
 * 创 建 人： 艺仔加油
 * 创建时间： 2017/8/10 15:40
 * 本类描述：
 * 1.隐藏身份证
 * 2.验证身份证 加 验证身份证的出生日期
 * 3.判断是否有安装微信
 * 4.调用系统拨打电话
 * 5.判断电话号码
 * 6.防止快速点击
 * 7.根据年月日查询星期几
 * 8.图库的申请和获取图库所选择的文件
 * 9.获取相机和获取相机拍照的文件
 * 10.判断是否有SIM卡
 * 11.隐藏手机号码
 * 12.判断email格式是否正确
 * 13.获取像素点的倍数
 */

public class Tools {

    /**
     * 1.  441203***********46  隐藏身份证
     *
     * @param idCard 身份证
     * @return
     */
    public static String hideIDCard(String idCard) {
        if (!TextUtils.isEmpty(idCard)) {
            String start = idCard.substring(0, 6);
            String end = idCard.substring(idCard.length() - 2, idCard.length());
            return start + "*******" + end;
        }
        return "";
    }

    /**
     * 2.验证身份证加验证身份证的出生日期
     *
     * @param context 上下文对象
     * @param idcard  身份证
     * @return
     */
    public static boolean getIDCard(Context context, String idcard) {
        try {
            if (idcard.length() == 18) {
                SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
                String regexIdCard = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
                Matcher m = Pattern.compile(regexIdCard).matcher(idcard);
                String birth = idcard.substring(6, idcard.length() - 4); //身份证的出生年月日
                int year = Integer.parseInt(birth.substring(0, 4));//身份证的年
                int nowYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date(System.currentTimeMillis())));//当前的年
                long time = s.parse(birth).getTime();//身份证日期的毫秒
                long nowTime = System.currentTimeMillis() - 86400000;//当前的毫秒  （间隔一天）

                if (m.find()) {
                    if (time < nowTime) {
                        if ((nowYear - 200) < year) {
                            return true;
                        } else {
                            Toast.makeText(context, "请输入200年内的身份证号码", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "请输入不大于今天的身份证号码", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "请输入正确的身份证号码", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "请输入18位的身份证号码", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            L.e("Tools", "身份证验证发生异常");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 3.判断是否有安装微信
     *
     * @param context 上下文对象
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 4.调用系统拨打电话
     *
     * @param context 上下文
     * @param phone   电话号码
     */
    public static void SystemCallPhone(final Context context, final String phone) {
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(context, "很抱歉，该网点未预留电话号码", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder alerDialog = new AlertDialog.Builder(context);
            alerDialog.setTitle("提示");
            alerDialog.setNegativeButton("取消", null);
            alerDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
            alerDialog.setMessage("确认拨打：" + phone);
            alerDialog.create().show();
        }
    }

    /**
     * 5.判断电话号码 （项目的电话是国外的）
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 6.防止快速点击
     * 不填默认1秒，单位为秒 填 1 | 2等等
     */
    public static long lastClick = 0;

    public static boolean fastClick(int x) {
        if (System.currentTimeMillis() - lastClick <= (x * 1000)) {
            return false;
        }
        lastClick = System.currentTimeMillis();
        return true;
    }

    public static boolean fastClick() {
        if (System.currentTimeMillis() - lastClick <= 1000) {
            return false;
        }
        lastClick = System.currentTimeMillis();
        return true;
    }

    /**
     * 7.根据年月日查询星期几
     */
    //传入 年 ， 月  ，日
    public static String getWeek(String year, String month, String day) {
        String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
        String week = weeks[calendar.get(Calendar.DAY_OF_WEEK)];
        return "星期" + week;
    }

    //传入形式 2017-08-10
    public static String getWeek(String date) {
        String year = date.split("-")[0];
        String month = date.split("-")[1];
        String day = date.split("-")[2];
        String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
        String week = weeks[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        return "星期" + week;
    }

    public final static int OPEN_PICTURE = 1000;//图库
    public final static int OPEN_CAREMA = 1001;//相机

    /**
     * 8.图库的申请和获取图库所选择的文件
     *
     * @param activity
     */
    public static void Picture(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, OPEN_PICTURE);
        } else {
            activity.startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), OPEN_PICTURE);
        }
    }

    public static File getPictureFile(Activity activity, Uri uri) {
        L.out("getPictureFile==" + uri.toString());
        try {
            if (uri != null) {
                String s = uri.toString().split(":")[0];
                if (s.equals("file")) {//     file:///storage/emulated/0/MIUI/wallpaper/sb10063035c-001_%26_f7cd5d79-6e01-419a-b668-130c19b481db.jpg
                    return new File(new URI(uri.toString()));
                } else {//    content://media/external/images/media/33
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = activity.getContentResolver().query(uri, proj, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String filePath = cursor.getString(column_index);
                    cursor.close();
                    return new File(filePath);
                }
            }
        } catch (URISyntaxException e) {
            Log.e("Tools", "获取图图片出现异常");
            e.printStackTrace();
        }
        Log.e("Tools", "图库图片Uri空值");
        return null;
    }

    /**
     * 9.获取相机和获取相机拍照的文件
     */
    private static File output;
    private static Uri imageUri;

    public static void getCarema(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, OPEN_CAREMA);
        } else {
            MakeCarema(activity);
        }
    }

    private static void MakeCarema(Activity activity) {
        File file = new File(Environment.getExternalStorageDirectory(), "拍照");
        if (!file.exists()) {
            file.mkdir();
        }
        output = new File(file, "takeCarema.jpg");
//-----------------------------------------------------------------------------
        try {
            if (output.exists())
                output.delete();
            output.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
//-----------------------------------------------------------------------------
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(activity.getApplicationContext(), "com.example.administrator.zsandroid.fileprovider", output);
        } else {
            imageUri = Uri.fromFile(output);
        }
        activity.startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, imageUri), OPEN_CAREMA);
    }

    //获取拍照的Uri
    public static Uri getCaremaUri() {
        if (imageUri != null)
            return imageUri;
        Log.e("Tools", "拍照获取到的照片为空值");
        return null;
    }

    public static File getCaremaFile() {
        if (output != null)
            return output;
        Log.e("Tools", "拍照获取到的照片为空值");
        return null;
    }

    /**
     * 10、判断是否有SIM卡
     *
     * @param context
     * @return
     */
    public static boolean IsHasSim(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        String simSer = tm.getSimSerialNumber();
        if (TextUtils.isEmpty(simSer)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 11.隐藏手机号码
     *
     * @return
     */
    public static String getLast4phoneNumber(String str) {
        if (str.length() < 7)
            return str;
        else
            return str.substring(0, 3) + "****" + str.substring(str.length() - 4, str.length());
    }
    /**
     * 12 .判断email格式是否正确
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 13.获取像素点的倍数
     * 1080  : 3
     */
    public static float getDesity(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.density;
    }

}
