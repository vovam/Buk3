package ru.bukvica.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import ru.bukvica.App;
import ru.bukvica.R;

/**
 * User: vovam
 * Date: 02.11.13
 * Time: 1:36
 * Project: letterpress
 */
public class HelpActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(App.getAppTheme());
        setContentView(R.layout.help);

//        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg)));
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
