package com.example.terrasproject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class Timer_QR extends Service {

    private Thread mThread;

    private int mCount = 0;

    public Timer_QR() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) {
            return Service.START_STICKY;
        }
        else {

            final int status = intent.getIntExtra("success", 0);
            final int status_QR = intent.getIntExtra("result",0);

            if (status == 1 && mThread == null) {
                mThread = new Thread("Timer_QR") {
                    public void run() {
                        for (int i = 0; i < 15; i++) {
                            try {
                                if(status_QR == 1){
                                    break;                          // qr 성공시 타이머 중지
                                }

                                // 15분 지나면 db에서 삭제

                                Thread.sleep(1000 * 60);


                            } catch (InterruptedException e) {
                                break;
                            }
                            Log.d("timer", "timer running" + i);
                        }
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