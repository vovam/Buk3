package ru.bukvica.pojos;

import java.util.Arrays;
import java.util.Comparator;

/**
 * User: vovam
 * Date: 24.10.13
 * Time: 23:28
 */
public class Word implements Comparable<Word>, Comparator<Word> {
    private String word;
    private int player;
    private int[] index;

    public Word(){
        word = "";
        player = -1;
        index = new int[0];
    }

    public Word(int player, int[] index, String word) {
        this.player = player;
        this.index = index;
        this.word = word;
    }

    public static String getAnagram(String word){
        char buf[] = word.toCharArray();
        Arrays.sort(buf);
        return new String(buf);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public int[] getIndex() {
        return index;
    }

    public void setIndex(String stringIndex){
        if(stringIndex.equals("")){
            this.index = new int[0];
            return;
        }
        String[] srtingIndexes = stringIndex.split("\\.");
        this.index = new int[srtingIndexes.length];
        for(int i = 0; i < this.index.length; i++)
            this.index[i] = Integer.parseInt(srtingIndexes[i]);
    }

    @Override
    public int compareTo(Word another) {
        return this.word.compareTo(another.word);
    }

    @Override
    public int compare(Word lhs, Word rhs) {
        return getAnagram(lhs.getWord()).compareTo(getAnagram(rhs.getWord()));
    }

    public String getStringIndex() {
        StringBuilder str = new StringBuilder(word.length());
        for(int i = 0; i < word.length(); i++)
            str.append(index[i]).append(".");
        return str.toString();
    }
}
