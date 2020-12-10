package com.example.terrasproject;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Alarm extends BroadcastReceiver {
    String terras, startTime;
    int usetime;

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            String studentID = (String) extras.get("studentID");
            SharedPreferences sp = context.getSharedPreferences("file", context.MODE_PRIVATE);
            //앱이 종료되기 전의 데이터를 불러옴

            terras = sp.getString("terras"+ studentID,"");
            startTime = sp.getString("startTime"+ studentID,"");
            usetime = sp.getInt("usetime"+ studentID,0);

            //DB에서 정보없애기

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Terras").child(terras);
            for (int i = 0; i <= usetime; i++) {
                String reservationctime = Integer.toString(Integer.parseInt(startTime) + i);
                reference.child(reservationctime).child("today").child("seat").setValue("empty");
                reference.child(reservationctime).child("today").child("state").setValue("empty");
                reference.child(reservationctime).child("today").child("studentID").setValue("empty");
            }

            reference = FirebaseDatabase.getInstance().getReference().child("Student");
            reference.child(studentID).child("reservation").setValue("비어있음");

            //파일에서 삭제하기
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("terras"+ studentID);
            editor.remove("startTime"+ studentID);
            editor.remove("usetime"+ studentID);

            editor.commit();

            //최종 커밋
            editor.commit();

        }



}
