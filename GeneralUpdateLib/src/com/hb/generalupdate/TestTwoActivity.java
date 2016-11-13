package com.hb.generalupdate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hb.update.UpdateManager;
import com.hb.util.SharedPreferencesUtil;

/**
 * 主工程集成使用示例，正式发布时删除此TestTwoActivity及xml里面配置。
 * 集成步骤：
 * 1、实现接口InitUpdateInterface，实现 checkUpdate()方法
 * 2、onCreate中调用 checkUpdate(),参考TestActivity写法;
 * 3、将JSON_URL地址修改为你的地址(json文件见asset中)
 * 4、集成时将build.gradle中apply plugin: 'com.android.application'改为apply plugin: 'com.android.library'
 * @author hb 2016-11-14
 */
public class TestTwoActivity extends Activity implements InitUpdateInterface {

    private String TAG = TestTwoActivity.class.getSimpleName();
    private Context mContext;
    //保存更新标记 再次回到界面不丢失数据
    public static final String SAVE_ISUPDATE = "isupdate";
    public static final String SAVE_VER_UPDATEURL = "ver_updateurl";
    public static final String SAVE_VER_VERNAME = "ver_vername";
    public static final String SAVE_VER_CODE = "ver_vercode";
    public static final String SAVE_VER_CONTENT = "ver_content";

    private String JSON_URL = "http://192.168.1.100:8088/HW1919/checkvercode.json";//此处修改你的json文件地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_two);
        mContext = this;
        // 调用一次
        checkUpdate();
    }

    @Override
    public void checkUpdate() {
        ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cwjManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            CheckVerRunnable chackRunnable = new CheckVerRunnable();
            Thread checkThread = new Thread(chackRunnable);
            checkThread.start();
        } else {
            Log.d("GeneralUpdateLib", "Network connection failed, please check the network.");
        }
    }

    /**
     * request server checkvercode.json file.
     */
    class CheckVerRunnable implements Runnable {
        @Override
        public void run() {
            // replace your .json file url.
            boolean isUpdate = UpdateManager.getUpdateInfo(mContext, JSON_URL);
            if (isUpdate) {
                updateHandler.sendEmptyMessage(1);
            } else {
                updateHandler.sendEmptyMessage(0);
            }
        }
    }

    /**
     * handler rec.
     */
    @SuppressLint("HandlerLeak")
    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    // To do something.
                    SharedPreferencesUtil.setBoolean(mContext, SAVE_ISUPDATE, false);//置为更新标记false
                    Log.d("GeneralUpdateLib", "There's no new version here.");
                    break;
                case 1:
                    // Find new version.
                    SharedPreferencesUtil.setString(mContext, SAVE_VER_UPDATEURL, UpdateManager.getVerUpdateURL());
                    SharedPreferencesUtil.setString(mContext, SAVE_VER_VERNAME, UpdateManager.getVerVerName());
                    SharedPreferencesUtil.setString(mContext, SAVE_VER_CODE, UpdateManager.getVerVerCode());
                    SharedPreferencesUtil.setString(mContext, SAVE_VER_CONTENT, UpdateManager.getVerContent());

                    SharedPreferencesUtil.setBoolean(mContext, SAVE_ISUPDATE, true);//置为更新标记true
                    //弹出更新对话框
                    //进入对应的页面判断标记是否有更新在进行调用此方法
                    UpdateManager.update(mContext);
                    break;
                default:
                    break;
            }
        }
    };
}
