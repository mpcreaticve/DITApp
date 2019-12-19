package com.mpcreativesoftware.dit.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.appizona.yehiahd.fastsave.FastSave
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mpcreativesoftware.dit.MainActivity
import com.mpcreativesoftware.dit.R
import com.mpcreativesoftware.dit.SplashActivity
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class MyFirebaseMessagingService :
    FirebaseMessagingService() {
    private var notificationUtils: NotificationUtils? = null
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.from)
        val titel = remoteMessage.data.get("title")
        Log.e(TAG, "Titel" + titel.toString().toString())
        Log.e(TAG, "Message" + remoteMessage.data.get("message").toString())

        if (remoteMessage.data.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)


        }
        var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var remoteViews = RemoteViews(packageName, R.layout.noti_request)

        remoteViews.setImageViewResource(R.id.img_request, R.mipmap.ic_launcher_round)
        var msg = remoteMessage.data.get("message")
        var msgstrings = msg!!.split(",")

        for (i in 0 until msgstrings.size) {
            Log.e("msg", msgstrings[i].toString())

        }
        var title = msgstrings[2].split(":")
        for (i in 0 until title.size) {
            Log.e("title", title[i].toString())
        }
        var titleFinal = title[1].split("\"")
        for (i in 0 until titleFinal.size) {
            Log.e("titleFinal", titleFinal[i].toString())

        }
        var message = msgstrings[3].split(":")

        for (i in 0 until message.size) {
            Log.e("message", message[i].toString())
        }
        var messageFinal = message[1].split("\"")
        for (i in 0 until messageFinal.size) {
            Log.e("messageFinal", messageFinal[i].toString())
        }
        remoteViews.setTextViewText(
            R.id.tv_msg, messageFinal[1].toString()
        )

        remoteViews.setTextViewText(
            R.id.tv_title,
            titleFinal[1].toString()
        )

        RemoteViews(packageName, R.id.ll_notification)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        Log.v("notification","Intent")
        FastSave.getInstance().saveBoolean("notification", true)
        var pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        var builder = NotificationCompat.Builder(this, "Default")
            .setSmallIcon(R.drawable.ic_noti)
            .setContent(remoteViews)
            .setCustomContentView(remoteViews)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "Default",
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        val k = Random().nextInt(9999 - 1000) + 1000
        manager.notify(k, builder.build())
      /*  if (!titleFinal[1].equals("")) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }*/

    }

    private fun handleNotification(message: String?) {
        if (notificationUtils!!.isAppIsInBackground(
                applicationContext
            )
        ) {
            // app is in foreground, broadcast the push message

            val pushNotification =
                Intent(Config.PUSH_NOTIFICATION)
            pushNotification.putExtra("message", message)
            LocalBroadcastManager.getInstance(this)
                .sendBroadcast(pushNotification)

            // play notification sound


            val notificationUtils =
                NotificationUtils(applicationContext)
            notificationUtils.playNotificationSound()
        } else {
            // If the app is in background, firebase itself handles the notification

        }
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        val refreshedToken =
            FirebaseInstanceId.getInstance().token

        // Saving reg id to shared preferences


        storeRegIdInPref(refreshedToken)

        // sending reg id to your server


        sendRegistrationToServer(refreshedToken)

        // Notify UI that registration has completed, so the progress indicator can be hidden.


        val registrationComplete =
            Intent(Config.REGISTRATION_COMPLETE)
        registrationComplete.putExtra("token", refreshedToken)
        LocalBroadcastManager.getInstance(this)
            .sendBroadcast(registrationComplete)
    }

    private fun sendRegistrationToServer(token: String?) {
        // sending gcm token to server

        Log.e(TAG, "sendRegistrationToServer: $token")
    }

    private fun storeRegIdInPref(token: String?) {
        val pref: SharedPreferences = applicationContext.getSharedPreferences(
            Config.SHARED_PREF,
            0
        )
        val editor: Editor = pref.edit()
        editor.putString("regId", token)
        editor.commit()
    }
}