package ru.bukvica.pojos;

import ru.bukvica.models.GameModel;

/**
 * User: vovam
 * Date: 24.10.13
 * Time: 23:12
 */
public class Letter{
    public static int COL_SELECTED;
    public static int COL_BG;
    public static int COL_BG_SELECTED;
    public static int COL_PL[] = new int[2];
    public static int COL_PD[] = new int[2];

    private int id;
    private String l;
    private int player;
    private int playerT;
    private boolean hold;

    public Letter() {
        this.id = -1;
        this.l = "*";
        this.player = GameModel.EMPTY;
        this.playerT = GameModel.EMPTY;
    }

    public void consume(Letter l){
        this.id = l.id;
        this.l = l.l;
        this.player = l.player;
        this.playerT = l.playerT;
        this.hold = l.hold;
    }

    public int getId() {
        return id;
    }

    public int getBGColor() {
        if(playerT == GameModel.EMPTY && player == GameModel.EMPTY)
            return COL_BG;
        else if(playerT != GameModel.EMPTY)
            return COL_BG_SELECTED;
        else
            return hold ? COL_PD[player] : COL_PL[player];
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
        this.playerT = GameModel.EMPTY;
    }

    public boolean isHold() {
        return hold;
    }

    public void setHold(boolean hold) {
        this.hold = hold;
        this.playerT = GameModel.EMPTY;
    }

    public String getL() {
        return l;
    }

    public void setPlayerT(int playerT){
        this.playerT = playerT;
    }

    public int getPlayerT() {
        return playerT;
    }

    public Letter clone(){
        Letter clone = new Letter();
        clone.consume(this);
        return clone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setL(String l) {
        this.l = l;
    }
}
