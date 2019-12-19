package com.mpcreativesoftware.dit.Model

import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {
    @Headers("Accept: application/json")
    @POST("push.php")
    fun sendNotification(@Body fcmRequest: Notification): Call<FCM_Response>
}

