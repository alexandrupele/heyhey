package com.apele.heyhey.message

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by alexandrupele on 01/06/2017.
 */
@Entity(tableName = "message_options")
class MessageOption {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var text: String = ""

    override fun toString(): String = text
}