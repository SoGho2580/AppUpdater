package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar toolbar1;
    private Button button2;
    private LinearLayout linear2;
    private Intent i = new Intent();
    private FirebaseUser user;
    private Button button4;
    private ProgressDialog prog;
    private TextView textview5;
    private TextView textview6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        setContentView(R.layout.activity_main);
        Toolbar toolbar1 = findViewById(R.id.toolbar1);
        LinearLayout linear2 = findViewById(R.id.linear2);
        textview5 = findViewById(R.id.textview5);
        textview6 = findViewById(R.id.textview6);
        button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final ProgressDialog prog= new ProgressDialog(MainActivity.this);prog.setMax(100);prog.setMessage("Checking for updates...");prog.setIndeterminate(true);prog.setCancelable(false);
                updateCheck();
            }
        });
        Button button2 = findViewById(R.id.button2);
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

        Context context = getApplicationContext();
        PackageManager manager = context.getPackageManager();
        try{
            PackageInfo pinfo = manager.getPackageInfo(context.getPackageName(),0);
            String verName = pinfo.versionName;
            int verCode = pinfo.versionCode;
            textview5.setText("Your version: "+verCode);
        }
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateCheck() {
        //prog.show();
        AppUpdater appUpdater = new AppUpdater(this);
        appUpdater.setUpdateFrom(UpdateFrom.XML);
        appUpdater.setUpdateXML("https://raw.githubusercontent.com/SoGho2580/AppUpdater/master/app/update.xml");
        appUpdater.setDisplay(Display.DIALOG).setCancelable(true);
        appUpdater.setTitleOnUpdateAvailable("Update available")
                .setContentOnUpdateAvailable("Click 'Update now?' to update your app")
                .setTitleOnUpdateNotAvailable("Update not available")
                .setContentOnUpdateNotAvailable("No update available. Check for updates again later!")
                .setButtonUpdate("Update now?");
                //.setButtonUpdateClickListener()
	appUpdater.setButtonDismiss("Maybe later");
                //.setButtonDismissClickListener(...)
        appUpdater.showAppUpdated(true);
        appUpdater.start();
        //prog.cancel();
    }
}