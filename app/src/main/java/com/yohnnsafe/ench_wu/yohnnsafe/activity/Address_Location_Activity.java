package com.yohnnsafe.ench_wu.yohnnsafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yohnnsafe.ench_wu.yohnnsafe.R;

public class Address_Location_Activity extends Activity {
    private ImageView ivDrag;
    private int startX;
    private int startY;
    private SharedPreferences sp;
    private int winW,winH;
private TextView tvTop,tvBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address__location);
        ivDrag = (ImageView) findViewById(R.id.iv_drag);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        winW = getWindowManager().getDefaultDisplay().getWidth();
        winH = getWindowManager().getDefaultDisplay().getHeight();
        int lastX = sp.getInt("lastX", 0);
        int lastY = sp.getInt("lastY", 0);

        tvTop = (TextView) findViewById(R.id.tv_top);
        tvBottom = (TextView) findViewById(R.id.tv_bottom);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDrag.getLayoutParams();
        layoutParams.leftMargin = lastX;
        layoutParams.topMargin = lastY;

        ivDrag.setLayoutParams(layoutParams);

        ivDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();

                        int dX = endX - startX;
                        int dY = endY - startY;

                        int l = ivDrag.getLeft() + dX;
                        int r = ivDrag.getRight() + dX;

                        int t = ivDrag.getTop() + dY;
                        int b = ivDrag.getBottom() + dY;

                        if (l<0||r>winW||t<0||b>winH){
                            break;
                        }

                        if (ivDrag.getTop()<winH/2){
                            tvTop.setVisibility(View.INVISIBLE);
                            tvBottom.setVisibility(View.VISIBLE);
                        }else {
                            tvTop.setVisibility(View.VISIBLE);
                            tvBottom.setVisibility(View.INVISIBLE);
                        }

                        ivDrag.layout(l, t, r, b);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        sp.edit().putInt("lastX", ivDrag.getLeft()).commit();
                        sp.edit().putInt("lastY", ivDrag.getTop()).commit();
                        break;
                }

                return true;
            }
        });

    }

}
