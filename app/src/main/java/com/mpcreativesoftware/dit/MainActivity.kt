package com.mpcreativesoftware.dit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.mpcreativesoftware.dit.Activity.VideoActivity
import com.mpcreativesoftware.dit.Fragment.DeclimarFragment
import com.mpcreativesoftware.dit.Fragment.HomeFragment
import com.mpcreativesoftware.dit.Fragment.MarketInfoFragment
import com.mpcreativesoftware.dit.Fragment.Rules_of_TreadingFragment
import com.mpcreativesoftware.dit.Model.User
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar


/*
* Version :1.2.1
* Version Code :11
*/
class MainActivity : AppCompatActivity() {
    private var exit = false
    private var mAdView: AdView? = null
    lateinit var dynamicLink: Uri
    var position = 0


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseMessaging.getInstance().subscribeToTopic("news")
        setSupportActionBar(toolbar)

        MobileAds.initialize(this, "ca-app-pub-4134787945289593~7228976307")
        mAdView = findViewById(R.id.adViewMain)
        val adRequest = AdRequest.Builder().build()
        mAdView!!.loadAd(adRequest)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        var strId = FirebaseInstanceId.getInstance().token
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("User")
        var android_id = Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        toolbar.setNavigationOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.mpcreativesoftware.dit&hl=en")
                )
            )
        }
//        val tMgr = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//        val mPhoneNumber = tMgr.line1Number
//        Log.d("Number",mPhoneNumber.toString())
        val user = User()
        user.deviceId = android_id
        user.registerId = strId
        myRef.child(android_id).setValue(user)

        Log.d("Device Id", FirebaseInstanceId.getInstance().token + "")
        val fragment = HomeFragment()
        addFragment(fragment)


/*Dynamic Link*/
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(
                this
            ) { pendingDynamicLinkData ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }

            }
            .addOnFailureListener(this, object : OnFailureListener {
                override fun onFailure(e: Exception) {
                    Log.w("TAG", "getDynamicLink:onFailure", e)
                }
            })
    }


    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> {
                    position = 0
                    val fragment = HomeFragment()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.ic_rules -> {
                    position = 1
                    val fragment = Rules_of_TreadingFragment()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.ic_declaimber -> {
                    position = 2
                    val fragment = DeclimarFragment()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.ic_information -> {
                    position = 3
                    val fragment = MarketInfoFragment()
                    addFragment(fragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.ic_youtub -> {
                    var intent = Intent(this@MainActivity, VideoActivity::class.java)
                    intent.putExtra("position", position)
                    startActivityForResult(intent, 1)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    fun setNavigationSeleceted(id: Int) {
        if (id == 0) {
            bottomNavigationView!!.selectedItemId = R.id.ic_home
            val fragment = HomeFragment()
            addFragment(fragment)
        } else if (id == 1) {
            bottomNavigationView!!.selectedItemId = R.id.ic_rules
            val fragment = DeclimarFragment()
            addFragment(fragment)
        } else if (id == 2) {
            bottomNavigationView!!.selectedItemId = R.id.ic_declaimber
            val fragment = MarketInfoFragment()
            addFragment(fragment)
        } else {
            bottomNavigationView!!.selectedItemId = R.id.ic_information
            val fragment = MarketInfoFragment()
            addFragment(fragment)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.ic_Share -> {

                //"Hey check out my app at: https://play.google.com/store/apps/details?id=com.mpcreativesoftware.dit"
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT, "https://dailyintradaytips.page.link/vgNL"

                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (exit) {
            super.onBackPressed() // finish activity
        } else {
            Toast.makeText(
                this, "Press Back again to Exit.",
                Toast.LENGTH_SHORT
            ).show()
            exit = true
            Handler().postDelayed(Runnable { exit = false }, 3 * 1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data!!.getIntExtra("position", 0)
                setNavigationSeleceted(result)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


}



