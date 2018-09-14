package com.example.kazukihayashi.androidsync;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.instacart.library.truetime.TrueTime;

import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.start_button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MovieActivity.class);
        startActivity(intent);
    }

    @Deprecated
    private class TrueTimeAsyncTack extends AsyncTask<Calendar, Long, Long> {

        @Override
        protected Long doInBackground(Calendar... calendars) {
            try {
                TrueTime.build().initialize();
                return calendars[0].getTimeInMillis() - TrueTime.now().getTime();
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Long delay) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), MovieActivity.class);
                    getApplicationContext().startActivity(intent);
                }
            }, delay);
        }
    }
}
