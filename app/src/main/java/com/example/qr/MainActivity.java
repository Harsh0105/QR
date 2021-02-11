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

    private EditText roll_no,pass;
    private Button log_in, forgot_pass, Sign_up;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roll_no=(EditText)findViewById(R.id.ET1);
        pass=(EditText)findViewById(R.id.Pass1);
        log_in=(Button)findViewById(R.id.B1);
        forgot_pass=(Button)findViewById(R.id.B2);
        Sign_up=(Button)findViewById(R.id.B3);

//        firebaseAuth= FirebaseAuth.getInstance();
//
//
//        FirebaseUser user= firebaseAuth.getCurrentUser();
//        if(user != null){
//            finish();
//            startActivity(new Intent(MainActivity.this, MainAct.class));
//        }
//
//
        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistrationAct.class));
            }
        });
    }
}