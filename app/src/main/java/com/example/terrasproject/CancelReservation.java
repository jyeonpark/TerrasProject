package com.example.terrasproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CancelReservation extends AppCompatActivity {
    static String terras,date,startTime;
    static int usetime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       cancleReservation();
    }

    public void cancleReservation() {

        SharedPreferences sp = getSharedPreferences("file", MODE_PRIVATE);

        //앱이 종료되기 전의 데이터를 불러옴
        terras = sp.getString("terras","");
        date = sp.getString("date","");
        startTime = sp.getString("startTime","");
        usetime = sp.getInt("usetime",0);

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
        editor.remove("terras");
        editor.remove("date");
        editor.remove("startTime");
        editor.remove("usetime");
        editor.commit();

        //최종 커밋
        editor.commit();

    }

    private void showToast(String msg)
    {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}