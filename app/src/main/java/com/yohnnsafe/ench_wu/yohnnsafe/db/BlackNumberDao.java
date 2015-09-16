package com.yohnnsafe.ench_wu.yohnnsafe.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.yohnnsafe.ench_wu.yohnnsafe.bean.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ench_Wu on 2015/9/10.
 */
public class BlackNumberDao {

    public BlackNumberOpenHelper helper;


    public BlackNumberDao(Context context) {
        helper = new BlackNumberOpenHelper(context);
    }

    public boolean add(String number, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("number", number);
        contentValues.put("mode", mode);
        long insert = db.insert("blacknumber", null, contentValues);
        System.out.println(insert + ">>>>>>>>>>>>");
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 通过电话号码删除
     *
     * @param number 电话号码
     */
    public boolean delet(String number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int blacknumber = db.delete("blacknumber", "number=?", new String[]{number});
        if (blacknumber != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 通过电话号码修改拦截的模式
     *
     * @param number
     */
    public void changeNumberMode(String number, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mode", mode);
        db.update("blacknumber", contentValues, "number=?", new String[]{number});
    }

    /**
     * 返回一个黑名单号码拦截模式
     *
     * @return
     */
    public String findNumber(String number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String mode = null;

        Cursor cursor = db.query("blacknumber", null, "number=?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()) {
            mode = cursor.getString(0);
        } else {
            return null;
        }
        db.close();
        cursor.close();
        return mode;
    }

    /**
     * 查询所有的黑名单
     *
     * @return
     */
    public List<BlackNumberInfo> findAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<BlackNumberInfo> blackNumberInfos = new ArrayList<>();

        Cursor cursor = db.query("blacknumber", new String[]{"number", "mode"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfos.add(blackNumberInfo);
        }
        db.close();
        cursor.close();
        SystemClock.sleep(3000);
        return blackNumberInfos;
    }

    /**
     * 分页查询
     * * @param pageNumber 表示当前是哪一页
     *
     * @param pageSize 表示每一页有多少条数据
     * @return limit 表示限制当前有多少数据
     * offset 表示跳过 从第几条开始
     */
    public List<BlackNumberInfo> findpar(int pageNumber, int pageSize) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ArrayList<BlackNumberInfo> blackNumberInfos = new ArrayList<>();
        Cursor cursor = db.rawQuery("select number,mode from blacknumber limit ? offset ?",
                new String[]{String.valueOf(pageSize), String.valueOf(pageNumber * pageSize)});
        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfos.add(blackNumberInfo);
        }
        cursor.close();
        db.close();
        return blackNumberInfos;
    }

    /**
     * /**
     * 分批加载数据
     *
     * @param startIndex 开始的位置
     * @param maxCount   每页展示的最大的条目
     * @return
     */
    public List<BlackNumberInfo> findPar2(int startIndex, int maxCount) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number,mode from blacknumber limit ? offset ?", new String[]{String.valueOf(maxCount),
                String.valueOf(startIndex)});
        List<BlackNumberInfo> blackNumberInfos = new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfos.add(blackNumberInfo);
        }
        cursor.close();
        db.close();
        return blackNumberInfos;
    }

    /**
     * 获取总的记录数
     *
     * @return
     */
    public int getTotalNumber() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }
}
