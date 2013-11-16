package ru.bukvica.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.bukvica.pojos.Word;

/**
 * User: vovam
 * Date: 30.10.13
 * Time: 10:28
 * Project: letterpress
 */
public class DictionaryModel2 {
    public final static int EXISTS = 1;
    public final static int NOT_EXISTS = 0;

    private static DictionaryModel2 ourInstance = new DictionaryModel2();
    private static Map<Integer, String> dictionary;
    private static ArrayList<String> userDictionary;
    private static ArrayList<String> userUnDictionary;

    public static DictionaryModel2 getInstance() {
        return ourInstance;
    }

    public Map<Integer, String> getDictionary(){
        try {
            while (dictionary.size() == 0)
                Thread.sleep(100);
        }catch (InterruptedException e){
            e.printStackTrace();
            Log.e(DictionaryModel2.class.getSimpleName(), "dictionary waiting interrupted");
        }
        return dictionary;
    }

    public List<String> getUserDictionary(){
        return Collections.unmodifiableList(userDictionary);
    }

    public List<String> getUserUnDictionary(){
        return Collections.unmodifiableList(userUnDictionary);
    }

    public void setDictionary(Map<Integer, String> dictionary) {
        DictionaryModel2.dictionary = dictionary;
    }

    public void setUserDictionary(ArrayList<String> userDictionary) {
        DictionaryModel2.userDictionary = userDictionary;
    }

    public void setUserUnDictionary(ArrayList<String> userUnDictionary) {
        DictionaryModel2.userUnDictionary = userUnDictionary;
    }

    private DictionaryModel2() {
        dictionary = new HashMap<Integer, String>(0);
        userDictionary = new ArrayList<String>(0);
        userUnDictionary = new ArrayList<String>(0);
    }

    public void includeToUserDictionary(Word word) {
        userDictionary.add(word.getWord());
    }

    public void excludeFromUserDictionary(Word word) {
        userUnDictionary.add(word.getWord());
    }
}
