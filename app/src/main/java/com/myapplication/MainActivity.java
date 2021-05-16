package com.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private Toolbar toolbar1;
    private Button button2;
    private LinearLayout linear2;
    private Intent i = new Intent();
    private FirebaseUser user;
    private ProgressDialog prog;
    private TextView textview6;
    private Button button4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        setContentView(R.layout.activity_main);
        Toolbar toolbar1 = findViewById(R.id.toolbar1);
        LinearLayout linear2 = findViewById(R.id.linear2);
        Button button2 = findViewById(R.id.button2);
        textview6 = findViewById(R.id.textview6);
        button4 = findViewById(R.id.button4);
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

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        {
            try {
                new Update().execute();
            }
            catch (Exception e){
                textview6.setText("Excpetion: "+e);
            }
        }
            }
        });

        /*Context context = getApplicationContext();
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo pinfo = manager.getPackageInfo(context.getPackageName(), 0);
            String verName = pinfo.versionName;
            int verCode = pinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }*/
    }
        public class Update extends AsyncTask<String , Void, String>{

        private String result = null;
        private String Lvercode = null;
            @Override
        public void onPreExecute(){

            super.onPreExecute();
            prog = new ProgressDialog(MainActivity.this);
            prog.setMax(100);prog.setMessage("Checking...");prog.setIndeterminate(true);prog.setCancelable(false);
            prog.show();
        }

            @Override
            public String doInBackground(String... strings) {

                try {
                    String url = "https://raw.githubusercontent.com/SoGho2580/AppUpdater/master/app/update.txt";
                    Connection connection = Jsoup.connect(url);
                    Document doc = connection.get();
                    Lvercode = doc.text();
                } catch (Exception e) {
                    System.out.println("Exception: "+e);
                }
                return Lvercode;
            }
            public void onPostExecute(String s){
            super.onPostExecute(s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int LverCode = Integer.parseInt(s);
                        Context context = getApplicationContext();
                        PackageManager manager = context.getPackageManager();
                        try {
                            PackageInfo pinfo = manager.getPackageInfo(context.getPackageName(), 0);
                            String verName = pinfo.versionName;
                            int verCode = pinfo.versionCode;
                            if (LverCode==verCode){
                                //textview6.setText("You have the latest version!"+verName);
                                String update = "No updates available! You have the latest version!";
                                //prog.dismiss()
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Update");
                                builder.setMessage(update);
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        textview6.setVisibility(View.INVISIBLE);
                                    }
                                });
                                prog.dismiss();
                                builder.show();
                                //textview6.setText(update);
                            }
                            else {
                                String update = "Update available";
                                prog.dismiss();
                                textview6.setText(update);
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }});
            }
        }
}