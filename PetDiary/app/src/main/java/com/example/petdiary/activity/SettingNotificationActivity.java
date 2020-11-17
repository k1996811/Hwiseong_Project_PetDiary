package com.example.petdiary.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petdiary.R;

public class SettingNotificationActivity extends AppCompatActivity {

    Switch pushSwitch;
    Switch soundSwitch;
    Switch vibrateSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_notification);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pushSwitch = findViewById(R.id.pushSwitch);
        soundSwitch = findViewById(R.id.soundSwitch);
        vibrateSwitch = findViewById(R.id.vibrateSwitch);

        pushSwitch.setOnCheckedChangeListener(new pushSwitchListener());

    }

    class pushSwitchListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                    dataNotification.createNotificationChannel(new NotificationChannel(
//                            "default", "SensorData", NotificationManager.IMPORTANCE_LOW));
//                }
            }
            else {

            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home ){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
