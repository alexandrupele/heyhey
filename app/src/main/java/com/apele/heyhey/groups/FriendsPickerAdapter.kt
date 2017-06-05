package com.apele.heyhey.groups

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.apele.heyhey.R
import com.apele.heyhey.model.User
import com.squareup.picasso.Picasso

/**
 * Created by alexandrupele on 05/06/2017.
 */
class FriendsPickerAdapter(context: Context,
                           val resource: Int,
                           val users: MutableList<User>) : ArrayAdapter<User>(context, resource, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view: View?
        val viewHolder: UserViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(parent!!.context).inflate(resource, parent, false)
            viewHolder = UserViewHolder(view)
            view.tag = viewHolder

        } else {
            view = convertView
            viewHolder = view.tag as UserViewHolder
        }

        viewHolder.userName.text = users[position].name
        viewHolder.userPicture.loadImage(users[position].pictureUrl)

        return view
    }

    override fun getItem(position: Int) = users[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = users.size

    private fun ImageView.loadImage(pictureUrl: String) {
        Picasso.with(context).load(pictureUrl).into(this)
    }

    internal class UserViewHolder(view: View?) {

        val userName: TextView = view?.findViewById(R.id.userNameTextView) as TextView
        val userPicture: ImageView = view?.findViewById(R.id.userPictureImageView) as ImageView
    }
}
