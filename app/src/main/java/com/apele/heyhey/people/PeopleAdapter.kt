package com.apele.heyhey.people

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.apele.heyhey.R
import com.apele.heyhey.model.User

class PeopleAdapter(var users : MutableList<User>, val onUserClicked: (user: User) -> Unit) : RecyclerView.Adapter<PeopleViewHolder>() {

    override fun onBindViewHolder(holder: PeopleViewHolder?, position: Int) {
        holder?.bind(users[position], onUserClicked)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, p1: Int): PeopleViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.friend_item, parent, false)

        return PeopleViewHolder(view)
    }

    override fun getItemCount() = users.size
}