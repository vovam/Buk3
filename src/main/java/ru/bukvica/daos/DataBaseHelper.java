package ru.bukvica.daos;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ru.bukvica.App;

/**
 * Created with IntelliJ IDEA.
 * User: vovam
 * Date: 01.11.13
 * Time: 12:45
 * To change this template use File | Settings | File Templates.
 */
final class DataBaseHelper extends SQLiteOpenHelper {

    private static DataBaseHelper instance;

    private static final String DATABASE_NAME = "GameDB";
    private static final int DATABASE_VERSION = 1;

    public static DataBaseHelper getInstance(){
        if(instance == null)
            instance = new DataBaseHelper();
        return instance;
    }

    private DataBaseHelper() {
        super(App.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
//        database.beginTransaction();

        String sql = "CREATE TABLE "+GameDao.TABLE+" ("+
                GameDao.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                GameDao.NET_ID+" INTEGER DEFAULT 0, "+
                GameDao.PLAYER1 +" TEXT NOT NULL, "+
                GameDao.PLAYER2 +" TEXT NOT NULL, "+
                GameDao.TIME+" TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "+
                GameDao.TYPE+" INTEGER, "+
                GameDao.SCORE1+" INTEGER DEFAULT 0, "+
                GameDao.SCORE2+" INTEGER DEFAULT 0, "+
                GameDao.PLAYER+" INTEGER)";
        database.execSQL(sql);

        sql = "CREATE TABLE " + WordListDao.TABLE + " (" +
                WordListDao.GAME_ID + " INTEGER, " +
                WordListDao.WORD + " TEXT NOT NULL, " +
                WordListDao.INDEX + " TEXT NOT NULL, " +
                WordListDao.PLAYER + " INTEGER)";
        database.execSQL(sql);

        sql = "CREATE TABLE "+LettersDao.TABLE+" ("+
                LettersDao.ID+" INTEGER, "+
                LettersDao.GAME_ID+" INTEGER, "+
                LettersDao.PLAYER +" INTEGER, "+
                LettersDao.HOLD +" INTEGER, "+
                LettersDao.L+" TEXT NOT NULL)";
        database.execSQL(sql);

        sql = "CREATE TABLE "+DictionaryDao.TABLE+" ("+
                DictionaryDao.WORD+" TEXT NOT NULL, "+
                DictionaryDao.TYPE+" INTEGER)";
        database.execSQL(sql);

//        database.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // first iteration. do nothing.
    }

}
