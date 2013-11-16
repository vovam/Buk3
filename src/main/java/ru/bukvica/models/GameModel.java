package ru.bukvica.models;

import ru.bukvica.events.BasicEvent;
import ru.bukvica.events.Event;
import ru.bukvica.events.EventDispatcher;

/**
 * User: vovam
 * Date: 24.10.13
 * Time: 22:20
 */
public class GameModel extends EventDispatcher{
    public static final int PLAYER1 = 0;
    public static final int PLAYER2 = 1;
    public static final int GAME_TYPE_FINISHED = -1;
    public static final int GAME_TYPE_USER = 0;
    public static final int GAME_TYPE_DEVICE = 1;
    public static final int GAME_TYPE_NETWORK = 2;
    public static final int EMPTY = -1;
    public static final String FIRST_TIME = "first_time";

    private long id;
    private int type;
    private int currentPlayer;
    private String[] playerNames;
    private int[] score;

    public long getId() {
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
        notifyChange(Event.PLAYER_CHANGED);
    }

    public String getPlayerName(int index) {
        return playerNames[index];
    }

    public void setPlayerName(int index, String playerName) {
        this.playerNames[index] = playerName;
        notifyChange(Event.PLAYERS_NAMES_CHANGED);
    }


    public int getScore(int index) {
        return score[index];
    }

    public void setScore(int index, int score) {
        this.score[index] = score;
        notifyChange(Event.SCORE_CHANGED);
    }

    public void consume(GameModel game){
        this.id = game.id;
        this.type = game.type;
        this.currentPlayer = game.currentPlayer;
        this.playerNames = game.playerNames;
        this.score = game.score;
        notifyChange(Event.PLAYER_CHANGED);
        notifyChange(Event.SCORE_CHANGED);
        notifyChange(Event.PLAYERS_NAMES_CHANGED);
    }

    public GameModel(){
        this.id = -1;
        this.type = GAME_TYPE_DEVICE;
        this.currentPlayer = PLAYER1;
        this.playerNames = new String[]{"Player1", "Player2"};
        this.score = new int[]{0, 0};
        notifyChange(Event.PLAYER_CHANGED);
        notifyChange(Event.SCORE_CHANGED);
        notifyChange(Event.PLAYERS_NAMES_CHANGED);
    }

    public void notifyChange(String type){
        dispatchEvent(new BasicEvent(type));
    }
}