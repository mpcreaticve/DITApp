package com.mpcreativesoftware.dit.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import com.google.firebase.database.FirebaseDatabase
import com.mpcreativesoftware.dit.Model.YouTube
import com.mpcreativesoftware.dit.R
import com.mpcreativesoftware.dit.Utils.Constants

import java.util.ArrayList


class YoutubeVideoAdapter(
    private val context: Context,
    private val youtubeVideoModelArrayList: ArrayList<YouTube>?
) : RecyclerView.Adapter<YoutubeViewHolder>() {

    //position to check which position is selected
    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.youtube_video_custom_layout, parent, false)
        return YoutubeViewHolder(view)
    }

    override fun onBindViewHolder(holder: YoutubeViewHolder, position: Int) {

        //if selected position is equal to that mean view is selected so change the cardview color
        if (selectedPosition == position) {
            holder.youtubeCardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimary
                )
            )
        } else {
            //if selected position is not equal to that mean view is not selected so change the cardview color to white back again
            holder.youtubeCardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    android.R.color.white
                )
            )
        }

        /*  initialize the thumbnail image view , we need to pass Developer Key */
        holder.videoThumbnailImageView.initialize(
            Constants.API_KEY,
            object : YouTubeThumbnailView.OnInitializedListener {
                override fun onInitializationSuccess(
                    youTubeThumbnailView: YouTubeThumbnailView,
                    youTubeThumbnailLoader: YouTubeThumbnailLoader
                ) {
                    //when initialization is sucess, set the video id to thumbnail to load
                    youTubeThumbnailLoader.setVideo(youtubeVideoModelArrayList!![position].id)

                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(object :
                        YouTubeThumbnailLoader.OnThumbnailLoadedListener {
                        override fun onThumbnailLoaded(
                            youTubeThumbnailView: YouTubeThumbnailView,
                            s: String
                        ) {
                            //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
                            youTubeThumbnailLoader.release()
                        }

                        override fun onThumbnailError(
                            youTubeThumbnailView: YouTubeThumbnailView,
                            errorReason: YouTubeThumbnailLoader.ErrorReason
                        ) {
                            //print or show error when thumbnail load failed
                            Log.e(TAG, "Youtube Thumbnail Error")
                        }
                    })
                }

                override fun onInitializationFailure(
                    youTubeThumbnailView: YouTubeThumbnailView,
                    youTubeInitializationResult: YouTubeInitializationResult
                ) {
                    //print or show error when initialization failed
                    Log.e(TAG, "Youtube Initialization Failure")

                }
            })

    /*    holder.iv_delete.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("Video")
            myRef.child(youtubeVideoModelArrayList!!.get(position).id).removeValue()
        }*/
    }

    override fun getItemCount(): Int {
        return youtubeVideoModelArrayList?.size ?: 0
    }

    /**
     * method the change the selected position when item clicked
     *
     * @param selectedPosition
     */
    fun setSelectedPosition(selectedPosition: Int) {
        this.selectedPosition = selectedPosition
        //when item selected notify the adapter
        notifyDataSetChanged()
    }

    companion object {
        private val TAG = YoutubeVideoAdapter::class.java.simpleName
    }
}
