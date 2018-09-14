package com.example.kazukihayashi.androidsync;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.instacart.library.truetime.TrueTimeRx;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MovieActivity extends AppCompatActivity {

    private static final int START_HOUR = 19;
    private static final int START_MINUTE_1 = 10;
    private static final int START_MINUTE_2 = START_MINUTE_1 + 1;
    private static final int START_MINUTE_3 = START_MINUTE_2 + 1;

    private TextView mTextView;
    private CompositeDisposable mDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        mTextView = findViewById(R.id.message_string);

        Map<Calendar, String> calendars = new HashMap<>();
        calendars.put(getCalender(START_HOUR, START_MINUTE_1), getString(R.string.message_movie1));
        calendars.put(getCalender(START_HOUR, START_MINUTE_2), getString(R.string.message_movie2));
        calendars.put(getCalender(START_HOUR, START_MINUTE_3), getString(R.string.message_movie3));

        mDisposable = new CompositeDisposable();
        mDisposable.add(
                TrueTimeRx.build()
                        .initializeRx("time.google.com")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(date -> {
                            for (Map.Entry<Calendar, String> calendar : calendars.entrySet()) {
                                new Handler().postDelayed(() -> {
                                    mTextView.setText(calendar.getValue());
                                }, calendar.getKey().getTimeInMillis() - date.getTime());
                            }
                                }
                        )
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

    private Calendar getCalender(int hour, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, second);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

}
