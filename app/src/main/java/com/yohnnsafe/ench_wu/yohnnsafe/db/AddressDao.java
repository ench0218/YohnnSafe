package com.yohnnsafe.ench_wu.yohnnsafe.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ench_Wu on 2015/9/6.
 */
public class AddressDao {
    private static final String PATH = "data/data/com.yohnnsafe.ench_wu.yohnnsafe/files/address.db";

    public static String getAddress(String number) {
        String address = "未知号码";

        SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);

        if (number.matches("^1[3-8]\\d{9}$")) {//匹配是否是手机号码

            Cursor cursor = database.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)",
                    new String[]{number.substring(0, 7)});

            if (cursor.moveToNext()) {
                address = cursor.getString(0);
            }
            cursor.close();

        }
        database.close();
        return address;
    }

}
