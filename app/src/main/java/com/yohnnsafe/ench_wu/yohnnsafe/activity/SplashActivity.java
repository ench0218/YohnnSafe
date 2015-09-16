package com.yohnnsafe.ench_wu.yohnnsafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yohnnsafe.ench_wu.yohnnsafe.R;
import com.yohnnsafe.ench_wu.yohnnsafe.utils.StreamUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Splash_Activity页面
 *
 * @author Ench_Wu
 */
public class SplashActivity extends Activity {
    private static final int CODE_UPDATE_DIALOG = 0;
    private static final int CODE_ENTER_DIALOG = 1;
    private static final int CODE_URL_ERROR = 2;
    private static final int CODE_NET_ERROR = 3;
    private static final int CODE_JSON_ERROR = 4;
    private TextView tvVersion, tvPr;
    private HttpURLConnection conn;
    private SharedPreferences mPref;
    private RelativeLayout splashLayout;


    private long startTime;
    private long resultTime;
    long endTime;

    private double mVersionName;
    private int mVersionCode;
    private String mDescription;
    private String mDownloadUrl;

    private android.os.Handler mhandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_UPDATE_DIALOG:
                    showUpdateDialong();
                    break;
                case CODE_ENTER_DIALOG:
                    enterHome();
                    break;
                case CODE_URL_ERROR:
                    enterHome();
                    Toast.makeText(getApplicationContext(), "URL错误异常", Toast.LENGTH_SHORT).show();
                    break;
                case CODE_NET_ERROR:
                    enterHome();
                    Toast.makeText(getApplicationContext(), "网络错误异常", Toast.LENGTH_SHORT).show();
                    break;
                case CODE_JSON_ERROR:
                    enterHome();
                    Toast.makeText(getApplicationContext(), "JSON解析异常", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("V" + getVersionName());
        tvPr = (TextView) findViewById(R.id.tv_pr);

        splashLayout = (RelativeLayout) findViewById(R.id.splash_layout);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.8f, 1f);
        alphaAnimation.setDuration(2000);
        splashLayout.setAnimation(alphaAnimation);
        copyDB("address.db");

        mPref = getSharedPreferences("auto_update", MODE_PRIVATE);

        boolean autoUpdate = mPref.getBoolean("auto_update", true);
        if (autoUpdate) {
            checkVersion();
        } else {
            mhandler.sendEmptyMessageDelayed(CODE_ENTER_DIALOG, 2000);
        }
        //创建快捷方式
        createShortCut();

    }

    /**
     * 创建快捷方式
     */
    private void createShortCut() {

//        Intent intent = new Intent();
//
//        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
//        //如果设置为true表示可以创建重复的快捷方式
//        intent.putExtra("duplicate", false);
//
//        /**
//         * 1 干什么事情
//         * 2 你叫什么名字
//         * 3你长成什么样子
//         */
//        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_delete_btn));
//        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "黑马手机卫士");
//        //干什么事情
//        /**
//         * 这个地方不能使用显示意图
//         * 必须使用隐式意图
//         */
//        Intent shortcut_intent = new Intent();
//
//        shortcut_intent.setAction("aaa.bbb.ccc");
//
//        shortcut_intent.addCategory("android.intent.category.DEFAULT");
//
//        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcut_intent);
//
//        sendBroadcast(intent);

        Intent intent = new Intent();
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.id.iv_icon));
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "大翰卫士");
        //如果设置为true表示可以创建重复的快捷方式
        intent.putExtra("duplicate", false);
        Intent dowhatIntent = new Intent();


        dowhatIntent.setClass(SplashActivity.this, SplashActivity.class);
        dowhatIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, dowhatIntent);


        sendBroadcast(intent);

    }

    /**
     * 获取版名
     */
    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取版本码
     */
    private int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 连接服务器判断是否有新版本
     */
    private void checkVersion() {

        new Thread() {
            @Override
            public void run() {
                startTime = System.currentTimeMillis();
                Message msg = Message.obtain();
                try {
                    URL url = new URL("http://10.0.2.2:8080/update.json");
                    conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);//设置请求超时
                    conn.setReadTimeout(5000);//设置读取超时

                    conn.connect();//建立connection连接

                    int responseCode = conn.getResponseCode();//获得返回码
                    if (responseCode == 200) {
                        InputStream inputStream = conn.getInputStream();
                        String result = StreamUtils.readFromStream(inputStream);

                        JSONObject jo = new JSONObject(result);
                        mVersionName = jo.getDouble("versionName");
                        mVersionCode = jo.getInt("versionCode");
                        mDescription = jo.getString("description");
                        mDownloadUrl = jo.getString("downloadUrl");
                        System.out.println(mDescription);

                        if (mVersionCode > getVersionCode()) {
                            msg.what = CODE_UPDATE_DIALOG;
                        } else {
                            msg.what = CODE_ENTER_DIALOG;
                        }
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();//url错误异常
                    msg.what = CODE_URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();//网络错误异常
                    msg.what = CODE_NET_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();//jason解析异常
                    msg.what = CODE_JSON_ERROR;
                } finally {
                    endTime = System.currentTimeMillis();
                    resultTime = endTime - startTime;

                    if (resultTime < 2000) {
                        try {
                            Thread.sleep(2000 - resultTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mhandler.sendMessage(msg);
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }.start();

    }

    /**
     * 更新提示框
     */
    protected void showUpdateDialong() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("获取更新V" + mVersionName);
        builder.setMessage(mDescription);

        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                download();
            }
        });
        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        //给DIALONG设置一个取消监听，那么按返回键将返回主界面
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });
        builder.show();
    }

    /**
     * 下载更新
     */
    private void download() {
        if (Environment.getExternalStorageState().
                equals(Environment.MEDIA_MOUNTED)) {

            tvPr.setVisibility(View.VISIBLE);
            String target = Environment.getExternalStorageDirectory() + "/update.apk";
            //使用xUtils下载更新包
            HttpUtils utils = new HttpUtils();
            utils.download(mDownloadUrl, target, new RequestCallBack<File>() {

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    System.out.println("下载进度:" + current + "/" + total);
                    tvPr.setText("下载进度：" + current * 100 / total + "%");
                }

                @Override
                public void onSuccess(ResponseInfo<File> arg0) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(arg0.result),
                            "application/vnd.android.package-archive");
                    startActivityForResult(intent, 0);//如果按取消会调用onActivityResult

                }

                //下载失败
                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "木有找到SD卡", Toast.LENGTH_SHORT).show();
        }

    }

    // 如果用户取消安装的话,回调此方法()---startActivityForResult(intent, 0);
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
    }

    /**
     * 进入主页
     */
    private void enterHome() {
        Intent intent = new Intent(SplashActivity.this, Home_Activity.class);
        startActivity(intent);
        finish();
    }

    /**
     * copy
     */
    private void copyDB(String dbName) {
        // File filesDir = getFilesDir();
        // System.out.println("路径:" + filesDir.getAbsolutePath());
        File destFile = new File(getFilesDir(), dbName);// 要拷贝的目标地址

        if (destFile.exists()) {
            System.out.println("数据库" + dbName + "已存在!");
            return;
        }

        FileOutputStream out = null;
        InputStream in = null;

        try {
            //不能使用getAssets获取db。
            in = getResources().openRawResource(R.raw.address);
            System.out.println("getRawResources");
            out = new FileOutputStream(destFile);

            int len = 0;
            byte[] buffer = new byte[1024];

            System.out.println("buffer");
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

