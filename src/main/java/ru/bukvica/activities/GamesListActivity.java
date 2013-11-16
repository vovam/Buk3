package ru.bukvica.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.ScrollView;

import ru.bukvica.App;
import ru.bukvica.R;
import ru.bukvica.daos.GameDao;
import ru.bukvica.models.GameModel;

import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * User: vovam
 * Date: 24.10.13
 * Time: 22:18
 */
public class GamesListActivity extends Activity {
    private LinearLayout view;
    private GameDao gameDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(App.getAppTheme());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.games_list);
        gameDao = new GameDao();
        view = (LinearLayout)findViewById(R.id.games_list_layout);
    }

    private void fillLayout(){

        view.removeAllViewsInLayout();
        ArrayList<GameModel> games = gameDao.getAll();

        for(final GameModel game : games){
            Button button = (Button) View.inflate(this, R.layout.games_list_row, null);
            button.setText(getGameDescription(game));

            Drawable gameIcon = getIcon(game.getId());
            if(gameIcon != null)
                button.setCompoundDrawablesWithIntrinsicBounds(gameIcon, null, null, null);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startGame(game);
                }
            });
            button.setTypeface(App.getTf());
            view.addView(button);
        }
    }

    private void startGame(GameModel game){
        Intent intent = new Intent(getBaseContext(), GameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(GameDao.ID, game.getId());
        bundle.putInt(GameDao.TYPE, game.getType());
        bundle.putInt(GameDao.PLAYER, game.getCurrentPlayer());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private Drawable getIcon(long id){
        try{
            String fileName = getCacheDir()+"/"+Long.toString(id)+".JPEG";
            return BitmapDrawable.createFromStream(new FileInputStream(fileName), fileName);
        }catch(Exception e){
            e.printStackTrace();
            Log.e(GamesListActivity.class.getSimpleName(), e.toString());
            return null;
        }
    }

    private String getGameDescription(GameModel game){
        StringBuilder str = new StringBuilder("");
        str.append(game.getPlayerName(GameModel.PLAYER1));
        str.append(" vs ");
        str.append(game.getPlayerName(GameModel.PLAYER2));
        str.append("\n");
        str.append(game.getScore(GameModel.PLAYER1));
        str.append(" - ");
        str.append(game.getScore(GameModel.PLAYER2));
        return str.toString();
    }

    @Override
    public void setTheme(int id){
        super.setTheme(id);
        if(id == R.style.AppThemeNight)
            getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg_night)));
    }


    @Override
    protected void onResume() {
        super.onResume();
        fillLayout();
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
