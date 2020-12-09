package com.example.terrasproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    int check,click=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleDateFormat format_hour = new SimpleDateFormat("kk");
        String date = format_hour.format(new Date());

        //앱이 종료되기 전의 데이터를 불러옴
        SharedPreferences sf = getSharedPreferences("file", MODE_PRIVATE);
        String loginid = sf.getString("id","");

        if(loginid == ""){
            myStartActivity(LogIn.class);
        }
        else{
            LogIn.studentID = loginid;
        }

        //이미 예약이 있으면 1, 아직 예약이 없으면 0
        check = checkDuplicateReservation();

        setContentView(R.layout.activity_main);
        findViewById(R.id.reservationbutton).setOnClickListener(onClickListener);
        findViewById(R.id.reservationcheckbutton).setOnClickListener(onClickListener);
        findViewById(R.id.qrcodescanbutton).setOnClickListener(onClickListener);
        findViewById(R.id.reportbutton).setOnClickListener(onClickListener);
        findViewById(R.id.userinformationbutton).setOnClickListener(onClickListener);
        findViewById(R.id.logoutbutton).setOnClickListener(onClickListener);

        TextView textView = findViewById(R.id.maintext);
        textView.setText(""+ LogIn.studentID+ "님 반갑습니다");
        textView.setTextSize(20);

        final RelativeLayout timelinearLayout = findViewById(R.id.parentlayout);
        int hour = Integer.parseInt(date);
        //시간 체크
        for (int i = 9; i < hour; i++) {
            Button button = timelinearLayout.findViewWithTag(Integer.toString(i));
            button.setBackgroundColor(Color.parseColor("#666666"));
            button.setTextColor(Color.parseColor("#FFFFFF"));
            button.setEnabled(false);
        }
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //예약이 이미 있을 때
            if(check == 1) {
                switch (v.getId()) {
                    case R.id.reservationbutton:
                        showToast("이미 예약된 상태입니다.");
                        break;

                    case R.id.reservationcheckbutton:
                        myStartActivity(CheckAndCancelReservation.class);
                        break;

                    case R.id.qrcodescanbutton:
                        myStartActivity(QRcode.class);
                        break;

                    case R.id.reportbutton:
                        myStartActivity(feedback.class);
                        break;

                    case R.id.userinformationbutton:
                        myStartActivity(UserInfo.class);
                        break;

                    case R.id.logoutbutton:
                        logout();
                        break;

                }
            }
            //아직 예약을 하지 않았을 때
            else{
                if(v.getId() == R.id.reservationbutton){
                    myStartActivity(Reservation.class);
                }
                else if(v.getId() == R.id.reportbutton){
                    myStartActivity(feedback.class);
                }
                else if(v.getId() == R.id.logoutbutton){
                    logout();
                }
                else if(v.getId() == R.id.userinformationbutton){
                    myStartActivity(UserInfo.class);
                }
                else{
                    showToast("예약이 필요한 항목입니다.");
                }
            }
        }
    };

    public int checkDuplicateReservation(){
        //로그인한 학번으로 예약이 되어있는지 확인
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals(LogIn.studentID)){
                        //등록된 학번으로 예약이 없을 때
                        if(snapshot.child("reservation").getValue().equals("비어있음"))
                            check = 0;
                        //예약이 있을 때
                        else
                            check = 1;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        //예약이 있을 때
        if(check==1)
            return 1;

        //아직 예약 안함
        else{
            return 0;
        }

    }

    public void logout(){

        SharedPreferences sp = getSharedPreferences("file", MODE_PRIVATE);
        //파일에서 삭제하기
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("id");
        editor.commit();

        myStartActivity(LogIn.class);

    }

    public void showseat(View view) {
        final Button v = (Button) view;
        click++;
        final RelativeLayout timelinearLayout = findViewById(R.id.parentlayout);

        if (click == 1) {
            for (int i = 2; i <= 4; i++) {
                for (int j = 1; j <= 3; j++) {
                    final String terras = "terras" + Integer.toString(i) + "-" + Integer.toString(j);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Terras").child(terras);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot.getKey().equals(v.getTag().toString())) {
                                    if(!(snapshot.child("today").child("seat").getValue().toString().equals("empty"))){
                                        String state = snapshot.child("today").child("state").getValue().toString();
                                        TextView textView = timelinearLayout.findViewWithTag(terras);
                                        textView.setText(textView.getText().toString() + "\n" +state);
                                        textView.setBackgroundColor(Color.parseColor("#4EBEF1"));
                                        findViewById(R.id.showterras).setVisibility(View.VISIBLE);
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        }
        for (int i = 2; i <= 4; i++) {
            for (int j = 1; j <= 3; j++) {
                final String terras = "terras" + Integer.toString(i) + "-" + Integer.toString(j);
                final String text = Integer.toString(i) + "-" + Integer.toString(j);
                TextView textView = timelinearLayout.findViewWithTag(terras);
                textView.setText(text);
                textView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }
        click=0;
    }

    private long time = 0;
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() - time >= 2000){
            time = System.currentTimeMillis();
            showToast("한번 더 누르면 종료됩니다.");
        }
        else{
            finish();
        }
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