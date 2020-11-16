package com.example.terrasproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.reservationbutton:
                    break;

                case R.id.reservationcheckbutton:
                    break;

                case R.id.qrcodescanbutton:
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
} 