package com.apele.heyhey.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.apele.heyhey.message.MessageOption

/**
 * Created by alexandrupele on 29/05/2017.
 */
@Database(entities = arrayOf(User::class, MessageOption::class), version = 2)
abstract class Database : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun messageOptionDao(): MessageOptionDao
}