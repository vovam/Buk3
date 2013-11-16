package ru.bukvica.controller;

import android.os.Bundle;
import android.os.Handler;
import ru.bukvica.App;
import ru.bukvica.daos.GameDao;
import ru.bukvica.daos.LettersDao;
import ru.bukvica.models.GameModel;
import ru.bukvica.models.LettersModel;
import ru.bukvica.pojos.Letter;
import ru.bukvica.utils.LetterUtils;

/**
 * User: vovam
 * Date: 05.11.13
 * Time: 11:01
 * Project: letterpress
 */
public class LettersController {
    private LettersModel model;
    private LettersDao dao;
    private Handler handler;

    public LettersController(LettersModel model){
        this.model = model;
        this.dao = new LettersDao();

        LetterUtils.setColors(App.getContext());
    }
    public Letter[] getArray(){
        Letter lettersArray[] = new Letter[LettersModel.SIZE*LettersModel.SIZE];

        String alphabet[] = LetterUtils.getAlphabet(App.getContext());

        for(int i = 0; i < LettersModel.SIZE; i++)
            for(int j = 0; j < LettersModel.SIZE; j++){
                Letter l = new Letter();
                l.setId(i*LettersModel.SIZE+j);
                l.setL(alphabet[i*LettersModel.SIZE+j]);
                lettersArray[i*LettersModel.SIZE+j] = l;
            }

        return lettersArray;
    }

    public void letterToggle(Letter letter, int currentPlayer){
        Letter l = letter.clone();
        l.setPlayerT(l.getPlayerT() == GameModel.EMPTY ? currentPlayer : GameModel.EMPTY);
        model.updateLetter(l.getId(), l);
    }

    public void wordPlay(){
        int size = LettersModel.SIZE*LettersModel.SIZE;
        for (int i = 0 ; i < size; i++){
            Letter l = model.getLetter(i);
            if(l.getPlayerT() == GameModel.EMPTY)
                continue;
            if(!l.isHold())
                l.setPlayer(l.getPlayerT());
            else
                l.setPlayer(l.getPlayer());
            model.updateLetter(i, l);
        }

        for(int i = 0; i < LettersModel.SIZE*LettersModel.SIZE; i++){
            Letter l = model.getLetter(i);
            if(!l.isHold() && checkLetterTake(i) || l.isHold() && checkLetterRelease(i)){
                l.setHold(!l.isHold());
                model.updateLetter(i, l);
            }
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                dao.update(model.getArray(), App.getGameId());
            }
        });
    }

    public void wordClear(){
        for(int i = 0; i < LettersModel.SIZE*LettersModel.SIZE; i++){
            Letter l = model.getLetter(i);
            if(l.getPlayerT() != GameModel.EMPTY){
                l.setPlayer(l.getPlayer());
                model.updateLetter(i, l);
            }
        }
    }

    private boolean checkLetterTake(int i){
        int S = LettersModel.SIZE;
        int __i = i-LettersModel.SIZE, _i = i-1, i_=i+1, i__=i+LettersModel.SIZE;
        int res = 0;
        if(model.getLetter(i).getPlayer() == GameModel.EMPTY)
            return false;
        res += ((__i >= 0 &&
                checkPlayers(__i, i)) ||
                __i < 0
                ? 1 : 0);
        res += ((i__ < S * S &&
                checkPlayers(i__, i)) ||
                i__ >= S * S
                ? 1 : 0);
        res += ((_i >= 0 &&
                i% S != 0 &&
                checkPlayers(_i, i)) ||
                _i < 0 ||
                i% S == 0
                ? 1 : 0);
        res += ((i_  < S * S &&
                i% S != (S -1) &&
                checkPlayers(i_, i)) ||
                i_ >= S * S ||
                i% S == (S -1)
                ? 1 : 0);

        return res == 4;
    }

    private boolean checkLetterRelease(int i){
        int L = LettersModel.SIZE;
        int __i = i- L, _i = i-1, i_=i+1, i__=i+ L;
        if(model.getLetter(i).isHold()){
            if(__i >= 0 && !checkPlayers(__i, i))
                return true;
            if(i__ < L * L && !checkPlayers(i__, i))
                return true;
            if(_i >= 0 && i% L != 0 && !checkPlayers(_i, i))
                return true;
            if(i_ < L * L && i% L != (L -1) && !checkPlayers(i_, i))
                return true;
        }
        return false;
    }

    private boolean checkPlayers(int i, int j){
        return model.getLetter(i).getPlayer() == model.getLetter(j).getPlayer();
    }

    public void populateModel(final Bundle bundle) {
        if(bundle != null && bundle.containsKey(GameDao.ID))
            if(bundle.getLong(GameDao.ID) < 0){
                model.setArray(getArray());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dao.addAll(model.getArray(), App.getGameId());
                    }
                }).start();
            }else{
                model.setArray(dao.getAll(bundle.getLong(GameDao.ID)));
            }
    }

    public LettersModel getModel() {
        return model;
    }
}
