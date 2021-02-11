package com.example.qr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationAct extends AppCompatActivity {
    private EditText rollNo, userPassword, userEmail, userMobile;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;
    String name, email, password, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();

        firebaseAuth =FirebaseAuth.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    //upload to the database , put the database code here
                    String roll= rollNo.getText().toString().trim();
                    String user_Mail= userEmail.getText().toString().trim();
                    String user_pass= userPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_Mail, user_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                sendUserData();
                                Toast.makeText(RegistrationAct.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationAct.this, MainActivity.class));
                            }
                            else{
                                Toast.makeText(RegistrationAct.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationAct.this, MainActivity.class));
            }
        });
    }
    private void setupUIViews() {
        rollNo = findViewById(R.id.ET2);
        userPassword = findViewById(R.id.Pass2);
        userEmail = findViewById(R.id.Email1);
        regButton = findViewById(R.id.B5);
        userLogin = findViewById(R.id.PT8);
        userMobile=findViewById(R.id.Phone1);
    }
    private Boolean validate() {
        Boolean result = false;
        name = rollNo.getText().toString();
        password = userPassword.getText().toString();
        email = userEmail.getText().toString();
        mobile=userMobile.getText().toString();

        if (name.isEmpty() || password.isEmpty() || email.isEmpty() || mobile.isEmpty()) {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }
        return result;

    }

    private void sendUserData(){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myRef=firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile= new UserProfile(name, email,mobile);
        myRef.setValue(userProfile);
    }
}