package com.example.terrasproject;

import android.app.AlarmManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    String terras,seat,state,studentID,startTime,finishTime;
    int clickcount=0,usetime=0,reservation;
    int flowcheck = 0;

    SimpleDateFormat format_hour = new SimpleDateFormat("kk");
    String date = format_hour.format(new Date());

    final int[] selected = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_terras);

    }

    public void btnSeatClick(View view){
         terras = view.getTag().toString();
        setContentView(R.layout.choose_time);
        resetSeat();
    }

    public void resetSeat(){
        final LinearLayout timelinearLayout = findViewById(R.id.parentview);
        int hour = Integer.parseInt(date);
        for(int i=9; i<=hour; i++){
                Button button = timelinearLayout.findViewWithTag(Integer.toString(i));
                button.setBackgroundColor(Color.parseColor("#666666"));
                button.setTextColor(Color.parseColor("#FFFFFF"));
               button.setEnabled(false);
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Terras").child(terras);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.child("today").child("seat").getValue().toString().equals("use")){
                        Button button = timelinearLayout.findViewWithTag(snapshot.getKey());
                        button.setBackgroundColor(Color.parseColor("#666666"));
                        button.setTextColor(Color.parseColor("#FFFFFF"));
                        timelinearLayout.findViewWithTag(snapshot.getKey()).setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void btnTimeClick(View v) {
        final LinearLayout timelinearLayout = findViewById(R.id.parentview);
        Button view = (Button) v;
        if(view == findViewById(R.id.btnreset)){
                clickcount=0;
                for (int i = 9; i <= 21; i++) {
                    String middletime = Integer.toString(i);
                    Button button = timelinearLayout.findViewWithTag(middletime);
                   button.setBackgroundColor(Color.parseColor("#FFFFFF"));
                   button.setTextColor(Color.parseColor("#000000"));
                }
                resetSeat();
        }
        else {
            clickcount++;
            //시작시간버튼
            if (clickcount == 1) {
                startTime = view.getTag().toString();
                view.setBackgroundColor(Color.parseColor("#4EBEF1"));
                view.setTextColor(Color.parseColor("#FFFFFF"));
            }
            //끝시간버튼
            else if (clickcount == 2) {
                finishTime = view.getTag().toString();
                usetime = Integer.parseInt(finishTime) - Integer.parseInt(startTime);

                //그사이에 예약된 시간있으면 선택못함
                allLoop:
                for (int i = 1; i < usetime; i++) {
                    String middletime = Integer.toString(Integer.parseInt(startTime) + i);
                    if(!timelinearLayout.findViewWithTag(middletime).isEnabled()) {
                        showToast("연속된 시간만 예약할 수 있습니다.");
                        clickcount=0;
                        timelinearLayout.findViewWithTag(startTime).setBackgroundColor(Color.parseColor("#FFFFFF"));
                        flowcheck++;
                        break allLoop;
                    }
                }

                        if (clickcount==2 && flowcheck == 0) {
                            if (usetime > 0) {
                                if (usetime <= 5) {
                                    for (int i = 1; i <= usetime; i++) {
                                        String middletime = Integer.toString(Integer.parseInt(startTime) + i);
                                        Button button = timelinearLayout.findViewWithTag(middletime);
                                        button.setBackgroundColor(Color.parseColor("#4EBEF1"));
                                        button.setTextColor(Color.parseColor("#FFFFFF"));
                                    }
                                } else {
                                    showToast("최대 5시간까지 예약할 수 있습니다.");
                                    clickcount--;
                                }
                            }
                            else {
                                clickcount = 1;
                            }
                        }
            }
            flowcheck=0;
        }
   }


    public void reservationdialog(View view){

        studentID = LogIn.studentID;
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
                    clickcount=0;
                    flowcheck=0;

                    Intent intent = new Intent(Reservation.this, AlarmMain.class);
                    intent.putExtra("activity", "reservation");
                    startActivity(intent);

                    /* Timer_Reservation 로  데이터 보내기
                    Intent intent = new Intent(Reservation.this,Timer_Reservation.class);
                    intent.putExtra("reservation",reservation);
                    startService(intent);
*/



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




   public void reservationtoDB(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Terras").child(terras);
       studentID = LogIn.studentID;

        //한시간짜리 예약
        //empty에서 사용중으로 바꿈
        if(usetime==0){
            reference.child(startTime).child("today").child("seat").setValue(seat);
            reference.child(startTime).child("today").child("state").setValue(state);
            reference.child(startTime).child("today").child("studentID").setValue(studentID);
        }
        else{
            for (int i = 0; i <= usetime; i++) {
                String reservationctime= Integer.toString(Integer.parseInt(startTime) + i);
                reference.child(reservationctime).child("today").child("seat").setValue("use");
                reference.child(reservationctime).child("today").child("state").setValue(state);
                reference.child(reservationctime).child("today").child("studentID").setValue(studentID);
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
        editor.putString("startTime"+studentID, startTime);
        editor.putInt("usetime"+studentID, usetime);// key, value를 이용하여 저장하는 형태

        //최종 커밋
        editor.commit();
    }


   @Override
   public void onBackPressed(){
       myStartActivity(MainActivity.class);
       clickcount=0;
       flowcheck=0;
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
