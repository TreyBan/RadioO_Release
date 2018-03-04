package com.example.treyban;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treyban.myapplication.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static com.example.treyban.myapplication.MainActivity.APP_PREFERENCES;

public class settingActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    public Button setTime;
    public Button cancel;
    public SeekBar seekBar;
    public TextView timeText;
    public Button reset;
    public Switch Sswitch;
    public static float time = 0;
    CountDownTimer cTimer = null;
    public static SharedPreferences maPrefs;
    public CardView sleep_timer, theme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        maPrefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean APP_THEME = maPrefs.getBoolean("APP_THEME",false);

        if(APP_THEME){
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_setting);

        Sswitch = findViewById(R.id.switch2);
        sleep_timer = findViewById(R.id.sleep_timer);
        theme = findViewById(R.id.theme);

        if(APP_THEME){
            Sswitch.setChecked(true);
            Sswitch.setText(R.string.theme_dark);
            sleep_timer.setCardBackgroundColor(getResources().getColor(R.color.dark_color));
            theme.setCardBackgroundColor(getResources().getColor(R.color.dark_color));
        } else {
            Sswitch.setChecked(false);
            Sswitch.setText(R.string.theme_light);
            sleep_timer.setCardBackgroundColor(getResources().getColor(R.color.white_dark));
            theme.setCardBackgroundColor(getResources().getColor(R.color.white_dark));
        }

        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);

        Sswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences.Editor editor = maPrefs.edit();
                    editor.putBoolean("APP_THEME", true);
                    editor.apply();
                    Sswitch.setText(R.string.theme_dark);
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    SharedPreferences.Editor editor = maPrefs.edit();
                    editor.putBoolean("APP_THEME", false);
                    editor.apply();
                    Sswitch.setText(R.string.theme_light);
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        });



        addListenerOnButton();

        //Реклама
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    public void addListenerOnButton(){
        setTime = findViewById(R.id.setTime);
        seekBar = findViewById(R.id.seekBar);
        timeText = findViewById(R.id.timeText);
        cancel = findViewById(R.id.cancel);
        reset = findViewById(R.id.reset);

        setTime.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Toast.makeText(settingActivity.this, getString(R.string.Music_off) + " " + time + " " + getString(R.string.Hours2), Toast.LENGTH_SHORT).show();
                        startTimer();
                        cancel.setVisibility(cancel.VISIBLE);
                        setTime.setVisibility(setTime.GONE);
                    }
                }
        );

        cancel.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        cancelTimer();
                        Toast.makeText(settingActivity.this, R.string.Timer_stopped, Toast.LENGTH_SHORT).show();
                        cancel.setVisibility(cancel.GONE);
                        setTime.setVisibility(setTime.VISIBLE);
                        time = 0;
                        seekBar.setProgress((int) time);
                    }
                }
        );

        reset.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        SharedPreferences.Editor editor = maPrefs.edit();
                        editor.putBoolean("APP_THEME", false);
                        editor.putBoolean("Tutorial", false);
                        editor.apply();
                    }
                }
        );

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        timeText = findViewById(R.id.timeText);
        time = progress;
        time = time / 2;
        timeText.setText(String.valueOf(time) + " " + getString(R.string.Hours2));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    void startTimer() {
        final long x;
        time = time * 3600000;
        x = (long) time;
        cTimer = new CountDownTimer(x, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        };
        cTimer.start();
    }

    void cancelTimer() {
        cTimer.cancel();
    }
}