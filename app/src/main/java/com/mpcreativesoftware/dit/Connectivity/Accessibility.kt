package com.neha.mystory.API

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkInfo


class Accessibility {
    companion object {
        fun IsConnectToInternet(context: Context): Boolean {
            var connectivity = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {
                var info = connectivity.allNetworkInfo
                if (info != null) {
                    for (i in 0..info.size - 1) {
                        if (info[i].state == NetworkInfo.State.CONNECTED) {
                            return true
                        }
                    }
                }
            }
            return false
        }
    }
}