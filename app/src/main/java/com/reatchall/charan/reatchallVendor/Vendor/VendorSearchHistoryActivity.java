package com.reatchall.charan.reatchallVendor.Vendor;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDashboard;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorSearchHistoryActivity extends AppCompatActivity {

    private static final String TAG = "VendorSearchHistoryActi";

    ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDashboard businessDashboard;

    RelativeLayout dateSearchLayout;
    FontEditText dateSearchHistoryET;
    FontTextView dateSearchTV,dateSearchValue;
    ImageView calendarIV;


    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_search_history);

        backArrow = (ImageView) findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        titleToolbar = (FontTextView) findViewById(R.id.title_toolbar);
        businessDashboard = getIntent().getExtras().getParcelable("businessDetails");
        if (businessDashboard != null) {
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }

        dateSearchHistoryET=(FontEditText) findViewById(R.id.dateSearchHistoryET);
        dateSearchLayout=(RelativeLayout)findViewById(R.id.dateSearchLayout);
        calendarIV=(ImageView)findViewById(R.id.calendarIV);
        dateSearchTV=(FontTextView)findViewById(R.id.dateSearchTV);
        dateSearchValue=(FontTextView)findViewById(R.id.dateSearchValue);

        dateSearchValue.setVisibility(View.GONE);
        dateSearchTV.setVisibility(View.GONE);

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateSearchHistoryET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(VendorSearchHistoryActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


//        year = sdf.format(myCalendar.getTime());
//        year=year.substring(6,year.length());
        //Log.e(TAG, "updateLabel: YEAR"+year );
       // long yr = Long.parseLong(year);

            dateSearchHistoryET.setText(sdf.format(myCalendar.getTime()));

        dateSearchValue.setVisibility(View.VISIBLE);
        dateSearchTV.setVisibility(View.VISIBLE);
        dateSearchTV.setText("Searches on"+dateSearchHistoryET.getText().toString());
    }
}
