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
        devices.put("Name", "Kangai V1.0 Irrigation Device "+id);
        devices.put("Reservoir", 0);
        FirebaseData fd = new FirebaseData();
        fd.addValues(path, devices);
    }
}
