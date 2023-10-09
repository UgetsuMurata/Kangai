package com.example.kangai.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kangai.Accounts.EditAccount;
import com.example.kangai.Accounts.SignIn;
import com.example.kangai.Dashboard.AddDevice;
import com.example.kangai.R;
import com.example.kangai.Settings.Settings;

public class ToolbarMenu {
    public static Boolean ToolbarOption(Context context, MenuItem item){
        switch (item.getItemId()) {
            case R.id.add_device:
                context.startActivity(new Intent(context, AddDevice.class));
                return true;
            case R.id.edit_acct:
                context.startActivity(new Intent(context, EditAccount.class));
                return true;
            case R.id.settings:
                context.startActivity(new Intent(context, Settings.class));
                return true;
            case R.id.sign_out:
                LocalStorageHelper.signOut(context);
                context.startActivity(new Intent(context, SignIn.class));
                Activity activity = (Activity) context;
                activity.finish();
                return true;
            default:
                return false;
        }
    }
}
