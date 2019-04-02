package com.reatchall.charan.reatchallVendor.Vendor.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.IDeletePromoVideo;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.SimpleMainThreadMediaPlayerListener;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;

import java.util.ArrayList;

public class VendorPromotionsVideoAdapter extends RecyclerView.Adapter<VendorPromotionsVideoAdapter.VendorPromotionsViewHolder> {

    Context context;
    ArrayList<String> arrayList;
    VideoPlayerManager<MetaData> videoPlayerManager;
    IDeletePromoVideo iDeletePromoVideo;

    public VendorPromotionsVideoAdapter(Context context, ArrayList<String> arrayList,VideoPlayerManager<MetaData> videoPlayerManager,IDeletePromoVideo iDeletePromoVideo) {
        this.context = context;
        this.arrayList = arrayList;
        this.videoPlayerManager=videoPlayerManager;
        this.iDeletePromoVideo=iDeletePromoVideo;
    }

    @Override
    public VendorPromotionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promo_video_list,parent,false);
        return new VendorPromotionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VendorPromotionsViewHolder holder, int position) {

        Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(arrayList.get(position), MediaStore.Video.Thumbnails.MINI_KIND);

        holder.squareImageView.setImageBitmap(bmThumbnail);
        holder.squareImageView.setVisibility(View.VISIBLE);
        holder.squareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                videoPlayerManager.playNewVideo(null,holder.videoPlayerView,arrayList.get(position));
                holder.playIcon.setVisibility(View.INVISIBLE);
                holder.deleteVideo.setVisibility(View.INVISIBLE);
            }
        });

        holder.videoPlayerView.addMediaPlayerListener(new SimpleMainThreadMediaPlayerListener(){
            @Override
            public void onVideoPreparedMainThread() {
                // We hide the cover when video is prepared. Playback is about to start
               holder.squareImageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onVideoStoppedMainThread() {
                // We show the cover when video is stopped
                holder.squareImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVideoCompletionMainThread() {
                // We show the cover when video is completed
               holder.squareImageView.setVisibility(View.VISIBLE);
               holder.playIcon.setVisibility(View.VISIBLE);
               holder.deleteVideo.setVisibility(View.VISIBLE);
            }
        });

        holder.deleteVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iDeletePromoVideo.deleteVideo(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VendorPromotionsViewHolder extends RecyclerView.ViewHolder {
        VideoPlayerView videoPlayerView;
        ImageView squareImageView;
        ImageView playIcon;
        ImageView deleteVideo;
        public VendorPromotionsViewHolder(View itemView) {
            super(itemView);
            videoPlayerView=(VideoPlayerView)itemView.findViewById(R.id.video_player);
            squareImageView=(ImageView) itemView.findViewById(R.id.video_cover);
            playIcon=(ImageView)itemView.findViewById(R.id.playIcon);
            deleteVideo=(ImageView)itemView.findViewById(R.id.deleteVideo);
        }
    }
}
