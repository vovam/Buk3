package ru.bukvica.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.bukvica.models.GameModel;

import java.util.ArrayList;

/**
 * User: vovam
 * Date: 24.10.13
 * Time: 22:21
 */
public class GameDao {
    public static final String TABLE = "game";

    public static final String NET_ID = "net_id";
    public static final String ID = "id";
    public static final String PLAYER1 = "player1";
    public static final String PLAYER2 = "player2";
    public static final String TIME = "time";
    public static final String TYPE = "type";
    public static final String SCORE1 = "score1";
    public static final String SCORE2 = "score2";
    public static final String PLAYER = "player";

    public boolean hasSavedGames(){
        SQLiteDatabase db = DataBaseHelper.getInstance().getWritableDatabase();
        final String sql = "SELECT "+ID+" FROM "+TABLE+" WHERE "+TYPE+"<>"+GameModel.GAME_TYPE_FINISHED+" LIMIT 1";
        Cursor cursor = db.rawQuery(sql, null);
        boolean result = cursor.getCount() == 1;
        cursor.close();
        return result;
    }

    public ArrayList<GameModel> getAll(){
        SQLiteDatabase db = DataBaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.query(TABLE, null, null, null, null, null, ID+" DESC");
        ArrayList<GameModel> list = new ArrayList<GameModel>(0);
        for(cursor.moveToFirst(); cursor.getPosition() < cursor.getCount(); cursor.moveToNext()){
            GameModel game = new GameModel();
            game.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            game.setCurrentPlayer(cursor.getInt(cursor.getColumnIndex(PLAYER)));
            game.setType(cursor.getInt(cursor.getColumnIndex(TYPE)));
            game.setPlayerName(GameModel.PLAYER1, cursor.getString(cursor.getColumnIndex(PLAYER1)));
            game.setPlayerName(GameModel.PLAYER2, cursor.getString(cursor.getColumnIndex(PLAYER2)));
            game.setScore(GameModel.PLAYER1, cursor.getInt(cursor.getColumnIndex(SCORE1)));
            game.setScore(GameModel.PLAYER2, cursor.getInt(cursor.getColumnIndex(SCORE2)));
            list.add(game);
        }

        cursor.close();
        return list;
    }

    public GameModel get(long id){
        SQLiteDatabase db = DataBaseHelper.getInstance().getWritableDatabase();
        Cursor cursor = db.query(TABLE, null, ID+"=?", new String[] {Long.toString(id)}, null, null, null);
        GameModel game = null;
        if (cursor.moveToFirst()) {
            game = new GameModel();
            game.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            game.setCurrentPlayer(cursor.getInt(cursor.getColumnIndex(PLAYER)));
            game.setType(cursor.getInt(cursor.getColumnIndex(TYPE)));
            game.setPlayerName(GameModel.PLAYER1, cursor.getString(cursor.getColumnIndex(PLAYER1)));
            game.setPlayerName(GameModel.PLAYER2, cursor.getString(cursor.getColumnIndex(PLAYER2)));
            game.setScore(GameModel.PLAYER1, cursor.getInt(cursor.getColumnIndex(SCORE1)));
            game.setScore(GameModel.PLAYER2, cursor.getInt(cursor.getColumnIndex(SCORE2)));
        }

        cursor.close();
        return game;
    }

    public long insert(GameModel game) {
        SQLiteDatabase db = DataBaseHelper.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        if (game.getId() > 0)
            values.put(ID, game.getId());
        values.put(PLAYER, game.getCurrentPlayer());
        values.put(TYPE, game.getType());
        values.put(PLAYER1, game.getPlayerName(GameModel.PLAYER1));
        values.put(PLAYER2, game.getPlayerName(GameModel.PLAYER2));
        values.put(SCORE1, game.getScore(GameModel.PLAYER1));
        values.put(SCORE2, game.getScore(GameModel.PLAYER2));

        long num = db.insert(TABLE, null, values);
        return num;
    }

    public void update(GameModel game) {
        SQLiteDatabase db = DataBaseHelper.getInstance().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PLAYER, game.getCurrentPlayer());
        values.put(PLAYER1, game.getPlayerName(GameModel.PLAYER1));
        values.put(PLAYER2, game.getPlayerName(GameModel.PLAYER2));
        values.put(SCORE1, game.getScore(GameModel.PLAYER1));
        values.put(SCORE2, game.getScore(GameModel.PLAYER2));

        db.update(TABLE, values, ID+"=?", new String[]{Long.toString(game.getId())});
    }
}
