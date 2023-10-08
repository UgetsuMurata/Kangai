package com.example.kangai.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.kangai.Accounts.EditAccount;
import com.example.kangai.Application.Kangai;
import com.example.kangai.CustomViews.ToggleImage;
import com.example.kangai.Dashboard.Dashboard;
import com.example.kangai.Firebase.FirebaseData;
import com.example.kangai.Helpers.LocalStorageHelper;
import com.example.kangai.Helpers.LocalStorageHelper.PrefNames;
import com.example.kangai.Helpers.ToolbarMenu;
import com.example.kangai.R;

import java.util.HashMap;

public class Settings extends AppCompatActivity {

    LinearLayout home;
    ToggleImage smsNotif, pushNotif, statesCateg, wateringCateg, plantUpdatesCateg;
    FirebaseData fd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        home = findViewById(R.id.home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, Dashboard.class));
            }
        });
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        smsNotif = findViewById(R.id.sms_notif);
        pushNotif = findViewById(R.id.push_notif);
        statesCateg = findViewById(R.id.states_categ);
        wateringCateg = findViewById(R.id.watering_categ);
        plantUpdatesCateg = findViewById(R.id.plant_updates_categ);

        fd = new FirebaseData();

        setStates(smsNotif, LocalStorageHelper.getPref(this, PrefNames.NOTIFICATION_SMS, true));
        setStates(pushNotif, LocalStorageHelper.getPref(this, PrefNames.NOTIFICATION_PUSHNOTIF, true));
        setStates(statesCateg, LocalStorageHelper.getPref(this, PrefNames.NOTIFICATION_CATEG_STATES, true));
        setStates(wateringCateg, LocalStorageHelper.getPref(this, PrefNames.NOTIFICATION_CATEG_WATERING, true));
        setStates(plantUpdatesCateg, LocalStorageHelper.getPref(this, PrefNames.NOTIFICATION_CATEG_PLANTUPDATES, true));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_header, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ToolbarMenu.ToolbarOption(this, item)) return true;
        else return super.onOptionsItemSelected(item);
    }

    private void setStates(ToggleImage icon, Boolean state){
        if (state) icon.setActive();
        else icon.setInactive();
    }

    @Override
    protected void onDestroy() {
        Boolean smsNotifState = smsNotif.isActive();
        Boolean pushNotifState = pushNotif.isActive();
        Boolean statesCategState = statesCateg.isActive();
        Boolean wateringCategState = wateringCateg.isActive();
        Boolean plantUpdatesCategState = plantUpdatesCateg.isActive();

        HashMap<String, Object> values = new HashMap<>();
        values.put("sms", smsNotifState);
        values.put("pushNotif", pushNotifState);
        values.put("statesCateg", statesCategState);
        values.put("wateringCateg", wateringCategState);
        values.put("plantUpdatesCateg", plantUpdatesCategState);

        fd.addValues("User/"+ Kangai.getInstance().getUserID()+"/Settings/Notifications/", values);

        LocalStorageHelper.setPref(this, PrefNames.NOTIFICATION_SMS, smsNotifState);
        LocalStorageHelper.setPref(this, PrefNames.NOTIFICATION_PUSHNOTIF, pushNotifState);
        LocalStorageHelper.setPref(this, PrefNames.NOTIFICATION_CATEG_STATES, statesCategState);
        LocalStorageHelper.setPref(this, PrefNames.NOTIFICATION_CATEG_WATERING, wateringCategState);
        LocalStorageHelper.setPref(this, PrefNames.NOTIFICATION_CATEG_PLANTUPDATES, plantUpdatesCategState);
        super.onDestroy();
    }
}