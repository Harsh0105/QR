package com.example.qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mail,pass;
    private Button log_in, forgot_pass, Sign_up;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mail=(EditText)findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.Pass1);
        log_in=(Button)findViewById(R.id.B1);
        forgot_pass=(Button)findViewById(R.id.B2);
        Sign_up=(Button)findViewById(R.id.B3);

        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user= firebaseAuth.getCurrentUser();

        if(user!=null){
            startActivity(new Intent(MainActivity.this, MainAct.class));
        }

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(mail.getText().toString(), pass.getText().toString());
            }
        });

        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistrationAct.class));
            }
        });
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PasswordActivity.class));
            }
        });
    }

    private void validate(String userName, String userPassword){
        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    checkEmailVerification();

                }else {
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void checkEmailVerification(){
        FirebaseUser firebaseUser= firebaseAuth.getInstance().getCurrentUser();
        Boolean flag= firebaseUser.isEmailVerified();

        if(flag){
            finish();//not sure
            startActivity(new Intent(MainActivity.this, MainAct.class));
        }else{
            Toast.makeText(this, "Verify Your Email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}