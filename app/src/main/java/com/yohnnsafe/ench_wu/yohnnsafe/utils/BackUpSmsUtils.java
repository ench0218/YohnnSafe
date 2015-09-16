package com.yohnnsafe.ench_wu.yohnnsafe.utils;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Ench_Wu on 2015/9/14.
 */
public class BackUpSmsUtils {

    private static XmlSerializer xmlSerializer;
    private static FileOutputStream os;
    private static Cursor cursor;

    public static boolean backUp(Context context, ProgressDialog pd) {
        //创建file
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = Uri.parse("content://sms/");
            cursor = contentResolver.query(uri, new String[]{"address",
                    "date", "type", "body"}, null, null, null);
            int process = 0;

            int count = cursor.getCount();
            System.out.println("count:" + count);
            pd.setMax(count);
            try {

                File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
                os = new FileOutputStream(file);
                xmlSerializer = Xml.newSerializer();

                xmlSerializer.setOutput(os, "utf-8");

                xmlSerializer.startDocument("utf-8", true);

                xmlSerializer.startTag(null, "smss");
                xmlSerializer.attribute(null, "size", String.valueOf(count));
                Crypto crypto = new Crypto();
                System.out.println("whileqian");
                while (cursor.moveToNext()) {
                    String address = cursor.getString(0);
                    String date = cursor.getString(1);
                    String type = cursor.getString(2);
                    String body = cursor.getString(3);

                    System.out.println("address:" + address);
                    System.out.println("date:" + date);
                    System.out.println("type:" + type);
                    System.out.println("body:" + body);

                    xmlSerializer.startTag(null, "sms");

                    xmlSerializer.startTag(null, "address");
                    xmlSerializer.text(crypto.encrypt("enchwu", address));
                    xmlSerializer.endTag(null, "address");

                    xmlSerializer.startTag(null, "date");
                    xmlSerializer.text(date);
                    xmlSerializer.endTag(null, "date");

                    xmlSerializer.startTag(null, "type");
                    xmlSerializer.text(type);
                    xmlSerializer.endTag(null, "type");

                    xmlSerializer.startTag(null, "body");

                    xmlSerializer.text(crypto.encrypt("enchwu", body));
                    xmlSerializer.endTag(null, "body");

                    xmlSerializer.endTag(null, "sms");
                    process++;

                    pd.setProgress(process);
//                    SystemClock.sleep(200);

                }
                xmlSerializer.endTag(null, "smss");

                xmlSerializer.endDocument();
                cursor.close();
                os.flush();
                os.close();
                pd.dismiss();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "SD卡!!!!!!!!!!", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

}
