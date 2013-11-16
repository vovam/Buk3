package ru.bukvica.controller;

import android.os.Bundle;
import ru.bukvica.App;
import ru.bukvica.daos.GameDao;
import ru.bukvica.daos.WordListDao;
import ru.bukvica.models.WordListModel;
import ru.bukvica.pojos.Word;

import java.util.List;

/**
 * User: vovam
 * Date: 05.11.13
 * Time: 10:17
 * Project: letterpress
 */
public class WordListController{
    private WordListModel model;
    private WordListDao dao;

    public WordListController(WordListModel model){
        this.model = model;
        this.dao = new WordListDao();
    }

    public boolean contains(String word){
        List<Word> words = model.getWords();
        for(Word w : words){
            if(w.getWord().contains(word))
                return true;
        }
        return false;
    }

    public void addWord(Word word) {
        model.addWord(word);
        dao.insert(word, App.getGameId());
    }

    public void populateModel(Bundle bundle) {
        if(bundle != null && bundle.containsKey(GameDao.ID) && bundle.getLong(GameDao.ID) >= 0)
            model.addAll(dao.getAll(bundle.getLong(GameDao.ID)));
    }

    public WordListModel getModel() {
        return model;
    }
}
