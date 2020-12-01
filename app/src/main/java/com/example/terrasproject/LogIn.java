package com.example.terrasproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class LogIn extends AppCompatActivity {
  EditText studentid,password;
  static String studentID;
  private DatabaseReference reference;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_log_in);

      studentid = findViewById(R.id.login_studentID);
      password = findViewById(R.id.login_password);

      reference = FirebaseDatabase.getInstance().getReference().child("Student");

      findViewById(R.id.loginButton).setOnClickListener(onClickListener);
      findViewById(R.id.signUpButton).setOnClickListener(onClickListener);

  }

  View.OnClickListener onClickListener = new View.OnClickListener(){
      @Override
      public void onClick(View v) {
          switch (v.getId()) {
              case R.id.loginButton:
                  btnLogin_Click();
                  break;

              case R.id.signUpButton:
                  btnSignup_Click();
                  break;
          }
      }
  };
  public void btnLogin_Click(){
      final String id = studentid.getText().toString();
      final String pw = password.getText().toString();

      Query checkuser =FirebaseDatabase.getInstance().getReference("Student").orderByChild("studentID").equalTo(id);
      checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot datasnapshot) {
              if (datasnapshot.exists()) {
                  String systemPassword = datasnapshot.child(id).child("password").getValue(String.class);
                  if (systemPassword.equals(pw)) {
                      studentID = id;
                      storeInFile();
                      myStartActivity(MainActivity.class);
                  } else {
                      showToast("비밀번호가 일치하지 않습니다.");
                  }
              } else {
                  showToast("등록된 학번이 아닙니다.");
              }
          }
          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
      });
  }

    public void storeInFile(){

        //앱이 종료되도 데이터를 사용할 수 있게 함
        SharedPreferences sp = getSharedPreferences("file", MODE_PRIVATE);

        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
        SharedPreferences.Editor editor = sp.edit();
       //id 저장
        editor.putString("id", studentID);

        //최종 커밋
        editor.commit();
    }


    public void btnSignup_Click(){
      myStartActivity(SignUp.class);
  }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed(){
      moveTaskToBack(true);
      finishAndRemoveTask();
      android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void showToast(String msg)
    {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}