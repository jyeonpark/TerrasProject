package com.example.terrasproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.graphics.Color;
import android.os.Bundle;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class feedback extends AppCompatActivity {
    EditText feedtext;
    static String terras, sendID, receiveID, text;

    FirebaseDatabase database;
    DatabaseReference reference,referenceReceiveID;

    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("hh");
    String hour = String.valueOf(date); //sdf.format(date);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setseat);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Feedback");

        feedtext = findViewById(R.id.feedtext);

        sendID = LogIn.studentID;


    }

    public void btnfeedseatClick(View view){
        terras = view.getTag().toString();

        System.out.println("button " + terras);
        setContentView(R.layout.activity_feedback);


        referenceReceiveID = FirebaseDatabase.getInstance().getReference();
        referenceReceiveID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        receiveID = snapshot.child(terras).child(hour).child("today/studentID").getValue().toString();
                    }catch(NullPointerException e){}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }


    public void btnsendClick(View view){
        makeNewFeed();
    }

    void makeNewFeed(){

        reference.child(receiveID).child("receiveID").setValue(receiveID);
        reference.child(receiveID).child("Terras").setValue(terras);
        reference.child(receiveID).child("Feedtext").setValue(text);
        reference.child(receiveID).child("SendID").setValue(sendID);


        showToast("신고완료");
        myStartActivity(MainActivity.class);
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}

