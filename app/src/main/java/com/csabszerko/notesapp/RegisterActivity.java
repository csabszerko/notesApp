package com.csabszerko.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmail_input_register, mPassword_input_register, mPassword_input_register_confirm;
    private Button mRegister_button;
    private TextView mGo_to_login_button;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //start here
        mAuth = FirebaseAuth.getInstance();

        mEmail_input_register = findViewById(R.id.email_input_register);
        mPassword_input_register = findViewById(R.id.password_input_register);
        mPassword_input_register_confirm = findViewById(R.id.password_input_register_confirm);
        mGo_to_login_button = findViewById(R.id.go_to_login_button);
        mRegister_button = findViewById(R.id.register_button);

        mGo_to_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().hide();

        mRegister_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail_input_register.getText().toString().trim();
                String pw=mPassword_input_register.getText().toString().trim();
                String pwConfirm=mPassword_input_register_confirm.getText().toString().trim();

                if(email.isEmpty() || pw.isEmpty() || pwConfirm.isEmpty()){
                    Toast.makeText(getApplicationContext(), "all fields are required!", Toast.LENGTH_SHORT).show();
                }else if (pw.length()<7) {
                    Toast.makeText(getApplicationContext(), "password has to be at least 7 characters long!", Toast.LENGTH_SHORT).show();
                }
                else if(!pw.equals(pwConfirm)){
                    Toast.makeText(getApplicationContext(), "passwords don't match!", Toast.LENGTH_SHORT).show();
                }
                else {
                    //register the user to firebase later
                    mAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "registration successful!", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "registration unsuccessful!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}