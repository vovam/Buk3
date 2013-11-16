package ru.bukvica.events;

/**
 * Created with IntelliJ IDEA.
 * User: vovam
 * Date: 25.10.13
 * Time: 13:55
 * To change this template use File | Settings | File Templates.
 */
public interface Event {
    public static final String SCORE_CHANGED = "score_changed";

    public static final String PLAYER_CHANGED = "player_changed";
    public static final String PLAYERS_NAMES_CHANGED = "players_names_changed";

    public static final String LETTER_TOGGLED = "letter_toggled";

    public static final String WORD_CLEARED = "word_cleared";
    public static final String WORD_ADDED = "word_added";

    public String getType();
    public Object getSource();
    public void setSource(Object source);
}
