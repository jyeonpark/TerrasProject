package com.example.terrasproject;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class Timer_Reservation extends Service {

    SharedPreferences sp = getSharedPreferences("file", MODE_PRIVATE);

    //앱이 종료되기 전의 데이터를 불러옴
    String terras = sp.getString("terras"+LogIn.studentID,"");
    String startTime = sp.getString("startTime"+LogIn.studentID,"");
    int usetime = sp.getInt("usetime"+LogIn.studentID,0);

    private Thread mThread;

    public Timer_Reservation() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) {
            return Service.START_STICKY;
        } else {


            if ( usetime > 0 && mThread == null) {
                mThread = new Thread("Timer_QR") {
                    public void run() {

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Terras").child(terras);
                            for (int i = 0; i <= usetime; i++) {
                                String reservationctime = Integer.toString(Integer.parseInt(startTime) + i);
                                reference.child(reservationctime).child("today").child("seat").setValue("empty");
                                reference.child(reservationctime).child("today").child("state").setValue("empty");
                                reference.child(reservationctime).child("today").child("studentID").setValue("empty");
                            }

                            reference = FirebaseDatabase.getInstance().getReference().child("Student");
                            reference.child(LogIn.studentID).child("reservation").setValue("empty");

                            Log.d("timer", "timer running" );

                    }
                };
                mThread.start();
            }
            return START_STICKY;
        }
    }



     @Override
   public void onDestroy(){

        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }



};