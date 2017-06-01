package com.apele.heyhey.fcm

import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

/**
 * Created by alexandrupele on 31/05/2017.
 */

class NotificationManager(val client: OkHttpClient, val gson: Gson) {

    val SERVER_KEY = "key=AAAAMMtIMkg:APA91bHzPyHbcZIcZxOxCNdJaLXYLqcjbgcoFnCNb5Ur94b8x5aIgtDJYE5Hbaajru8rBbo3kJBsWAkQPWLDauQMdrJpR-qZLlkitmdLjneospc5jJ7i-D0ROT1Q0q57tOrwPq6CtmxZ"
    val mediaType: MediaType? = MediaType.parse("application/json")

    fun send(message: String, title: String, targetDeviceId: String, onMessageSent: () -> Unit) {
        val notification = SendPayload(targetDeviceId, NotificationPayload(title, message))

        val requestBody = RequestBody.create(mediaType, gson.toJson(notification))

        val request = Request.Builder()
                .url("https://gcm-http.googleapis.com/gcm/send")
                .post(requestBody)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", SERVER_KEY)
                .addHeader("cache-control", "no-cache")
                .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call?, e: IOException?) {
                onMessageSent()
            }

            override fun onResponse(call: Call?, response: Response?) {
                onMessageSent()
            }
        })
    }
}