package com.mpcreativesoftware.dit

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        var handler = Handler()
        handler.postDelayed(Runnable {
            try {

                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()

            } catch (e: NullPointerException) {
                Log.e("SplashActivity", e.toString())
            }

        }, 2000)
    }



    override fun onResume() {
        super.onResume()
        try {
           // checkVersion()
        } catch (e: Exception) {
        }
    }
}
