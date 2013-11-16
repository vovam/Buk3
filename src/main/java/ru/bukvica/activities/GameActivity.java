package ru.bukvica.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.widget.TableLayout;
import ru.bukvica.App;
import ru.bukvica.controller.*;
import ru.bukvica.daos.GameDao;
import ru.bukvica.dialogs.HelpOverlayDialog;
import ru.bukvica.models.GameModel;
import ru.bukvica.models.LettersModel;
import ru.bukvica.models.WordListModel;
import ru.bukvica.models.WordModel;
import ru.bukvica.pojos.Letter;
import ru.bukvica.pojos.Word;
import ru.bukvica.views.GameView;
import ru.bukvica.R;

/**
 * User: vovam
 * Date: 24.10.13
 * Time: 22:18
 */
public class GameActivity extends Activity {
    private GameView view;
    private GameView.ViewListener viewListener;

    private LettersController lettersController;
    private ScoreController scoreController;
    private WordController wordController;
    private DictionaryController2 dictionaryController;
    private WordListController wordListController;
    private GameController gameController;
    private PlayerController2 playerController;

    private Handler handler;

    private MakeSnapshot makeSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(App.getAppTheme());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        handler = new Handler();

        GameModel gameModel = new GameModel();
        WordModel wordModel = new WordModel();
        LettersModel lettersModel = new LettersModel();
        WordListModel wordListModel = new WordListModel();

        Bundle bundle = getIntent().getExtras();

        gameController = new GameController(gameModel);
        gameController.populateModel(bundle);

        lettersController = new LettersController(lettersModel);
        lettersController.populateModel(bundle);

        wordListController = new WordListController(wordListModel);
        wordListController.populateModel(bundle);

        wordController = new WordController(wordModel);
        scoreController = new ScoreController(gameModel);
        dictionaryController = new DictionaryController2();

        viewListener = new GameViewListener();

        view.setModels(gameModel, wordModel, lettersModel);
        view.setViewListener(viewListener);
        setContentView(view);

        if(gameController.getModel().getType() == GameModel.GAME_TYPE_DEVICE){
            playerController = new PlayerController2(gameController, scoreController, wordController, lettersController, wordListController);
            if(gameController.getModel().getCurrentPlayer() == GameModel.PLAYER2){
                App.lock();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playerController.playWord();
                        App.unlock(0);
                    }
                }, getResources().getInteger(R.integer.device_word_play_delay));
            }
        }

        if(bundle.containsKey(GameModel.FIRST_TIME) && bundle.getBoolean(GameModel.FIRST_TIME))
            onHelpOverlay();

        makeSnapshot = new MakeSnapshot((TableLayout)view.findViewById(R.id.main_letter_layout));
        view.postDelayed(makeSnapshot, getResources().getInteger(R.integer.make_snapshot_delay));
    }

    @Override
    public void setTheme(int id){
        super.setTheme(id);
        view = (GameView)TableLayout.inflate(this, R.layout.game, null);
        if(id == R.style.AppThemeNight)
            view.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg_night)));
    }

    public class GameViewListener implements GameView.ViewListener{
        public final static int STATE_ADD = 0;
        public final static int STATE_INCLUDE = 1;
        public final static int STATE_CLEAR = 2;
        public final static int STATE_EXCLUDE = 3;
        private int stateWordAdd;
        private int stateWordClear;

        @Override
        public void onLetterToggle(Letter l) {
            if(stateWordAdd == STATE_INCLUDE){
                stateWordAdd = STATE_ADD;
                view.changeAddWordButton(stateWordAdd);
            }
            if(!App.lock())
                return;
            wordController.letterToggle(l.clone());
            scoreController.letterToggle(l.clone());
            lettersController.letterToggle(l.clone(), gameController.getModel().getCurrentPlayer());

            App.unlock(0);
        }

        @Override
        public void onWordClick(Word word){
            if(!App.lock())
                return;
            if(!App.isDebug()){ //если отлаживаем, то не проверяем слова
                if(!dictionaryController.containsWord(word.getWord())){
                    stateWordAdd = STATE_INCLUDE;
                    view.changeAddWordButton(stateWordAdd);
                    App.unlock(view.animateWord(R.anim.word_cancel));
                    return;
                }
                if(wordListController.contains(word.getWord())){
                    App.unlock(view.animateWord(R.anim.word_cancel));
                    return;
                }
            }

            wordListController.addWord(word);
            lettersController.wordPlay();
            scoreController.wordPlay();

            gameController.changeCurrentPlayer();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    wordController.wordClear();
                    if (gameController.getModel().getType() == GameModel.GAME_TYPE_DEVICE){
                        playerController.playWord();
                        view.animateWord(R.anim.word_apply);
                        view.postDelayed(makeSnapshot, getResources().getInteger(R.integer.make_snapshot_delay));
                    }else
                        App.unlock(0);

                }
            },  2*view.animateWord(R.anim.word_apply));

            view.postDelayed(makeSnapshot, getResources().getInteger(R.integer.make_snapshot_delay));
        }

        @Override
        public void onWordAddClick(Word word){
            if(stateWordAdd == STATE_INCLUDE){
                dictionaryController.includeWord(word);
                stateWordAdd = STATE_ADD;
                view.changeAddWordButton(stateWordAdd);
                return;
            }
            onWordClick(word);
        }

        @Override
        public void onWordClearClick(Word word){
            if(stateWordAdd == STATE_INCLUDE){
                stateWordAdd = STATE_ADD;
                view.changeAddWordButton(stateWordAdd);
                return;
            }
            lettersController.wordClear();
            scoreController.wordClear();
            wordController.wordClear();
        }

        @Override
        public void onWordListClick() {
            Intent intent = new Intent(getBaseContext(), WordListActivity.class);
            intent.putExtra(GameDao.ID, gameController.getModel().getId());
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_bar_help:
                onHelpOverlay();
                return false;
            case R.id.action_bar_list:
                Intent intent = new Intent(getBaseContext(), GamesListActivity.class);
                startActivity(intent);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onHelpOverlay() {
        final Dialog dialog = new HelpOverlayDialog(this);

        dialog.show();
    }

}
