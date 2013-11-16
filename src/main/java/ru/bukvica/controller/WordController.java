package ru.bukvica.controller;

import android.os.Handler;
import ru.bukvica.models.GameModel;
import ru.bukvica.models.WordModel;
import ru.bukvica.pojos.Letter;

/**
 * User: vovam
 * Date: 05.11.13
 * Time: 11:04
 * Project: letterpress
 */
public class WordController{
    private WordModel wordModel;

    public WordController(WordModel wordModel){
        this.wordModel = wordModel;
    }

    public void letterToggle(Letter l){
        if(l.getPlayerT() == GameModel.EMPTY)
            wordModel.addLetter(l);
        else
            wordModel.removeLetter(l);

    }

    public void wordPlay(int delay){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wordModel.clear();
            }
        }, delay);
    }

    public void wordClear(){
        wordModel.clear();
    }
}
