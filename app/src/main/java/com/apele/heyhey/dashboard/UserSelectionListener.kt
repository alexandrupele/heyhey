package com.apele.heyhey.dashboard

import com.apele.heyhey.model.User

/**
 * Created by alexandrupele on 05/06/2017.
 */
interface UserSelectionListener {
    fun onUserSelected(user: User)
}