package com.example.terrasproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

import org.w3c.dom.Text;

public class ShowReservation extends AppCompatActivity {
    static String terras,date,startTime;
    static int usetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_check);

        //좌석정보, 사용자정보
        final TextView seat = findViewById(R.id.seatinfo);
        final TextView user = findViewById(R.id.userinfo);

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

        //입실시간
        TextView starttime = findViewById(R.id.starttimeinfo);
        starttime.setText("");


        //퇴실시간
        TextView finishtime = findViewById(R.id.finishtimeinfo);
        finishtime.setText("");
    }

    //배정확정
    public void btncompletereservation(View view) {
        myStartActivity(QRcode.class);
    }

    //배정취소
    public void btncancelreservation(View view) {
        cancleReservation();
        showToast("좌석배정이 취소되었습니다.");
        myStartActivity(MainActivity.class);
    }

    public void cancleReservation(){

        SharedPreferences sp = getSharedPreferences("file", MODE_PRIVATE);

        //앱이 종료되기 전의 데이터를 불러옴
        terras = sp.getString("terras"+LogIn.studentID,"");
        date = sp.getString("date"+LogIn.studentID,"");
        startTime = sp.getString("startTime"+LogIn.studentID,"");
        usetime = sp.getInt("usetime"+LogIn.studentID,0);

        //DB에서 정보없애기
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Terras").child(terras);
        for (int i = 0; i <= usetime; i++) {
            String reservationctime = Integer.toString(Integer.parseInt(startTime) + i);
            reference.child(reservationctime).child(date).child("seat").setValue("empty");
            reference.child(reservationctime).child(date).child("state").setValue("empty");
            reference.child(reservationctime).child(date).child("studentID").setValue("empty");
        }

        reference = FirebaseDatabase.getInstance().getReference().child("Student");
        reference.child(LogIn.studentID).child("reservation").setValue("empty");

        //파일에서 삭제하기
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("terras"+LogIn.studentID);
        editor.remove("date"+LogIn.studentID);
        editor.remove("startTime"+LogIn.studentID);
        editor.remove("usetime"+LogIn.studentID);
        editor.commit();

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

