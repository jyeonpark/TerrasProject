package com.example.terrasproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.encoder.QRCode;

import org.json.JSONException;
import org.json.JSONObject;

public class QRcode extends AppCompatActivity {
    //view Objects
    private Button buttonScan;
    private TextView textViewName, textViewResult;
    //qr code scanner object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_rcode);
        //View Objects
        buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewResult = (TextView)  findViewById(R.id.textViewResult);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Student");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(LogIn.studentID)) {
                        textViewName.setText(snapshot.child("reservation").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        //intializing scan object
        qrScan = new IntentIntegrator(this);
        //button onClick
        buttonScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //scan option
                qrScan.setPrompt("Scanning...");
                //qrScan.setOrientationLocked(false);
                qrScan.initiateScan();
            }
        });
    }
    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){


        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {
                Toast.makeText(QRcode.this, "취소!", Toast.LENGTH_SHORT).show();
            } else {//qrcode 결과가 있으면예약
                try {
                    //data를 json으로 변환
                    JSONObject obj = new JSONObject(result.getContents());
                    textViewName.setText(obj.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                    textViewResult.setText(result.getContents());
                }

                if (textViewName.getText().toString().equals(textViewResult.getText().toString())) {
                    Toast.makeText(QRcode.this, "예약완료!", Toast.LENGTH_SHORT).show();

                    //타이머 끄기 기능 넣기
                    Intent intent = new Intent(QRcode.this, AlarmMain.class);
                    intent.putExtra("activity", "qrcancel");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(QRcode.this, "예약실패!", Toast.LENGTH_SHORT).show();
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}