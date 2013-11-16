package ru.bukvica.controller;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import ru.bukvica.daos.DictionaryDao;
import ru.bukvica.models.DictionaryModel;
import ru.bukvica.models.DictionaryModel2;
import ru.bukvica.pojos.Word;

/**
 * User: vovam
 * Date: 30.10.13
 * Time: 10:30
 * Project: letterpress
 */
public class DictionaryController2 {
    private DictionaryModel2 dictionary;
    private DictionaryDao dao;

    public DictionaryController2(){
        dictionary = DictionaryModel2.getInstance();
        dao = new DictionaryDao();
    }

    public String containsAnagram(String anagram){
        Map<Integer, String> dictionaryWords = dictionary.getDictionary();
        String dict = dictionaryWords.get(anagram.length());
        int index = binarySearch(dict, anagram, false);
        if(index < 0)
            return null;
        else
            return dict.substring(index*(anagram.length()), (index+1)*anagram.length());
    }

    public boolean containsWord(String word){
        Map<Integer, String> dictionaryWords = dictionary.getDictionary();
        String dict = dictionaryWords.get(word.length());

        int index = binarySearch(dict, word, true);

        int result = index < 0 ? DictionaryModel2.NOT_EXISTS : DictionaryModel2.EXISTS;

        List<String> userDictionary = dictionary.getUserDictionary();
        List<String> userUnDictionary = dictionary.getUserUnDictionary();

        if(result == DictionaryModel2.NOT_EXISTS && userDictionary != null && userDictionary.contains(word))
            result = DictionaryModel2.EXISTS;

        if(result == DictionaryModel2.EXISTS && userUnDictionary.contains(word))
            result = DictionaryModel2.NOT_EXISTS;

        return result == DictionaryModel2.EXISTS;
    }

    private int binarySearch(String dict, String word, boolean strict){
        String anagram = Word.getAnagram(word);
        int lenght = word.length();
        int words = dict.length()/word.length();
        int low = 0, high = words - 1;
        int mid = words/2;

        while (high > low) {
            String found = dict.substring(lenght * mid, lenght * (mid+1));

            int cmp = anagram.compareTo(Word.getAnagram(found));
            if (cmp == 0){
                if(!strict)
                    return mid;

                int i = mid;
                while (!dict.substring(lenght * i, lenght * (i+1)).equals(word) && anagram.equals(Word.getAnagram(dict.substring(lenght * i, lenght * (i+1)))))
                    i++;

                if(dict.substring(lenght * i, lenght * (i+1)).equals(word))
                    return i;

                i = mid;
                while (!dict.substring(lenght * i, lenght * (i+1)).equals(word) && anagram.equals(Word.getAnagram(dict.substring(lenght * i, lenght * (i+1)))))
                    i--;
                return mid;
            }

            if (cmp < 0)
                high = mid - 1;
            else
                low = mid + 1;

            mid = (low + high) / 2;
        }
        return -mid;
    }

    public void includeWord(final Word word){
        dictionary.includeToUserDictionary(word);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dao.insert(word.getWord(), DictionaryDao.TYPE_USER_INCLUDE);
            }
        });
    }

    public void excludeWord(final Word word){
        dictionary.excludeFromUserDictionary(word);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dao.insert(word.getWord(), DictionaryDao.TYPE_USER_EXCLUDE);
            }
        });
    }
}
