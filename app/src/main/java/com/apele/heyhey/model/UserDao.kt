package com.apele.heyhey.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.reactivex.Flowable

/**
 * Created by alexandrupele on 29/05/2017.
 */
@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAll() : Flowable<List<User>>

    @Insert
    fun addUser(user : User)
}