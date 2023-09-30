package com.example.kangai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kangai.CustomViews.LeafLoadingBar;
import com.example.kangai.Dashboard.Dashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    /*
     * This MainActivity will be used for the loading screen.
     * */

    LeafLoadingBar display;
    TextView label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.loading_display);
        label = findViewById(R.id.loading_label);

        final String Process1 = "Retrieving Resources";
        final String Process2 = "Processing";
        final String Process3 = "Another Sample Loading";

        ArrayList<String> loadingProcesses = new ArrayList<>(Arrays.asList(
                Process1,
                Process2,
                Process3
        ));
        display.setMax(loadingProcesses.size());
        new Thread(() -> {
            AtomicInteger process_number = new AtomicInteger(0);
            for (String process : loadingProcesses) {
                process_number.getAndIncrement();
                runOnUiThread(() -> {
                    label.setText(String.format("%s... %.0f%%", process, ((float) process_number.get() / loadingProcesses.size())*100));
                    display.setProgress(process_number.get());
                });
                switch (process){
                    case Process1:
                        Process1();
                        break;
                    case Process2:
                        Process2();
                        break;
                    case Process3:
                        Process3();
                        break;
                }
            }
            startActivity(new Intent(MainActivity.this, Dashboard.class));
            finish();
        }).start();
    }

    private void Process1(){
        long timeStarted = System.currentTimeMillis();
        //PROCESS 1
        while (System.currentTimeMillis()-timeStarted<1000);
    }
    private void Process2(){
        long timeStarted = System.currentTimeMillis();
        //PROCESS 2
        while (System.currentTimeMillis()-timeStarted<1000);
    }
    private void Process3(){
        long timeStarted = System.currentTimeMillis();
        //PROCESS 3
        while (System.currentTimeMillis()-timeStarted<1000);
    }
}