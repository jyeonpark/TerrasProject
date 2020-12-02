package com.example.terrasproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import java.util.Calendar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    int check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //현재시간
        showToast(Integer.toString(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));

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
                    //신고하기
                }
                else if(v.getId() == R.id.logoutbutton){
                    logout();
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
                        if(snapshot.child("reservation").getValue().equals("empty"))
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