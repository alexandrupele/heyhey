package com.apele.heyhey.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.firebase.database.Exclude

/**
 * Created by alexandrupele on 27/05/2017.
 */

@Entity(tableName = "users")
class User {

    @PrimaryKey
    @ColumnInfo(name = "fb_id")
    var fbUserId: String = ""

    @Ignore
    var fcmDeviceId: String = ""

    @get:Exclude
    @ColumnInfo(name = "full_name")
    var name : String = ""

    @get:Exclude
    @ColumnInfo(name = "picture_url")
    var pictureUrl : String = ""
}