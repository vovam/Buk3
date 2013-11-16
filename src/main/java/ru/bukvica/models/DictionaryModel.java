package ru.bukvica.models;

import android.util.Log;
import ru.bukvica.pojos.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: vovam
 * Date: 30.10.13
 * Time: 10:28
 * Project: letterpress
 */
public class DictionaryModel {
    public final static int EXISTS = 1;
    public final static int NOT_EXISTS = 0;

    private static DictionaryModel ourInstance = new DictionaryModel();
    private static ArrayList<String> dictionary;
    private static ArrayList<String> userDictionary;
    private static ArrayList<String> userUnDictionary;

    public static DictionaryModel getInstance() {
        return ourInstance;
    }

    public List<String> getDictionary(){
        try {
            while (dictionary.size() == 0)
                Thread.sleep(100);
        }catch (InterruptedException e){
            e.printStackTrace();
            Log.e(DictionaryModel.class.getSimpleName(), "dictionary waiting interrupted");
        }
        return Collections.unmodifiableList(dictionary);
    }

    public List<String> getUserDictionary(){
        return Collections.unmodifiableList(userDictionary);
    }

    public List<String> getUserUnDictionary(){
        return Collections.unmodifiableList(userUnDictionary);
    }

    public void setDictionary(ArrayList<String> dictionary) {
        DictionaryModel.dictionary = dictionary;
    }

    public void setUserDictionary(ArrayList<String> userDictionary) {
        DictionaryModel.userDictionary = userDictionary;
    }

    public void setUserUnDictionary(ArrayList<String> userUnDictionary) {
        DictionaryModel.userUnDictionary = userUnDictionary;
    }

    private DictionaryModel() {
        dictionary = new ArrayList<String>(0);
        userDictionary = new ArrayList<String>(0);
        userUnDictionary = new ArrayList<String>(0);

        dictionary.ensureCapacity(130000);
    }

    public void includeToUserDictionary(Word word) {
        userDictionary.add(word.getWord());
    }

    public void excludeFromUserDictionary(Word word) {
        userUnDictionary.add(word.getWord());
    }
}
