package com.reatchall.charan.reatchallVendor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by NaNi on 20/02/18.
 */

public class AddNewAddressActivity extends AppCompatActivity {

    private static final String TAG = "AddNewAddressActivity";
    ImageView backArrow;
    Button saveAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_new_address);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        saveAddress=(Button)findViewById(R.id.saveAddress);
        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
