package com.omidmsl.multiplyanddivisiononline.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.omidmsl.multiplyanddivisiononline.R;

public class Progress {

    public static Progress progress = null;
    private Dialog mDialog;

    public static Progress getInstance() {
        if (progress == null) {
            progress = new Progress();
        }
        return progress;
    }

    public void showProgress(Context context, String message, boolean cancelable) {
        mDialog = new Dialog(context);
        // no tile for the dialog
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_progress_bar);
        ProgressBar mProgressBar = mDialog.findViewById(R.id.progressBar2);
        //  mProgressBar.getIndeterminateDrawable().setColorFilter(context.getResources()
        // .getColor(R.color.material_blue_gray_500), PorterDuff.Mode.SRC_IN);
        TextView progressText = mDialog.findViewById(R.id.textViewProgressDialog);
        progressText.setText(message);
        progressText.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        // you can change or add this line according to your need
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_shape);
        mProgressBar.setIndeterminate(true);
        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();
    }

    public void hideProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
