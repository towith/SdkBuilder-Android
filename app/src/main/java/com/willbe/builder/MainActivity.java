package com.willbe.builder;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.duy.compile.BuildApkTask;
import com.duy.ide.file.FileManager;
import com.duy.project.file.android.AndroidProjectFolder;
import com.willbe.utils.FileUtil;

import javax.tools.Diagnostic;
import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public  Handler progressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            spin((String) msg.obj);
        }
    };


    private  void spin(String msg0) {
//        ActivityManager activityManager = (ActivityManager) MyApp.getAppContext().getSystemService(ACTIVITY_SERVICE);
//        ComponentName topActivity = activityManager.getRunningTasks(1).get(0).topActivity;
        ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(50);
        TextView spinText = (TextView) this.findViewById(R.id.spinText);
        spinText.setText(msg0);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    doBuild();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void doBuild() throws Exception {
        // prepare
        FileUtil.copyFileOrDir("templates");
        FileUtil.copyAssetFileToDest("android.jar",
                MyApp.getAppContext()
                        .getFilesDir() + File.separator + FileManager.SDK_DIR + File.separator + "android.jar");

//        File filesDir = MyApp.getAppContext().getFilesDir();

        BuildApkTask buildApkTask = new BuildApkTask(this, new BuildApkTask.CompileListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onError(Exception e, List<Diagnostic> diagnostics) {
            }

            @Override
            public void onComplete(File apk, List<Diagnostic> diagnostics) {
            }
        });


        AsyncTask<AndroidProjectFolder, Object, File> asyncTask = buildApkTask.execute(new AndroidProjectFolder(new File(
                FileUtil.APP_DATA_PATH,
                "templates"),
                "",
                "",
                ""));

        File buildApk = asyncTask.get();

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(buildApk),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
