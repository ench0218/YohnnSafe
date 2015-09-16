package com.yohnnsafe.ench_wu.yohnnsafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class CallSafe_Activity2 extends Activity {
    private ListView listView;
    private List<BlackNumberInfo> allBlackNumber;
    private LinearLayout llLayout;
    private int mPageSize = 20;
    private int mCurrentPageNumber = 0;

    private TextView showPage;
    private EditText etJump;
    private int totalNumber;
    private BlackNumberDao dao;
    MyAdapter adapter;

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
            showPage.setText(mCurrentPageNumber + 1 + "" + "/" + Integer.valueOf(totalNumber / mPageSize + 1));
            adapter = new MyAdapter();
            listView.setAdapter(adapter);

        }
    };

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                dao = new BlackNumberDao(getApplicationContext());
                totalNumber = dao.getTotalNumber();
                allBlackNumber = dao.findpar(mCurrentPageNumber, mPageSize);
                handler.sendEmptyMessage(0);
            }
        }.start();


    }

    private void init() {
        listView = (ListView) findViewById(R.id.lv);
        llLayout = (LinearLayout) findViewById(R.id.ll_layout);
        showPage = (TextView) findViewById(R.id.tv_showPage);
        etJump = (EditText) findViewById(R.id.et_jump);

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
                convertView = View.inflate(CallSafe_Activity2.this, R.layout.black_number_item, null);
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
            if (mode.equals("1")) {
                holder.tvBlackMode.setText("电话拦截");
            } else if (mode.equals("2")) {
                holder.tvBlackMode.setText("短信拦截");
            } else if (mode.equals("3")) {
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
                        allBlackNumber = dao.findpar(mCurrentPageNumber, mPageSize);
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

    public void nextPage(View v) {
        if (mCurrentPageNumber < totalNumber / mPageSize) {
            mCurrentPageNumber++;
        } else {
            Toast.makeText(getApplicationContext(), "已经最后一页了!!!", Toast.LENGTH_SHORT).show();
        }
        initData();
    }

    public void prePage(View view) {
//        mCurrentPageNumber
        if (mCurrentPageNumber > 0) {
            mCurrentPageNumber--;

        } else {
            Toast.makeText(getApplicationContext(), "这里就是第一页啦!!!", Toast.LENGTH_SHORT).show();
        }
        initData();
    }

    public void jump(View view) {
        String page = etJump.getText().toString().trim();
        int pageNumber = Integer.valueOf(page);
        if (TextUtils.isEmpty(page)) {
            Toast.makeText(getApplicationContext(), "大哥,请您输入正确的页码", Toast.LENGTH_SHORT).show();
        } else if (pageNumber > 0 && pageNumber <= totalNumber / mPageSize + 1) {
            mCurrentPageNumber = Integer.valueOf(page) - 1;
            initData();
        } else {
            Toast.makeText(getApplicationContext(), "大哥,您输入的页码超出范围了...", Toast.LENGTH_SHORT).show();
        }

    }
}
