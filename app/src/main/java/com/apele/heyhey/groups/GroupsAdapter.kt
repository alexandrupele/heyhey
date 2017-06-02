package com.apele.heyhey.groups

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.apele.heyhey.R

/**
 * Created by alexandrupele on 02/06/2017.
 */

class GroupsAdapter(var groups: MutableList<Group>) : RecyclerView.Adapter<GroupViewHolder>() {

    override fun onBindViewHolder(holder: GroupViewHolder?, position: Int) {
        holder?.bind(groups[position])
    }

    override fun getItemCount(): Int = groups.size

    override fun onCreateViewHolder(parent: ViewGroup?, position: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.group_item, parent, false)

        return GroupViewHolder(view)
    }
}