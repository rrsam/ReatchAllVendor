package com.reatchall.charan.reatchallVendor.Vendor.Offers;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.OfferItemsReviewAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ReviewItemOffer;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class ConfirmItemOffer extends Dialog {
    private String message;
    private String title;
    private String btYesText;
    private String btNoText;
    private int icon=0;
    private View.OnClickListener btYesListener=null;
    private View.OnClickListener btNoListener=null;
    RecyclerView recyclerView;
    Context context;
    public ConfirmItemOffer(Context context) {
        super(context);
        this.context = context;
    }

    public ConfirmItemOffer(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

    }

    protected ConfirmItemOffer(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;

    }
    ArrayList<ReviewItemOffer> reviewItemOffers= new ArrayList<>();
    OfferItemsReviewAdapter reviewAdapter;

    public ArrayList<ReviewItemOffer> getReviewItemOffers() {
        return reviewItemOffers;
    }

    public void setReviewItemOffers(ArrayList<ReviewItemOffer> reviewItemOffers) {
        this.reviewItemOffers = reviewItemOffers;
        notify();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm_item_offer);
        FontTextView tv = (FontTextView) findViewById(R.id.malertTitle);
        FontTextView businessOffer = (FontTextView) findViewById(R.id.msg);
        FontTextView edit = (FontTextView) findViewById(R.id.edit);
        FontTextView confirm = (FontTextView) findViewById(R.id.confirm);
        recyclerView= (RecyclerView) findViewById(R.id.reviewsRcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        //reviewAdapter = new OfferItemsReviewAdapter(context,reviewItemOffers);
        recyclerView.setAdapter(reviewAdapter);
        edit.setOnClickListener(btYesListener);
        confirm.setOnClickListener(btNoListener);
    }

    public void setupRcv(ArrayList<ReviewItemOffer> reviewItemOffers){
       // reviewAdapter = new OfferItemsReviewAdapter(context,reviewItemOffers);
        recyclerView.setAdapter(reviewAdapter);
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
