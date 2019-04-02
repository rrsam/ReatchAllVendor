package com.reatchall.charan.reatchallVendor.Vendor;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.reatchall.charan.reatchallVendor.R;

import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class ManagerPasswordDialog extends Dialog {

    private String message;
    private String title;
    private String btYesText;
    private String btNoText;
    private int icon=0;
    private View.OnClickListener btYesListener=null;
    private View.OnClickListener btNoListener=null;

    FontEditText password;
    public ManagerPasswordDialog(Context context) {
        super(context);
    }

    public ManagerPasswordDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ManagerPasswordDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_manager_password);
         password = (FontEditText)findViewById(R.id.password);
        FontTextView submit =(FontTextView)findViewById(R.id.submit);
        submit.setOnClickListener(btYesListener);
    }

    public String getPassword(){
     return password.getText().toString();
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
