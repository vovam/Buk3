package ru.bukvica.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import ru.bukvica.App;
import ru.bukvica.daos.GameDao;
import ru.bukvica.models.GameModel;
import ru.bukvica.views.StartView;
import ru.bukvica.R;

/**
 * User: vovam
 * Date: 24.10.13
 * Time: 22:17
 */
public class StartActivity extends Activity {
    private StartView view;
    private StartView.ViewListener viewListener;

    private GameDao gameDao;
    private int appTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(App.getAppTheme());
        appTheme = App.getAppTheme();

        view = (StartView) TableLayout.inflate(this, R.layout.start, null);
        viewListener = new StartViewListener();
        view.setViewListener(viewListener);
        setContentView(view);

        gameDao = new GameDao();

        if(gameDao.hasSavedGames())
            view.getContinueRow().setVisibility(View.VISIBLE);
        else
            view.getContinueRow().setVisibility(View.GONE);
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
        if(appTheme != App.getAppTheme()){
            recreate();
            return;
        }
        if(gameDao.hasSavedGames())
            view.getContinueRow().setVisibility(View.VISIBLE);
        else
            view.getContinueRow().setVisibility(View.GONE);
    }

    private class StartViewListener implements StartView.ViewListener{
        private int type = GameModel.GAME_TYPE_DEVICE;

        @Override
        public void onStartClick() {
            Intent intent = new Intent(getBaseContext(), GameActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(GameModel.FIRST_TIME, !gameDao.hasSavedGames());
            bundle.putLong(GameDao.ID, -1l);
            bundle.putInt(GameDao.TYPE, type);
            bundle.putInt(GameDao.PLAYER, GameModel.PLAYER1);
            bundle.putString(GameDao.PLAYER1, App.getPlayerName(GameModel.PLAYER1));
            bundle.putString(GameDao.PLAYER2, type == GameModel.GAME_TYPE_USER ? App.getPlayerName(GameModel.PLAYER2) : "Android");
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void onContinueClick() {
            Intent intent = new Intent(getBaseContext(), GamesListActivity.class);
            startActivity(intent);
        }

        @Override
        public void onExitClick() {
            finish();
        }

        @Override
        public void onOptionsClick() {
            Intent intent = new Intent(getBaseContext(), OptionsActivity.class);
            startActivity(intent);
        }

        @Override
        public void onGameTypeClick(int checkedId) {
            switch (checkedId){
                case R.id.start_rbutton_human:
                    type = GameModel.GAME_TYPE_USER;
                    break;
                case R.id.start_rbutton_device:
                    type = GameModel.GAME_TYPE_DEVICE;
                    break;
                case R.id.start_rbutton_network:
                    type = GameModel.GAME_TYPE_NETWORK;
                    break;
            }

        }
    }
}
