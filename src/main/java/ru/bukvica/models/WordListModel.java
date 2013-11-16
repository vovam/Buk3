package ru.bukvica.models;

import ru.bukvica.pojos.Word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: vovam
 * Date: 24.10.13
 * Time: 22:20
 */
public class WordListModel {
    public final static int NOT_USED = 0;
    public final static int USED = 1;
    public final static int PART = 2;

    private ArrayList<Word> words;

    public WordListModel() {
       words = new ArrayList<Word>(0);
    }

    public WordListModel(ArrayList<Word> words) {
        this.words = words;
    }

    public List<Word> getWords() {
        return Collections.unmodifiableList(words);
    }

    public void addWord(Word word){
        this.words.add(word);
    }

    public void addAll(ArrayList<Word> words){
        this.words = words;
    }
}
