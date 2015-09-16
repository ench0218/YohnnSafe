package com.yohnnsafe.ench_wu.yohnnsafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yohnnsafe.ench_wu.yohnnsafe.R;
import com.yohnnsafe.ench_wu.yohnnsafe.utils.Md5Utils;

/**
 * Home_Activity页面
 *
 * @author Ench_Wu
 */
public class Home_Activity extends Activity {
    private GridView gvHome;
    private LinearLayout homeLayout;

//    private TextView tvItem;
//    private ImageView ivItem;

    private String[] mItems = new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理",
            "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};

    private int[] mPics = new int[]{R.mipmap.home_safe,
            R.mipmap.home_callmsgsafe, R.mipmap.home_apps,
            R.mipmap.home_taskmanager, R.mipmap.home_netmanager,
            R.mipmap.home_trojan, R.mipmap.home_sysoptimize,
            R.mipmap.home_tools, R.mipmap.home_settings};
    private EditText etPassword, etPasswordConfirm;
    private SharedPreferences mPres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //设置过度效果
        homeLayout = (LinearLayout) findViewById(R.id.home_layout);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.6f, 1);
        alphaAnimation.setDuration(1000);
        homeLayout.setAnimation(alphaAnimation);

        mPres = getSharedPreferences("password", MODE_PRIVATE);

        gvHome = (GridView) findViewById(R.id.gv_home);
        gvHome.setAdapter(new HomeAdapter());

        gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://手机防盗
                        showPasswordDialog();
                        break;
                    case 1://通讯卫士
                        startActivity(new Intent(Home_Activity.this, CallSafe_Activity.class));
                        break;
                    case 2://软件管理
                        startActivity(new Intent(Home_Activity.this, AppManager_Activity.class));
                        break;
                    case 3://进程管理
                        startActivity(new Intent(Home_Activity.this, TaskManager_Activity.class));
                        break;
                    case 7://高级设置
                        startActivity(new Intent(Home_Activity.this, ATools_Activity.class));
                        break;
                    case 8://设置中心
                        startActivity(new Intent(Home_Activity.this, Setting_Activity.class));
                        break;
                }
            }
        });
    }

    /**
     * 判断显示密码的弹窗
     */
    private void showPasswordDialog() {

        String savePass = mPres.getString("password", null);
        // 判断是否设置密码
        if (!TextUtils.isEmpty(savePass)) {
            // 输入密码弹窗
            PasswordInputDialog();
            System.out.println("PasswordInputDialog()");
        } else {
            // 如果没有设置过, 弹出设置密码的弹窗
            showSetPasswordDialog();
            System.out.println("showSetPasswordDialog()");

        }

    }

    //已经设置了密码，再次进入弹出
    public void PasswordInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_input, null);
        dialog.setView(view);

        System.out.println("PasswordInputDialog>>>>>>>>>>>>>>>>>>>>");

        final EditText etInputPassword = (EditText) view.findViewById(R.id.et_inputpassword);


        Button btnOK = (Button) view.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String mInputPassword = etInputPassword.getText().toString();
                if (!TextUtils.isEmpty(mInputPassword)) {
                    String savePass = mPres.getString("password", null);
                    if (Md5Utils.encode(mInputPassword).equals(savePass)) {
//                        Toast.makeText(Home_Activity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Home_Activity.this, LostFind_Activity.class));
                        dialog.dismiss();
                    } else {
                        Toast.makeText(Home_Activity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Home_Activity.this, "请输入密码", Toast.LENGTH_SHORT).show();

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    //首次设置进入手机防盗弹出设置密码弹窗
    private void showSetPasswordDialog() {
        //创建一个AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(Home_Activity.this, R.layout.dialog_set_password, null);
        // dialog.setView(view);// 将自定义的布局文件设置给dialog
        dialog.setView(view);

        etPassword = (EditText) view.findViewById(R.id.et_password);
        etPasswordConfirm = (EditText) view.findViewById(R.id.et_password_confirm);


        final Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        final Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mPassword = etPassword.getText().toString();
                String mPasswordConfirm = etPasswordConfirm.getText().toString();
                if (!TextUtils.isEmpty(mPassword) && !TextUtils.isEmpty(mPasswordConfirm)) {
                    if (mPassword.equals(mPasswordConfirm)) {
                        mPres.edit().putString("password", Md5Utils.encode(mPassword)).commit();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(Home_Activity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Home_Activity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    /**
     * GridView适配器
     */
    class HomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(Home_Activity.this, R.layout.home_list_item, null);
            ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
            TextView tvItem = (TextView) view.findViewById(R.id.tv_item);

            tvItem.setText(mItems[position]);
            ivItem.setImageResource(mPics[position]);

            return view;
        }
    }

}
