    package com.myapplication;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import android.content.Intent;
import android.app.Activity;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

    public class StartActivity extends AppCompatActivity {


    private static final String TAG = "FirebaseTopic";
    private FirebaseAuth mAuth;

    private LinearLayout linear1;
    private TextInputLayout textinputlayout1;
    private TextInputLayout textinputlayout2;
    private TextInputEditText edittext1;
    private TextInputEditText edittext2;
    private Button button1;
    private TextView textview1;
    private Intent i =new Intent();
    private SharedPreferences user;
    private OnCompleteListener<AuthResult> _mAuth_sign_in_listener;
    private void reload(){}
    private void updateUI(FirebaseUser user){}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        PackageManager packageManager = getApplicationContext().getPackageManager();
        try {
            PackageInfo pInfo = packageManager.getPackageInfo(getApplicationContext().getPackageName(), 0);
            int verCode = pInfo.versionCode;
            String verName = pInfo.versionName;
            FirebaseMessaging.getInstance().subscribeToTopic(String.valueOf(verCode))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            String result = "You are using version: "+verName;
                        if(!task.isSuccessful()){
                            result = "Can't connect to Firebase! Check your network and restart the app or contact developer!";
                        }
                        Log.d(TAG, result);
                        Toast.makeText(StartActivity.this,result,Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_start);

        LinearLayout _nav_view = (LinearLayout) findViewById(R.id._nav_view);
        TextInputLayout textinputlayout1 = findViewById(R.id.textinputlayout1);
        TextInputLayout textinputlayout2 = findViewById(R.id.textinputlayout2);
        edittext1 = findViewById(R.id.edittext1);
        edittext2 = findViewById(R.id.edittext2);
        Button button1 = findViewById(R.id.button1);
        TextView textview1 = findViewById(R.id.textview1);
        SharedPreferences user = getSharedPreferences("user", Activity.MODE_PRIVATE);
        Intent i= new Intent();
        textview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setClass(getApplicationContext(), ForgotPassword.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            i.setClass(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    }

    private void signIn(){

                    String email=Objects.requireNonNull(edittext1.getText()).toString();
                    String password= Objects.requireNonNull(edittext2.getText()).toString();
                    if(!email.isEmpty() && !password.isEmpty()){
                        final ProgressDialog prog = new ProgressDialog(StartActivity.this);
                        prog.setMax(100);prog.setMessage("Logging in...");prog.setIndeterminate(true);prog.setCancelable(false);
                        prog.show();
                        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(StartActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final boolean success= task.isSuccessful();
                                if(success){
                                    i.setClass(getApplicationContext(),MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    prog.cancel();
                                    edittext1.setText(null);
                                    edittext2.setText(null);
                                }
                                else{
                                    final String error=task.getException()!=null?task.getException().getMessage():"";
                                    prog.cancel();
                                    edittext2.setText(null);
                                    Toast.makeText(StartActivity.this,""+error,Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Email/Password cannot be empty!", Toast.LENGTH_SHORT).show();
                    }
                }


    /*private void signIn() {
        // [START sign_in_with_email]
        // [END sign_in_with_email]


    button1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Toast.makeText(StartActivity.this,"Button pressed",Toast.LENGTH_SHORT).show();
            String email = edittext1.getText().toString();
            String password = edittext2.getText().toString();
    if((0<edittext1.length()) && (0<edittext2.length())) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(StartActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(StartActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            i.setClass(getApplicationContext(),MainActivity.class);
                            startActivity(i);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(StartActivity.this, "Authentication failed, contact developer!",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    } else {
        Toast.makeText(getApplicationContext(),"Email/Password cannot be empty!", Toast.LENGTH_SHORT).show();
        //Log.println()
    }

}}  );}*/

}
