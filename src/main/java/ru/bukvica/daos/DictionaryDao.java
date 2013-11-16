package ru.bukvica.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * User: vovam
 * Date: 01.11.13
 * Time: 13:42
 */
public class DictionaryDao {
    public static final String TABLE = "user_dictionary";

    public static final String WORD = "word";
    public static final String TYPE = "type";

    public static final int TYPE_USER_INCLUDE = 0;
    public static final int TYPE_USER_EXCLUDE = 1;

    public ArrayList<String> getAll(int type){
        SQLiteDatabase db = DataBaseHelper.getInstance().getWritableDatabase();
        Cursor cursor = db.query(TABLE, null, TYPE+"=?", new String[]{Integer.toString(type)}, null, null, null);
        ArrayList<String> list = new ArrayList<String>(0);
        for(cursor.moveToFirst(); cursor.getPosition() < cursor.getCount(); cursor.moveToNext()){
            list.add(cursor.getString(cursor.getColumnIndex(WORD)));
        }
        cursor.close();
        return list;
    }

    public long insert(String word, int type) {
        SQLiteDatabase db = DataBaseHelper.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WORD, word);
        values.put(TYPE, type);

        long num = db.insert(TABLE, null, values);
        return num;
    }

}
