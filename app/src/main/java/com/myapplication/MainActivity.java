package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar toolbar1;
    private Button button2;
    private LinearLayout linear2;
    private Intent i=new Intent();
    private FirebaseUser user;
    private TextView textview5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user= mAuth.getCurrentUser();
        setContentView(R.layout.activity_main);
        Toolbar toolbar1=findViewById(R.id.toolbar1);
        LinearLayout linear2=findViewById(R.id.linear2);
        textview5=findViewById(R.id.textview5);
        AppUpdater appUpdater = new AppUpdater(this);
        appUpdater.start();
        appUpdater.setDisplay(Display.DIALOG).setCancelable(false);
        appUpdater.setDisplay(Display.NOTIFICATION);
        appUpdater.setUpdateFrom(UpdateFrom.XML).setUpdateXML("https://github.com/SoGho2580/AppUpdater/raw/master/app/update.xml");
        Button button2=findViewById(R.id.button2);

        textview5.setText(user.getEmail());
         button2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 FirebaseAuth.getInstance().signOut();
                 finish();
             }
         });
         toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 onBackPressed();
             }
         });
    }

}