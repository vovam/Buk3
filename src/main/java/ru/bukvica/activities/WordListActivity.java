package ru.bukvica.activities;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import ru.bukvica.App;
import ru.bukvica.R;
import ru.bukvica.daos.GameDao;
import ru.bukvica.daos.WordListDao;
import ru.bukvica.models.GameModel;
import ru.bukvica.pojos.Word;
import ru.bukvica.views.StartView;

import java.util.ArrayList;

/**
 * User: vovam
 * Date: 24.10.13
 * Time: 22:18
 */
public class WordListActivity extends Activity {
    private TableLayout view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(App.getAppTheme());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.words_list);
        if(App.getAppTheme() == R.style.AppThemeNight)
            findViewById(R.id.word_list_row).setBackgroundDrawable(getResources().getDrawable(R.drawable.border_night));

        Bundle bundle = getIntent().getExtras();
        if(!bundle.containsKey(GameDao.ID))
            return;

        GameDao gameDao = new GameDao();
        GameModel game = gameDao.get(getIntent().getExtras().getLong(GameDao.ID));
        WordListDao wordListDao = new WordListDao();
        ArrayList<Word> wordList = wordListDao.getAll(game.getId());

        TableLayout tableLayout = (TableLayout)findViewById(R.id.words_list_scrollable);

        TextView textViewPlayer1 = (TextView)findViewById(R.id.words_list_player1_name);
        TextView textViewPlayer2 = (TextView)findViewById(R.id.words_list_player2_name);
        textViewPlayer1.setText(game.getPlayerName(GameModel.PLAYER1));
        textViewPlayer2.setText(game.getPlayerName(GameModel.PLAYER2));
        textViewPlayer1.setTypeface(App.getTf());
        textViewPlayer2.setTypeface(App.getTf());

        for(int i = 0; i < wordList.size(); i++){
            TableRow row = (TableRow)TableRow.inflate(this, R.layout.words_list_row, null);
            TextView textView1 = (TextView)row.getChildAt(0);
            TextView textView2 = (TextView)row.getChildAt(1);
            textView1.setTypeface(App.getTf());
            textView2.setTypeface(App.getTf());
            textView1.setText(wordList.get(i).getWord());
            if(i+1 < wordList.size()){
                textView2.setText(wordList.get(i+1).getWord());
                i++;
            }else
                textView2.setText("");
            tableLayout.addView(row);
        }


    }

    @Override
    public void setTheme(int id){
        super.setTheme(id);
        if(id == R.style.AppThemeNight)
            getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg_night)));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
