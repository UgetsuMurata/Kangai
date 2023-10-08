package com.example.kangai.DEVELOPER;

import com.example.kangai.Firebase.FirebaseData;

import java.util.HashMap;

public class FIREBASE_ {
    private static String generateNewID(){
        return String.valueOf((int)(Math.random() * (999999999 - 100000000)) + 100000000);
    }
    public static void addDevice(){
        String id = generateNewID();
        String path = "Devices/"+id+"/";
        HashMap<String, Object> devices = new HashMap<>();
        devices.put("AppCommand", "NULL");
        devices.put("LastUpdate", 0);
        devices.put("Manager", "NULL");
        devices.put("Name", "Device "+id);
        devices.put("Reservoir", 0);
        FirebaseData fd = new FirebaseData();
        fd.addValues(path, devices);

        String path1 = path + "Plants/Slot1";
        String path2 = path + "Plants/Slot2";
        String path3 = path + "Plants/Slot3";
        String path4 = path + "Plants/Slot4";

        devices.clear();
        devices.put("LastWatered", 0);
        devices.put("Name", "Slot1");
        devices.put("Status", "NULL");
        devices.put("Value", 0);

        fd.addValues(path1, devices);
        fd.addValues(path2, devices);
        fd.addValues(path3, devices);
        fd.addValues(path4, devices);
    }
}
