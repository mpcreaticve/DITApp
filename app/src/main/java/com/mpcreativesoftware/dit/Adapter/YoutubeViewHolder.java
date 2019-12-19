package com.mpcreativesoftware.dit.Adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mpcreativesoftware.dit.R;

/**
 * Created by sonu on 10/11/17.
 */

public class YoutubeViewHolder extends RecyclerView.ViewHolder {
    public YouTubeThumbnailView videoThumbnailImageView;
    public CardView youtubeCardView;

    public YoutubeViewHolder(View itemView) {
        super(itemView);
        videoThumbnailImageView = itemView.findViewById(R.id.video_thumbnail_image_view);
       youtubeCardView = itemView.findViewById(R.id.youtube_row_card_view);
    }
}
