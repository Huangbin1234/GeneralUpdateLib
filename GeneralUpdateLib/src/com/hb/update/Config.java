package com.hb.update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.hb.bean.VersionBean;
import com.hb.generalupdate.R;
import com.hb.http.HttpRequestService;
import com.hb.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class Config {
    public static int NEW_VER_CODE = 0;
    public static int NEW_VER_NAME;
    public static String NEW_R_VER_NAME;
    public static String UPDATE_CONTENT;
    public static String APK_DOWNLOAD_URL;
    public String SERVER_JSON_URL = "";// 服务器JSON文件地址



    public Config(String jsonUrl) {
        this.SERVER_JSON_URL = jsonUrl;
    }

    /**
     * 获取APP名
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        return context.getResources().getText(R.string.app_name).toString();
    }


    /**
     * 获取当前版本号
     *
     * @param context
     * @return
     */
    public int getVerCode(Context context) {
        int verCode = -1;
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            verCode = packInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // 0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            verName = packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 获取服务器的版本号
     *
     * @return
     */
    public int getServerVerCode() {
        try {
            VersionBean versionBean = updateVersion();
            if (versionBean == null) {
                return NEW_VER_CODE;
            }
            NEW_VER_CODE = Integer.parseInt(versionBean.getVerCode());
            NEW_VER_NAME = Integer.parseInt(StringUtils.getNumbers(versionBean.getVerName()));
            NEW_R_VER_NAME=versionBean.getVerName();
            APK_DOWNLOAD_URL = versionBean.getDownloadUrl();
            UPDATE_CONTENT = versionBean.getContent();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return NEW_VER_CODE;
    }

    /**
     * 获取服务器的版本名
     *
     * @return
     */
    public int getServerVerName() {
        try {
            VersionBean versionBean = updateVersion();
            if (versionBean == null) {
                return NEW_VER_CODE;
            }
            NEW_VER_CODE = Integer.parseInt(versionBean.getVerCode());
            NEW_R_VER_NAME=versionBean.getVerName();
            NEW_VER_NAME = Integer.parseInt(StringUtils.getNumbers(versionBean.getVerName()));
            UPDATE_CONTENT = versionBean.getContent();
            APK_DOWNLOAD_URL = versionBean.getDownloadUrl();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return NEW_VER_NAME;
    }

    /**
     * JSON字符串解析
     *
     * @return
     */
    public VersionBean updateVersion() {
        VersionBean versionBean = new VersionBean();
        String result = HttpRequestService.httpPost(SERVER_JSON_URL);
        if (!StringUtils.isBlank(result)) {
            try {
                // 解决UTF-8 另存文件是BOM打头造成解析JSON错误的问题
                if (result.startsWith("\ufeff")) {
                    result = result.substring(1);
                }
                JSONObject object = new JSONObject(result);
                if (object.has("android")) {
                    String android = object.getString("android");
                    if (!StringUtils.isBlank(android)) {
                        JSONObject jsonObject = new JSONObject(android);
                        if (jsonObject.has("app_one")) {
                            String app_one = jsonObject.getString("app_one");
                            JSONObject jsonFinal = new JSONObject(app_one);

                            String verCode = jsonFinal.getString("verCode");
                            String verName = jsonFinal.getString("verName");
                            String content = jsonFinal.getString("content");
                            String downloadUrl = jsonFinal.getString("downloadUrl");

                            versionBean.setVerCode(verCode);// 版本号
                            versionBean.setVerName(verName);// 版本名
                            versionBean.setContent(content);// 更新内容
                            versionBean.setDownloadUrl(downloadUrl);// 下载地址
                        }
                    }
                }
                return versionBean;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
