package ru.bukvica.views;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import ru.bukvica.App;
import ru.bukvica.R;
import ru.bukvica.activities.GameActivity;
import ru.bukvica.events.Event;
import ru.bukvica.events.EventListener;
import ru.bukvica.models.GameModel;
import ru.bukvica.models.LettersModel;
import ru.bukvica.models.WordModel;
import ru.bukvica.pojos.*;

import java.util.ArrayList;
import android.os.Handler;

/**
 * User: vovam
 * Date: 24.10.13
 * Time: 22:20
 */
public class GameView extends TableLayout{

    private ViewListener viewListener;

    private LetterView[] letterViews;
    private TextView[] textViewScores;
    private TextView[] textViewPlayerNames;
    private TextView textViewWord;
    private TextView textViewWordList;
    private TextView textViewWordAdd;
    private TextView textViewWordClear;

    private GameModel gameModel;
    private LettersModel lettersModel;
    private WordModel wordModel;

    private Handler handler;
    private boolean initialized = false;

    private final float WORD_FONT_SIZE;

    public GameView(Context context, AttributeSet attrs){
        super(context, attrs);
        Resources resources = context.getResources();
        handler = new Handler();
        WORD_FONT_SIZE = resources.getDimension(R.dimen.main_word_text_size);
    }

