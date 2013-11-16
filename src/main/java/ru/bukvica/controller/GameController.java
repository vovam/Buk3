package ru.bukvica.controller;

import android.os.Bundle;
import android.os.Handler;
import ru.bukvica.App;
import ru.bukvica.daos.GameDao;
import ru.bukvica.models.GameModel;

/**
 * User: vovam
 * Date: 05.11.13
 * Time: 22:27
 * Project: letterpress
 */
public class GameController {
    private GameModel model;
    private GameDao dao;
    public GameController(GameModel model){
        this.model = model;
        this.dao = new GameDao();
    }

    public void populateModel(Bundle bundle){
        long gameId = -1;
        if(bundle != null && bundle.containsKey(GameDao.ID)){
            if(bundle.getLong(GameDao.ID) < 0){
                model.setType(bundle.getInt(GameDao.TYPE));
                model.setCurrentPlayer(bundle.getInt(GameDao.PLAYER));
                model.setPlayerName(GameModel.PLAYER1, bundle.getString(GameDao.PLAYER1));
                model.setPlayerName(GameModel.PLAYER2, bundle.getString(GameDao.PLAYER2));
                gameId = dao.insert(model);
                model.setId(gameId);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        dao.update(model);
                    }
                });
            }else{
                gameId = bundle.getLong(GameDao.ID);
                model.consume(dao.get(gameId));
            }
        }
        App.setGameId(gameId);
    }

    public GameModel getModel(){
        return model;
    }
    public void changeCurrentPlayer(){
        model.setCurrentPlayer(model.getCurrentPlayer() == GameModel.PLAYER1 ? GameModel.PLAYER2 : GameModel.PLAYER1);
        dao.update(model);
    }
}
