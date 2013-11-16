package ru.bukvica.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.bukvica.pojos.Word;

import java.util.ArrayList;

/**
 * User: vovam
 * Date: 24.10.13
 * Time: 22:21
 */
public class WordListDao {
    public static final String TABLE = "word_list";

    public static final String GAME_ID = "game_id";
    public static final String WORD = "word";
    public static final String PLAYER = "player";
    public static final String INDEX = "word_index";

    public ArrayList<Word> getAll(long gameId){
        SQLiteDatabase db = DataBaseHelper.getInstance().getWritableDatabase();
        Cursor cursor = db.query(TABLE, null, GAME_ID+"=?", new String[]{Long.toString(gameId)}, null, null, null);
        ArrayList<Word> list = new ArrayList<Word>(0);
        for(cursor.moveToFirst(); cursor.getPosition() < cursor.getCount(); cursor.moveToNext()){
            Word word = new Word();
            word.setWord(cursor.getString(cursor.getColumnIndex(WORD)));
            word.setPlayer(cursor.getInt(cursor.getColumnIndex(PLAYER)));
            word.setIndex(cursor.getString(cursor.getColumnIndex(INDEX)));
            list.add(word);
        }

        cursor.close();
        return list;
    }

    public void insert(Word word, long gameId) {
        SQLiteDatabase db = DataBaseHelper.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GAME_ID, gameId);
        values.put(PLAYER, word.getPlayer());
        values.put(WORD, word.getWord());
        values.put(INDEX, word.getStringIndex());

        db.insert(TABLE, null, values);
    }
}
