package ru.bukvica.controller;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;
import android.widget.TableLayout;
import ru.bukvica.App;

import java.io.*;

/**
 * User: vovam
 * Date: 03.11.13
 * Time: 19:20
 * Project: letterpress
 */
public class MakeSnapshot implements Runnable {
    private TableLayout layout;

    public MakeSnapshot(TableLayout layout){
        this.layout = layout;
    }

    @Override
    public void run() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return;

        Bitmap b = Bitmap.createBitmap(layout.getWidth(), layout.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(b);
        layout.draw(c);

        OutputStream out = null;
        b = Bitmap.createScaledBitmap(b, b.getWidth()/2, b.getWidth()/2, true);
        try{
            Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
            File file = new File(App.getContext().getCacheDir(), Long.toString(App.getGameId()) +"."+format.name());
            out = new FileOutputStream(file);
            b.compress(format, 50, new BufferedOutputStream(out));
        }catch(FileNotFoundException e){
            e.printStackTrace();
            Log.e(MakeSnapshot.class.getSimpleName(), e.toString());
        }finally {
            if (out != null) {
                try{
                    out.close();
                }catch (IOException e){
                    Log.e("snapshot", "output stream close", e);
                }
            }
        }

    }
}
