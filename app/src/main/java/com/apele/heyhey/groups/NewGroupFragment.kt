package com.apele.heyhey.groups

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apele.heyhey.HeyHeyApp
import com.apele.heyhey.R
import kotlinx.android.synthetic.main.fragment_new_group.view.*

/**
 * Created by alexandrupele on 05/06/2017.
 */
class NewGroupFragment : Fragment() {

    lateinit var adapter: FriendsPickerAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_new_group, container, false)

        adapter = FriendsPickerAdapter(context, R.layout.friend_item_flat, HeyHeyApp.friends!!)

        view.friendsListView.adapter = adapter
        view.friendsListView.adapter


        view.friendsListView.setOnItemClickListener { parent, view, position, id ->
            Log.d("","")
        }

        return view
    }
}