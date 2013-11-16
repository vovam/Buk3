package ru.bukvica.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import ru.bukvica.App;
import ru.bukvica.R;
import ru.bukvica.models.GameModel;

/**
 * User: vovam
 * Date: 02.11.13
 * Time: 1:36
 * Project: letterpress
 */
public class OptionsActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(App.getAppTheme());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.options);

        ToggleButton buttonTheme = ((ToggleButton)findViewById(R.id.options_theme));
        buttonTheme.setChecked(App.getAppTheme() == R.style.AppThemeDay);
        buttonTheme.setTypeface(App.getTf());

        buttonTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                App.setAppTheme(isChecked ? R.style.AppThemeDay : R.style.AppThemeNight);
                OptionsActivity.this.setTheme(App.getAppTheme());
                recreate();
            }
        });

        ToggleButton buttonDebug = ((ToggleButton)findViewById(R.id.options_debug));
        buttonDebug.setChecked(App.isDebug());
        buttonDebug.setTypeface(App.getTf());

        buttonDebug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                App.setDebug(isChecked);
            }
        });

        EditText textViewPlayer1 = (EditText)findViewById(R.id.options_player1_name);
        textViewPlayer1.setTypeface(App.getTf());
        textViewPlayer1.setText(App.getPlayerName(GameModel.PLAYER1));
        textViewPlayer1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if(focus)
                    inputDialogShow((EditText)view, GameModel.PLAYER1);
            }
        });

        EditText textViewPlayer2 = (EditText)findViewById(R.id.options_player2_name);
        textViewPlayer2.setTypeface(App.getTf());
        textViewPlayer2.setText(App.getPlayerName(GameModel.PLAYER2));
        textViewPlayer2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus)
                    inputDialogShow((EditText) view, GameModel.PLAYER1);
            }
        });

        ((TextView)findViewById(R.id.options_player1_name_title)).setTypeface(App.getTf());
        ((TextView)findViewById(R.id.options_player2_name_title)).setTypeface(App.getTf());
        ((TextView)findViewById(R.id.options_theme_title)).setTypeface(App.getTf());
        ((TextView)findViewById(R.id.options_debug_title)).setTypeface(App.getTf());

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

    private void inputDialogShow(final EditText view, final int player){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set player "+(player+1)+" name:");
        final EditText input = new EditText(this);
        input.setText(view.getText());
        input.setHint(view.getHint());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onEditingDone(input, view, player);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void onEditingDone(View input, EditText view, int player){
        TextView textView = (TextView)input;
        String text = textView.getText().toString();
        String hint = textView.getHint().toString();
        view.setText(text);
        App.setPlayerName(text.equals("") ? hint : text, player);
    }
}
