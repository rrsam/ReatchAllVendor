package com.reatchall.charan.reatchallVendor.Vendor.Fragments;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.ImageCompression.Compressor;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.VendorBizPromoImagesAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.VendorPromotionsVideoAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BannerImages;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ImageUpload;
import com.reatchall.charan.reatchallVendor.Vendor.VendorPromotionsActivity;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.IDeletePromoVideo;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;


public class VendorBizPromoVideosFragment extends Fragment implements IDeletePromoVideo {
    private static final String TAG = "VendorBizPromoVideosFra";
    Context context;
    View view;
    BusinessDetails businessDashboard;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;

    AmazonS3Client s3;
    BasicAWSCredentials credentials;
    TransferUtility transferUtility;
    TransferObserver observer;


    String selectedVideoPath;
    ArrayList<String> selectedVideoList = new ArrayList<>();
    VendorPromotionsVideoAdapter vendorPromotionsVideoAdapter;
    LinearLayout uploadImages;
    RecyclerView recyclerView;

    FontTextView saveLogo;

    ArrayList<String> arrayList;

    FontEditText etTitle,etDescripiton;

    private VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
        @Override
        public void onPlayerItemChanged(MetaData metaData) {

        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vendor_biz_promo_videos, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context =getActivity();
        VendorPromotionsActivity vendorPromotionsActivity =(VendorPromotionsActivity)context;
        vendorPromotionsActivity.highlightTab(0);
        businessDashboard = getArguments().getParcelable("businessDetails");
        customProgressDialog = new CustomProgressDialog(context);

        customProgressDialog.showDialog();
        getVideos();
        setupAws();
        recyclerView = (RecyclerView)view.findViewById(R.id.promotionalRcv);
        etTitle=(FontEditText) view.findViewById(R.id.promoVideoTitle);
        etDescripiton=(FontEditText)view.findViewById(R.id.promoVideoDesc);
        saveLogo = (FontTextView)view.findViewById(R.id.saveLogo);
        uploadImages=(LinearLayout)view.findViewById(R.id.uploadImages);

        uploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectVideo();

            }
        });



        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        vendorPromotionsVideoAdapter = new VendorPromotionsVideoAdapter(context,selectedVideoList,mVideoPlayerManager,this);
        recyclerView.setAdapter(vendorPromotionsVideoAdapter);


        saveLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etTitle.getText().toString().isEmpty()) {
                    if(!etDescripiton.getText().toString().isEmpty()) {
                        if (selectedVideoList.size() > 0) {
                            customProgressDialog.showDialog();
                            compressImagesNupload();
                        } else {
                            customProgressDialog.showDialog();
                            formString();
                        }
                    }else{
                        Toast.makeText(vendorPromotionsActivity, "Please enter Promo Description", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(vendorPromotionsActivity, "Please enter Promo Title", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void selectVideo(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),101);
    }

    private void getVideos(){
        String url = Constants.BASE_URL+"vendor/get-promotional-videos/"+businessDashboard.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: " + response.toString());
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        arrayList = new ArrayList<>();
                        JSONObject msg = response.getJSONObject("msg");
                        String title = msg.getString("title");
                        String descripiton = msg.getString("description");
                        etTitle.setText(title);
                        etDescripiton.setText(descripiton);
                        JSONArray basicDetails = msg.getJSONArray("images");
                        for (int i = 0; i < basicDetails.length(); i++) {
                            arrayList.add(basicDetails.getJSONObject(i).getString("url"));
                            selectedVideoList.add(null);
                        }

                        vendorPromotionsVideoAdapter = new VendorPromotionsVideoAdapter(context,arrayList,mVideoPlayerManager,VendorBizPromoVideosFragment.this);
                        recyclerView.setAdapter(vendorPromotionsVideoAdapter);
                        vendorPromotionsVideoAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(context,"No Videos Found",Toast.LENGTH_LONG).show();
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
                Toast.makeText(context,"Couldn't fetch videos",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"GET_BIZ_LOGOS");
    }

    private void setupAws(){
        credentials = new BasicAWSCredentials(Constants.S3_ACCESS_KEY_ID,Constants.S3_SECRET_ACCESS_KEY);
        s3 = new AmazonS3Client(credentials);
        s3.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
        transferUtility = new TransferUtility(s3, context);
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                Uri selectedImageUri = data.getData();

                // OI FILE Manager
                // filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                selectedVideoPath = getPath(context, selectedImageUri);
                if (selectedVideoPath != null) {

                    Log.e(TAG, "onActivityResult: PATH  " + selectedVideoPath);
                    selectedVideoList.add(selectedVideoPath);
                    vendorPromotionsVideoAdapter.notifyDataSetChanged();
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }/**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static MultipartBody.Part getMultiPartBody(String key, String mMediaUrl) {
        if (mMediaUrl != null) {
            File file = new File(mMediaUrl);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            return MultipartBody.Part.createFormData(key, file.getName(), requestFile);
        } else {
            return MultipartBody.Part.createFormData(key, "");
        }
    }


    @Override
    public void deleteVideo(int videoPosition) {
        selectedVideoList.remove(videoPosition);
        if(selectedVideoList!=null){
            if(selectedVideoList.size()>0){
                selectedVideoList.remove(videoPosition);
            }
        }
        vendorPromotionsVideoAdapter.notifyDataSetChanged();
    }


    String videoKey;
    ArrayList<File> compressedFilesList ;
    ArrayList<ImageUpload> videoUploadArrayList = new ArrayList<>();

    private void compressImagesNupload(){
        compressedFilesList = new ArrayList<>();
        videoUploadArrayList = new ArrayList<>();

        Log.e(TAG, "compressImagesNupload: "+selectedVideoList.size());
        for(int i =0;i<selectedVideoList.size();i++){
            File cmpFile = new File(selectedVideoList.get(i));
            if(cmpFile.exists()){
                Log.e(TAG, "compressImagesNupload: CMP EXISTS" );
                compressedFilesList.add(cmpFile);
            }
        }

        if(compressedFilesList.size()==selectedVideoList.size()){
            Log.e(TAG, "compressImagesNupload: EQUALS" );
            findUploadFiles( 0);
        }
    }

    private void findUploadFiles(int i){
        Log.e(TAG, "findUploadFiles: "+i );
        if(i<compressedFilesList.size()){
            if(compressedFilesList.get(i)!=null)
            {
                uploadImageToAws(compressedFilesList.get(i),i);
            } else{
                Log.e(TAG, "findUploadFiles: NULL" +i);
                findUploadFiles(i+1);
            }
        }else{
            formString();
        }

    }
    private void uploadImageToAws(File selectedFilePath,int filePosition){
        // imgKey = getRandomString(13);
        videoKey = System.currentTimeMillis()+"";
        Log.e(TAG, "uploadImageToAws: "+videoKey );
        observer = transferUtility.upload(
                Constants.S3_BUCKET_CATEGORY,
                videoKey,
                selectedFilePath
        );

        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {

                if (state.COMPLETED.equals(observer.getState())) {

                    Log.e(TAG, "onStateChanged: UPLOAD COMPLETE" + selectedFilePath.getAbsolutePath() );
                    Log.e(TAG, "onStateChanged: https://"+Constants.S3_BUCKET_CATEGORY+".s3.amazonaws.com/"+observer.getKey());
                    String url = "https://"+Constants.S3_BUCKET_CATEGORY+".s3.amazonaws.com/"+observer.getKey();
                    videoUploadArrayList.add(new ImageUpload(url,observer.getKey()));
                    if(filePosition<compressedFilesList.size()-1){
                        uploadImageToAws(compressedFilesList.get(filePosition+1),filePosition+1);
                    }else{
                        Log.e(TAG, "onStateChanged: FORM STRING" );
                        formString();
                    }
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {



                long _bytesCurrent = bytesCurrent;
                long _bytesTotal = bytesTotal;

                float percentage =  ((float)_bytesCurrent /(float)_bytesTotal * 100);
                Log.e("percentage","" +percentage);
                /*pb.setProgress((int) percentage);
                _status.setText(percentage + "%");*/
            }

            @Override
            public void onError(int id, Exception ex) {

                Log.e(TAG, "onStateChanged: UPLOAD"+ex.getMessage());

                //Toast.makeText(MainActivity.this, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void formString(){

        try {
            JSONObject postItemJson = new JSONObject();

            postItemJson.put("business_id",businessDashboard.getBusinessId());
            postItemJson.put("title",etTitle.getText().toString().trim());
            postItemJson.put("description",etDescripiton.getText().toString().trim());

            JSONArray itemImagesJsonArray = new JSONArray();
            for(int i=0;i<videoUploadArrayList.size();i++){
                JSONObject itemImageObject = new JSONObject();
                itemImageObject.put("imageurl",videoUploadArrayList.get(i).getUrl());
                itemImageObject.put("key",videoUploadArrayList.get(i).getKey());
                itemImagesJsonArray.put(itemImageObject);
            }
            Log.e(TAG, "formString: "+itemImagesJsonArray.length()+itemImagesJsonArray.toString() );
            postItemJson.put("video",itemImagesJsonArray);


            addItemToBusiness(postItemJson);
        } catch (JSONException e) {
            Log.e(TAG, "formString: "+e.getMessage() );
            e.printStackTrace();
        }

    }

    private void addItemToBusiness(JSONObject jsonObject){
        String url=Constants.BASE_URL+"vendor_mobile/add-promotional-videos-mobile";

        Log.e(TAG, "addItemToBusiness: "+ jsonObject.toString() );

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.POST, url, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onResponse: "+response.toString() );
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Success",Toast.LENGTH_LONG).show();

                    }else{
                        Toast.makeText(context,"Error",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onErrorResponse: "+error.toString() );
            }
        });
        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"UPLOADLOGO"+businessDashboard.getBusinessId());
    }

}
