package com.example.kangai.Application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.kangai.Firebase.FirebaseData;
import com.example.kangai.Helpers.TimeHelper;
import com.example.kangai.MainActivity;
import com.example.kangai.Objects.Device;
import com.example.kangai.Objects.Logs;
import com.example.kangai.Objects.Plants;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Kangai extends Application {

    public static Kangai instance;

    private FirebaseData fd;
    private List<Device> devices;
    private List<Logs> logs;
    public String userID;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        userID = "1696198336724";

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        fd = new FirebaseData();
        devices = new ArrayList<>();
        logs = new ArrayList<>();
    }

    public static Kangai getInstance() {
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    public List<Device> getDevices(){
        return devices;
    }
    public List<Logs> getLogs(){
        return logs;
    }

    public void setDevices(List<Device> devices){
        this.devices = devices;
    }
    public void addDevice(Device device){
        this.devices.add(device);
    }
    public void getAllDevice(Context context){
        if (devices != null) devices.clear();
        else devices = new ArrayList<>();
        fd.retrieveData(this, "Users/"+userID+"/Devices", new FirebaseData.FirebaseDataCallback() {
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
                                    new ArrayList<Plants>(){{add(plant1); add(plant2); add(plant3); add(plant4);}},
                                    Long.getLong(reservoir != null ? reservoir.toString() : "0"),
                                    Long.getLong(lastUpdate != null ? lastUpdate.toString() : "0"));

                            addDevice(device);
                        }
                    });
                }
            }
        });
    }
    public void updateADevice(Context context, String id){
        int devicesID = 0;
        for (Device device : new ArrayList<>(devices)) {
            if (Objects.equals(device.getId(), id)){
                devices.remove(devicesID);
                break;
            }
            devicesID++;
        }
        int finalDevicesID = devicesID;
        fd.retrieveData(context, "Devices/"+id+"/", new FirebaseData.FirebaseDataCallback() {
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
                    new ArrayList<Plants>(){{add(plant1); add(plant2); add(plant3); add(plant4);}},
                    Long.getLong(reservoir != null ? reservoir.toString() : "0"),
                    Long.getLong(lastUpdate != null ? lastUpdate.toString() : "0"));

                devices.add(finalDevicesID, device);
            }
        });
    }

    public void setLogs(List<Logs> logs) {
        this.logs = logs;
    }
    public void addLogs(Logs logs){
        this.logs.add(logs);
    }
    public void getAllLogs(){
        if (logs != null) logs.clear();
        else logs = new ArrayList<>();
        fd.retrieveData(this, "Users/"+userID+"/Settings/Logs/", new FirebaseData.FirebaseDataCallback() {
            @Override
            public void onDataReceived(DataSnapshot dataSnapshot) {
                for (DataSnapshot logDS : dataSnapshot.getChildren()) {
                    String timestamp = logDS.getKey();
                    String log = String.valueOf(logDS.getValue()!=null?logDS.getValue():"");
                    logs.add(new Logs(TimeHelper.millisToReadable(
                            Long.valueOf(timestamp!=null?timestamp:"0")), log));
                }
            }
        });
    }

}
