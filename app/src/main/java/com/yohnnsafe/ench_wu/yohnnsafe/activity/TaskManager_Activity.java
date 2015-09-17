package com.yohnnsafe.ench_wu.yohnnsafe.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yohnnsafe.ench_wu.yohnnsafe.R;
import com.yohnnsafe.ench_wu.yohnnsafe.bean.TaskInfo;
import com.yohnnsafe.ench_wu.yohnnsafe.engine.TaskInfos;
import com.yohnnsafe.ench_wu.yohnnsafe.utils.SystemInfoUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskManager_Activity extends Activity {
    @ViewInject(R.id.tv_task)
    private TextView tvTask;
    @ViewInject(R.id.tv_ram)
    private TextView tvRam;
    @ViewInject(R.id.lv_task)
    private ListView lvTask;
    @ViewInject(R.id.tv_task_tesutiaomu)
    private TextView tvTs;
    private int processCount;
    private long totalMem;
    private long availMem;
    private List<TaskInfo> taskInfos;
    private ArrayList<TaskInfo> userTaskInfos;
    private ArrayList<TaskInfo> systemAppInfos;
    private TaskManagerAdapter adapter;

    //哈哈修改了
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        initDate();
        initUI();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initDate() {


        System.out.println("processCount:" + processCount);
        //获取当前可用的RAM
        availMem = SystemInfoUtils.getAvailMem(this);
        System.out.println("availMem" + availMem / 1024 + "M");
        //获取当前设备的RAM总大小
        totalMem = SystemInfoUtils.getTotalMem(this);
        System.out.println("availMem" + Formatter.formatFileSize(this, totalMem));
        new Thread() {
            @Override
            public void run() {
                super.run();

                taskInfos = TaskInfos
                        .getTaskInfos(TaskManager_Activity.this);

                userTaskInfos = new ArrayList<TaskInfo>();

                systemAppInfos = new ArrayList<TaskInfo>();

                for (TaskInfo taskInfo : taskInfos) {

                    if (taskInfo.isUserApp()) {
                        userTaskInfos.add(taskInfo);
                    } else {
                        systemAppInfos.add(taskInfo);
                    }

                }

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        adapter = new TaskManagerAdapter();
                        lvTask.setAdapter(adapter);
                    }
                });

            }
        }.start();
    }

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            lvTask.setAdapter(new TaskAdapter());
//        }
//    };

    public void initUI() {

        ViewUtils.inject(this);
        //通过SystemInfoUtils 获取当前进程数
        processCount = SystemInfoUtils.getProcessCount(this);
        tvTask.setText("进程:" + processCount);
        tvRam.setText("剩余/内存大小:"
                + Formatter.formatFileSize(this, availMem)
                + "/"
                + Formatter.formatFileSize(this, totalMem));

        lvTask.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (userTaskInfos!=null & systemAppInfos!=null){
                    if (firstVisibleItem<userTaskInfos.size()+1){
                        tvTs.setText("用户程序");
                    }else {
                        tvTs.setText("系统程序");
                    }
                }
            }
        });

    }

    private class TaskManagerAdapter extends BaseAdapter {
        @Override
        public int getCount() {

            return userTaskInfos.size() + 1 + systemAppInfos.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (position == 0) {
                return null;
            } else if (position == userTaskInfos.size() + 1) {
                return null;
            }

            TaskInfo taskInfo;

            if (position < (userTaskInfos.size() + 1)) {
                // 用户程序
                taskInfo = userTaskInfos.get(position - 1);// 多了一个textview的标签 ，
                // 位置需要-1
            } else {
                // 系统程序
                int location = position - 1 - userTaskInfos.size() - 1;
                taskInfo = systemAppInfos.get(location);
            }
            return taskInfo;
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //特殊条目
            if (position == 0) {
                TextView tv = new TextView(TaskManager_Activity.this);
                tv.setText("用户应用");
                tv.setTextColor(Color.WHITE);
                tv.setBackgroundColor(Color.GRAY);
                return tv;
            } else if (position == userTaskInfos.size() + 1) {
                TextView tv = new TextView(TaskManager_Activity.this);
                tv.setText("系统应用");
                tv.setTextColor(Color.WHITE);
                tv.setBackgroundColor(Color.GRAY);
                return tv;
            }


            ViewHolder holder;
            View view;
            if (convertView != null && convertView instanceof LinearLayout) {
                view = convertView;
                holder = (ViewHolder) convertView.getTag();
            } else {
                view = View.inflate(TaskManager_Activity.this, R.layout.item_task_manager, null);
                holder = new ViewHolder();
                holder.ivTaskIcon = (ImageView) view.findViewById(R.id.iv_task_icon);
                holder.tvTaskTitle = (TextView) view.findViewById(R.id.tv_task_title);
                holder.tvTaskStorage = (TextView) view.findViewById(R.id.tv_task_storage);
                view.setTag(holder);
            }
            TaskInfo taskInfo;
            if (position < userTaskInfos.size() + 1) {
                taskInfo = userTaskInfos.get(position - 1);
            } else {
                int location = position - 1 - userTaskInfos.size() - 1;
                taskInfo = systemAppInfos.get(location);
            }
            holder.ivTaskIcon.setImageDrawable(taskInfo.getIcon());
            holder.tvTaskTitle.setText(taskInfo.getAppName());
            long memorySize = taskInfo.getMemorySize();
            holder.tvTaskStorage.setText("内存占用:" + Formatter.formatFileSize(TaskManager_Activity.this, memorySize));

            return view;
        }

    }

    static class ViewHolder {
        ImageView ivTaskIcon;
        TextView tvTaskTitle;
        TextView tvTaskStorage;

    }
}
