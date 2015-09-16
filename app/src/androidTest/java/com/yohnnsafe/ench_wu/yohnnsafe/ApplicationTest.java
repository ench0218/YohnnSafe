package com.yohnnsafe.ench_wu.yohnnsafe;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.yohnnsafe.ench_wu.yohnnsafe.db.BlackNumberDao;

import java.util.Random;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public ApplicationTest() {
        super(Application.class);
    }

    public void testAdd() {
        BlackNumberDao dao = new BlackNumberDao(mContext);
        Random random = new Random();
        for (int i = 0; i < 200; i++) {
            Long number = 133000000l + i;
            dao.add(number + "", String.valueOf(random.nextInt(3) + 1));
        }
    }

    public void testDelet() {
        BlackNumberDao dao = new BlackNumberDao(mContext);
        dao.delet("133000000");
    }
}