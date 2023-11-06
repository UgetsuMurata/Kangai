package com.example.kangai.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kangai.Accounts.EditAccount;
import com.example.kangai.Accounts.SignIn;
import com.example.kangai.Application.Kangai;
import com.example.kangai.Dashboard.AddDevice;
import com.example.kangai.Dashboard.Dashboard;
import com.example.kangai.Firebase.FirebaseData;
import com.example.kangai.Objects.Device;
import com.example.kangai.R;
import com.example.kangai.Settings.Settings;

public class ToolbarMenu {
    public static Boolean ToolbarOption(Context context, MenuItem item){
        Activity activity = (Activity) context;
        switch (item.getItemId()) {
            case R.id.edit_acct:
                context.startActivity(new Intent(context, EditAccount.class));
                return true;
            case R.id.sign_out:
                LocalStorageHelper.signOut(context);
                context.startActivity(new Intent(context, SignIn.class));
                activity.finish();
                return true;
            case R.id.delete:
                Device device = Kangai.getInstance().getDevice();
                Kangai.getInstance().getDevices().remove(Kangai.getInstance().getDevice());
                Kangai.getInstance().setDevice(null);
                FirebaseData fd = new FirebaseData();
                fd.removeData("Users/"+Kangai.getInstance().getUserID()+"/Devices/"+device.getId());
                fd.updateValue("Devices/"+device.getId()+"/Manager", "NULL");
                Kangai.getInstance().showNotification(
                        Kangai.getInstance().getNotificationID(),
                        "Device deletion",
                        String.format("Deleted device %s from this account.", device.getName()));
                context.startActivity(new Intent(context, Dashboard.class));
                activity.finish();
            default:
                return false;
        }
    }
}
