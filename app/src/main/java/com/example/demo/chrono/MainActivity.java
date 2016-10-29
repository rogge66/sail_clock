package com.example.demo.chrono;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

class makeBeep {
    public static boolean isBetween(long x, long lower, long upper) {
        return lower <= x && x <= upper;
    }
    ToneGenerator m_tg;

    makeBeep() {
        m_tg = new ToneGenerator(
                AudioManager.STREAM_MUSIC, 100);
    };
    void countDown(long t) {
        t /= 1000;
        if (t / 60 > 5 || t / 60 == 3 || t / 60 == 2) {
            return;
        }
        if (isBetween(t % 60, 0, 5)) {
            m_tg.startTone(ToneGenerator.TONE_PROP_PROMPT, 500);
        }
    }
    void beep() {
        m_tg.startTone(ToneGenerator.TONE_PROP_BEEP, 500);
    }
}

public class MainActivity extends AppCompatActivity {

    Chronometer focus;
    Button start, stop;
    TextView text;
    long timeWhenStopped = 0;
    boolean m_Stopped = false;
    makeBeep m_mb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //focus = new Chronometer (MainActivity.this);
        focus = (Chronometer) findViewById(R.id.cr);
        start = (Button) findViewById(R.id.button1);
        stop = (Button) findViewById(R.id.button2);
        text = (TextView)findViewById(R.id.textView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        m_mb = new makeBeep();

        focus.setCountDown(true);


        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                long t = SystemClock.elapsedRealtime() + timeWhenStopped;
                if (!m_Stopped) {
                    t += 5000 * 60;
                }
                focus.setBase(t);
                focus.start();
                m_mb.beep();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                timeWhenStopped = focus.getBase() - SystemClock.elapsedRealtime();
                focus.stop();
                m_Stopped = true;
                m_mb.beep();
            }
        });
        focus.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long t = SystemClock.elapsedRealtime() - focus.getBase();
                if (t < 0) {
                    m_mb.countDown(-t);
                }
                text.setText(String.valueOf(timeWhenStopped)
                        + " -- "
                        + String.valueOf(t/1000 % 60)
                        + "--"
                        + String.valueOf(t/1000 / 60)
                        + "--"
                        + String.valueOf(focus.getBase())
                        + "--"
                        + String.valueOf(SystemClock.elapsedRealtime()));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
