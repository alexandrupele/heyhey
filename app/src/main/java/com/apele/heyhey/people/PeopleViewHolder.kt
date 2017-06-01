package com.apele.heyhey.people

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.apele.heyhey.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.friend_item.view.*

/**
 * Created by alexandrupele on 28/05/2017.
 */
class PeopleViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(user: User, onUserClicked : (user: User) -> Unit) {
        with (itemView) {
            nameTextView.text = user.name
            avatarImageView.loadImage(user.pictureUrl)
            setOnClickListener { _ ->
                onUserClicked(user)
            }
        }
    }

    private fun ImageView.loadImage(pictureUrl: String) {
        Picasso.with(context).load(pictureUrl).into(this)
    }
}

