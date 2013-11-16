package ru.bukvica.models;

import ru.bukvica.events.BasicEvent;
import ru.bukvica.events.Event;
import ru.bukvica.events.EventDispatcher;
import ru.bukvica.pojos.*;

import java.util.ArrayList;

/**
 * User: vovam
 * Date: 24.10.13
 * Time: 22:48
 */
public class LettersModel extends EventDispatcher{
    public static final int SIZE = 5;
    private Letter array[];
    private ArrayList<Integer> changed;
    private String letters;

    public LettersModel(){
        this.array = new Letter[SIZE*SIZE];
        this.changed = new ArrayList<Integer>();
        StringBuilder stringBuilder = new StringBuilder(array.length);
        for(int i = 0; i < array.length; i++){
            array[i] = new Letter();
            stringBuilder.append(array[i].getL());
        }
        letters = stringBuilder.toString();
    }

    public void setArray(Letter[] letters) {
        this.array = letters;
        this.changed = new ArrayList<Integer>(letters.length);
        StringBuilder stringBuilder = new StringBuilder(array.length);
        for(int i = 0; i < array.length; i++){
            array[i] = letters[i].clone();
            changed.add(i);
            stringBuilder.append(array[i].getL());
        }
        this.letters = stringBuilder.toString();
        notifyChange(Event.LETTER_TOGGLED);
    }

    public Letter getLetter(int index){
        return array[index].clone();
    }

    public void updateLetter(int index, Letter l){
        changed.add(index);
        array[index].consume(l);
        notifyChange(Event.LETTER_TOGGLED);
    }

    public Letter[] getArray(){
        return array.clone();
    }

    public ArrayList<Integer> getChangedLetter() {
        return changed;
    }

    private void notifyChange(String type) {
        dispatchEvent(new BasicEvent(type));
        changed.clear();
    }

    public String getLettersString() {
        return letters;
    }
}
