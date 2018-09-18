package com.example.kazukihayashi.androidsync;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.instacart.library.truetime.TrueTimeRx;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MovieActivity extends AppCompatActivity {

    private ConstraintLayout mConstraintLayout;
    private TextView mTextView;
    private CompositeDisposable mDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        mTextView = findViewById(R.id.message_string);
        mConstraintLayout = findViewById(R.id.background);

        Map<Calendar, MovieModel> movies = new HashMap<>();
        movies.put(getCalender(getResources().getInteger(R.integer.start_hour), getResources().getInteger(R.integer.start_minute)), new MovieModel(1, getString(R.string.message_movie1), Color.BLUE));
        movies.put(getCalender(getResources().getInteger(R.integer.start_hour), getResources().getInteger(R.integer.start_minute) + 1), new MovieModel(1, getString(R.string.message_movie2), Color.RED));
        movies.put(getCalender(getResources().getInteger(R.integer.start_hour), getResources().getInteger(R.integer.start_minute) + 2), new MovieModel(1, getString(R.string.message_movie3), Color.GREEN));

        mDisposable = new CompositeDisposable();
        mDisposable.add(
                TrueTimeRx.build()
                        .initializeRx("time.google.com")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(date -> {
                            for (Map.Entry<Calendar, MovieModel> movie : movies.entrySet()) {
                                new Handler().postDelayed(() -> {
                                    mConstraintLayout.setBackgroundColor(movie.getValue().getBgColor());
                                    mTextView.setText(MessageFormat.format("{0}: {1}分間", movie.getValue().getMessage(), movie.getValue().getLength()));
                                }, movie.getKey().getTimeInMillis() - date.getTime());
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