    public void setModels(final GameModel gameModel, final WordModel wordModel, final LettersModel lettersModel){
        this.gameModel = gameModel;
        this.wordModel = wordModel;
        this.lettersModel = lettersModel;

        bindAll();

        lettersModel.addListener(Event.LETTER_TOGGLED, letterListener);
        lettersModel.addListener(Event.WORD_ADDED, letterListener);

        wordModel.addListener(Event.LETTER_TOGGLED, wordListener);
        wordModel.addListener(Event.WORD_CLEARED, wordListener);
        wordModel.addListener(Event.WORD_ADDED, wordListener);

        gameModel.addListener(Event.SCORE_CHANGED, scoreListener);
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    protected void onFinishInflate(){
        final TableLayout layoutLetters = (TableLayout)findViewById(R.id.main_letter_layout);
        final Resources resources = getContext().getResources();
        final int drawableId = App.getAppTheme() == R.style.AppThemeDay ? R.drawable.border_day : R.drawable.border_night;


        letterViews = new LetterView[LettersModel.SIZE*LettersModel.SIZE];

        for(int i = 0; i < LettersModel.SIZE; i++){
            TableRow tableRow = (TableRow)layoutLetters.getChildAt(i);
            for(int j = 0; j < LettersModel.SIZE; j++){
                LetterView letterView = (LetterView)tableRow.getChildAt(j);
                letterView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewListener.onLetterToggle(lettersModel.getLetter(((LetterView) v).getIndex()));
                    }
                });
                letterView.setIndex(i*LettersModel.SIZE+j);
                letterViews[i*LettersModel.SIZE+j] = letterView;
            }
        }

        textViewScores = new TextView[2];
        textViewScores[0] = (TextView)findViewById(R.id.main_score_player1);
        textViewScores[1] = (TextView)findViewById(R.id.main_score_player2);

        textViewPlayerNames = new TextView[2];
        textViewPlayerNames[0] = (TextView)findViewById(R.id.main_score_player1_name);
        textViewPlayerNames[1] = (TextView)findViewById(R.id.main_score_player2_name);

        textViewWord = (TextView)findViewById(R.id.main_word);
        textViewWordList = (TextView)findViewById(R.id.main_word_list);
        textViewWordAdd = (TextView)findViewById(R.id.main_word_add);
        textViewWordClear = (TextView)findViewById(R.id.main_word_clear);

        layoutLetters.post(new Runnable() {
            @Override
            public void run(){
                int height = layoutLetters.getHeight() / 5;
                layoutLetters.setMinimumHeight(height * 5);
                textViewWord.setMinimumHeight(height);
                findViewById(R.id.main_space_1).setMinimumHeight((int) (height * 0.4));
                findViewById(R.id.main_space_2).setMinimumHeight((int) (height * 0.4));
                findViewById(R.id.main_word).setMinimumHeight(height);
                findViewById(R.id.main_score_row).setMinimumHeight(height);
                findViewById(R.id.main_word_list).setMinimumWidth(height);
                findViewById(R.id.main_score_left_layout).setMinimumWidth(height);
                findViewById(R.id.main_score_right_layout).setMinimumWidth(height);

                findViewById(R.id.main_score_row).setBackgroundDrawable(resources.getDrawable(drawableId));
                findViewById(R.id.main_word_row).setBackgroundDrawable(resources.getDrawable(drawableId));
                findViewById(R.id.main_letter_layout).setBackgroundDrawable(resources.getDrawable(drawableId));

                ((TextView) findViewById(R.id.main_word)).setTypeface(App.getTf());
                ((TextView) findViewById(R.id.main_score_player1_name)).setTypeface(App.getTf());
                ((TextView) findViewById(R.id.main_score_player2_name)).setTypeface(App.getTf());
                ((TextView) findViewById(R.id.main_score_player1)).setTypeface(App.getTf());
                ((TextView) findViewById(R.id.main_score_player2)).setTypeface(App.getTf());
            }
        });

        textViewWord.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onWordClick(new Word(gameModel.getCurrentPlayer(), wordModel.getIndex(), wordModel.getWord()));
            }
        });

        textViewWordList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onWordListClick();
            }
        });

        textViewWordAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onWordAddClick(new Word(gameModel.getCurrentPlayer(), wordModel.getIndex(), wordModel.getWord()));
            }
        });

        textViewWordClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onWordClearClick(new Word(gameModel.getCurrentPlayer(), wordModel.getIndex(), wordModel.getWord()));
            }
        });

    }

    private void bindAll(){
        bindPlayersNames();
        bindLetters();
        bindScore();
        bindWord();
    }

    private void bindScore(){
        textViewScores[GameModel.PLAYER1].setText(Integer.toString(gameModel.getScore(GameModel.PLAYER1)));
        textViewScores[GameModel.PLAYER2].setText(Integer.toString(gameModel.getScore(GameModel.PLAYER2)));
    }

    private void bindPlayersNames(){
        textViewPlayerNames[0].setText(gameModel.getPlayerName(0));
        textViewPlayerNames[1].setText(gameModel.getPlayerName(1));
    }

    private void bindWord(){
        SpannableStringBuilder word = wordModel.getSpannable();
        if(word.length() > 21)
            textViewWord.setTextSize(TypedValue.COMPLEX_UNIT_SP, 0.40f*WORD_FONT_SIZE);
        else if(word.length() > 17)
            textViewWord.setTextSize(TypedValue.COMPLEX_UNIT_SP, 0.47f*WORD_FONT_SIZE);
        else if(word.length() > 13)
            textViewWord.setTextSize(TypedValue.COMPLEX_UNIT_SP, 0.55f*WORD_FONT_SIZE);
        else if(word.length() > 9)
            textViewWord.setTextSize(TypedValue.COMPLEX_UNIT_SP, 0.70f*WORD_FONT_SIZE);
        else if(word.length() > 5)
            textViewWord.setTextSize(TypedValue.COMPLEX_UNIT_SP, 0.85f*WORD_FONT_SIZE);
        else
            textViewWord.setTextSize(TypedValue.COMPLEX_UNIT_SP, WORD_FONT_SIZE);
        textViewWord.setText(word);
    }

    private void bindLetters(){
        if(!initialized){
            for(int i = 0 ; i < letterViews.length; i++){
                letterViews[i].setText(lettersModel.getLetter(i).getL());
                letterViews[i].setBackgroundColor(lettersModel.getLetter(i).getBGColor());
            }
            initialized = true;
        }else{
            ArrayList<Integer> changedLetters = lettersModel.getChangedLetter();
            for(Integer i : changedLetters)
                bindLetter(i);
        }
    }

    private void bindLetter(final int id){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (App.getAppTheme() == R.style.AppThemeNight) {
                    if (lettersModel.getLetter(id).getPlayerT() != GameModel.EMPTY)
                        letterViews[id].setTextColor(getResources().getColor(R.color.bg_night));
                    else
                        letterViews[id].setTextColor(getResources().getColor(R.color.text_night));
                }

                letterViews[id].setBackgroundColor(lettersModel.getLetter(id).getBGColor());
            }
        }, animateLetter(id));
    }

    private EventListener letterListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            bindLetters();
        }
    };

    private EventListener wordListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            bindWord();
        }
    };

    private EventListener scoreListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            bindScore();
        }
    };

    private int animateLetter(int letterIndex) {
        Animation animationSet = AnimationUtils.loadAnimation(getContext(), R.anim.letter_toggle);
        letterViews[letterIndex].startAnimation(animationSet);
        return (int)animationSet.getDuration();
    }

    public int animateWord(int animId) {
        Animation animationSet = AnimationUtils.loadAnimation(getContext(), animId);
        textViewWord.startAnimation(animationSet);
        return (int)animationSet.getDuration();
    }

    public void changeAddWordButton(int stateWordAdd) {
        if(stateWordAdd == GameActivity.GameViewListener.STATE_ADD)
            textViewWordAdd.setText(App.getContext().getResources().getString(R.string.main_word_add_sign));
        else
            textViewWordAdd.setText(App.getContext().getResources().getString(R.string.main_word_include_sign));
    }

    public void changeClearWordButton(int stateWordClear) {
        if(stateWordClear == GameActivity.GameViewListener.STATE_CLEAR)
            textViewWordAdd.setText(App.getContext().getResources().getString(R.string.main_word_clear_sign));
        else
            textViewWordAdd.setText(App.getContext().getResources().getString(R.string.main_word_exclude_sign));
    }

    public static interface ViewListener{
        public void onLetterToggle(Letter l);
        public void onWordClick(Word word);
        public void onWordAddClick(Word word);
        public void onWordClearClick(Word word);
        public void onWordListClick();
    }
}
