package ru.bukvica;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import ru.bukvica.controller.DictionaryLoadTask;

import android.os.Handler;
import android.util.Log;

import ru.bukvica.controller.DictionaryLoadTask2;
import ru.bukvica.models.GameModel;

/**
 * User: vovam
 * Date: 30.10.13
 * Time: 23:07
 * Project: letterpress
 */
public class App extends Application {
    public static final String PREFERENCES = "app_preferences";
    public static final String PREF_APP_THEME = "app_theme";
    private static final String PREF_APP_DEBUG = "app_debug";
    private static final String PREF_PLAYER1_NAME = "app_player1_name";
    private static final String PREF_PLAYER2_NAME = "app_player2_name";

    private DictionaryLoadTask2 task;
    private static App instance;
    private static long gameId;
    private static boolean locked;
    private static int theme;
    private static boolean debug;
    private static Typeface tf;
    private static Handler handler;

    public static long getGameId() {
        return gameId;
    }

    public static void setGameId(long gameId) {
        App.gameId = gameId;
    }

    public static boolean lock(){
        if(locked)
            return false;
        else
            locked = true;
        return true;
    }

    public static void unlock(int delay){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                locked = false;
            }
        }, delay);
    }

    public static void setAppTheme(int theme) {
        SharedPreferences.Editor editor = instance.getSharedPreferences(App.PREFERENCES, MODE_MULTI_PROCESS).edit();
        editor.putInt(App.PREF_APP_THEME, theme);
        editor.commit();
        App.theme = theme;
    }

    public static int getAppTheme(){
        return theme;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        SharedPreferences.Editor editor = instance.getSharedPreferences(App.PREFERENCES, MODE_MULTI_PROCESS).edit();
        editor.putBoolean(App.PREF_APP_DEBUG, debug);
        editor.commit();
        App.debug = debug;
    }

    public static Typeface getTf() {
        return tf;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        task = new DictionaryLoadTask2();
        task.setParameters(getBaseContext());
        task.execute();
        handler = new Handler();
        instance = this;
        SharedPreferences preferences = getSharedPreferences(PREFERENCES, MODE_MULTI_PROCESS);
        theme = preferences.getInt(PREF_APP_THEME, R.style.AppThemeDay);
        debug = preferences.getBoolean(PREF_APP_DEBUG, false);
        try {
            tf = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/typeface.ttf");
        }catch (Exception e){
            e.printStackTrace();
            Log.e(App.class.getSimpleName(), e.toString());
        }
    }

    public static Context getContext(){
        return instance.getApplicationContext();
    }

    public static void setPlayerName(String text, int player) {
        SharedPreferences.Editor editor = instance.getSharedPreferences(App.PREFERENCES, MODE_MULTI_PROCESS).edit();
        editor.putString(player == GameModel.PLAYER1 ? App.PREF_PLAYER1_NAME : App.PREF_PLAYER2_NAME, text);
        editor.commit();
    }

    public static String getPlayerName(int player) {
        SharedPreferences preferences = instance.getSharedPreferences(PREFERENCES, MODE_MULTI_PROCESS);
        String defaultValue = getContext().getResources().getString(player == GameModel.PLAYER1 ? R.string.player1_name : R.string.player2_name);
        return preferences.getString(player == GameModel.PLAYER1 ? App.PREF_PLAYER1_NAME : App.PREF_PLAYER2_NAME, defaultValue);
    }
}
