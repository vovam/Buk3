package ru.bukvica.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.bukvica.App;
import ru.bukvica.R;

/**
 * User: vovam
 * Date: 07.11.13
 * Time: 19:46
 * Project: letterpress
 */
public class HelpOverlayDialog extends Dialog implements View.OnClickListener {
    private LinearLayout layout;
    private int state = 0;
    public HelpOverlayDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.help_overlay1);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        setCanceledOnTouchOutside(true);

        LinearLayout view = (LinearLayout)findViewById(R.id.help_overlay1);
        int childCount = view.getChildCount();
        for(int i = 0; i < childCount; i++){
            TextView tv = (TextView)view.getChildAt(i);
            tv.setShadowLayer(40.0f, 0.0f, 0.0f, Color.BLACK);
            tv.setTypeface(App.getTf());
        }
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(state == 0){
            setContentView(R.layout.help_overlay2);
            layout = (LinearLayout)findViewById(R.id.help_overlay2);
            int childCount = layout.getChildCount();
            for(int i = 0; i < childCount; i++){
                TextView tv = (TextView)layout.getChildAt(i);
                tv.setShadowLayer(40.0f, 0.0f, 0.0f, Color.BLACK);
                tv.setTypeface(App.getTf());
            }
            layout.setOnClickListener(this);
            state++;
        }else
            dismiss();
    }
}
