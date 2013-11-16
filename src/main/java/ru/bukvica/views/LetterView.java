package ru.bukvica.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import ru.bukvica.App;

/**
 * User: vovam
 * Date: 27.10.13
 * Time: 15:14
 */
public class LetterView extends TextView {
    private int index = -1;
    public LetterView(Context context) {
        super(context);
        setTypeface(App.getTf());
    }

    public LetterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(App.getTf());
    }

    public void setIndex(int index){
        this.index = index;
    }
    public int getIndex() {
        return index;
    }
}
