package com.yohnnsafe.ench_wu.yohnnsafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yohnnsafe.ench_wu.yohnnsafe.R;
import com.yohnnsafe.ench_wu.yohnnsafe.bean.BlackNumberInfo;
import com.yohnnsafe.ench_wu.yohnnsafe.db.BlackNumberDao;

import java.util.List;

public class CallSafe_Activity extends Activity {
    ListView listView;
    private List<BlackNumberInfo> allBlackNumber;
    private LinearLayout llLayout;
    private int maxCount = 20;
    private int startIndex = 0;

    private BlackNumberDao dao;
    MyAdapter adapter;
    private int totalNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);
        initData();
        init();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            llLayout.setVisibility(View.INVISIBLE);

            if (adapter == null) {
                adapter = new MyAdapter();
                listView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }

        }
    };

    private void initData() {
        dao = new BlackNumberDao(CallSafe_Activity.this);
        totalNumber = dao.getTotalNumber();
        new Thread() {
            @Override
            public void run() {
                super.run();


                //分批加载数据
                if (allBlackNumber == null) {
                    allBlackNumber = dao.findPar2(startIndex, maxCount);
                } else {
                    //把后面的数据。追加到blackNumberInfos集合里面。防止黑名单被覆盖
                    allBlackNumber.addAll(dao.findPar2(startIndex, maxCount));
                }

                handler.sendEmptyMessage(0);
            }
        }.start();


    }

    private void init() {
        listView = (ListView) findViewById(R.id.lv);
        llLayout = (LinearLayout) findViewById(R.id.ll_layout);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            //状态改变时候回调的方法

            /**
             *
             * @param view
             * @param scrollState  表示滚动的状态
             *
             *                     AbsListView.OnScrollListener.SCROLL_STATE_IDLE 闲置状态
             *                     AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL 手指触摸的时候的状态
             *                     AbsListView.OnScrollListener.SCROLL_STATE_FLING 惯性
             */
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        //获取到最后一条显示的数据
                        int lastVisiblePosition = listView.getLastVisiblePosition();
                        System.out.println("lastVisiblePosition==========" + lastVisiblePosition);
                        if (lastVisiblePosition == allBlackNumber.size() - 1) {
                            // 加载更多的数据。 更改加载数据的开始位置
                            startIndex += maxCount;
                            if (startIndex >= totalNumber) {
                                Toast.makeText(getApplicationContext(),
                                        "没有更多的数据了。", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            initData();
                        }


                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return allBlackNumber.size();
        }

        @Override
        public Object getItem(int position) {
            return allBlackNumber.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            adapter.notifyDataSetChanged();

            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(CallSafe_Activity.this, R.layout.black_number_item, null);
                holder = new ViewHolder();
                holder.tvBlackNumber = (TextView) convertView.findViewById(R.id.tv_blackNumber);
                holder.tvBlackMode = (TextView) convertView.findViewById(R.id.tv_blackMode);
                holder.delete = (ImageView) convertView.findViewById(R.id.iv_blackNumberDelet);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();

            }

            holder.tvBlackNumber.setText(allBlackNumber.get(position).getNumber());
            String mode = allBlackNumber.get(position).getMode();
            System.out.println("mode" + mode);
            if (mode.equals("2")) {
                holder.tvBlackMode.setText("电话拦截");
            } else if (mode.equals("3")) {
                holder.tvBlackMode.setText("短信拦截");
            } else if (mode.equals("1")) {
                holder.tvBlackMode.setText("短信+电话拦截");
            }
            final BlackNumberInfo info = allBlackNumber.get(position);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = info.getNumber();
                    boolean result = dao.delet(number);
                    if (result) {

                        allBlackNumber.remove(number);
                        //刷新界面
                        //重新加载数据后再刷新
                        allBlackNumber = dao.findPar2(startIndex, maxCount);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "删除失败", Toast.LENGTH_SHORT).show();

                    }

                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tvBlackNumber;
        TextView tvBlackMode;
        ImageView delete;
    }

    public void addBlackNumber(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog Dialog = builder.create();
        View dialog_view = View.inflate(this, R.layout.dialog_black_number, null);

        final EditText etBlackNumber = (EditText) dialog_view.findViewById(R.id.et_black_number);
        final CheckBox cbNumberSafe = (CheckBox) dialog_view.findViewById(R.id.cb_number_safe);
        final CheckBox cbSmsSafe = (CheckBox) dialog_view.findViewById(R.id.cb_sms_safe);
        Button btnOk = (Button) dialog_view.findViewById(R.id.btn_ok);
        final Button btnCancel = (Button) dialog_view.findViewById(R.id.btn_cancel);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String blackNumber = etBlackNumber.getText().toString().trim();
                String mode = "";

                if(cbNumberSafe.isChecked()&& cbSmsSafe.isChecked()){
                    mode = "1";
                }else if(cbNumberSafe.isChecked()){
                    mode = "2";
                }else if(cbSmsSafe.isChecked()){
                    mode = "3";
                }else{
                    Toast.makeText(CallSafe_Activity.this,"请勾选拦截模式",Toast.LENGTH_SHORT).show();
                    return;
                }
                BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                blackNumberInfo.setNumber(blackNumber);
                blackNumberInfo.setMode(mode);
                allBlackNumber.add(0, blackNumberInfo);
                //把电话号码和拦截模式添加到数据库。
                dao.add(blackNumber,mode);


                if (adapter == null) {
                    adapter = new MyAdapter();
                    listView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                Dialog.dismiss();
            }

        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.dismiss();
            }
        });
        Dialog.setView(dialog_view);
        Dialog.show();
    }
}
