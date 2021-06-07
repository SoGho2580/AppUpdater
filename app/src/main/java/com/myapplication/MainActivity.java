package com.myapplication;

import androidx.appcompat.app.AlertDialog;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import static android.os.Build.VERSION.SDK_INT;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
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
        storage = FirebaseStorage.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        setContentView(R.layout.activity_main);
        Toolbar toolbar1 = findViewById(R.id.toolbar1);
        LinearLayout linear2 = findViewById(R.id.linear2);
        Button button2 = findViewById(R.id.button2);
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
                    } catch (Exception e) {
                        textview6.setText("Exception: " + e);
                    }
                }
            }
        });

    }

    public class Update extends AsyncTask<String, Void, String> {

        private String result = null;
        private String Lvercode = null;

        @Override
        public void onPreExecute() {

            super.onPreExecute();
            prog = new ProgressDialog(MainActivity.this);
            prog.setMax(100);
            prog.setMessage("Checking...");
            prog.setIndeterminate(true);
            prog.setCancelable(false);
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
                System.out.println("Exception: " + e);
            }
            return Lvercode;
        }

        public void onPostExecute(final String s) {
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
                        if (LverCode == verCode) {
                            String update = "You have the latest version: " + verName;
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("No updates available!");
                            builder.setMessage(update);
                            builder.setPositiveButton("Ok", (dialog, which) -> textview6.setVisibility(View.INVISIBLE));
                            builder.show();
                            prog.dismiss();
                        } else {
                            String update = "New update is ready to be downloaded! Click 'Update now' to update now!";
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("New update available!");
                            builder.setMessage(update);
                            builder.setPositiveButton("Update now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference reference = storage.getReference();
                                    StorageReference gsreference = storage.getReferenceFromUrl("gs://my-chat-app-9cf4a.appspot.com/app-release.apk");
                                    try {
                                        String apkname = pinfo.packageName;
                                        File mydir = context.getDir(apkname, Context.MODE_APPEND);
                                        File file = new File(mydir, "localFile");
                                        File localFile = File.createTempFile("update", ".apk", mydir);
                                        gsreference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                Toast.makeText(MainActivity.this, "Apk downloaded successfully!", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            });
                            builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            builder.show();
                            prog.dismiss();
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}