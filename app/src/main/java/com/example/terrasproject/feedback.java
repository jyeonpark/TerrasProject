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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class feedback extends AppCompatActivity {
    static String terras, sendID, receiveID, feednum;
    private feedbackText feedbackText;

    FirebaseDatabase database;
    DatabaseReference reference, referenceReceiveID, addfeedreference, feedreference;

    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdf = new SimpleDateFormat("h");
    String hour = sdf.format(date);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setseat);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Feedback").push();
        addfeedreference = database.getReference("Student");
        sendID = LogIn.studentID;
    }

    public void btnfeedseatClick(View view){
        terras = view.getTag().toString();

        System.out.println("button " + terras);
        setContentView(R.layout.activity_feedback);

        referenceReceiveID = database.getReference("Student");
        referenceReceiveID.orderByChild("reservation").equalTo(terras).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        receiveID = snapshot.getKey();
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
        try {
            EditText feedtext = (EditText) findViewById(R.id.feedtext);
            feedbackText = new feedbackText();
            feedbackText.setFeedback(feedtext.getText().toString());
        }
        catch(NullPointerException e ){}

            System.out.println(feedbackText.getFeedback());
            System.out.println(receiveID);
            System.out.println(terras);
            System.out.println(sendID);
            reference.child("receiveID").setValue(receiveID);
            reference.child("Terras").setValue(terras);
            reference.child("Feedtext").setValue(feedbackText.getFeedback());
            reference.child("SendID").setValue(sendID);

            addfeed();

        showToast("신고완료");
        myStartActivity(MainActivity.class);
    }

    public void addfeed(){

        feedreference = FirebaseDatabase.getInstance().getReference().child("Student").child(receiveID);
        feedreference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals("feedback")) {
                        feednum = snapshot.getValue().toString();
                        int numfeed = Integer.parseInt(feednum);
                        numfeed = numfeed + 1;
                        feednum = Integer.toString(numfeed);
                        feedreference.child("feedback").setValue(feednum);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

