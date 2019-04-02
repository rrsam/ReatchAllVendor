package com.reatchall.charan.reatchallVendor.Vendor.Offers;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Utils.RetainableDialogFragment;
import com.reatchall.charan.reatchallVendor.Utils.StringConstants;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.OfferItemsReviewAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.ViewOfferSelectedItemsAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ReviewItemOffer;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class ReviewOfferItemsPopupFragment extends RetainableDialogFragment {

    LinearLayout searchBottomSheet;

    BottomSheetBehavior bottomSheetBehavior;
    RecyclerView multipleRestaurantsRcv;
    ArrayList<ReviewItemOffer> multipleRestaurantsPopupList;
    OfferItemsReviewAdapter multipleRestaurantsPopupAdapter;
    View touch_outside;

    ReatchAll helper = ReatchAll.getInstance();
    PrefManager prefManager;
    boolean neg = false;
    boolean clearance = false;
    FontTextView edit,confirm;

    Context context;

    /*public ReviewOfferItemsPopupFragment(Context context) {
        this.context = context;
    }*/
    LinearLayout header;



    public ReviewOfferItemsPopupFragment() {

    }

    public static interface ReviewListener {
         void onEdit();
         void onConfirm();
    }

    private ReviewListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (ReviewListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_offer_items_popup, new LinearLayout(getActivity()), false);
        setRetainInstance(true);
        multipleRestaurantsPopupList = new ArrayList<>();
        multipleRestaurantsPopupList = getArguments().getParcelableArrayList(StringConstants.OFFER_ITEMS_ARRAY);
        neg = getArguments().getBoolean("NEG");
        clearance = getArguments().getBoolean("CLR");

       /* multipleRestaurantsPopupList = new ArrayList<>();
        for(int i =0;i<10;i++){
            multipleRestaurantsPopupList.add("");
        }*/
        prefManager = new PrefManager(getActivity());

        searchBottomSheet=(LinearLayout)view.findViewById(R.id.searchBottomSheet);
        header=(LinearLayout)view.findViewById(R.id.header);
        touch_outside=(View)view.findViewById(R.id.touch_outside);
        touch_outside.setVisibility(View.GONE);
        multipleRestaurantsRcv=(RecyclerView)view.findViewById(R.id.multipleRestaurantsRcv);
        edit = (FontTextView)view. findViewById(R.id.edit);
        confirm = (FontTextView)view. findViewById(R.id.confirm);
        bottomSheetBehavior = BottomSheetBehavior.from(searchBottomSheet);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        if(clearance){
            header.setVisibility(View.GONE);
        }else{
            header.setVisibility(View.VISIBLE);
        }

        touch_outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                touch_outside.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        touch_outside.setVisibility(View.GONE);
                        if(getActivity()!=null){
                            android.app.Fragment prev = getActivity().getFragmentManager().findFragmentByTag("dialog");
                            if (prev != null && getRetainInstance()) {
                                DialogFragment df = (DialogFragment) prev;
                                df.dismiss();
                            }
                        }
                    }
                },100);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                touch_outside.performClick();
                mListener.onEdit();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(neg){
                    Toast.makeText(getActivity(),"Please click on edit to remove items with negative price value",Toast.LENGTH_LONG).show();
                }else{
                    touch_outside.performClick();
                    mListener.onConfirm();
                }
            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        touch_outside.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // touch_outside.setVisibility(View.GONE);
                                touch_outside.performClick();
                            }
                        },500);
                        setStatusBarDim(false);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        touch_outside.setVisibility(View.VISIBLE);
                        setStatusBarDim(true);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        showMultipleOutletsPopup();
        // Build dialog
        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setContentView(view);
        return builder;
    }

    private void setStatusBarDim(boolean dim) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(getActivity()!=null) {
                getActivity().getWindow().setStatusBarColor(dim ? getResources().getColor(R.color.black_opacity) :
                        ContextCompat.getColor(getActivity(), getThemedResId(R.attr.colorPrimaryDark)));
            }
        }
    }

    public void showMultipleOutletsPopup() {
        touch_outside.setVisibility(View.VISIBLE);
        multipleRestaurantsRcv.setHasFixedSize(true);
        multipleRestaurantsRcv.setNestedScrollingEnabled(false);
        multipleRestaurantsRcv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        multipleRestaurantsPopupAdapter = new OfferItemsReviewAdapter(getActivity(),multipleRestaurantsPopupList,clearance);
        multipleRestaurantsRcv.setAdapter(multipleRestaurantsPopupAdapter);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }


    private int getThemedResId(@AttrRes int attr) {
        TypedArray a = getActivity().getTheme().obtainStyledAttributes(new int[]{attr});
        int resId = a.getResourceId(0, 0);
        a.recycle();
        return resId;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private static final String TAG = "MultipleRestaurantsPopu";




    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                dialog.getWindow().setStatusBarColor(getActivity().getResources().getColor(R.color.red));
            }
        }
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }


    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                -view.getHeight(),                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(1000);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
