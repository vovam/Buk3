package ru.bukvica.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.bukvica.models.LettersModel;
import ru.bukvica.pojos.Letter;

/**
 * Created with IntelliJ IDEA.
 * User: vovam
 * Date: 24.10.13
 * Time: 22:20
 * To change this template use File | Settings | File Templates.
 */
public class LettersDao {
    public static final String TABLE = "letters";

    public static final String GAME_ID = "game_id";
    public static final String ID = "id";
    public static final String PLAYER = "player";
    public static final String HOLD = "hold";
    public static final String L = "l";

    public Letter[] getAll(long gameId){
        SQLiteDatabase db = DataBaseHelper.getInstance().getWritableDatabase();
        Cursor cursor = db.query(TABLE, null, GAME_ID+"=?", new String[]{Long.toString(gameId)}, null, null, null);
        Letter[] array = new Letter[LettersModel.SIZE*LettersModel.SIZE];
        for(cursor.moveToFirst(); cursor.getPosition() < cursor.getCount(); cursor.moveToNext()){
            Letter l = new Letter();
            l.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            l.setPlayer(cursor.getInt(cursor.getColumnIndex(PLAYER)));
            l.setHold(cursor.getInt(cursor.getColumnIndex(HOLD)) == 1);
            l.setL(cursor.getString(cursor.getColumnIndex(L)));
            array[l.getId()] = l;
        }
        cursor.close();
        return array;
    }

    public void update(Letter[] list, long gameId){
        SQLiteDatabase db = DataBaseHelper.getInstance().getWritableDatabase();
        for(Letter l : list){
            ContentValues value = new ContentValues();
            value.put(PLAYER, l.getPlayer());
            value.put(HOLD, l.isHold());
            value.put(L, l.getL());
            db.update(TABLE, value, GAME_ID+"=? AND "+ID+"=?", new String[]{Long.toString(gameId), Integer.toString(l.getId())});
        }
    }

    public void addAll(Letter[] list, long gameId) {
        SQLiteDatabase db = DataBaseHelper.getInstance().getWritableDatabase();
        for(Letter l : list){
            ContentValues value = new ContentValues();
            value.put(GAME_ID, gameId);
            value.put(ID, l.getId());
            value.put(PLAYER, l.getPlayer());
            value.put(HOLD, l.isHold());
            value.put(L, l.getL());
            db.insert(TABLE, null, value);
        }
    }
}
