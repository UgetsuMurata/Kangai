package com.example.kangai.Dashboard;

import static com.example.kangai.Application.Kangai.NotificationID.stateChange;
import static com.example.kangai.Helpers.Utilities.timestampTo12HourFormat;
import static com.example.kangai.Helpers.Utilities.toTitle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.example.kangai.Objects.Logs;
import com.example.kangai.Objects.Plants;
import com.example.kangai.R;

import org.w3c.dom.Text;

import java.util.HashMap;

@SuppressLint("DefaultLocale")
public class ViewPlants extends AppCompatActivity {

    Kangai kangai;
    Device device;

    LinearLayout home;

    CardView slot1, slot2;
    TextView deviceName, reservoir;
    Boolean hasChanges = false;

    FirebaseData fd;

    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_viewplants);

        Toolbar toolbar = findViewById(R.id.toolbar);
        home = findViewById(R.id.home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        home.setOnClickListener(view -> {
            startActivity(new Intent(ViewPlants.this, Dashboard.class));
            finish();
        });
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        //initialize views and objects
        kangai = Kangai.getInstance();
        fd = new FirebaseData();

        deviceName = findViewById(R.id.device_name);
        reservoir = findViewById(R.id.reservoir);
        slot1 = findViewById(R.id.slot1);
        slot2 = findViewById(R.id.slot2);

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
        setUpSlot(Slot1Plant, slot1, 1);
        setUpSlot(Slot2Plant, slot2, 2);

        long reservoirWaterLevel = device.getReservoir_water_level();
        double percentage = (reservoirWaterLevel / 230.0) * 100.0;
        String formattedPercentage = String.format("%.0f%%", percentage);
        reservoir.setText(formattedPercentage);

        runnable = new Runnable() {
            @Override
            public void run() {
                for (Device deviceItem : kangai.getDevices()) {
                    if (deviceItem.getId().equals(intent.getStringExtra("ID"))){
                        device = deviceItem;
                        break;
                    }
                }
                if (device == null) finish();

                long reservoirWaterLevel = device.getReservoir_water_level();
                double percentage = (reservoirWaterLevel / 1023.0) * 100.0;
                String formattedPercentage = String.format("%.0f%%", percentage);
                reservoir.setText(formattedPercentage);

                Plants Slot1Plant = device.getPlantSlots().get(0);
                Plants Slot2Plant = device.getPlantSlots().get(1);
                updateSlotStatus(Slot1Plant, slot1);
                updateSlotStatus(Slot2Plant, slot2);
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }

    public void setUpSlot(Plants plant, CardView slot, Integer slotNum){
        if (!plant.exists()){
            slot.findViewById(R.id.exists).setVisibility(View.GONE);
            slot.findViewById(R.id.not_exists).setVisibility(View.VISIBLE);
            slot.findViewById(R.id.not_exists).setOnClickListener(view -> initializeSlot(slot, slotNum));
            return;
        }
        slot.findViewById(R.id.exists).setVisibility(View.VISIBLE);
        slot.findViewById(R.id.not_exists).setVisibility(View.GONE);

        updateSlotStatus(plant, slot);

        slot.findViewById(R.id.water_now_button).setOnClickListener(view -> sendWaterCommand(slotNum));

        slot.findViewById(R.id.delete_button).setOnClickListener(view -> removeSlot(slot, slotNum));
    }

    public void initializeSlot(CardView slot, Integer slotNum){
        slot.findViewById(R.id.exists).setVisibility(View.VISIBLE);
        slot.findViewById(R.id.not_exists).setVisibility(View.GONE);

        HashMap<String, Object> slotData = new HashMap<>();
        slotData.put("Name", String.format("Slot %d", slotNum));
        slotData.put("Status", "DRY");
        fd.addValues(String.format("Devices/%s/Plants/Slot%d", device.getId(), slotNum), slotData);
        fd.updateValue(String.format("Devices/%s/LastUpdate", device.getId()), System.currentTimeMillis());

        Plants plants = new Plants(slotNum,
                                    String.format("Slot %d", slotNum),
                                    "DRY");
        setUpSlot(plants, slot, slotNum);
    }

    public void removeSlot(CardView slot, Integer slotNum){
        slot.findViewById(R.id.exists).setVisibility(View.GONE);
        slot.findViewById(R.id.not_exists).setVisibility(View.VISIBLE);
        fd.removeData(String.format("Devices/%s/Plants/Slot%d", device.getId(), slotNum));
        fd.updateValue(String.format("Devices/%s/LastUpdate", device.getId()), System.currentTimeMillis());
        HashMap<String, Object> value = new HashMap<>();
        value.put(String.valueOf(System.currentTimeMillis()),
                String.format("%s's Plant %d has been deleted.",
                        device.getName(), slotNum));
        fd.addValues("Users/" + kangai.getUserID() + "/Settings/Logs", value);
    }

    public void updateSlotStatus(Plants plant, CardView slot){
        if (!plant.exists()) return;

        TextView slotName = slot.findViewById(R.id.name);
        slotName.setText(plant.getName());

        switch (plant.getStatus()) {
            case "WET":
                slot.findViewById(R.id.status).setBackgroundTintList(ColorStateList.valueOf(
                        this.getResources().getColor(R.color.state_watered)));
                break;
            case "SEMI-WET":
                slot.findViewById(R.id.status).setBackgroundTintList(ColorStateList.valueOf(
                        this.getResources().getColor(R.color.state_50_percent)));
                break;
            case "DRY":
                slot.findViewById(R.id.status).setBackgroundTintList(ColorStateList.valueOf(
                        this.getResources().getColor(R.color.state_need_water)));
                break;
        }
        TextView currentStatus = slot.findViewById(R.id.current_status);
        currentStatus.setText(String.format("Status: %s", toTitle(plant.getStatus())));
    }

    public void sendWaterCommand(Integer slotNum){
        kangai.addLogs(new Logs(System.currentTimeMillis(),
                String.format("Manually watered Slot %d of %s", slotNum, device.getName())));
        fd.updateValue(String.format("Devices/%s/AppCommand", device.getId()),
                String.format("WATER_SLOT%d", slotNum));

        Toast.makeText(this, String.format("Sending command to water Slot %d", slotNum),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_plants_menu_header, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ToolbarMenu.ToolbarOption(this, item)) return true;
        else return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (hasChanges) kangai.updateADevice(this, device.getId());
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (hasChanges) kangai.updateADevice(this, device.getId());
        handler.removeCallbacks(runnable);
        super.onBackPressed();
    }
}