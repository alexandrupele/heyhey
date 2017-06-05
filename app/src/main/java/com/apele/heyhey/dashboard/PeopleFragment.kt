package com.apele.heyhey.dashboard

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.apele.heyhey.HeyHeyApp
import com.apele.heyhey.R
import com.apele.heyhey.model.User
import com.facebook.AccessToken
import com.facebook.GraphRequest
import kotlinx.android.synthetic.main.people_content.view.*
import org.json.JSONArray

/**
 * Created by alexandrupele on 02/06/2017.
 */
class PeopleFragment : Fragment() {

    val USERS_PER_ROW = 2

    lateinit var adapter: PeopleAdapter
    var listener: UserSelectionListener? = null
    var progressDialog: MaterialDialog? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.people_content, container, false)

        configureRecyclerView(view)

        showProgress(getString(R.string.loading_message_facebook_friends))
        getFriendsFromFacebook { friends ->
            friends.add(HeyHeyApp.currentUser!!)

            HeyHeyApp.friends = friends

            adapter.users = friends
            adapter.notifyDataSetChanged()
            hideProgress()
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is UserSelectionListener) {
            listener = context
        }
    }

    private fun configureRecyclerView(view: View) {
        adapter = PeopleAdapter(mutableListOf()) {
            listener?.onUserSelected(it)
        }

        view.recyclerView.layoutManager = GridLayoutManager(context, USERS_PER_ROW)
        view.recyclerView.adapter = adapter
    }

    private fun showProgress(message: String) {
        progressDialog = MaterialDialog.Builder(context)
                .title(getString(R.string.loading_title_please_wait))
                .content(message)
                .progress(true, 0)
                .show()
    }

    private fun hideProgress() {
        progressDialog?.dismiss()
    }

    private fun getFriendsFromFacebook(onFriendsLoaded: (friends: MutableList<User>) -> Unit) {
        val request = GraphRequest.newMyFriendsRequest(AccessToken.getCurrentAccessToken()) { jsonArray, _ ->
            onFriendsLoaded(convertToUsers(jsonArray))
        }

        val requestParameters = Bundle()
        requestParameters.putString("fields", "name,picture.type(large)")
        request.parameters = requestParameters

        request.executeAsync()
    }

    private fun convertToUsers(friends: JSONArray) : MutableList<User> {
        return friends.asSequence().map {
            val user = User()

            user.fbUserId = it.getString("id")
            user.name = it.getString("name")
            user.pictureUrl = it.getJSONObject("picture").getJSONObject("data").getString("url")

            user
        }.toMutableList()
    }
}