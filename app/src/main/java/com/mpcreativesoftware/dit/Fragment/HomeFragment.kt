package com.mpcreativesoftware.dit.Fragment


import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mpcreativesoftware.dit.Adapter.HomeAdapter
import com.mpcreativesoftware.dit.BuildConfig
import com.mpcreativesoftware.dit.Model.LiveData
import com.mpcreativesoftware.dit.R

import com.neha.mystory.API.Accessibility
import hotchemi.android.rate.AppRate
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    lateinit var mActivity:FragmentActivity
    lateinit var shimmer_home: ShimmerFrameLayout
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: HomeAdapter
    lateinit var arrayList: ArrayList<LiveData>
    val database = FirebaseDatabase.getInstance()
    private var mAdView: AdView? = null

    var str_version: String? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity=context as FragmentActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.rv_Home)
        var layout = LinearLayoutManager(mActivity)
        rateUs()

        checkVersion()

        recyclerView.layoutManager = layout
        recyclerView.itemAnimator = DefaultItemAnimator()
        arrayList = ArrayList<LiveData>()
        shimmer_home = view.findViewById(R.id.shimmer_home) as ShimmerFrameLayout

        if ((Accessibility.IsConnectToInternet(mActivity))) {
            shimmer_home.startShimmerAnimation()
            getTasks()
        } else {
            shimmer_home.visibility = View.GONE
            shimmer_home.stopShimmerAnimation()
            view.cv_query.visibility = View.VISIBLE
            view.tv_dl.text = "No Intenet connection!"
            view.iv_dl.setBackgroundResource(R.drawable.ic_network)
        }
        view.btn_retry.setOnClickListener {

            if ((Accessibility.IsConnectToInternet(mActivity))) {
                shimmer_home.startShimmerAnimation()
                view.cv_query.visibility = View.GONE
                getTasks()
            } else {
                shimmer_home.visibility = View.GONE
                shimmer_home.stopShimmerAnimation()
                view.cv_query.visibility = View.VISIBLE
                view.tv_dl.text = "No Intenet connection!"
                view.iv_dl.setBackgroundResource(R.drawable.ic_network)
            }


        }

        return view
    }




    private fun checkVersion() {

        Log.v("Version", BuildConfig.VERSION_NAME.toString())
        val version = database.getReference("Version")
        version.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (noteDataSnapshot in dataSnapshot.children) {
                    Log.v("VersionKey", noteDataSnapshot.key.toString())
                    Log.v("VersionValue", noteDataSnapshot.value.toString())
                    str_version = noteDataSnapshot.value.toString()
                    if (!str_version.equals(BuildConfig.VERSION_NAME)) {
                        simpleAlert()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun simpleAlert() {
        try {
            val builder: AlertDialog.Builder = AlertDialog.Builder(mActivity)
            builder.setTitle("New Version available")
            builder.setMessage("Please, Update app to new version to continue reporting.")
            builder.setPositiveButton("UPDATE",
                DialogInterface.OnClickListener { dialog, which ->
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.mpcreativesoftware.dit&hl=en")
                        )
                    )
                })

            builder.setCancelable(true)
            builder.show()
        } catch (e: java.lang.Exception) {
            Log.e("SplashActivity", e.toString())
        }
    }
    fun getTasks() {

        val myRef = database.getReference("DIT").orderByChild("date")
        myRef.keepSynced(true)
        Log.e("Databse", myRef.toString())

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                arrayList.clear()
                for (noteDataSnapshot in dataSnapshot.children) {
                    var liveData = noteDataSnapshot.getValue(LiveData::class.java)
                    arrayList.add(liveData!!)
                }

                shimmer_home.visibility = View.GONE
                shimmer_home.stopShimmerAnimation()

                if (arrayList.size != 0) {
                    Log.e("ArrayList", arrayList.toString())
                    try {
                        arrayList.reverse()
                        adapter = HomeAdapter(mActivity, arrayList)
                        recyclerView.adapter = adapter
                    } catch (e: KotlinNullPointerException) {

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun rateUs() {

   AppRate.with(mActivity)
            .setInstallDays(0) // default 10, 0 means install day.
            .setLaunchTimes(1) // default 10
            .setRemindInterval(5) // default 1
            .setShowLaterButton(true) // default true
            .setDebug(false) // default false
            .setTitle(R.string.my_own_title)
            .setMessage(R.string.my_own_message)
            .setOnClickButtonListener { which ->
                // callback listener.
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.mpcreativesoftware.dit&hl=en")
                    )
                )
            }
       .setCancelable(true)
            .monitor()

        AppRate.showRateDialogIfMeetsConditions(mActivity)


    }




}
