package com.yohnnsafe.ench_wu.yohnnsafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.yohnnsafe.ench_wu.yohnnsafe.R;

/**
 * Created by Ench_Wu on 2015/9/1.
 */
public abstract class BaseSetupActivity_parent extends Activity {
    private GestureDetector mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        //手势识别器
        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //上一页
                if (e2.getRawX() - e1.getRawX() > 200) {
                    setupPrevious();
                    return true;
                }
                //下一页
                if (e1.getRawX() - e2.getRawX() > 200) {
                    setupNext();
                    return true;
                }
                //Y有较大幅度时不做任何操作
                if (Math.abs(e2.getRawY() - e1.getRawY()) > 10) {
                    Toast.makeText(BaseSetupActivity_parent.this, "不能这样划哦!",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    protected abstract void setupPrevious();

    protected abstract void setupNext();


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
