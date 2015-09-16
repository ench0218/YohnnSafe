package com.yohnnsafe.ench_wu.yohnnsafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.yohnnsafe.ench_wu.yohnnsafe.R;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Contacts_Activity
 *
 * @author Ench_Wu
 */
public class Contacts_Activity extends Activity {
    private ListView contacts_lv;
    private ArrayList<HashMap<String, String>> readContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        contacts_lv = (ListView) findViewById(R.id.contacts_lv);

        readContact = readContacts();
        contacts_lv.setAdapter(new SimpleAdapter(this, readContact, R.layout.contact_item,
                new String[]{"name", "phone"}, new int[]{R.id.name, R.id.phone}));
        contacts_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = readContact.get(position).get("name");
                String phone = readContact.get(position).get("phone");
                Intent intent= new Intent();
                intent.putExtra("name",name);
                intent.putExtra("phone",phone);
                setResult(Activity.RESULT_OK, intent);

                finish();

            }
        });

    }

    private ArrayList<HashMap<String, String>> readContacts() {
        Uri rawContactsUri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dateUri = Uri.parse("content://com.android.contacts/data");
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        Cursor rawContactCursor = getContentResolver().query(rawContactsUri,
                new String[]{"contact_id"}, null, null, null);

        if (rawContactCursor != null) {

            while (rawContactCursor.moveToNext()) {
                String rawContactId = rawContactCursor.getString(0);
                HashMap<String, String> map = new HashMap<String, String>();
                Cursor dateCursor = getContentResolver().query(dateUri,
                        new String[]{"data1", "mimetype"}, "contact_id=?",
                        new String[]{rawContactId}, null);
//

                if (dateCursor != null) {
                    while (dateCursor.moveToNext()) {
                        String date1 = dateCursor.getString(0);
                        String mimetype = dateCursor.getString(1);

                        if ("vnd.android.cursor.item/name"
                                .equals(mimetype)) {
                            System.out.println("name:" + date1);

                            map.put("name", date1);
                        } else if ("vnd.android.cursor.item/phone_v2"
                                .equals(mimetype)) {
                            System.out.println("phone:" + date1);
                            map.put("phone", date1);
                        }
                    }
                    list.add(map);
                    dateCursor.close();
                }

            }
            rawContactCursor.close();

        }
        return list;
    }


}
