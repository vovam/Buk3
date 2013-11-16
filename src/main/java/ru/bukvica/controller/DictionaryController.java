package ru.bukvica.controller;

import ru.bukvica.daos.DictionaryDao;
import ru.bukvica.models.*;
import ru.bukvica.pojos.Word;
import android.os.Handler;
import java.util.*;

/**
 * User: vovam
 * Date: 30.10.13
 * Time: 10:30
 * Project: letterpress
 */
public class DictionaryController {
    private DictionaryModel dictionary;
    private DictionaryDao dao;

    public DictionaryController(){
        dictionary = DictionaryModel.getInstance();
        dao = new DictionaryDao();
    }

    public boolean containsWord(String word){
        List<String> dictionaryWords = dictionary.getDictionary();
        int index = Collections.binarySearch(dictionaryWords, Word.getAnagram(word), new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return Word.getAnagram(lhs).compareTo(rhs);
            }
        });

        int result = DictionaryModel.NOT_EXISTS;

        if(index > -1){
            int i = index;
            while (!dictionaryWords.get(i).equals(word) && Word.getAnagram(dictionaryWords.get(i)).equals(Word.getAnagram(word)))
                i++;

            if(dictionaryWords.get(i).equals(word))
                result = DictionaryModel.EXISTS;

            i = index;
            while (!dictionaryWords.get(i).equals(word) && Word.getAnagram(dictionaryWords.get(i)).equals(Word.getAnagram(word)))
                i--;

            if(dictionaryWords.get(i).equals(word))
                result = DictionaryModel.EXISTS;
        }

        List<String> userDictionary = dictionary.getUserDictionary();
        List<String> userUnDictionary = dictionary.getUserUnDictionary();

        if(result == DictionaryModel.NOT_EXISTS && userDictionary != null && userDictionary.contains(word))
            result = DictionaryModel.EXISTS;

        if(result == DictionaryModel.EXISTS && userUnDictionary.contains(word))
            result = DictionaryModel.NOT_EXISTS;

        return result == DictionaryModel.EXISTS;
    }

    public ArrayList<String> getWords(String anagram){
        List<String> dictionaryWords = dictionary.getDictionary();
        int i = Collections.binarySearch(dictionaryWords, anagram, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return Word.getAnagram(lhs).compareTo(rhs);
            }
        });

        int j = i;
        ArrayList<String> words = new ArrayList<String>(0);
        if(i < 0)
            return words;

        while (Word.getAnagram(dictionaryWords.get(j)).equals(anagram)){
            words.add(dictionaryWords.get(j));
            j++;
        }

        j = i-1;
        while (Word.getAnagram(dictionaryWords.get(j)).equals(anagram)){
            words.add(dictionaryWords.get(j));
            j--;
        }

        return words;
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
