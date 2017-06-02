package com.apele.heyhey.groups

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apele.heyhey.R
import kotlinx.android.synthetic.main.fragment_groups.*

/**
 * Created by alexandrupele on 02/06/2017.
 */
class GroupsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_groups, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createButton.setOnClickListener {

        }

        groupsRecyclerView.layoutManager = LinearLayoutManager(context)
        groupsRecyclerView.adapter = GroupsAdapter(mutableListOf())
    }
}