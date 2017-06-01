package com.apele.heyhey

import android.app.Application
import android.arch.persistence.room.Room
import android.support.multidex.MultiDexApplication
import com.apele.heyhey.fcm.NotificationManager
import com.apele.heyhey.model.Database
import com.apele.heyhey.model.User
import com.google.firebase.FirebaseApp
import com.google.gson.Gson
import okhttp3.OkHttpClient

/**
 * Created by alexandrupele on 27/05/2017.
 */

class HeyHeyApp : MultiDexApplication() {

    companion object {
        var currentUser: User? = null
        val notificationManager = NotificationManager(OkHttpClient(), Gson())
        lateinit var database: Database
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this, Database::class.java, "heyhey-database").build()

        FirebaseApp.initializeApp(this)
    }
}