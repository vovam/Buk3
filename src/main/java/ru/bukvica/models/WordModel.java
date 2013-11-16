package ru.bukvica.models;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import ru.bukvica.events.BasicEvent;
import ru.bukvica.events.Event;
import ru.bukvica.events.EventDispatcher;
import ru.bukvica.pojos.Letter;

import java.util.ArrayList;

/**
 * User: vovam
 * Date: 25.10.13
 * Time: 10:20
 */
public class WordModel extends EventDispatcher{
    private SpannableStringBuilder word = new SpannableStringBuilder("");
    private ArrayList<Integer> index = new ArrayList<Integer>(20);

    public int[] getIndex(){
        int[] index = new int[this.index.size()];
        for(int i = 0; i < this.index.size(); i++)
            index[i] = this.index.get(i);
        return index;
    }

    public void clear(){
        word = new SpannableStringBuilder("");
        index = new ArrayList<Integer>(20);
        notifyChange(Event.WORD_CLEARED);
    }

    public void addLetter(Letter l){
        int color = l.getPlayer() == GameModel.EMPTY ? Letter.COL_SELECTED : Letter.COL_PL[l.getPlayer()];
        ForegroundColorSpan span = new ForegroundColorSpan(colorDarker(color));
        word.append(l.getL().toUpperCase());
        word.setSpan(span, word.length()-1, word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        this.index.add(l.getId());
        notifyChange(Event.LETTER_TOGGLED);
    }

    public void removeLetter(Letter l){
        int i = this.index.indexOf(l.getId());
        if(i < 0)
            return;
        word.delete(i, i + 1);
        index.remove(i);
        notifyChange(Event.LETTER_TOGGLED);
    }

    public SpannableStringBuilder getSpannable() {
        return new SpannableStringBuilder(word);
    }

    public String getWord(){
        return word.toString().toLowerCase();
    }

    private static int colorDarker(int color){
        float hsv[] = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8;
        hsv[1] *= 1.5;
        return Color.HSVToColor(hsv);
    }

    private void notifyChange(String type) {
        dispatchEvent(new BasicEvent(type));
    }
}
