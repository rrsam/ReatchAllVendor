package com.reatchall.charan.reatchallVendor.Utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by NaNi on 30/03/18.
 */

public class CustomProgressDialog {

    Context context;
    ProgressDialog dialog;

    public CustomProgressDialog(Context context) {
        this.context = context;
    }

    public  void showDialog(){

        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("please wait...");
        dialog.show();
    }



    public void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
