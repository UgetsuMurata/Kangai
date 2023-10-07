package com.example.kangai.Helpers;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.example.kangai.Accounts.EditAccount;
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
                // SIGN OUT ACCOUNT, THEN CHANGE VISIBILITY
                return true;
            case R.id.sign_in:
                // SIGN IN ACCOUNT, THEN CHANGE VISIBILITY
                return true;
            default:
                return false;
        }
    }
}
