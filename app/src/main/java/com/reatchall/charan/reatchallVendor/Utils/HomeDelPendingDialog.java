package com.reatchall.charan.reatchallVendor.Utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.reatchall.charan.reatchallVendor.R;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class HomeDelPendingDialog extends Dialog {

    private String message;
    private String title;
    private String btYesText;
    private String btNoText;
    private int icon=0;
    private View.OnClickListener btYesListener=null;
    private View.OnClickListener btNoListener=null;

    public HomeDelPendingDialog(Context context) {
        super(context);
    }

    public HomeDelPendingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected HomeDelPendingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirmation);
        FontTextView tv = (FontTextView) findViewById(R.id.malertTitle);
        tv.setText(getTitle());
        FontTextView tvmessage = (FontTextView) findViewById(R.id.aleartMessage);
        tvmessage.setText(getMessage());
        FontTextView btYes = (FontTextView) findViewById(R.id.aleartYes);
        FontTextView btNo = (FontTextView) findViewById(R.id.aleartNo);
        btNo.setVisibility(View.GONE);
        btYes.setText(btYesText);
        btNo.setText(btNoText);
        btYes.setOnClickListener(btYesListener);
        btNo.setOnClickListener(btNoListener);

    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public void setPositveButton(String yes, View.OnClickListener onClickListener) {
        dismiss();
        this.btYesText = yes;
        this.btYesListener = onClickListener;


    }

    public void setNegativeButton(String no, View.OnClickListener onClickListener) {
        dismiss();
        this.btNoText = no;
        this.btNoListener = onClickListener;


    }
}
