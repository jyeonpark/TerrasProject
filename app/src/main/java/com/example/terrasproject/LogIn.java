package com.example.terrasproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.terrasproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class LogIn extends AppCompatActivity {
  EditText studentID,password;


  private DatabaseReference reference;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_log_in);

      studentID = findViewById(R.id.login_studentID);
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
      final String id = studentID.getText().toString();
      final String pw = password.getText().toString();

      Query checkuser =FirebaseDatabase.getInstance().getReference("Student").orderByChild("studentID").equalTo(id);
      checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot datasnapshot) {
              if(datasnapshot.exists()) {
                  String  systemPassword = datasnapshot.child(id).child("password").getValue(String.class);
                  if(systemPassword.equals(pw)) {
                      showToast("로그인에 성공하였습니다.");
                      myStartActivity(MainActivity.class);
                  } else {
                      showToast("비밀번호가 일치하지 않습니다.");
                  }
              }
              else {
                  showToast("등록된 학번이 아닙니다.");
              }
          }
          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
      });
  }

  public void btnSignup_Click(){
      myStartActivity(SignUp.class);
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