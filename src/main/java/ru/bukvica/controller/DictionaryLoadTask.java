package ru.bukvica.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import ru.bukvica.daos.DictionaryDao;
import ru.bukvica.models.DictionaryModel;
import ru.bukvica.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * User: vovam
 * Date: 30.10.13
 * Time: 22:14
 * Project: letterpress
 */
public class DictionaryLoadTask extends AsyncTask<Void, Integer, Void> {
    private Context context;

    public void setParameters(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        try {

            DictionaryModel dictionary = DictionaryModel.getInstance();

            InputStream inputStream = context.getResources().openRawResource(R.raw.words_txt);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "cp1251"), 65536);
            String line;
            ArrayList<String> words = new ArrayList<String>(130000);
            while ((line = reader.readLine()) != null)
                words.add(line);

            inputStream.close();
            reader.close();

            dictionary.setDictionary(words);
            dictionary.setUserDictionary(new DictionaryDao().getAll(DictionaryDao.TYPE_USER_INCLUDE));
            dictionary.setUserUnDictionary(new DictionaryDao().getAll(DictionaryDao.TYPE_USER_EXCLUDE));

        }catch (IOException e){
            e.printStackTrace();
            Log.e(DictionaryLoadTask.class.getSimpleName(), "reading dictionary");
        }
        return null;
    }
}
