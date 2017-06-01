package com.apele.heyhey.fcm

/**
 * Created by alexandrupele on 01/06/2017.
 */

data class SendPayload(val to: String, val notification: NotificationPayload)