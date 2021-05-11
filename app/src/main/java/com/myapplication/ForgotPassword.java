package com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private TextInputLayout textinputlayout3;
    private LinearLayout linear3;
    private FirebaseAuth mAuth;
    private TextView textview2;
    private TextView textview3;
    private TextInputEditText edittext3;
    private Button button3;
    private TextView textview4;
    private Intent i;
    private OnCompleteListener<Void> _mAuth_reset_password_listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_forgot_password);
        textinputlayout3 = findViewById(R.id.textinputlayout3);
        linear3 = findViewById(R.id.linear3);
        textview2 = findViewById(R.id.textview2);
        textview3 = findViewById(R.id.textview3);
        edittext3 = findViewById(R.id.edittext3);
        button3 = findViewById(R.id.button3);
        textview4 = findViewById(R.id.textview4);
        Intent i = new Intent();

        textview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setClass(getApplicationContext(), StartActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
    }

    protected void onStart(){
        super.onStart();
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edittext3.getText().toString();
                if (!email.isEmpty()) {
                    Toast.makeText(ForgotPassword.this,"Please wait...",Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(ForgotPassword.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            final boolean success = task.isSuccessful();
                            if (success){
                                Toast.makeText(ForgotPassword.this,"Password reset email successfully sent!",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ForgotPassword.this,"Email not registered, contact developer!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(ForgotPassword.this, "Email cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}