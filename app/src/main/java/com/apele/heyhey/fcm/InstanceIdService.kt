package com.apele.heyhey.fcm

import android.util.Log
import com.apele.heyhey.model.User
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService


/**
 * Created by alexandrupele on 27/05/2017.
 */

class InstanceIdService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        super.onTokenRefresh()

        FirebaseAuth.getInstance().currentUser?.let {
            val user = User()

            user.fbUserId = AccessToken.getCurrentAccessToken().userId
            user.fcmDeviceId = FirebaseInstanceId.getInstance().token!!

            val db = FirebaseDatabase.getInstance().reference
            db.child("users").child(it.uid).setValue(user)
        }
    }
}