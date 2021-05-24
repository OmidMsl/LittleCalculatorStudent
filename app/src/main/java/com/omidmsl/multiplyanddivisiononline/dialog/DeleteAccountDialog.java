package com.omidmsl.multiplyanddivisiononline.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import com.omidmsl.multiplyanddivisiononline.R;

import java.util.Objects;

public class DeleteAccountDialog {

    Context context;
    Dialog mDialog;

    public DeleteAccountDialog(Context context, View.OnClickListener confirm) {
        this.context = context;
        mDialog = new Dialog(context);
        // no tile for the dialog
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_delete_account);

        mDialog.findViewById(R.id.dda_dialog_button_confirm).setOnClickListener(confirm);

        mDialog.findViewById(R.id.dda_dialog_button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        // you can change or add this line according to your need
        Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_shape);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
    }

    public void dismiss(){
        mDialog.dismiss();
    }
}
