package com.reatchall.charan.reatchallVendor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.ImageView;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 21/02/18.
 */

public class ViewProductDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ViewProductDetailsActiv";


    FontTextView realPrice;
    ImageView backArrow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        realPrice=(FontTextView)findViewById(R.id.realPrice);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Spannable wordtoSpan = new SpannableString("1000");
        wordtoSpan.setSpan(new StrikethroughSpan(), 0, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        realPrice.setText(wordtoSpan);

    }
}
