package com.apele.heyhey.model

import android.arch.persistence.room.*
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

    @Delete
    fun delete(user: User)
}