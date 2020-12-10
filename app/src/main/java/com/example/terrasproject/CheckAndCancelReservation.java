package com.example.terrasproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CheckAndCancelReservation extends AppCompatActivity {
    static String terras,startTime;
    static int usetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelreservation);

        //좌석정보, 사용자정보
        final TextView seat = findViewById(R.id.seatinfo);
        final TextView user = findViewById(R.id.userinfo);


        SharedPreferences sp = getSharedPreferences("file", MODE_PRIVATE);

        //앱이 종료되기 전의 데이터를 불러옴
        terras = sp.getString("terras"+LogIn.studentID,"");
        startTime = sp.getString("startTime"+LogIn.studentID,"");
        usetime = sp.getInt("usetime"+LogIn.studentID,0);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(LogIn.studentID)) {
                        user.setText(snapshot.child("studentName").getValue().toString() +  " "+
                                snapshot.child("studentID").getValue().toString());
                        seat.setText(snapshot.child("reservation").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());


        //입실시간
        TextView starttime = findViewById(R.id.starttimeinfo);
        starttime.setText(date + "   " +String.valueOf(startTime)+":00");


        //퇴실시간
        TextView finishtime = findViewById(R.id.finishtimeinfo);
        finishtime.setText(date + "   "+Integer.toString(Integer.parseInt(startTime)+usetime+1)+":00");
    }

    //퇴실하기(예 버튼)
    public void btnyes(View view) {
        cancleReservation();
        showToast("좌석배정이 취소되었습니다.");
        myStartActivity(MainActivity.class);
    }

    //아니오버튼
    public void btnno(View view) {
       myStartActivity(MainActivity.class);
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
            String reservationctime = Integer.toString(Integer.parseInt(startTime) + i);
            reference.child(reservationctime).child("today").child("seat").setValue("empty");
            reference.child(reservationctime).child("today").child("state").setValue("empty");
            reference.child(reservationctime).child("today").child("studentID").setValue("empty");
        }

        reference = FirebaseDatabase.getInstance().getReference().child("Student");
        reference.child(LogIn.studentID).child("reservation").setValue("비어있음");

        //파일에서 삭제하기
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("terras"+LogIn.studentID);
        editor.remove("startTime"+LogIn.studentID);
        editor.remove("usetime"+LogIn.studentID);

        editor.commit();

        //최종 커밋
        editor.commit();

        Intent intent = new Intent(CheckAndCancelReservation.this, AlarmMain.class);
        intent.putExtra("activity", "reservationcancel");
        startActivity(intent);

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

