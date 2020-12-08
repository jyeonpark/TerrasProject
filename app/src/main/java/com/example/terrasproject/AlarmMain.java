package com.example.terrasproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class AlarmMain extends AppCompatActivity {
    private int hour, minute, qrhour, qrminute;
    String terras,startTime,activity,studentID; //activity값에 따라 다른 실행하는 게 다름
    int usetime;
    private AlarmManager AM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getIntent().getStringExtra("activity");
        studentID = LogIn.studentID;

        Toast.makeText(this,activity,Toast.LENGTH_SHORT).show();

        SharedPreferences sp = getSharedPreferences("file",MODE_PRIVATE);
        //앱이 종료되기 전의 데이터를 불러옴

        terras = sp.getString("terras"+studentID,"");
        startTime = sp.getString("startTime"+studentID,"");
        usetime = sp.getInt("usetime"+studentID,0);

        if(activity.equals("reservation")) {
            regist();
            myStartActivity(ShowReservation.class);
        }
        else{
            cancel();
        }
    }

    public void regist() {

        AM = (AlarmManager) getSystemService(ALARM_SERVICE);

        //예약에 대한 알람
        Intent intent = new Intent(this, Alarm.class);
        intent.putExtra("studentID", studentID);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, Integer.parseInt(studentID), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //qr에 대한 알람, studentID에 100을 곱한값으로 아이디 부여
        Intent intent2 = new Intent(this, Alarm.class);
        intent.putExtra("studentID", studentID);
        PendingIntent pIntent2 = PendingIntent.getBroadcast(this, Integer.parseInt(studentID) * 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = Integer.parseInt(startTime) + usetime + 1;
            minute = 0;
            qrhour = Integer.parseInt(startTime);
            qrminute = 15;

        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar qrcalendar = Calendar.getInstance();
        qrcalendar.set(Calendar.HOUR_OF_DAY, qrhour);
        qrcalendar.set(Calendar.MINUTE, qrminute);
        qrcalendar.set(Calendar.SECOND, 0);
        qrcalendar.set(Calendar.MILLISECOND, 0);

        AM.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);//예약알람
        AM.set(AlarmManager.RTC_WAKEUP, qrcalendar.getTimeInMillis(), pIntent2);//qr알람

    }

    public void cancel(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pIntent2 = PendingIntent.getBroadcast(this, Integer.parseInt(studentID)*100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pIntent2);
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

