package ru.bukvica.controller;

import android.content.Context;
import android.content.res.Resources;
import android.os.*;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import ru.bukvica.App;
import ru.bukvica.R;
import ru.bukvica.pojos.Letter;
import ru.bukvica.pojos.Word;

import java.util.*;

/**
 * User: vovam
 * Date: 05.11.13
 * Time: 9:54
 * Project: letterpress
 */
public class PlayerController {
    private GameController gameController;
    private ScoreController scoreController;
    private LettersController lettersController;
    private WordController wordController;
    private WordListController wordListController;
    private DictionaryController dictionaryController;
    private Handler handler;
    private CollectWordsTask task;

    private Context context;
    private Resources resources;

    private ArrayList<String> words;
    private String lettersString;

    public PlayerController(final GameController gameController, final ScoreController scoreController, final WordController wordController, final LettersController lettersController, final WordListController wordListController){
        this.gameController = gameController;
        this.scoreController = scoreController;
        this.wordController = wordController;
        this.wordListController = wordListController;
        this.lettersController = lettersController;
        this.dictionaryController = new DictionaryController();

        this.handler = new Handler();

        this.context = App.getContext();
        this.resources = context.getResources();

        this.lettersString = lettersController.getModel().getLettersString();
        this.words = new ArrayList<String>(0);

        this.task = new CollectWordsTask();
        task.execute();
    }

    public void playWord(){
        String tWord = "";
        int[] tWordIndex = new int[0];
        for (int i = 0; i < 4 && tWord.length() == 0; i++) {
            tWord = getDeviceWord();
            tWordIndex = getDeviceWordIndex(tWord);
            if (tWord.length() == 0) {
                try {
                    Toast.makeText(context, resources.getString(R.string.main_word_search_loop), Toast.LENGTH_SHORT).show();
                    Thread.sleep(Toast.LENGTH_SHORT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        final String word = tWord;
        final int[] wordIndex = tWordIndex;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for(int i = 0; i < word.length(); i++){
                        final Letter l = lettersController.getModel().getLetter(wordIndex[i]);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                wordController.letterToggle(l);
                                scoreController.letterToggle(l);
                                lettersController.letterToggle(l, gameController.getModel().getCurrentPlayer());
                            }
                        });
                        Thread.sleep(resources.getInteger(R.integer.letter_player_delay));
                    }
                    Thread.sleep(resources.getInteger(R.integer.device_word_play_delay)/4);
                }catch(InterruptedException e){
                    e.printStackTrace();
                    Log.e(getClass().getSimpleName(), e.toString());
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        wordListController.addWord(new Word(gameController.getModel().getCurrentPlayer(), wordIndex, word));
                        lettersController.wordPlay();
                        scoreController.wordPlay();
                        wordController.wordPlay(resources.getInteger(R.integer.device_word_play_delay)/2);
                        App.unlock(resources.getInteger(R.integer.device_word_play_delay)/2);
                        gameController.changeCurrentPlayer();
                    }
                });
            }
        }).start();
    }

    private String getDeviceWord(){
        synchronized (words){
            while(words.size() > 0){
                String word = words.get(0);
                if(wordListController.contains(word))
                    words.remove(0);
                else
                    return word;
            }
        }
        return "";
    }

    private int[] getDeviceWordIndex(final String word){
        final int[] wordIndex = new int[word.length()];
        Arrays.fill(wordIndex, -2);
        for(int i = 0; i < word.length(); i++){
            String temp = lettersString;
            String wl = word.substring(i, i+1);

            boolean way = Math.random() > 0.5;
            int pos = way ? lettersString.indexOf(wl) : lettersString.lastIndexOf(wl);

            while(way && indexOf(wordIndex, pos) >= 0)
                pos = temp.indexOf(wl, pos+1);

            while(!way && indexOf(wordIndex, pos) >= 0)
                pos = temp.lastIndexOf(wl, pos-1);

            wordIndex[i] = pos;
        }
        return wordIndex;
    }

    private int indexOf(int[] array, int value){
        for(int i = 0; i < array.length; i++)
            if(array[i] == value)
                return i;
        return -1;
    }

    private class CollectWordsTask extends AsyncTask<Void, Integer, Void> {

        private boolean stop;

        @Override
        protected Void doInBackground(Void... params) {
            int iteration = 0;
            while (!stop) {
                iteration++;
                String anagram = getAnagram();

                if (anagram.length() < getMeanLength()-1 || anagram.length() > getMeanLength()+5)
                    continue;

                ArrayList<String> anagrams = dictionaryController.getWords(anagram);
                for(int i = anagrams.size()-1; i >= 0; i--){
                    String word = anagrams.get(i);
                    if(words.contains(word))
                        anagrams.remove(word);
                    if(wordListController.contains(word))
                        anagrams.remove(word);
                }

                words.addAll(anagrams);

                synchronized (words){
                    Collections.sort(words, new Comparator<String>() {
                        @Override
                        public int compare(String lhs, String rhs) {
                            return rhs.length() - lhs.length();
                        }
                    });
                }

                if(words.size() > 300)
                    stop = true;
                else if(words.size() > 100)
                    Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
                else if(words.size() > 60)
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                else
                    Process.setThreadPriority(Process.THREAD_PRIORITY_MORE_FAVORABLE);
                if(iteration%300 == 0)
                    Log.i(CollectWordsTask.class.getSimpleName(), "Iteration="+iteration+" Words="+words.size());

            }
            return null;
        }

        private String getAnagram(){
            StringBuilder anagram = new StringBuilder("");
            ArrayList<Integer> anagramIndex = new ArrayList<Integer>(0);
            List<Word> usedWords = wordListController.getModel().getWords();

            if(usedWords.size() > 0){
                int rnd = (int)(Math.random()*usedWords.size());
                String root = usedWords.get(rnd).getWord();
                int[] rootIndex = usedWords.get(rnd).getIndex();
                for(int j = 0; j < root.length(); j++)
                    if(Math.random() > 0.5 && !anagramIndex.contains(rootIndex[j])){
                        anagram.append(root.substring(j, j+1));
                        anagramIndex.add(rootIndex[j]);
                    }
            }

            for(int j = 0; j < lettersString.length(); j++)
                if(Math.random() > 0.85 && !anagramIndex.contains(j)){
                    anagram.append(lettersString.substring(j, j+1));
                    anagramIndex.add(j);
                }

            char buf[] = anagram.toString().toCharArray();
            Arrays.sort(buf);
            return new String(buf);
        }

        private int getMeanLength(){
            return 4;
        }
    }
}
