package com.reatchall.charan.reatchallVendor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by NaNi on 20/02/18.
 */

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String TAG = "NotificationsActivity";
    ImageView backArrow;
    Button saveChanges,cancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        saveChanges=(Button)findViewById(R.id.saveChanges);
        cancel=(Button)findViewById(R.id.cancel);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}
