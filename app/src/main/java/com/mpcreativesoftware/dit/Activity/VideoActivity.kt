package com.mpcreativesoftware.dit.Activity

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mpcreativesoftware.dit.Adapter.YoutubeVideoAdapter
import com.mpcreativesoftware.dit.MainActivity
import com.mpcreativesoftware.dit.Model.YouTube
import com.mpcreativesoftware.dit.R
import com.mpcreativesoftware.dit.Utils.Constants
import com.mpcreativesoftware.dit.Utils.RecyclerViewOnClickListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_video.*


class VideoActivity : AppCompatActivity() {

    var positions = 0
    val mACtivity: Activity? = null
    private var youTubePlayerFragment: YouTubePlayerSupportFragment? = null
    private var youtubeVideoArrayList: ArrayList<YouTube>? = null
    private var youTubePlayer: YouTubePlayer? = null
    private lateinit var mInterstitialAd: InterstitialAd
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        setSupportActionBar(toolbar)

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-4134787945289593/6805625853"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        positions = intent.getIntExtra("position", 0)
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
            }
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        youtubeVideoArrayList = ArrayList()
        val database = FirebaseDatabase.getInstance()
        val myRefs = database.getReference("Video")



        myRefs.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (noteDataSnapshot in dataSnapshot.children) {
                    val user = noteDataSnapshot.getValue(YouTube::class.java)
                    youtubeVideoArrayList!!.add(user!!)
                }
                youtubeVideoArrayList!!.reverse()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })


        setUpRecyclerView()
        populateRecyclerView()
        initializeYoutubePlayer()
    }

    private fun initializeYoutubePlayer() {
        youTubePlayerFragment =
            supportFragmentManager.findFragmentById(R.id.youtube_player_fragment) as YouTubePlayerSupportFragment

        if (youTubePlayerFragment == null)
            return

        youTubePlayerFragment!!.initialize(
            Constants.API_KEY,
            object : YouTubePlayer.OnInitializedListener {

                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider, player: YouTubePlayer,
                    wasRestored: Boolean
                ) {
                    if (!wasRestored) {
                        youTubePlayer = player

                        //set the player style default
                        youTubePlayer!!.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                        Log.e("pos", youtubeVideoArrayList!!.size.toString())
                        //cue the 1st video by default
                        try {
                            youTubePlayer!!.cueVideo(youtubeVideoArrayList!!.get(0).id)
                        } catch (er: IndexOutOfBoundsException) {
                            Log.e("catch", er.toString())
                        }

                    }
                }

                override fun onInitializationFailure(
                    arg0: YouTubePlayer.Provider,
                    arg1: YouTubeInitializationResult
                ) {

                    //print or show error if initialization failed
                    Log.e(ContentValues.TAG, "Youtube Player View initialization failed")
                }
            })
    }

    private fun setUpRecyclerView() {
        recycler_view.setHasFixedSize(true)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_view.layoutManager = linearLayoutManager
    }

    private fun populateRecyclerView() {

        val adapter = YoutubeVideoAdapter(this, youtubeVideoArrayList)
        recycler_view.adapter = adapter

        //set click event
        recycler_view.addOnItemTouchListener(
            RecyclerViewOnClickListener(
                this,
                object : RecyclerViewOnClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {

                        if (youTubePlayerFragment != null && youTubePlayer != null) {
                            //update selected position
                            adapter.setSelectedPosition(position)

                            //load selected video
                            youTubePlayer!!.cueVideo(youtubeVideoArrayList!!.get(position).id)
                        }

                    }
                })
        )
    }

    override fun onBackPressed() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {

            val returnIntent = Intent()
            returnIntent.putExtra("position", positions)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
            super.onBackPressed()
            Log.d("TAG", "The interstitial wasn't loaded yet.")
        }

    }

}
