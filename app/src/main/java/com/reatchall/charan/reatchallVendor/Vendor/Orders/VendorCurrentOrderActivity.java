package com.reatchall.charan.reatchallVendor.Vendor.Orders;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Utils.StringConstants;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ItemOffer;
import com.reatchall.charan.reatchallVendor.Vendor.Models.OrderedItem;
import com.reatchall.charan.reatchallVendor.Vendor.Models.TaxesChargesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorCurrentOrderActivity extends AppCompatActivity {

    private static final String TAG = "VendorCurrentOrderActiv";
    Context context;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    PrefManager prefManager;
    String orderId,buzId;
    OrderedItemsAdapter orderedItemsAdapter;
    ArrayList<OrderedItem> orderedItemArrayList;
    LinearLayout acceptRejectLayout,parentLayout;

    RecyclerView itemsRcv;
    ImageView backArrow;
    FontTextView acceptOrder,rejectOrder;

    FontTextView amountToPay,itemTotal;
    RecyclerView taxesChargesRcv;
    TaxesChargesAdapter taxesChargesAdapter;
    ArrayList<TaxesChargesModel> taxesArrayList = new ArrayList<>();
    FontTextView orderStatusTV,orderStatusDetailTv;
    int orderStatus = 0;

    private Socket socket;
    {
        try{
            socket = IO.socket(Constants.SOCKET_URL);
        }catch(URISyntaxException e){
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_current_order);
        context = VendorCurrentOrderActivity.this;
        customProgressDialog = new CustomProgressDialog(context);
        prefManager = new PrefManager(context);

        backArrow =(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initViews();

        orderId = getIntent().getStringExtra(StringConstants.CURRENT_ORDER_ID);
        buzId = getIntent().getStringExtra(StringConstants.BUZ_ID);
        Log.e(TAG, "onCreate: "+orderId);
       // Log.e(TAG, "onCreate: NOTI DATA "+getIntent().getStringExtra("TITLE")+" "+getIntent().getStringExtra("BODY") );
        customProgressDialog.showDialog();
        getOrderDetails();

       //onNewIntent(getIntent());
    }

   /* @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        orderId = intent.getStringExtra(StringConstants.CURRENT_ORDER_ID);
        buzId = intent.getStringExtra(StringConstants.BUZ_ID);
        Log.e(TAG, "newIntent: "+orderId);
        Log.e(TAG, "newIntent: NOTI DATA "+getIntent().getExtras().getString("TITLE")+" "+getIntent().getExtras().getString("BODY") );
    }*/

    private  void initViews(){
        itemsRcv =(RecyclerView)findViewById(R.id.itemsRcv);
        itemsRcv.setHasFixedSize(true);
        itemsRcv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        orderedItemArrayList = new ArrayList<>();
        orderedItemsAdapter = new OrderedItemsAdapter(context,orderedItemArrayList);
        itemsRcv.setAdapter(orderedItemsAdapter);

        acceptOrder =(FontTextView)findViewById(R.id.acceptOrder);
        rejectOrder =(FontTextView)findViewById(R.id.rejectOrder);
        amountToPay =(FontTextView)findViewById(R.id.amountToPay);
        orderStatusTV =(FontTextView)findViewById(R.id.orderStatusTV);
        orderStatusDetailTv =(FontTextView)findViewById(R.id.orderStatusDetailTv);
        itemTotal =(FontTextView)findViewById(R.id.itemTotal);
        acceptRejectLayout =(LinearLayout) findViewById(R.id.acceptRejectLayout);
        parentLayout =(LinearLayout) findViewById(R.id.parentLayout);

        taxesChargesRcv =(RecyclerView)findViewById(R.id.taxesChargesRcv);
        taxesChargesRcv.setHasFixedSize(true);
        taxesChargesRcv.setNestedScrollingEnabled(false);
        taxesChargesRcv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        taxesArrayList.clear();
        taxesArrayList.add(new TaxesChargesModel("Delivery Fee","20"));
        taxesArrayList.add(new TaxesChargesModel("Restaurant charges(Gst)","24"));
        taxesChargesAdapter = new TaxesChargesAdapter(context,taxesArrayList);
        taxesChargesRcv.setAdapter(taxesChargesAdapter);


        acceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.showDialog();
                acceptOrder();
            }
        });

        rejectOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.showDialog();
                rejectOrder();
            }
        });

    }


    private void acceptOrder(){
        String url = Constants.BASE_URL+"vendor/accept-order";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("business_id",buzId);
            jsonObject.put("order_id",orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Order Accepted!",Toast.LENGTH_LONG).show();
                        socket.connect();
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("status","order rejected by vendor");
                        jsonObject1.put("business_id",buzId);
                        jsonObject1.put("vendor_id",prefManager.getVendorId(context));
                        jsonObject1.put("order_id",orderId);
                        socket.emit("ORDER_CONFIRMED", jsonObject1, new Ack() {
                            @Override
                            public void call(Object... args) {

                            }
                        });

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                socket.disconnect();
                            }
                        },100);
                        orderStatus = OrderDefaults.ORDER_REJECTED;
                        checkOrderStatus();
                    }else{
                        Toast.makeText(context,response.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error.getMessage() );
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Please try again!",Toast.LENGTH_LONG).show();

            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"ACCEPT_ORDER");
    }
    private void rejectOrder(){
        String url = Constants.BASE_URL+"vendor/reject-order";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("business_id",buzId);
            jsonObject.put("order_id",orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Order Accepted!",Toast.LENGTH_LONG).show();
                        socket.connect();
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("status","order rejected by vendor");
                        jsonObject1.put("business_id",buzId);
                        jsonObject1.put("vendor_id",prefManager.getVendorId(context));
                        jsonObject1.put("order_id",orderId);
                        socket.emit("ORDER_REJECTED",jsonObject1);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                socket.disconnect();
                            }
                        },100);
                        orderStatus = OrderDefaults.ORDER_REJECTED;
                        checkOrderStatus();
                    }else{
                        Toast.makeText(context,response.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onErrorResponse: "+error.getMessage() );
                Toast.makeText(context,"Please try again!",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"ACCEPT_ORDER");
    }
    private void getOrderDetails(){
        String url = Constants.BASE_URL+"delivery/get-order-details/"+orderId;
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    orderedItemArrayList = new ArrayList<>();
                    if(response.getBoolean("success")){
                        JSONObject msg = response.getJSONObject("msg");
                        orderStatus = msg.getInt("status");
                        if(msg.has("business_offer")){
                            JSONObject buzOfferObj = msg.getJSONObject("business_offer");
                        }else{

                        }

                        JSONArray itemsArray = msg.getJSONArray("items");
                        for(int i=0;i<itemsArray.length();i++){
                            JSONObject itemPObj = itemsArray.getJSONObject(i);
                            JSONObject itemObj = itemPObj.getJSONObject("item");
                            int orderQuantity = itemPObj.getInt("quantity");

                            if(itemObj.getJSONArray("images").length()>0){
                                if(itemObj.has("pack")){
                                    if(itemPObj.has("item_offer")){
                                        JSONObject itemOffer = itemPObj.getJSONObject("item_offer");
                                        ArrayList<String> itemIds = new ArrayList<>();
                                        for(int k=0;k<itemOffer.getJSONArray("item").length();k++){
                                            itemIds.add(itemOffer.getJSONArray("item").getString(k));
                                        }
                                        ItemOffer itemOffer1 = new ItemOffer(itemOffer.getString("offer_id"),"",
                                                "",itemOffer.getString("offer_type"),itemOffer.getBoolean("offer"),
                                                itemOffer.getBoolean("rupees"),itemOffer.getInt("discount"),itemOffer.getInt("buy_value"),itemOffer.getInt("get_value"),
                                                itemOffer.getString("startDate"),itemOffer.getString("endDate"),false,
                                                false,itemIds,null);

                                        orderedItemArrayList.add(new OrderedItem(itemObj.getString("_id"),itemObj.getString("business_id"),
                                                itemObj.getString("vendor_id"),itemObj.getString("name"),itemObj.getString("brand"),
                                                itemObj.getJSONArray("images").getJSONObject(0).getString("url"),
                                                itemObj.getInt("price"),itemObj.getInt("quantity"),itemPObj.getJSONObject("units").getString("_id"),itemPObj.getJSONObject("units").getString("name"),
                                                itemObj.getBoolean("single"),itemObj.getInt("pack"),itemObj.getString("description"),itemOffer1,orderQuantity));

                                    }else{
                                        //no Offer
                                        orderedItemArrayList.add(new OrderedItem(itemObj.getString("_id"),itemObj.getString("business_id"),
                                                itemObj.getString("vendor_id"),itemObj.getString("name"),itemObj.getString("brand"),
                                                itemObj.getJSONArray("images").getJSONObject(0).getString("url"),
                                                itemObj.getInt("price"),itemObj.getInt("quantity"),itemPObj.getJSONObject("units").getString("_id"),itemPObj.getJSONObject("units").getString("name"),
                                                itemObj.getBoolean("single"),itemObj.getInt("pack"),itemObj.getString("description"),null,orderQuantity));
                                    }
                                }else{
                                    if(itemPObj.has("item_offer")){
                                        JSONObject itemOffer = itemPObj.getJSONObject("item_offer");
                                        ArrayList<String> itemIds = new ArrayList<>();
                                        for(int k=0;k<itemOffer.getJSONArray("item").length();k++){
                                            itemIds.add(itemOffer.getJSONArray("item").getString(k));
                                        }
                                        ItemOffer itemOffer1 = new ItemOffer(itemOffer.getString("offer_id"),"",
                                                "",itemOffer.getString("offer_type"),itemOffer.getBoolean("offer"),
                                                itemOffer.getBoolean("rupees"),itemOffer.getInt("discount"),itemOffer.getInt("buy_value"),itemOffer.getInt("get_value"),
                                                itemOffer.getString("startDate"),itemOffer.getString("endDate"),false,
                                                false,itemIds,null);

                                        orderedItemArrayList.add(new OrderedItem(itemObj.getString("_id"),itemObj.getString("business_id"),
                                                itemObj.getString("vendor_id"),itemObj.getString("name"),itemObj.getString("brand"),
                                                itemObj.getJSONArray("images").getJSONObject(0).getString("url"),
                                                itemObj.getInt("price"),itemObj.getInt("quantity"),itemPObj.getJSONObject("units").getString("_id"),itemPObj.getJSONObject("units").getString("name"),
                                                itemObj.getBoolean("single"),0,itemObj.getString("description"),itemOffer1,orderQuantity));
                                    } else{
                                        //no Offer
                                        orderedItemArrayList.add(new OrderedItem(itemObj.getString("_id"),itemObj.getString("business_id"),
                                                itemObj.getString("vendor_id"),itemObj.getString("name"),itemObj.getString("brand"),
                                                itemObj.getJSONArray("images").getJSONObject(0).getString("url"),
                                                itemObj.getInt("price"),itemObj.getInt("quantity"),itemPObj.getJSONObject("units").getString("_id"),itemPObj.getJSONObject("units").getString("name"),
                                                itemObj.getBoolean("single"),0,itemObj.getString("description"),null,orderQuantity));
                                    }
                                }
                            }else{
                                if(itemObj.has("pack")){
                                    if(itemPObj.has("item_offer")){
                                        JSONObject itemOffer = itemPObj.getJSONObject("item_offer");
                                        ArrayList<String> itemIds = new ArrayList<>();
                                        for(int k=0;k<itemOffer.getJSONArray("item").length();k++){
                                            itemIds.add(itemOffer.getJSONArray("item").getString(k));
                                        }
                                        ItemOffer itemOffer1 = new ItemOffer(itemOffer.getString("offer_id"),"",
                                                "",itemOffer.getString("offer_type"),itemOffer.getBoolean("offer"),
                                                itemOffer.getBoolean("rupees"),itemOffer.getInt("discount"),itemOffer.getInt("buy_value"),itemOffer.getInt("get_value"),
                                                itemOffer.getString("startDate"),itemOffer.getString("endDate"),false,
                                                false,itemIds,null);

                                        orderedItemArrayList.add(new OrderedItem(itemObj.getString("_id"),itemObj.getString("business_id"),
                                                itemObj.getString("vendor_id"),itemObj.getString("name"),itemObj.getString("brand"),
                                                itemObj.getJSONArray("images").getJSONObject(0).getString("url"),
                                                itemObj.getInt("price"),itemObj.getInt("quantity"),itemPObj.getJSONObject("units").getString("_id"),itemPObj.getJSONObject("units").getString("name"),
                                                itemObj.getBoolean("single"),itemObj.getInt("pack"),itemObj.getString("description"),itemOffer1,orderQuantity));
                                    } else{
                                        //no Offer
                                        orderedItemArrayList.add(new OrderedItem(itemObj.getString("_id"),itemObj.getString("business_id"),
                                                itemObj.getString("vendor_id"),itemObj.getString("name"),itemObj.getString("brand"),
                                                itemObj.getJSONArray("images").getJSONObject(0).getString("url"),
                                                itemObj.getInt("price"),itemObj.getInt("quantity"),itemPObj.getJSONObject("units").getString("_id"),itemPObj.getJSONObject("units").getString("name"),
                                                itemObj.getBoolean("single"),itemObj.getInt("pack"),itemObj.getString("description"),null,orderQuantity));
                                    }
                                }else{
                                    if(itemPObj.has("item_offer")){
                                        JSONObject itemOffer = itemPObj.getJSONObject("item_offer");
                                        ArrayList<String> itemIds = new ArrayList<>();
                                        for(int k=0;k<itemOffer.getJSONArray("item").length();k++){
                                            itemIds.add(itemOffer.getJSONArray("item").getString(k));
                                        }
                                        ItemOffer itemOffer1 = new ItemOffer(itemOffer.getString("offer_id"),"",
                                                "",itemOffer.getString("offer_type"),itemOffer.getBoolean("offer"),
                                                itemOffer.getBoolean("rupees"),itemOffer.getInt("discount"),itemOffer.getInt("buy_value"),itemOffer.getInt("get_value"),
                                                itemOffer.getString("startDate"),itemOffer.getString("endDate"),false,
                                                false,itemIds,null);

                                        orderedItemArrayList.add(new OrderedItem(itemObj.getString("_id"),itemObj.getString("business_id"),
                                                itemObj.getString("vendor_id"),itemObj.getString("name"),itemObj.getString("brand"),
                                                "", itemObj.getInt("price"),itemObj.getInt("quantity"),itemPObj.getJSONObject("units").getString("_id"),itemPObj.getJSONObject("units").getString("name"),
                                                itemObj.getBoolean("single"),0,itemObj.getString("description"),itemOffer1,orderQuantity));
                                    } else{
                                        //no Offer
                                        orderedItemArrayList.add(new OrderedItem(itemObj.getString("_id"),itemObj.getString("business_id"),
                                                itemObj.getString("vendor_id"),itemObj.getString("name"),itemObj.getString("brand"),
                                                "",
                                                itemObj.getInt("price"),itemObj.getInt("quantity"),itemPObj.getJSONObject("units").getString("_id"),itemPObj.getJSONObject("units").getString("name"),
                                                itemObj.getBoolean("single"),0,itemObj.getString("description"),null,orderQuantity));
                                    }
                                }
                            }
                        }

                    }else{
                        Toast.makeText(context,response.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                    orderedItemsAdapter = new OrderedItemsAdapter(context,orderedItemArrayList);
                    itemsRcv.setAdapter(orderedItemsAdapter);

                    if(orderedItemArrayList.size()>0){
                        parentLayout.setVisibility(View.VISIBLE);
                        acceptRejectLayout.setVisibility(View.VISIBLE);

                        int itemTotalValue=0;
                        for(int i=0;i<orderedItemArrayList.size();i++){
                            int finalPriceValue = 0;
                            if(orderedItemArrayList.get(i).getItemOffer()!=null){
                                ItemOffer itemOffer = orderedItemArrayList.get(i).getItemOffer();
                                if(itemOffer.isOfferType()){
                                    if(itemOffer.isRupees()){
                                        finalPriceValue = ((int) (orderedItemArrayList.get(i).getPrice() - itemOffer.getDiscountValue()))*orderedItemArrayList.get(i).getOrderedQuantity();
                                    }else{
                                        finalPriceValue = ((int) (orderedItemArrayList.get(i).getPrice() - (orderedItemArrayList.get(i).getPrice()*itemOffer.getDiscountValue())/100))*orderedItemArrayList.get(i).getOrderedQuantity();
                                    }
                                }else{
                                    //clearance
                                    finalPriceValue = orderedItemArrayList.get(i).getPrice() * orderedItemArrayList.get(i).getOrderedQuantity();
                                }
                            }else {
                                finalPriceValue = orderedItemArrayList.get(i).getPrice() * orderedItemArrayList.get(i).getOrderedQuantity();
                            }

                            itemTotalValue = itemTotalValue + finalPriceValue;
                        }

                        int amountToPayValue = itemTotalValue+20+24;
                        itemTotal.setText(itemTotalValue+"");
                        amountToPay.setText(amountToPayValue+"");

                        checkOrderStatus();
                    }else{
                        parentLayout.setVisibility(View.GONE);
                        acceptRejectLayout.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Couldn't fetch data",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"ORDER_DETAILS");
    }

    private void checkOrderStatus(){
        switch (orderStatus){
                case OrderDefaults.ORDER_PLACED : {
                    acceptRejectLayout.setVisibility(View.VISIBLE);
                    orderStatusTV.setText("waiting for your confirmation");
                    orderStatusDetailTv.setText("");
                    break;
                }
                case OrderDefaults.ORDER_PENDING_AT_VENDOR: {
                    acceptRejectLayout.setVisibility(View.VISIBLE);
                    orderStatusTV.setText("waiting for your confirmation");
                    orderStatusDetailTv.setText("");
                    break;
                }
                case OrderDefaults.ORDER_ACCEPTED: {
                    acceptRejectLayout.setVisibility(View.GONE);
                    orderStatusTV.setText("Order has been confirmed by you");
                    orderStatusDetailTv.setText("");
                    break;
                }

                case OrderDefaults.ORDER_REJECTED: {
                    acceptRejectLayout.setVisibility(View.GONE);
                    orderStatusTV.setText("Order has been cancelled by you");
                    orderStatusDetailTv.setText("");
                    break;
                }

                case OrderDefaults.DB_NOT_ASSIGNED: {
                    acceptRejectLayout.setVisibility(View.GONE);
                    orderStatusTV.setText("Order has been confirmed by you");
                    orderStatusDetailTv.setText("Delivery boy will be assigned shortly");
                    break;
                }

                case OrderDefaults.DB_ASSIGNED: {
                    acceptRejectLayout.setVisibility(View.GONE);
                    orderStatusTV.setText("Order has been confirmed by you");
                    orderStatusDetailTv.setText("Delivery boy has been assigned");
                    break;
                }

                case OrderDefaults.DB_AT_VENDOR: {
                    acceptRejectLayout.setVisibility(View.GONE);
                    orderStatusTV.setText("Order has been confirmed by you");
                    orderStatusDetailTv.setText("Delivery boy has reached your location");
                    break;
                }

                case OrderDefaults.ORDER_PICKED: {
                    acceptRejectLayout.setVisibility(View.GONE);
                    orderStatusTV.setText("Order has been confirmed by you");
                    orderStatusDetailTv.setText("Delivery boy has picked up the package");
                    break;
                }

                case OrderDefaults.ORDER_IN_TRANSIT: {
                    acceptRejectLayout.setVisibility(View.GONE);
                    orderStatusTV.setText("Order has been confirmed by you");
                    orderStatusDetailTv.setText("Package in-transit. Will be delivered shortly.");
                    break;
                }

                case OrderDefaults.ORDER_DELIVERED: {
                    acceptRejectLayout.setVisibility(View.GONE);
                    orderStatusTV.setText("Order has been confirmed by you");
                    orderStatusDetailTv.setText("Package has been delivered.");
                    break;
                }

                case OrderDefaults.ORDER_CANCELLED_BY_USER: {
                    acceptRejectLayout.setVisibility(View.GONE);
                    orderStatusTV.setText("Order has been cancelled by user");
                    orderStatusDetailTv.setText("");
                    break;
                }



        }
    }
}
