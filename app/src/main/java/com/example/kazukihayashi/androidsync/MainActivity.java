package com.example.kazukihayashi.androidsync;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.instacart.library.truetime.TrueTime;
import com.instacart.library.truetime.TrueTimeRx;

import java.io.IOException;
import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final int START_HOUR = 17;
    private static final int START_MINUTE = 48;
    private CompositeDisposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, START_HOUR);
        calendar.set(Calendar.MINUTE, START_MINUTE);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        mDisposable = new CompositeDisposable();
        mDisposable.add(
                TrueTimeRx.build()
                        .initializeRx("time.google.com")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(date -> new Handler().postDelayed(() -> {
                            Intent intent = new Intent(getApplicationContext(), MovieActivity.class);
                            getApplicationContext().startActivity(intent);
                        }, calendar.getTimeInMillis() - date.getTime())
                        )
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
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
