package ru.bukvica.utils;

import android.content.Context;
import android.content.res.Resources;
import ru.bukvica.App;
import ru.bukvica.R;
import ru.bukvica.models.LettersModel;
import ru.bukvica.pojos.*;

import java.util.Arrays;
import java.util.Collections;

/**
 * User: vovam
 * Date: 26.10.13
 * Time: 13:03
 */
public class LetterUtils {
    public static String[] getAlphabet(Context context){
        String freq = context.getString(R.string.letters_freq);
        String unfreq = context.getString(R.string.letters_unfreq);
        String all = context.getString(R.string.letters_all);
        String letters[] = new String[LettersModel.SIZE*LettersModel.SIZE];
        for(int i = 0; i < 8; i++){
            int rnd = (int) (freq.length() * Math.random());
            letters[i] = freq.substring(rnd, rnd+1);
        }
        for(int i = 8; i < 20; i++){
            int rnd = (int) (unfreq.length() * Math.random());
            letters[i] = unfreq.substring(rnd, rnd+1);
        }
        for(int i = 20; i < 25; i++){
            int rnd = (int) (all.length() * Math.random());
            letters[i] = all.substring(rnd, rnd+1);
        }
        Collections.shuffle(Arrays.asList(letters));
        if(App.isDebug())
            return "п р о к р а с т и н а ц и я б е з д е л и е б р о".split(" ");
        else
            return letters;
    }
    public static void setColors(Context context){
        Resources resources = context.getResources();
        Letter.COL_BG = App.getAppTheme() == R.style.AppThemeDay ? resources.getColor(R.color.bg_day) : resources.getColor(R.color.bg_night);
        Letter.COL_SELECTED = App.getAppTheme() == R.style.AppThemeDay ? resources.getColor(R.color.text_day) : resources.getColor(R.color.text_night);
        Letter.COL_BG_SELECTED = resources.getColor(R.color.selected);
        Letter.COL_PL[0] = resources.getColor(R.color.player1);
        Letter.COL_PL[1] = resources.getColor(R.color.player2);
        Letter.COL_PD[0] = resources.getColor(R.color.player1d);
        Letter.COL_PD[1] = resources.getColor(R.color.player2d);
    }
}
