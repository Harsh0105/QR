package com.example.qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {

    private EditText passwordEmail;
    private Button resetPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        passwordEmail=(EditText)findViewById(R.id.ETEmailPass);
        resetPassword=(Button)findViewById(R.id.Resetpass);
        firebaseAuth=FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail=passwordEmail.getText().toString().trim();

                if(userEmail.equals("")){
                    Toast.makeText(PasswordActivity.this, "Please Enter the registered Email_ID", Toast.LENGTH_SHORT).show();

                }else{
                    firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                          if (task.isSuccessful()){
                              Toast.makeText(PasswordActivity.this,"Password Reset Email Sent",Toast.LENGTH_SHORT).show();
                              startActivity(new Intent(PasswordActivity.this, MainActivity.class));
                          }else{
                              Toast.makeText(PasswordActivity.this,"Error in Sending Password Reset Email",Toast.LENGTH_SHORT).show();
                          }

                        }
                    });
                }
            }
        });
    }
}