package com.yohnnsafe.ench_wu.yohnnsafe.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yohnnsafe.ench_wu.yohnnsafe.R;
import com.yohnnsafe.ench_wu.yohnnsafe.bean.AppInfo;
import com.yohnnsafe.ench_wu.yohnnsafe.engine.AppInfos;

import java.util.ArrayList;
import java.util.List;


public class AppManager_Activity extends Activity {

    private TextView tvRom, tvSd;
    private ListView listView;
    private List<AppInfo> appInfos;
    private List<AppInfo> userAppInfos;
    private List<AppInfo> systemAppInfos;
    private AppInfo clickAppInfo;
    private TextView appTag;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager_);

        initUI();
        initData();


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AppManagerAdapter appManagerAdapter = new AppManagerAdapter();
            listView.setAdapter(appManagerAdapter);
        }

    };

    public void initData() {
        new Thread() {

            @Override
            public void run() {
                super.run();
                appInfos = AppInfos.getAppInfos(AppManager_Activity.this);

                userAppInfos = new ArrayList<AppInfo>();
                systemAppInfos = new ArrayList<AppInfo>();

                for (AppInfo appInfo : appInfos
                        ) {
                    if (appInfo.isUserApp()) {
                        userAppInfos.add(appInfo);
                    } else {
                        systemAppInfos.add(appInfo);
                    }
                }
                handler.sendEmptyMessage(0);
            }

        }.start();


    }

    private class AppManagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return userAppInfos.size() + 1 + systemAppInfos.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (position == 0) {
                return null;
            } else if (position == userAppInfos.size() + 1) {
                return null;
            }
            AppInfo appInfo;

            if (position < userAppInfos.size() + 1) {
                //把多出来的特殊的条目减掉
                appInfo = userAppInfos.get(position - 1);

            } else {

                int location = userAppInfos.size() + 2;

                appInfo = systemAppInfos.get(position - location);
            }

            return appInfo;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                TextView tvUserApp = new TextView(AppManager_Activity.this);
                tvUserApp.setText("用户程序(" + userAppInfos.size() + ")个");
                tvUserApp.setTextColor(Color.BLACK);
                tvUserApp.setBackgroundColor(Color.GRAY);

                return tvUserApp;
            } else if (position == userAppInfos.size() + 1) {
                TextView tvsysApp = new TextView(AppManager_Activity.this);
                tvsysApp.setText("系统程序(" + systemAppInfos.size() + ")个");
                tvsysApp.setTextColor(Color.BLACK);
                tvsysApp.setBackgroundColor(Color.GRAY);

                return tvsysApp;
            }
            AppInfo info;
            if (position < userAppInfos.size() + 1) {
                info = userAppInfos.get(position - 1);
            } else {
                int location = userAppInfos.size() + 2;
                info = systemAppInfos.get(position - location);
            }

            ViewHolder holder;
            if (convertView != null && convertView instanceof LinearLayout) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = convertView.inflate(AppManager_Activity.this, R.layout.item_app_manage, null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.appTitle = (TextView) convertView.findViewById(R.id.tv_app_title);
                holder.appStorage = (TextView) convertView.findViewById(R.id.tv_app_storage);
                holder.appSize = (TextView) convertView.findViewById(R.id.tv_app_size);
                convertView.setTag(holder);
            }

            holder.icon.setImageDrawable(info.getIcon());
            holder.appTitle.setText(info.getApkName());
            holder.appSize.setText(Formatter.formatFileSize(AppManager_Activity.this
                    , info.getApkSize()));
            if (info.isRom()) {
                holder.appStorage.setText("手机内存");
            } else {
                holder.appStorage.setText("系统内存");
            }

            return convertView;
        }
    }

    static class ViewHolder {
        ImageView icon;
        TextView appTitle;
        TextView appStorage;
        TextView appSize;
    }

    public void initUI() {
        tvRom = (TextView) findViewById(R.id.tv_rom);
        tvSd = (TextView) findViewById(R.id.tv_sd);
        long romFree = Environment.getDataDirectory().getFreeSpace();
        long sdFree = Environment.getExternalStorageDirectory().getFreeSpace();
        listView = (ListView) findViewById(R.id.lv_app);
        tvRom.setText("ROM内存剩余:" + Formatter.formatFileSize(this, romFree));
        tvSd.setText("SD内存剩余:" + Formatter.formatFileSize(this, sdFree));

        appTag = (TextView) findViewById(R.id.tv_app_tag);
        appTag.setBackgroundColor(Color.GRAY);
        appTag.setTextColor(Color.BLACK);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                dismissPopupWindow();
                if (userAppInfos != null && systemAppInfos != null) {
                    if (firstVisibleItem > (userAppInfos.size() + 1)) {
                        //系统应用程序
                        appTag.setText("系统程序(" + systemAppInfos.size() + ")个");
                    } else {
                        //用户应用程序
                        appTag.setText("用户程序(" + userAppInfos.size() + ")个");
                    }
                }

            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取到当前点击的item对象
                Object obj = listView.getItemAtPosition(position);

                if (obj!=null&&obj instanceof AppInfo){
                    View contentView = View.inflate(AppManager_Activity.this, R.layout.item_popupwindow, null);

                    dismissPopupWindow();

                    popupWindow = new PopupWindow(contentView, -2, -2);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    int[] location = new int[2];
                    view.getLocationInWindow(location);
                    popupWindow.showAtLocation(parent, Gravity.TOP + Gravity.LEFT, 70, location[1]);

                    ScaleAnimation scaleAnim = new ScaleAnimation(0.5f,1,0.5f,1,
                            Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                    scaleAnim.setDuration(100);
                    contentView.setAnimation(scaleAnim);

                }
            }
        });
    }
    public void dismissPopupWindow(){
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
}
