package com.example.terrasproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.common.base.Joiner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.reservationbutton).setOnClickListener(onClickListener);
        findViewById(R.id.reservationcheckbutton).setOnClickListener(onClickListener);
        findViewById(R.id.qrcodescanbutton).setOnClickListener(onClickListener);
        findViewById(R.id.reportbutton).setOnClickListener(onClickListener);
        findViewById(R.id.cancelreservationbutton).setOnClickListener(onClickListener);
        findViewById(R.id.userinformationbutton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.reservationbutton:
                    myStartActivity(Reservation.class);
                    break;

                case R.id.reservationcheckbutton:
                    break;

                case R.id.qrcodescanbutton:
                    myStartActivity(QRcode.class);
                    break;

                case R.id.reportbutton:
                    break;

                case R.id.cancelreservationbutton:
                    break;

                case R.id.userinformationbutton:
                    break;
            }
        }
    };
    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
} 