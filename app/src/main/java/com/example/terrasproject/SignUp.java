package com.example.terrasproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class  SignUp extends AppCompatActivity {
    EditText studentID,password,studentName,phoneNumber;
    private Student student;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        studentID = findViewById(R.id.studentID);
        password = findViewById(R.id.password);
        studentName = findViewById(R.id.studentName);
        phoneNumber = findViewById(R.id.phoneNumber);

        student = new Student();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Student");
    }

    public void btnSignup_Click(View view) {
        student.setStudentID(studentID.getText().toString());
        student.setPassword(password.getText().toString());
        student.setStudentName(studentName.getText().toString());
        student.setPhoneNumber(phoneNumber.getText().toString());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (studentID.length() > 0 && studentName.length() > 0 && password.length() > 0 && phoneNumber.length() > 0) {
                    if (studentID.length() == 8) {
                        if (password.length() >= 8) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                if (data.getKey().equals(studentID.getText().toString())) {
                                    showToast("이미 존재하는 학번입니다.");
                                    reference.removeEventListener(this);
                                    return;
                                }
                            }
                            makeNewID();
                        }
                        else{
                            showToast("비밀번호는 8자리 이상입니다."); }
                    }
                    else{
                        showToast("학번은 8자리 입니다."); }
                }
                else {
                    showToast("정보를 다 입력해 주세요"); }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    void makeNewID(){
        reference.child(student.getStudentID()).setValue(student);
        showToast("회원가입완료");
        myStartActivity(LogIn.class);
    }
    public void btnLogin_Click(View view){
        myStartActivity(LogIn.class);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
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