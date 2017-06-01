package com.apele.heyhey.fcm

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.apele.heyhey.message.INBOX_EXTRA_FROM
import com.apele.heyhey.message.INBOX_EXTRA_MESSAGE
import com.apele.heyhey.message.InboxActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.jetbrains.anko.intentFor

/**
 * Created by alexandrupele on 27/05/2017.
 */

class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

        message?.notification?.let {
            Handler(Looper.getMainLooper()).post {
                startActivity(intentFor<InboxActivity>(INBOX_EXTRA_FROM to it.title!!,
                        INBOX_EXTRA_MESSAGE to it.body!!))
            }
        }
    }
}