package com.example.terrasproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Reservation extends AppCompatActivity {
    static String terras,date,seat,state,studentID,startTime,finishTime;
    static int clickcount=0,usetime;
    static int reservationcheck = 0;
    static String currentTime, closeTime;
    final int[] selected = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_terras);

    }

    public void btnSeatClick(View view){
         terras = view.getTag().toString();
         showToast(terras);
         new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   setContentView(R.layout.choose_date);
                   Button btntoday = findViewById(R.id.btntoday);
                   Button btntomorrow = findViewById(R.id.btntomorrow);
                   btntoday.setText(DateTimeUtil.getToday("yyyy-MM-dd"));
                   btntomorrow.setText(DateTimeUtil.getTomorrow("yyyy-MM-dd"));
               }
           },500);
    }

    public void btnDateClick(View view){

        date = view.getTag().toString();
        showToast(date);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.choose_time);
                resetSeat();

            }
        },500);
    }


    public void resetSeat(){
        final LinearLayout timelinearLayout = findViewById(R.id.parentview);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Terras").child(terras);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.child(date).child("seat").getValue().toString().equals("use")){
                        timelinearLayout.findViewWithTag(snapshot.getKey()).setBackgroundColor(Color.parseColor("#FFFFFF"));
                        timelinearLayout.findViewWithTag(snapshot.getKey()).setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void btnTimeClick(View view) {
        LinearLayout timelinearLayout = findViewById(R.id.parentview);
        if(view == findViewById(R.id.btnreset)){
                clickcount=0;
                for (int i = 9; i <= 21; i++) {
                    String middletime = Integer.toString(i);
                   timelinearLayout.findViewWithTag(middletime).setBackgroundColor(Color.parseColor("#CCCCCC"));
                }
        }
        else {
            clickcount++;
            //시작시간버튼
            if (clickcount == 1) {
                startTime = view.getTag().toString();
                view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            //끝시간버튼
            else if (clickcount == 2) {
                finishTime = view.getTag().toString();
                usetime = Integer.parseInt(finishTime) - Integer.parseInt(startTime);
                if (usetime > 0) {
                    if (usetime <= 5) {
                        for (int i = 1; i <= usetime; i++) {
                            String middletime = Integer.toString(Integer.parseInt(startTime) + i);
                            timelinearLayout.findViewWithTag(middletime).setBackgroundColor(Color.parseColor("#FFFFFF"));
                        }
                    } else {
                        showToast("최대 5시간까지 예약할 수 있습니다.");
                        clickcount--;
                    }
                } else {
                    clickcount = 1;
                }
            }
        }
   }


    public void reservationdialog(View view){
        if(clickcount!=0) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(Reservation.this);
            dialog.setTitle("사용하실 테라스 상태를 선택해주세요");
            final String[] statearray = new String[]{"소음", "조용"};
            dialog.setSingleChoiceItems(statearray, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selected[0] = which;
                }
            }).setPositiveButton("예약하기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    seat = "use";
                    if (selected[0] == 0) {
                        state = "소음";
                    } else {
                        state = "조용";
                    }
                    reservationtoDB();
                    storeInFile();
                    reservationcheck++;

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date());
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    currentTime =  df.format(cal.getTime());

                    cal.add(Calendar.HOUR, usetime+1);
                    closeTime =  df.format(cal.getTime());


                    /* Timer_QR 로  데이터 보내기  */
                    int success = 1;
                    Intent intent_QR = new Intent(Reservation.this, Timer_QR.class);
                    intent_QR.putExtra("success",success);
                    startService(intent_QR);

                    /* Timer_Reservation 로  데이터 보내기  */
                    Intent intent_Reservation = new Intent(Reservation.this, Timer_Reservation.class);
                    intent_Reservation.putExtra("usetime",usetime);
                    startService(intent_Reservation);


                    myStartActivity(ShowReservation.class);

                }

            }).setNegativeButton("취소하기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.create();
            dialog.show();
        }
        else{
            showToast("예약할 시간을 선택해주세요");
        }

    }





                    /*현재 시간 데이터 생성
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);

                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    strNow = sdfNow.format(now);

                    /*3시간 뒤 데이터
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date); // 10분 더하기
                    cal.add(Calendar.HOUR, 3);

                    today = sdformat.format(cal.getTime()); */




                    /* Timer_Reservation 로  데이터 보내기
                    Intent intent_Reservation = new Intent(Reservation.this, Timer_Reservation.class);
                    intent_Reservation.putExtra("usetime",usetime);
                    startService(intent_Reservation);
                    myStartActivity(ShowReservation.class);  */




   public void reservationtoDB(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Terras").child(terras);
       studentID = LogIn.studentID;
       showToast(studentID);

        //한시간짜리 예약
        //empty에서 사용중으로 바꿈
        if(usetime==0){
            reference.child(startTime).child(date).child("seat").setValue(seat);
            reference.child(startTime).child(date).child("state").setValue(state);
            reference.child(startTime).child(date).child("studentID").setValue(studentID);
        }
        else{
            for (int i = 0; i <= usetime; i++) {
                String reservationctime= Integer.toString(Integer.parseInt(startTime) + i);
                reference.child(reservationctime).child(date).child("seat").setValue("use");
                reference.child(reservationctime).child(date).child("state").setValue(state);
                reference.child(reservationctime).child(date).child("studentID").setValue(studentID);
            }
        }

        //DB 학생정보에 예약한 테라스자리 저장 -> 중복 예약 막기위함
        reference = FirebaseDatabase.getInstance().getReference().child("Student");
        reference.child(studentID).child("reservation").setValue(terras);
   }

    public void storeInFile(){

        //앱이 종료되도 데이터를 사용할 수 있게 함
        SharedPreferences sp = getSharedPreferences("file", MODE_PRIVATE);

        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
        SharedPreferences.Editor editor = sp.edit();
        //테라스와 날짜 정보 저장
        editor.putString("terras"+studentID, terras);
        editor.putString("date"+studentID, date);
        editor.putString("startTime"+studentID, startTime);
        editor.putInt("usetime"+studentID, usetime);// key, value를 이용하여 저장하는 형태
        editor.putString("strNow"+studentID, currentTime);
        editor.putString("strAfter"+studentID, closeTime);


        //최종 커밋
        editor.commit();
    }

   @Override
   public void onBackPressed(){
       myStartActivity(MainActivity.class);
   }
    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private void showToast(String msg)
    {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
