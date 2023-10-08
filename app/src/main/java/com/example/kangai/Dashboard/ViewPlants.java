package com.example.kangai.Dashboard;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.example.kangai.Application.Kangai;
import com.example.kangai.Firebase.FirebaseData;
import com.example.kangai.Helpers.ToolbarMenu;
import com.example.kangai.Objects.BooleanReference;
import com.example.kangai.Objects.Device;
import com.example.kangai.Objects.Plants;
import com.example.kangai.R;

public class ViewPlants extends AppCompatActivity {

    Kangai kangai;
    Device device;

    LinearLayout home;

    TextView deviceName, slot1Name, slot2Name, slot3Name, slot4Name;
    TextView slot1LastWatered, slot2LastWatered, slot3LastWatered, slot4LastWatered;
    CardView slot1Status, slot2Status, slot3Status, slot4Status;
    CardView slot1Water, slot2Water, slot3Water, slot4Water;
    ImageView slot1Edit, slot2Edit, slot3Edit, slot4Edit;
    EditText slot1NameEdittext, slot2NameEdittext, slot3NameEdittext, slot4NameEdittext;
    BooleanReference slot1EditState, slot2EditState, slot3EditState, slot4EditState;
    Boolean hasChanges = false;

    FirebaseData fd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_viewplants);

        Toolbar toolbar = findViewById(R.id.toolbar);
        home = findViewById(R.id.home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewPlants.this, Dashboard.class));
                finish();
            }
        });
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        kangai = Kangai.getInstance();
        deviceName = findViewById(R.id.device_name);
        slot1Name = findViewById(R.id.slot1_name);
        slot2Name = findViewById(R.id.slot2_name);
        slot3Name = findViewById(R.id.slot3_name);
        slot4Name = findViewById(R.id.slot4_name);
        slot1Status = findViewById(R.id.slot1_status);
        slot2Status = findViewById(R.id.slot2_status);
        slot3Status = findViewById(R.id.slot3_status);
        slot4Status = findViewById(R.id.slot4_status);
        slot1Water = findViewById(R.id.slot1_water);
        slot2Water = findViewById(R.id.slot2_water);
        slot3Water = findViewById(R.id.slot3_water);
        slot4Water = findViewById(R.id.slot4_water);
        slot1Edit = findViewById(R.id.slot1_edit_name);
        slot2Edit = findViewById(R.id.slot2_edit_name);
        slot3Edit = findViewById(R.id.slot3_edit_name);
        slot4Edit = findViewById(R.id.slot4_edit_name);
        slot1NameEdittext = findViewById(R.id.slot1_name_edittext);
        slot2NameEdittext = findViewById(R.id.slot2_name_edittext);
        slot3NameEdittext = findViewById(R.id.slot3_name_edittext);
        slot4NameEdittext = findViewById(R.id.slot4_name_edittext);
        slot1LastWatered = findViewById(R.id.slot1_last_watered);
        slot2LastWatered = findViewById(R.id.slot2_last_watered);
        slot3LastWatered = findViewById(R.id.slot3_last_watered);
        slot4LastWatered = findViewById(R.id.slot4_last_watered);

        fd = new FirebaseData();

        Intent intent = getIntent();
        for (Device deviceItem : kangai.getDevices()) {
            if (deviceItem.getId().equals(intent.getStringExtra("ID"))){
                this.device = deviceItem;
                break;
            }
        }
        if (device == null) finish(); //end this activity if there is no device chosen. Meaning, unexpected entry.
        deviceName.setText(device.getName());

        Plants Slot1Plant = device.getPlantSlots().get(0);
        Plants Slot2Plant = device.getPlantSlots().get(1);
        Plants Slot3Plant = device.getPlantSlots().get(2);
        Plants Slot4Plant = device.getPlantSlots().get(3);
        //SET SLOT 1
        slot1Name.setText(Slot1Plant.getName());
        slot1LastWatered.setText(Slot1Plant.getLastWatered());
        switch (Slot1Plant.getStatus()){
            case "RED":
                slot1Status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.state_need_water)));
                break;
            case "YELLOW":
                slot1Status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.state_50_percent)));
                break;
            case "GREEN":
                slot1Status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.state_watered)));
                break;
        }
        slot1Water.setOnClickListener(view -> {
            fd.addValue("Devices/" + device.getId() + "/AppCommand/", "WATER_SLOT1");
            Toast.makeText(this, "Watering "+device.getPlantSlots().get(0).getName()+"...", Toast.LENGTH_SHORT).show();
        });
        slot1EditState = new BooleanReference(false);
        slot1Edit.setOnClickListener(view -> editName(slot1EditState, slot1NameEdittext, slot1Name, slot1Edit, "Slot1"));
        //SET SLOT 2
        slot2Name.setText(Slot2Plant.getName());
        slot2LastWatered.setText(Slot2Plant.getLastWatered());
        switch (Slot2Plant.getStatus()){
            case "RED":
                slot2Status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.state_need_water)));
                break;
            case "YELLOW":
                slot2Status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.state_50_percent)));
                break;
            case "GREEN":
                slot2Status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.state_watered)));
                break;
        }
        slot2Water.setOnClickListener(view -> {
            fd.addValue("Devices/" + device.getId() + "/AppCommand/", "WATER_SLOT2");
            Toast.makeText(this, "Watering "+device.getPlantSlots().get(1).getName()+"...", Toast.LENGTH_SHORT).show();
        });
        slot2EditState = new BooleanReference(false);
        slot2Edit.setOnClickListener(view -> editName(slot2EditState, slot2NameEdittext, slot2Name, slot2Edit, "Slot2"));
        //SET SLOT 3
        slot3Name.setText(Slot3Plant.getName());
        slot3LastWatered.setText(Slot3Plant.getLastWatered());
        switch (Slot3Plant.getStatus()){
            case "RED":
                slot3Status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.state_need_water)));
                break;
            case "YELLOW":
                slot3Status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.state_50_percent)));
                break;
            case "GREEN":
                slot3Status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.state_watered)));
                break;
        }
        slot3Water.setOnClickListener(view -> {
            fd.addValue("Devices/" + device.getId() + "/AppCommand/", "WATER_SLOT3");
            Toast.makeText(this, "Watering "+device.getPlantSlots().get(2).getName()+"...", Toast.LENGTH_SHORT).show();
        });
        slot3EditState = new BooleanReference(false);
        slot3Edit.setOnClickListener(view -> editName(slot3EditState, slot3NameEdittext, slot3Name, slot3Edit, "Slot3"));
        //SET SLOT 4
        slot4Name.setText(Slot4Plant.getName());
        slot4LastWatered.setText(Slot4Plant.getLastWatered());
        switch (Slot4Plant.getStatus()){
            case "RED":
                slot4Status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.state_need_water)));
                break;
            case "YELLOW":
                slot4Status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.state_50_percent)));
                break;
            case "GREEN":
                slot4Status.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.state_watered)));
                break;
        }
        slot4Water.setOnClickListener(view -> {
            fd.addValue("Devices/" + device.getId() + "/AppCommand/", "WATER_SLOT4");
            Toast.makeText(this, "Watering "+device.getPlantSlots().get(3).getName()+"...", Toast.LENGTH_SHORT).show();
        });
        slot4EditState = new BooleanReference(false);
        slot4Edit.setOnClickListener(view -> editName(slot4EditState, slot4NameEdittext, slot4Name, slot4Edit, "Slot4"));
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

    private void editName(BooleanReference editState, EditText nameEditText, TextView slotName, ImageView slotEdit, String slotNumber){
        if (editState.value()){
            String newName = nameEditText.getText().toString().trim();
            if (!newName.equals("")){
                slotName.setText(newName);
                fd.addValue("Devices/"+device.getId()+"/Plants/"+slotNumber+"/Name/", newName);
                hasChanges = true;
            }
            slotName.setVisibility(View.VISIBLE);
            nameEditText.setVisibility(View.GONE);
            slotEdit.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.edit, getTheme()));
        } else {
            String currentName = slotName.getText().toString().trim();
            slotName.setVisibility(View.GONE);
            nameEditText.setVisibility(View.VISIBLE);
            nameEditText.setHint(currentName);
            slotEdit.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.check, getTheme()));
        }
        editState.switchValue();
    }

    @Override
    protected void onDestroy() {
        if (hasChanges) kangai.updateADevice(this, device.getId());
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (hasChanges) kangai.updateADevice(this, device.getId());
        super.onBackPressed();
    }
}