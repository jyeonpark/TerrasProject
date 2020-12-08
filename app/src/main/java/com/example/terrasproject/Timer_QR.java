/*package com.example.terrasproject;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class Timer_QR extends Service {

    static String terras,startTime;
    static int usetime;

    private Thread mThread;

    private int mCount = 0;

    public Timer_QR() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("startService","startCommand");

        if (intent == null) {
            return Service.START_STICKY;
        }
        else {

            final int trash = intent.getIntExtra("status", 0);


            if (ShowReservation.status == 0 && mThread == null) {
                mThread = new Thread("Timer_QR") {
                    public void run() {
                        for (int i = 0; i < 15; i++) {
                            try {
                                if(ShowReservation.status == 1)
                                    break;                          // qr 성공시 타이머 중지
                                Thread.sleep(1000 * 60 );
                            } catch (InterruptedException e) {
                                break;
                            }

                            Log.d("timer_QR", "timer running" + i);
                        }
                        if(ShowReservation.status == 0) {
                            cancleReservation();
                        }
                        Log.d("Reservation","canceled");


                    }
                };
                mThread.start();
            }
            return START_STICKY;
        }
    }

    public void cancleReservation(){

        SharedPreferences sp = getSharedPreferences("file", MODE_PRIVATE);

        //앱이 종료되기 전의 데이터를 불러옴
        terras = sp.getString("terras"+LogIn.studentID,"");
        startTime = sp.getString("startTime"+LogIn.studentID,"");
        usetime = sp.getInt("usetime"+LogIn.studentID,0);

        //DB에서 정보없애기
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Terras").child(terras);
        for (int i = 0; i <= usetime; i++) {
            try {
                String reservationctime = Integer.toString(Integer.parseInt(startTime) + i);
                reference.child(reservationctime).child("today").child("seat").setValue("empty");
                reference.child(reservationctime).child("today").child("state").setValue("empty");
                reference.child(reservationctime).child("today").child("studentID").setValue("empty");
            }catch(NumberFormatException e){

            }
        }

        reference = FirebaseDatabase.getInstance().getReference().child("Student");
        reference.child(LogIn.studentID).child("reservation").setValue("empty");

        //파일에서 삭제하기
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("terras"+LogIn.studentID);
        editor.remove("startTime"+LogIn.studentID);
        editor.remove("usetime"+LogIn.studentID);

        editor.commit();

        //최종 커밋
        editor.commit();

    }



    @Override
   public void onDestroy(){

        super.onDestroy();
        Log.d("startService","onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d("startService","onBind");


        throw new UnsupportedOperationException("Not yet implemented");

    }



};
*/
