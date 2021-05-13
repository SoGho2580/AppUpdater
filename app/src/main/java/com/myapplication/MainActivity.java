package com.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar toolbar1;
    private Button button2;
    private LinearLayout linear2;
    private Intent i = new Intent();
    private FirebaseUser user;
    private ProgressDialog prog;
    private static String url = "https://raw.githubusercontent.com/SoGho2580/AppUpdater/master/app/update.json";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        setContentView(R.layout.activity_main);
        Toolbar toolbar1 = findViewById(R.id.toolbar1);
        LinearLayout linear2 = findViewById(R.id.linear2);
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
        try {
            PackageInfo pinfo = manager.getPackageInfo(context.getPackageName(), 0);
            String verName = pinfo.versionName;
            int verCode = pinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private class getNames extends AsyncTask<Void,Void,Void>{

        @Override
        public Void doInBackground(Void... voids) {
            Handler handler = new Handler();
            String jsonString = handler.httpServiceCall(url);
            if(jsonString!=null){
                try {
                    JSONObject jsonObject1 = new JSONObject();
                        Integer LverCode = Integer.valueOf(jsonObject1.getString("verCode"));
                        String LverName = jsonObject1.getString("verName");

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"Json Parsing Error"+e,Toast.LENGTH_LONG).show();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Json Parsing Error"+e,Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();
                    }
                });

            }
            return null;
        }

    }
}