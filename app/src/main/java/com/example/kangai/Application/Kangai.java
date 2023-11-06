package com.example.kangai.Application;

import static com.example.kangai.Application.Kangai.NotificationID.stateChange;
import static com.example.kangai.Helpers.TimeHelper.millisToReadable;
import static com.example.kangai.Helpers.TimeHelper.readableToMillis;

import android.Manifest;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.kangai.Dashboard.Dashboard;
import com.example.kangai.Firebase.FirebaseData;
import com.example.kangai.Helpers.TimeHelper;
import com.example.kangai.MainActivity;
import com.example.kangai.Objects.Device;
import com.example.kangai.Objects.Logs;
import com.example.kangai.Objects.Plants;
import com.example.kangai.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Kangai extends Application {

    public static Kangai instance;

    private FirebaseData fd;
    private List<Device> devices;
    private List<Device> referenceDevices;
    private boolean devicesHasChanges = false;
    private Device device;
    private List<Logs> logs;
    private List<Logs> referenceLogs;
    private boolean logsHasChanges = false;
    public String userID;
    public String username;

    private Handler handler = new Handler();
    private Runnable updatePlants;

    private static final String PRIMARY_CHANNEL_ID = "default";
    private NotificationManager mNotifyManager;
    private Integer notificationID = 0;

    private Device newDevice;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        fd = new FirebaseData();
        devices = new ArrayList<>();
        referenceDevices = new ArrayList<>();
        logs = new ArrayList<>();
        referenceLogs = new ArrayList<>();

        createNotificationChannel();

        updatePlants = new Runnable() {
            @Override
            public void run() {
                UpdateDataRetrieved();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(updatePlants);
    }

    public static Kangai getInstance() {
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        handler.removeCallbacks(updatePlants);
    }

    public List<Device> getDevices() {
        return devices;
    }

    public List<Logs> getLogs() {
        return logs;
    }

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public Device getDevice() {
        return device;
    }

    public void setReferenceDevices(List<Device> referenceDevices) {
        this.referenceDevices = referenceDevices;
    }

    public List<Device> getReferenceDevices() {
        return referenceDevices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public void addDevice(Device device) {
        this.devices.add(device);
    }

    public void getAllDevice(Context context) {
        if (devices != null) devices.clear();
        else devices = new ArrayList<>();
        fd.retrieveData(this, "Users/" + userID + "/Devices", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                for (DataSnapshot devices : dataSnapshot.getChildren()) {
                    String key = devices.getKey();
                    fd.retrieveData(context, "Devices/" + key + "/", new FirebaseData.FirebaseDataCallback() {
                        @Override
                        public void onDataReceived(DataSnapshot dataSnapshot) {
                            Object name = dataSnapshot.child("Name").getValue();
                            Object manager = dataSnapshot.child("Manager").getValue();
                            Object reservoir = dataSnapshot.child("Reservoir").getValue();
                            Object lastUpdate = dataSnapshot.child("LastUpdate").getValue();

                            DataSnapshot Slot1 = dataSnapshot.child("Plants/Slot1");
                            Plants plant1 = new Plants(1,
                                    Slot1.child("Name").getValue().toString(),
                                    Slot1.child("Status").getValue().toString(),
                                    Slot1.child("Value").getValue().toString(),
                                    Slot1.child("LastWatered").getValue().toString());

                            DataSnapshot Slot2 = dataSnapshot.child("Plants/Slot2");
                            Plants plant2 = new Plants(1,
                                    Slot2.child("Name").getValue().toString(),
                                    Slot2.child("Status").getValue().toString(),
                                    Slot2.child("Value").getValue().toString(),
                                    Slot2.child("LastWatered").getValue().toString());

                            DataSnapshot Slot3 = dataSnapshot.child("Plants/Slot3");
                            Plants plant3 = new Plants(1,
                                    Slot3.child("Name").getValue().toString(),
                                    Slot3.child("Status").getValue().toString(),
                                    Slot3.child("Value").getValue().toString(),
                                    Slot3.child("LastWatered").getValue().toString());

                            DataSnapshot Slot4 = dataSnapshot.child("Plants/Slot4");
                            Plants plant4 = new Plants(1,
                                    Slot4.child("Name").getValue().toString(),
                                    Slot4.child("Status").getValue().toString(),
                                    Slot4.child("Value").getValue().toString(),
                                    Slot4.child("LastWatered").getValue().toString());

                            Device device = new Device(key,
                                    manager != null ? manager.toString() : null,
                                    name != null ? name.toString() : null,
                                    new ArrayList<Plants>() {{
                                        add(plant1);
                                        add(plant2);
                                        add(plant3);
                                        add(plant4);
                                    }},
                                    Long.getLong(reservoir != null ? reservoir.toString() : "0"),
                                    Long.getLong(lastUpdate != null ? lastUpdate.toString() : "0"));

                            addDevice(device);
                        }
                    });
                }
            }
        });
    }

    public void updateADevice(Context context, String id) {
        int devicesID = 0;
        for (Device device : new ArrayList<>(devices)) {
            if (Objects.equals(device.getId(), id)) {
                devices.remove(devicesID);
                break;
            }
            devicesID++;
        }
        int finalDevicesID = devicesID;
        fd.retrieveData(context, "Devices/" + id + "/", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                Object name = dataSnapshot.child("Name").getValue();
                Object manager = dataSnapshot.child("Manager").getValue();
                Object reservoir = dataSnapshot.child("Reservoir").getValue();
                Object lastUpdate = dataSnapshot.child("LastUpdate").getValue();

                DataSnapshot Slot1 = dataSnapshot.child("Plants/Slot1");
                Plants plant1 = new Plants(1,
                        Slot1.child("Name").getValue().toString(),
                        Slot1.child("Status").getValue().toString(),
                        Slot1.child("Value").getValue().toString(),
                        Slot1.child("LastWatered").getValue().toString());

                DataSnapshot Slot2 = dataSnapshot.child("Plants/Slot2");
                Plants plant2 = new Plants(1,
                        Slot2.child("Name").getValue().toString(),
                        Slot2.child("Status").getValue().toString(),
                        Slot2.child("Value").getValue().toString(),
                        Slot2.child("LastWatered").getValue().toString());

                DataSnapshot Slot3 = dataSnapshot.child("Plants/Slot3");
                Plants plant3 = new Plants(1,
                        Slot3.child("Name").getValue().toString(),
                        Slot3.child("Status").getValue().toString(),
                        Slot3.child("Value").getValue().toString(),
                        Slot3.child("LastWatered").getValue().toString());

                DataSnapshot Slot4 = dataSnapshot.child("Plants/Slot4");
                Plants plant4 = new Plants(1,
                        Slot4.child("Name").getValue().toString(),
                        Slot4.child("Status").getValue().toString(),
                        Slot4.child("Value").getValue().toString(),
                        Slot4.child("LastWatered").getValue().toString());

                Device device = new Device(id,
                        manager != null ? manager.toString() : null,
                        name != null ? name.toString() : null,
                        new ArrayList<Plants>() {{
                            add(plant1);
                            add(plant2);
                            add(plant3);
                            add(plant4);
                        }},
                        Long.getLong(reservoir != null ? reservoir.toString() : "0"),
                        Long.getLong(lastUpdate != null ? lastUpdate.toString() : "0"));

                devices.add(finalDevicesID, device);
            }
        });
    }

    public void setLogs(List<Logs> logs) {
        this.logs = logs;
    }

    public List<Logs> getReferenceLogs() {
        return referenceLogs;
    }

    public void setReferenceLogs(List<Logs> referenceLogs) {
        this.referenceLogs = referenceLogs;
    }

    public void addLogs(Logs logs) {
        this.logs.add(logs);
    }

    public void getAllLogs() {
        if (logs != null) logs.clear();
        else logs = new ArrayList<>();
        fd.retrieveData(this, "Users/" + userID + "/Settings/Logs/", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                for (DataSnapshot logDS : dataSnapshot.getChildren()) {
                    String timestamp = logDS.getKey();
                    String log = String.valueOf(logDS.getValue() != null ? logDS.getValue() : "");
                    logs.add(new Logs(Long.valueOf(timestamp != null ? timestamp : "0"), log));
                }
            }
        });
    }


    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Device getNewDevice() {
        return newDevice;
    }

    public void setNewDevice(Device newDevice) {
        this.newDevice = newDevice;
    }

    public void nullifyNewDevice() {
        this.newDevice = null;
    }

    public boolean isLogsHasChanges() {
        return logsHasChanges;
    }

    public void setLogsHasChanges(boolean logsHasChanges) {
        this.logsHasChanges = logsHasChanges;
    }

    public boolean isDevicesHasChanges() {
        return devicesHasChanges;
    }

    public void setDevicesHasChanges(boolean devicesHasChanges) {
        this.devicesHasChanges = devicesHasChanges;
    }

    public Integer getNotificationID() {
        return notificationID++;
    }

    public void setNotificationID(Integer notificationID) {
        this.notificationID = notificationID;
    }

    public void UpdateDataRetrieved() {
        fd.retrieveData(this, "Users/" + userID + "/Devices", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                setReferenceDevices(getDevices());
                devicesHasChanges = false;
                setDevices(new ArrayList<>());
                for (DataSnapshot devices : dataSnapshot.getChildren()) {
                    String key = devices.getKey();
                    fd.retrieveData(Kangai.this, "Devices/" + key + "/", new FirebaseData.FirebaseDataCallback() {
                        @Override
                        public void onDataReceived(DataSnapshot dataSnapshot) {
                            Object name = dataSnapshot.child("Name").getValue();
                            Object manager = dataSnapshot.child("Manager").getValue();
                            Object reservoir = dataSnapshot.child("Reservoir").getValue();
                            Object lastUpdate = dataSnapshot.child("LastUpdate").getValue();
                            Log.d("UpdateDataRetrieved", "CREATING THE NEW DEVICE OBJECT");
                            Device reference = null;
                            for (Device device : getReferenceDevices()) {
                                if (device.getId().equals(key)) {
                                    if (Objects.equals(String.valueOf(device.getLastUpdate()), lastUpdate.toString())) {
                                        addDevice(device);
                                        return;
                                    }
                                    reference = device;
                                    break;
                                }
                            }
                            DataSnapshot Slot1 = dataSnapshot.child("Plants/Slot1");
                            Plants plant1 = new Plants(null, null, null, null, null);
                            if (Slot1.getValue() != null) {
                                plant1 = new Plants(1,
                                        Slot1.child("Name").getValue().toString(),
                                        Slot1.child("Status").getValue().toString(),
                                        Slot1.child("Value").getValue().toString(),
                                        Slot1.child("LastWatered").getValue().toString());
                                if (reference != null) {
                                    Plants refPlant1 = reference.getPlantSlots().get(0);
                                    if (!(Objects.equals(refPlant1.getStatus(), plant1.getStatus()))) {
                                        HashMap<String, Object> value = new HashMap<>();
                                        //TODO: REMOVE THIS LOG UPDATE ONCE NODEMCU IS FIXED
                                        value.put(String.valueOf(System.currentTimeMillis()),
                                                String.format("%s's Plant 1 state changed to %s.",
                                                        name.toString(), plant1.getStatus()));
                                        fd.addValues("Users/" + userID + "/Settings/Logs", value);
                                        showNotification(getNotificationID(),
                                                stateChange("Slot 1"),
                                                String.format("%s's Plant 1 state changed to %s.",
                                                        name, plant1.getStatus()));
                                        devicesHasChanges = true;
                                    }
                                }
                            }
                            DataSnapshot Slot2 = dataSnapshot.child("Plants/Slot2");
                            Plants plant2 = new Plants(null, null, null, null, null);
                            if (Slot2.getValue() != null) {
                                plant2 = new Plants(2,
                                        Slot2.child("Name").getValue().toString(),
                                        Slot2.child("Status").getValue().toString(),
                                        Slot2.child("Value").getValue().toString(),
                                        Slot2.child("LastWatered").getValue().toString());
                                if (reference != null) {
                                    Plants refPlant = reference.getPlantSlots().get(1);
                                    if (!(Objects.equals(refPlant.getStatus(), plant2.getStatus()))) {
                                        HashMap<String, Object> value = new HashMap<>();
                                        value.put(String.valueOf(System.currentTimeMillis()),
                                                String.format("%s's Plant 2 state changed to %s.",
                                                        name.toString(), plant2.getStatus()));
                                        fd.addValues("Users/" + userID + "/Settings/Logs", value);
                                        showNotification(getNotificationID(),
                                                stateChange("Slot 2"),
                                                String.format("%s's Plant 2 state changed to %s.",
                                                        name, plant2.getStatus()));
                                        devicesHasChanges = true;
                                    }
                                }
                            }
                            DataSnapshot Slot3 = dataSnapshot.child("Plants/Slot3");
                            Plants plant3 = new Plants(null, null, null, null, null);
                            if (Slot3.getValue() != null) {
                                plant3 = new Plants(3,
                                        Slot3.child("Name").getValue().toString(),
                                        Slot3.child("Status").getValue().toString(),
                                        Slot3.child("Value").getValue().toString(),
                                        Slot3.child("LastWatered").getValue().toString());
                                if (reference != null) {
                                    Plants refPlant = reference.getPlantSlots().get(2);
                                    if (!(Objects.equals(refPlant.getStatus(), plant3.getStatus()))) {
                                        HashMap<String, Object> value = new HashMap<>();
                                        value.put(String.valueOf(System.currentTimeMillis()),
                                                String.format("%s's Plant 3 state changed to %s.",
                                                        name.toString(), plant3.getStatus()));
                                        fd.addValues("Users/" + userID + "/Settings/Logs", value);
                                        showNotification(getNotificationID(),
                                                stateChange("Slot 3"),
                                                String.format("%s's Plant 3 state changed to %s.",
                                                        name, plant3.getStatus()));
                                        devicesHasChanges = true;
                                    }
                                }
                            }
                            DataSnapshot Slot4 = dataSnapshot.child("Plants/Slot4");
                            Plants plant4 = new Plants(null, null, null, null, null);
                            if (Slot4.getValue() != null) {
                                plant4 = new Plants(4,
                                        Slot4.child("Name").getValue().toString(),
                                        Slot4.child("Status").getValue().toString(),
                                        Slot4.child("Value").getValue().toString(),
                                        Slot4.child("LastWatered").getValue().toString());
                                if (reference != null) {
                                    Plants refPlant = reference.getPlantSlots().get(3);
                                    if (!(Objects.equals(refPlant.getStatus(), plant4.getStatus()))) {
                                        HashMap<String, Object> value = new HashMap<>();
                                        value.put(String.valueOf(System.currentTimeMillis()),
                                                String.format("%s's Plant 4 state changed to %s.",
                                                        name.toString(), plant4.getStatus()));
                                        fd.addValues("Users/" + userID + "/Settings/Logs", value);
                                        showNotification(getNotificationID(),
                                                stateChange("Slot 4"),
                                                String.format("%s's Plant 4 state changed to %s.",
                                                        name, plant4.getStatus()));
                                        devicesHasChanges = true;
                                    }
                                }
                            }
                            Plants finalPlant1 = plant1;
                            Plants finalPlant2 = plant2;
                            Plants finalPlant3 = plant3;
                            Plants finalPlant4 = plant4;
                            Device device = new Device(key,
                                    manager != null ? manager.toString() : null,
                                    name != null ? name.toString() : null,
                                    new ArrayList<Plants>() {{
                                        add(finalPlant1);
                                        add(finalPlant2);
                                        add(finalPlant3);
                                        add(finalPlant4);
                                    }},
                                    Long.valueOf(reservoir != null ? reservoir.toString() : "0"),
                                    Long.valueOf(lastUpdate != null ? lastUpdate.toString() : "0"));
                            addDevice(device);
                        }
                    });
                }

            }
        });
        fd.retrieveData(this, "Users/" + userID + "/Settings/Logs/", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                setReferenceLogs(getLogs());
                ArrayList<Long> logKeys = new ArrayList<>();
                for (Logs log : referenceLogs){
                    logKeys.add(log.getTimestamp());
                }
                logsHasChanges = false;
                for (DataSnapshot logDS : dataSnapshot.getChildren()) {
                    String timestamp = logDS.getKey();
                    if (logKeys.contains(Long.valueOf(timestamp))) continue;
                    String log = String.valueOf(logDS.getValue() != null ? logDS.getValue() : "");
                    addLogs(new Logs(Long.valueOf(timestamp != null ? timestamp : "0"), log));
                    logsHasChanges = true;
                }
            }
        });
    }

    public NotificationCompat.Builder buildNotification(String ChannelID, String title, String content) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, ChannelID)
                .setSmallIcon(R.drawable.kangai_bw)
                .setContentTitle(title)
                .setContentText(content)
                .setLargeIcon(
                        BitmapFactory.decodeResource(this.getResources(), R.drawable.kangai_colored))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroup("KANGAI_NOTIFICATIONS")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }

    public void showNotification(Integer notificationID, String title, String content) {
        NotificationCompat.Builder builder = buildNotification(PRIMARY_CHANNEL_ID, title, content);
        mNotifyManager.notify(notificationID, builder.build());
    }

    public void createNotificationChannel(){
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    public static class NotificationID{
        static String TITLE_STATE_CHANGE = "State Change";
        public static String stateChange(String slot){
            return String.format("%s %s", slot, TITLE_STATE_CHANGE);
        }
    }


}
