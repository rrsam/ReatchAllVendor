package com.reatchall.charan.reatchallVendor.Vendor.CreateBusiness;

import android.app.Dialog;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.reatchall.charan.reatchallVendor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class TestTimingsActivity extends AppCompatActivity {

    private static final String TAG = "TestTimingsActivity";

    int openAllNew,closeAllNew;
    boolean workingMon=false, workingTue=false, workingWed=false, workingThur=false, workingfri=false,
            workingSat=false, workingSun=false;
    FontButton submit;

    FontTextView allCheckTv;
    ImageView allCheckIV,monCheckIV,tueCheckIV,wedCheckIV,thurCheckIV,friCheckIV,satCheckIV,sunCheckIV;
    ImageView allUncheckIV,monUnCheckIV,tueUnCheckIV,wedUnCheckIV,thurUnCheckIV,friUnCheckIV,satUnCheckIV,sunUnCheckIV;

    Spinner monOpenSpinner,tueOpenSpinner,wedOpenSpinner,thurOpenSpinner,friOpenSpinner,satOpenSpinner,sunOpenSpinner;
    Spinner monCloseSpinner,tueCloseSpinner,wedCloseSpinner,thurCloseSpinner,friCloseSpinner,satCloseSpinner,sunCloseSpinner;

    List<String> plansList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_timings);


        plansList = Arrays.asList(getResources().getStringArray(R.array.timings_array));
        initWidgetsTimings();
        submit=(FontButton)findViewById(R.id.submit);
        setupTimingsClickListeners();
    }

    private void setupTimingsClickListeners(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieveTimesNew();
                if(workingMon || workingTue || workingWed || workingThur || workingfri || workingSat || workingSun)
                    formJsonTimings();
                else{
                    Toast.makeText(TestTimingsActivity.this,"Atleast 1 day should be a working day",Toast.LENGTH_LONG).show();
                }
            }
        });

        allUncheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllDayTimingsNew();
                checkAllIV();
            }
        });

        allCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckAllIV();
            }
        });

        monUnCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                individualUncheckListener(monUnCheckIV,monCheckIV);

            }
        });

        monCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allUncheckIV.setVisibility(View.VISIBLE);
                allCheckIV.setVisibility(View.GONE);
                individualCheckListener(monUnCheckIV,monCheckIV);
                monOpenSpinner.setSelection(0);
                monCloseSpinner.setSelection(0);
            }
        });

        tueUnCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                individualUncheckListener(tueUnCheckIV,tueCheckIV);

            }
        });

        tueCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allUncheckIV.setVisibility(View.VISIBLE);
                allCheckIV.setVisibility(View.GONE);
                individualCheckListener(tueUnCheckIV,tueCheckIV);
                monOpenSpinner.setSelection(0);
                monCloseSpinner.setSelection(0);
            }
        });

        wedUnCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                individualUncheckListener(wedUnCheckIV,wedCheckIV);

            }
        });

        wedCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allUncheckIV.setVisibility(View.VISIBLE);
                allCheckIV.setVisibility(View.GONE);
                individualCheckListener(wedUnCheckIV,wedCheckIV);
                monOpenSpinner.setSelection(0);
                monCloseSpinner.setSelection(0);
            }
        });

        thurUnCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                individualUncheckListener(thurUnCheckIV,thurCheckIV);

            }
        });

        thurCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allUncheckIV.setVisibility(View.VISIBLE);
                allCheckIV.setVisibility(View.GONE);
                individualCheckListener(thurUnCheckIV,thurCheckIV);
                monOpenSpinner.setSelection(0);
                monCloseSpinner.setSelection(0);
            }
        });

        friUnCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                individualUncheckListener(friUnCheckIV,friCheckIV);

            }
        });

        friCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allUncheckIV.setVisibility(View.VISIBLE);
                allCheckIV.setVisibility(View.GONE);
                individualCheckListener(friUnCheckIV,friCheckIV);
                monOpenSpinner.setSelection(0);
                monCloseSpinner.setSelection(0);
            }
        });

        satUnCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                individualUncheckListener(satUnCheckIV,satCheckIV);

            }
        });

        satCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allUncheckIV.setVisibility(View.VISIBLE);
                allCheckIV.setVisibility(View.GONE);
                individualCheckListener(satUnCheckIV,satCheckIV);
                monOpenSpinner.setSelection(0);
                monCloseSpinner.setSelection(0);
            }
        });

        sunUnCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                individualUncheckListener(sunUnCheckIV,sunCheckIV);

            }
        });

        sunCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allUncheckIV.setVisibility(View.VISIBLE);
                allCheckIV.setVisibility(View.GONE);
                individualCheckListener(sunUnCheckIV,sunCheckIV);
                monOpenSpinner.setSelection(0);
                monCloseSpinner.setSelection(0);
            }
        });
    }

    private void individualUncheckListener(ImageView unCheck, ImageView Check){
        Check.setVisibility(View.VISIBLE);
        unCheck.setVisibility(View.GONE);
        checkAllVisible();
    }

    private void individualCheckListener(ImageView unCheck, ImageView Check){
        Check.setVisibility(View.GONE);
        unCheck.setVisibility(View.VISIBLE);
    }

    private void checkAllVisible(){
        if(monCheckIV.getVisibility()==View.VISIBLE &&
                tueCheckIV.getVisibility()==View.VISIBLE && friCheckIV.getVisibility()==View.VISIBLE &&
                wedCheckIV.getVisibility()==View.VISIBLE && satCheckIV.getVisibility()==View.VISIBLE &&
                thurCheckIV.getVisibility()==View.VISIBLE && sunCheckIV.getVisibility()==View.VISIBLE ){

            allUncheckIV.setVisibility(View.GONE);
            allCheckIV.setVisibility(View.VISIBLE);
        }else{
            allUncheckIV.setVisibility(View.VISIBLE);
            allCheckIV.setVisibility(View.GONE);
        }
    }

    private void checkAllIV(){
        allUncheckIV.setVisibility(View.GONE);
        monUnCheckIV.setVisibility(View.GONE);
        tueUnCheckIV.setVisibility(View.GONE);
        wedUnCheckIV.setVisibility(View.GONE);
        thurUnCheckIV.setVisibility(View.GONE);
        friUnCheckIV.setVisibility(View.GONE);
        satUnCheckIV.setVisibility(View.GONE);
        sunUnCheckIV.setVisibility(View.GONE);

        allCheckIV.setVisibility(View.VISIBLE);
        monCheckIV.setVisibility(View.VISIBLE);
        tueCheckIV.setVisibility(View.VISIBLE);
        wedCheckIV.setVisibility(View.VISIBLE);
        thurCheckIV.setVisibility(View.VISIBLE);
        friCheckIV.setVisibility(View.VISIBLE);
        satCheckIV.setVisibility(View.VISIBLE);
        sunCheckIV.setVisibility(View.VISIBLE);
    }

    private void uncheckAllIV(){
        allUncheckIV.setVisibility(View.VISIBLE);
        monUnCheckIV.setVisibility(View.VISIBLE);
        tueUnCheckIV.setVisibility(View.VISIBLE);
        wedUnCheckIV.setVisibility(View.VISIBLE);
        thurUnCheckIV.setVisibility(View.VISIBLE);
        friUnCheckIV.setVisibility(View.VISIBLE);
        satUnCheckIV.setVisibility(View.VISIBLE);
        sunUnCheckIV.setVisibility(View.VISIBLE);

        allCheckIV.setVisibility(View.GONE);
        monCheckIV.setVisibility(View.GONE);
        tueCheckIV.setVisibility(View.GONE);
        wedCheckIV.setVisibility(View.GONE);
        thurCheckIV.setVisibility(View.GONE);
        friCheckIV.setVisibility(View.GONE);
        satCheckIV.setVisibility(View.GONE);
        sunCheckIV.setVisibility(View.GONE);


        monOpenSpinner.setSelection(0);
        tueOpenSpinner.setSelection(0);
        wedOpenSpinner.setSelection(0);
        thurOpenSpinner.setSelection(0);
        friOpenSpinner.setSelection(0);
        satOpenSpinner.setSelection(0);
        sunOpenSpinner.setSelection(0);

        monCloseSpinner.setSelection(0);
        tueCloseSpinner.setSelection(0);
        wedCloseSpinner.setSelection(0);
        thurCloseSpinner.setSelection(0);
        friCloseSpinner.setSelection(0);
        satCloseSpinner.setSelection(0);
        sunCloseSpinner.setSelection(0);

    }

    private void initWidgetsTimings(){
        allCheckTv=(FontTextView)findViewById(R.id.allCheckTV);

        allCheckIV=(ImageView)findViewById(R.id.allcheckIV);
        allUncheckIV=(ImageView)findViewById(R.id.allUncheckIV);

        monCheckIV=(ImageView)findViewById(R.id.moncheckIV);
        tueCheckIV=(ImageView)findViewById(R.id.tuecheckIV);
        wedCheckIV=(ImageView)findViewById(R.id.wedcheckIV);
        thurCheckIV=(ImageView)findViewById(R.id.thurcheckIV);
        friCheckIV=(ImageView)findViewById(R.id.fricheckIV);
        satCheckIV=(ImageView)findViewById(R.id.satcheckIV);
        sunCheckIV=(ImageView)findViewById(R.id.suncheckIV);

        monUnCheckIV=(ImageView)findViewById(R.id.monUncheckIV);
        tueUnCheckIV=(ImageView)findViewById(R.id.tueUncheckIV);
        wedUnCheckIV=(ImageView)findViewById(R.id.wedUncheckIV);
        thurUnCheckIV=(ImageView)findViewById(R.id.thurUncheckIV);
        friUnCheckIV=(ImageView)findViewById(R.id.friUncheckIV);
        satUnCheckIV=(ImageView)findViewById(R.id.satUncheckIV);
        sunUnCheckIV=(ImageView)findViewById(R.id.sunUncheckIV);

        monOpenSpinner=(Spinner)findViewById(R.id.monOpenSpinner);
        tueOpenSpinner=(Spinner)findViewById(R.id.tueOpenSpinner);
        wedOpenSpinner=(Spinner)findViewById(R.id.wedOpenSpinner);
        thurOpenSpinner=(Spinner)findViewById(R.id.thurOpenSpinner);
        friOpenSpinner=(Spinner)findViewById(R.id.friOpenSpinner);
        satOpenSpinner=(Spinner)findViewById(R.id.satOpenSpinner);
        sunOpenSpinner=(Spinner)findViewById(R.id.sunOpenSpinner);

        monCloseSpinner=(Spinner)findViewById(R.id.monCloseSpinner);
        tueCloseSpinner=(Spinner)findViewById(R.id.tueCloseSpinner);
        wedCloseSpinner=(Spinner)findViewById(R.id.wedCloseSpinner);
        thurCloseSpinner=(Spinner)findViewById(R.id.thurCloseSpinner);
        friCloseSpinner=(Spinner)findViewById(R.id.friCloseSpinner);
        satCloseSpinner=(Spinner)findViewById(R.id.satCloseSpinner);
        sunCloseSpinner=(Spinner)findViewById(R.id.sunCloseSpinner);

        initiateSpinners(monOpenSpinner);
        initiateSpinners(tueOpenSpinner);
        initiateSpinners(wedOpenSpinner);
        initiateSpinners(thurOpenSpinner);
        initiateSpinners(friOpenSpinner);
        initiateSpinners(satOpenSpinner);
        initiateSpinners(sunOpenSpinner);

        initiateSpinners(monCloseSpinner);
        initiateSpinners(tueCloseSpinner);
        initiateSpinners(wedCloseSpinner);
        initiateSpinners(thurCloseSpinner);
        initiateSpinners(friCloseSpinner);
        initiateSpinners(satCloseSpinner);
        initiateSpinners(sunCloseSpinner);

    }

    private void initiateSpinners(Spinner spinner){

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, plansList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                Typeface externalFont= Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(12);


                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(12);

                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void formJsonTimings(){
        try {
            JSONObject business = new JSONObject();
            JSONObject timings = new JSONObject();
            JSONObject days = new JSONObject();
            if (workingSun) {
                JSONObject sunTimings = new JSONObject();
                sunTimings.put("closing", sunCloseSpinner.getSelectedItem().toString());
                sunTimings.put("opening", sunOpenSpinner.getSelectedItem().toString());
                timings.put("sunday", sunTimings);
                days.put("sunday", workingSun);
            } else {
                days.put("sunday", false);
            }
            if (workingSat) {
                JSONObject satTimings = new JSONObject();
                satTimings.put("closing", satCloseSpinner.getSelectedItem().toString());
                satTimings.put("opening", satOpenSpinner.getSelectedItem().toString());
                timings.put("saturday", satTimings);
                days.put("saturday", workingSat);
            } else {
                days.put("saturday", false);
            }
            if (workingfri) {
                JSONObject friTimings = new JSONObject();
                friTimings.put("closing", friCloseSpinner.getSelectedItem().toString());
                friTimings.put("opening", friOpenSpinner.getSelectedItem().toString());
                timings.put("friday", friTimings);
                days.put("friday", workingfri);
            } else {
                days.put("friday", false);
            }
            if (workingThur) {
                JSONObject thurTimings = new JSONObject();
                thurTimings.put("closing", thurCloseSpinner.getSelectedItem().toString());
                thurTimings.put("opening", thurOpenSpinner.getSelectedItem().toString());
                timings.put("thursday", thurTimings);
                days.put("thursday", workingThur);
            } else {
                days.put("thursday", false);
            }
            if (workingWed) {
                JSONObject wedTimings = new JSONObject();
                wedTimings.put("closing", wedCloseSpinner.getSelectedItem().toString());
                wedTimings.put("opening", wedOpenSpinner.getSelectedItem().toString());
                timings.put("wednesday", wedTimings);
                days.put("wednesday", workingWed);
            } else {
                days.put("wednesday", false);
            }
            if (workingTue) {
                JSONObject tueTimings = new JSONObject();
                tueTimings.put("closing", tueCloseSpinner.getSelectedItem().toString());
                tueTimings.put("opening", tueOpenSpinner.getSelectedItem().toString());
                timings.put("tuesday", tueTimings);
                days.put("tuesday", workingTue);
            } else {
                days.put("tuesday", false);
            }
            if (workingMon) {
                JSONObject monTimings = new JSONObject();
                monTimings.put("closing", monCloseSpinner.getSelectedItem().toString());
                monTimings.put("opening", monOpenSpinner.getSelectedItem().toString());
                timings.put("monday", monTimings);
                days.put("monday", workingMon);
            } else {
                days.put("monday", false);
            }
            business.put("timings", timings);
            business.put("days", days);

            Log.e(TAG, "formJsonTimings: "+business.toString() );

        }catch (JSONException e){

        }

    }

    private void retrieveTimesNew(){

        if(allCheckIV.getVisibility()==View.VISIBLE){

            workingMon=true;
            workingTue=true;
            workingWed=true;
            workingThur=true;
            workingfri=true;
            workingSat=true;
            workingSun=true;

        }else{

            if(monCheckIV.getVisibility()==View.VISIBLE){
                workingMon=true;

            } else{
                workingMon=false;
            }

            if(tueCheckIV.getVisibility()==View.VISIBLE){
                workingTue=true;

            } else{
                workingTue=false;
            }

            if(wedCheckIV.getVisibility()==View.VISIBLE){
                workingWed=true;

            } else{
                workingWed=false;
            }

            if(thurCheckIV.getVisibility()==View.VISIBLE){
                workingThur=true;

            } else{
                workingThur=false;
            }

            if(friCheckIV.getVisibility()==View.VISIBLE){
                workingfri=true;

            } else{
                workingfri=false;
            }

            if(satCheckIV.getVisibility()==View.VISIBLE){
                workingSat=true;

            } else{
                workingSat=false;
            }

            if(sunCheckIV.getVisibility()==View.VISIBLE){
                workingSun =true;

            } else{
                workingSun=false;
            }

        }


    }

    private void setAllDayTimingsNew(){
        final Dialog dialog = new Dialog(TestTimingsActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_timings);
        dialog.setCancelable(false);
        dialog.show();

        final Spinner fromSpinner =(Spinner)dialog.findViewById(R.id.selectFrom);
        final Spinner toSpinner =(Spinner)dialog.findViewById(R.id.selectTo);
        final FontTextView okButton=(FontTextView)dialog.findViewById(R.id.okPopUp);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                monOpenSpinner.setSelection(openAllNew);
                tueOpenSpinner.setSelection(openAllNew);
                wedOpenSpinner.setSelection(openAllNew);
                thurOpenSpinner.setSelection(openAllNew);
                friOpenSpinner.setSelection(openAllNew);
                satOpenSpinner.setSelection(openAllNew);
                sunOpenSpinner.setSelection(openAllNew);

                monCloseSpinner.setSelection(closeAllNew);
                tueCloseSpinner.setSelection(closeAllNew);
                wedCloseSpinner.setSelection(closeAllNew);
                thurCloseSpinner.setSelection(closeAllNew);
                friCloseSpinner.setSelection(closeAllNew);
                satCloseSpinner.setSelection(closeAllNew);
                sunCloseSpinner.setSelection(closeAllNew);

                dialog.dismiss();
            }
        });
        final List<String> timingsList = Arrays.asList(getResources().getStringArray(R.array.timings_array));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(TestTimingsActivity.this, android.R.layout.simple_spinner_item, timingsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> dataAdapterr = new ArrayAdapter<String>(TestTimingsActivity.this, android.R.layout.simple_spinner_item, timingsList);
        dataAdapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(dataAdapter);
        toSpinner.setAdapter(dataAdapterr);

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                openAllNew=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                openAllNew=0;

                Toast.makeText(TestTimingsActivity.this,"PLEASE SELECT OPENING TIME",Toast.LENGTH_LONG).show();

            }
        });

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                closeAllNew=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                closeAllNew=0;

                Toast.makeText(TestTimingsActivity.this,"PLEASE SELECT CLOSING TIME",Toast.LENGTH_LONG).show();

            }
        });
    }
}
