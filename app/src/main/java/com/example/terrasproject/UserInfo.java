package com.example.terrasproject;


import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        final TextView name = findViewById(R.id.nameinfo);
        final TextView num = findViewById(R.id.numinfo);
        final TextView phone = findViewById(R.id.phoneinfo);
        final TextView point = findViewById(R.id.pointinfo);
        final TextView seat = findViewById(R.id.nowseatinfo);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student").child(LogIn.studentID);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(snapshot.getKey().equals("phoneNumber")){
                            phone.setText(snapshot.getValue().toString());
                        }
                        else if(snapshot.getKey().equals("studentName")){
                            name.setText(snapshot.getValue().toString());
                        }
                        else if(snapshot.getKey().equals("studentID")){
                            num.setText(snapshot.getValue().toString());
                        }
                        else if(snapshot.getKey().equals("feedback")){
                            point.setText(snapshot.getValue().toString());
                        }
                        else if(snapshot.getKey().equals("reservation")){
                            seat.setText(snapshot.getValue().toString());
                        }
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


    }
}