package ru.bukvica.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;
import ru.bukvica.App;
import ru.bukvica.R;

/**
 * User: vovam
 * Date: 24.10.13
 * Time: 22:19
 */

public class StartView extends TableLayout{
    private ViewListener viewListener;
    private TableRow continueRow;
    private Context context;

    public StartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate(){

        continueRow = (TableRow)findViewById(R.id.start_continue_row);

        continueRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onContinueClick();
            }
        });
        findViewById(R.id.start_newgame_row).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onStartClick();
            }
        });
        findViewById(R.id.start_options_row).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onOptionsClick();
            }
        });
        findViewById(R.id.start_exit_row).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListener.onExitClick();
            }
        });
        ((RadioGroup)findViewById(R.id.start_game_type)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                viewListener.onGameTypeClick(checkedId);
            }
        });

        ((TextView)findViewById(R.id.start_continue_symbol)).setTypeface(App.getTf());
        ((TextView)findViewById(R.id.start_newgame_symbol)).setTypeface(App.getTf());
        ((TextView)findViewById(R.id.start_options_symbol)).setTypeface(App.getTf());
        ((TextView)findViewById(R.id.start_exit_symbol)).setTypeface(App.getTf());

        ((TextView)findViewById(R.id.start_continue_text)).setTypeface(App.getTf());
        ((TextView)findViewById(R.id.start_newgame_text)).setTypeface(App.getTf());
        ((TextView)findViewById(R.id.start_options_text)).setTypeface(App.getTf());
        ((TextView)findViewById(R.id.start_exit_text)).setTypeface(App.getTf());

        ((RadioButton)findViewById(R.id.start_rbutton_device)).setTypeface(App.getTf());
        ((RadioButton)findViewById(R.id.start_rbutton_human)).setTypeface(App.getTf());
        ((RadioButton)findViewById(R.id.start_rbutton_network)).setTypeface(App.getTf());

    }

    public TableRow getContinueRow(){
        return continueRow;
    }

    public void setViewListener(ViewListener viewListener){
        this.viewListener = viewListener;
    }

    public static interface ViewListener{
        public void onStartClick();
        public void onContinueClick();
        public void onExitClick();
        public void onOptionsClick();
        public void onGameTypeClick(int checkedId);
    }
}
