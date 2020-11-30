package com.example.terrasproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
            //예약 안했을 때 예약 버튼,신고 버튼 외 다른 버튼 눌렀을 때
            if(Reservation.reservationcheck == 0) {
                if(v.getId() == R.id.reservationbutton){
                    myStartActivity(Reservation.class);
                }
                else if(v.getId() == R.id.reportbutton){
                    //신고하기
                }
                else{
                    showToast("예약이 필요한 항목입니다.");
                }
            }
            else{
                switch (v.getId()) {
                    case R.id.reservationbutton:
                        showToast("이미 예약된 상태입니다.");
                        break;

                    case R.id.reservationcheckbutton:
                        myStartActivity(ReservationCheck.class);
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
        }
    };

    @Override
    public void onBackPressed(){
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