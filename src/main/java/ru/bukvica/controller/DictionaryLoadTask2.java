package ru.bukvica.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.bukvica.R;
import ru.bukvica.daos.DictionaryDao;
import ru.bukvica.models.DictionaryModel;
import ru.bukvica.models.DictionaryModel2;

/**
 * User: vovam
 * Date: 30.10.13
 * Time: 22:14
 * Project: letterpress
 */
public class DictionaryLoadTask2 extends AsyncTask<Void, Integer, Void> {
    private Context context;
    private int WORD_LENGHT_MIN = 2;
    private int WORD_LENGHT_MAX = 20;

    public void setParameters(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        try {

            DictionaryModel2 dictionary = DictionaryModel2.getInstance();

            InputStream inputStream = context.getResources().openRawResource(R.raw.words_txt);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "cp1251"), 65536);
            String line;
            Map<Integer, String> words = new HashMap<Integer, String>();
            for(int i = WORD_LENGHT_MIN; i <= WORD_LENGHT_MAX; i++){
                line = reader.readLine();
                words.put(i, line);
            }

            inputStream.close();
            reader.close();

            dictionary.setDictionary(words);
            dictionary.setUserDictionary(new DictionaryDao().getAll(DictionaryDao.TYPE_USER_INCLUDE));
            dictionary.setUserUnDictionary(new DictionaryDao().getAll(DictionaryDao.TYPE_USER_EXCLUDE));

        }catch (IOException e){
            e.printStackTrace();
            Log.e(DictionaryLoadTask2.class.getSimpleName(), "reading dictionary");
        }
        return null;
    }
}
