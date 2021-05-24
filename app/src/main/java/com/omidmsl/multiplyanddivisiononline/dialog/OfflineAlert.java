package com.omidmsl.multiplyanddivisiononline.dialog;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import com.omidmsl.multiplyanddivisiononline.Connection;
import com.omidmsl.multiplyanddivisiononline.R;


public class OfflineAlert {

    public OfflineAlert(final Context context) {
        final Dialog mDialog = new Dialog(context);
        // no tile for the dialog
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_offline_alert);
        Button wifiButton = mDialog.findViewById(R.id.button_turn_on_wifi);
        wifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                context.startActivity(intent);
                mDialog.dismiss();
            }
        });
        Button dataButton = mDialog.findViewById(R.id.button_turn_on_data);
        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName("com.android.settings",
                        "com.android.settings.Settings$DataUsageSummaryActivity"));
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                mDialog.dismiss();
            }
        });
        // you can change or add this line according to your need
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_shape);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();

        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!Connection.isOnline(context))
                    new OfflineAlert(context);
            }
        });
    }


}
