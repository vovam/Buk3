package ru.bukvica.controller;

import android.os.Handler;

import ru.bukvica.models.GameModel;
import ru.bukvica.pojos.Letter;

/**
 * User: vovam
 * Date: 05.11.13
 * Time: 11:06
 * Project: letterpress
 */
public class ScoreController{
    private GameModel gameModel;
    private int[] savedScore;

    public ScoreController(GameModel gameModel){
        this.gameModel = gameModel;
        this.savedScore = new int[]{gameModel.getScore(GameModel.PLAYER1), gameModel.getScore(GameModel.PLAYER2)};
    }

    public void letterToggle(Letter letter){
        int []score = new int[]{gameModel.getScore(GameModel.PLAYER1), gameModel.getScore(GameModel.PLAYER2)};
        Letter ll = letter.clone();
        Letter l = letter.clone();

        l.setPlayerT(l.getPlayerT() == GameModel.EMPTY ? gameModel.getCurrentPlayer() : GameModel.EMPTY);

        if(ll.getPlayerT() == GameModel.EMPTY && ll.getPlayer() == GameModel.EMPTY) //letter was toggled first
            score[gameModel.getCurrentPlayer()]++;
        else if(ll.getPlayer() == GameModel.EMPTY)
            score[gameModel.getCurrentPlayer()]--;
        else{
            int value = l.getPlayerT() == GameModel.EMPTY ? -1 : 1;

            if(l.getPlayer() == gameModel.getCurrentPlayer())
                score[gameModel.getCurrentPlayer()]+=value;
            else if(!l.isHold()){
                score[gameModel.getCurrentPlayer()]+=value;
                score[gameModel.getCurrentPlayer() == GameModel.PLAYER1 ? GameModel.PLAYER2 : GameModel.PLAYER1]-=value;
            }
        }
        gameModel.setScore(GameModel.PLAYER1, score[GameModel.PLAYER1]);
        gameModel.setScore(GameModel.PLAYER2, score[GameModel.PLAYER2]);

    }

    public void wordPlay(){
        savedScore = new int[]{gameModel.getScore(GameModel.PLAYER1), gameModel.getScore(GameModel.PLAYER2)};
    }

    public void wordClear(){
        gameModel.setScore(GameModel.PLAYER1, savedScore[GameModel.PLAYER1]);
        gameModel.setScore(GameModel.PLAYER2, savedScore[GameModel.PLAYER2]);
    }
}
