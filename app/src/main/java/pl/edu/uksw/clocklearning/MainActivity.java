package pl.edu.uksw.clocklearning;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static int h;
    public static int m;

    public static int currentH;
    public static int currentM;

    Random rnd;
    TextView hourTV;
    LinearLayout container;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hourTV = (TextView) findViewById(R.id.hour);
        container = (LinearLayout) findViewById(R.id.container);

        rnd = new Random();

        MainActivity.h = rnd.nextInt(12)+1;
        MainActivity.m = rnd.nextInt(12)*5;
        drawClock();
        setRandomHour();
    }

    private void setRandomHour() {
        MainActivity.currentH = rnd.nextInt(12) + 1;
        MainActivity.currentM = rnd.nextInt(12) * 5;
        setDigitalHour(MainActivity.currentH, MainActivity.currentM);
    }

    private void setDigitalHour(int h, int m) {
        NumberFormat f = new DecimalFormat("00");

        hourTV.setText(f.format(h) + " : " + f.format(m));
    }

    private void drawClock(){
        int w = getWindowManager().getDefaultDisplay().getWidth();
        Bitmap bg = Bitmap.createBitmap(w, (int) (0.7 * w), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);

        drawFace(canvas, w / 2, (int) (0.7 * w / 2), (int) (0.6 * w / 2));
        drawHands(canvas, w / 2, (int) (0.7 * w / 2), (int) (0.6 * w / 2), MainActivity.h, MainActivity.m);

        ImageView clock = (ImageView) findViewById(R.id.clock);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            clock.setBackground(new BitmapDrawable(getApplicationContext().getResources(), bg));
        }else {
            clock.setBackgroundDrawable(new BitmapDrawable(getApplicationContext().getResources(), bg));
        }
    }

    private void drawHands(Canvas canvas, int x, int y, int r, int h, int m) {
        Paint pWhite = new Paint();
        Paint pBlue = new Paint();
        Paint pBlack = new Paint();
        Paint pBlack2 = new Paint();
        pWhite.setColor(Color.parseColor("#FFFFFF"));
        pBlue.setColor(Color.parseColor("#0000FF"));
        pBlack2.setColor(Color.parseColor("#000000"));
        pBlack.setColor(Color.parseColor("#000000"));
        pBlue.setStrokeWidth(3);
        pBlack2.setStrokeWidth(5);

        int x3 = x+(int)(0.85*r*Math.cos(2*Math.PI*(m-15)/60));
        int y3 = y+(int)(0.85*r*Math.sin(2*Math.PI*(m-15)/ 60));
        canvas.drawLine(x, y, x3, y3, pBlue);

        int x2 = x+(int)(0.65*r*Math.cos(2*Math.PI*((h-3 + m/60f)/12)));
        int y2 = y+(int)(0.65*r*Math.sin(2*Math.PI*((h-3 + m/60f)/12)));
        canvas.drawLine(x, y, x2, y2, pBlack2);

        canvas.drawCircle(x, y, r/18, pWhite);
        canvas.drawCircle(x, y, r/20, pBlack);
    }

    private void drawFace(Canvas canvas, int x, int y, int r) {

        Paint pGreen = new Paint();
        Paint pRed = new Paint();
        Paint pBlackFill = new Paint();
        Paint pBlackStroke = new Paint();
        pGreen.setColor(Color.parseColor("#147D4C"));
        pRed.setColor(Color.parseColor("#FF0000"));
        pBlackFill.setColor(Color.parseColor("#000000"));
        pBlackStroke.setColor(Color.parseColor("#000000"));
        pBlackStroke.setStyle(Paint.Style.STROKE);
        pBlackStroke.setStrokeWidth(2f);
        int size = getResources().getDimensionPixelSize(R.dimen.num_font_size);
        pBlackFill.setTextSize(size);
        canvas.drawCircle(x, y, (int) (1.1 * r), pGreen);
        canvas.drawCircle(x, y, r, pRed);
        canvas.drawCircle(x, y, r, pBlackStroke);

        for(int i = 1; i<=12; ++i){
            int x2 = x+(int)(0.9*r*Math.cos(2*Math.PI*(i-3)/12)) - size/2;
            int y2 = y+(int)(0.9*r*Math.sin(2*Math.PI*(i-3)/12)) + size/2;

            canvas.drawText(String.valueOf(i), x2, y2, pBlackFill);
        }
    }

    public void onNextHour(View view) {
        ++MainActivity.currentH;
        if(MainActivity.currentH == 13){
            MainActivity.currentH = 1;
        }

        setDigitalHour(MainActivity.currentH, MainActivity.currentM);
    }

    public void onPreviousHour(View view) {
        --MainActivity.currentH;
        if(MainActivity.currentH == 0){
            MainActivity.currentH = 12;
        }

        setDigitalHour(MainActivity.currentH, MainActivity.currentM);
    }

    public void onNextMinute(View view) {
        MainActivity.currentM += 5;
        if(MainActivity.currentM == 60){
            MainActivity.currentM = 0;
        }

        setDigitalHour(MainActivity.currentH, MainActivity.currentM);
    }

    public void onPreviousMinute(View view) {
        MainActivity.currentM -= 5;
        if(MainActivity.currentM == -5){
            MainActivity.currentM = 55;
        }

        setDigitalHour(MainActivity.currentH, MainActivity.currentM);
    }

    public void onCheck(View view) {
        if(MainActivity.currentM == MainActivity.m && MainActivity.currentH == MainActivity.h){
            onWin();
        } else {
            onLost();
        }
    }

    private void onWin(){
        container.setBackgroundColor(Color.parseColor("#00FF00"));

        YoYo.with(Techniques.Tada)
                .duration(700)
                .playOn(findViewById(R.id.container));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                container.setBackgroundColor(Color.parseColor("#DDDDDD"));
                MainActivity.h = rnd.nextInt(12)+1;
                MainActivity.m = rnd.nextInt(12)*5;
                drawClock();
                setRandomHour();
            }
        }, 500);
    }

    private void onLost(){
        container.setBackgroundColor(Color.parseColor("#FF0000"));

        YoYo.with(Techniques.Tada)
                .duration(700)
                .playOn(findViewById(R.id.container));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                container.setBackgroundColor(Color.parseColor("#DDDDDD"));
            }
        }, 500);
    }
}