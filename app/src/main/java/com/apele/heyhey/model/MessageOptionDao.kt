package com.apele.heyhey.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.apele.heyhey.message.MessageOption
import io.reactivex.Flowable

/**
 * Created by alexandrupele on 01/06/2017.
 */
@Dao
interface MessageOptionDao {

    @Query("SELECT * FROM message_options")
    fun getAll() : Flowable<List<MessageOption>>

    @Insert
    fun addMessage(message : MessageOption)

    @Insert
    fun addMessages(messages: List<MessageOption>)
}