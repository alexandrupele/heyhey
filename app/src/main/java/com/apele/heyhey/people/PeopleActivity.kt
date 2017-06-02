package com.apele.heyhey.people

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.apele.heyhey.HeyHeyApp
import com.apele.heyhey.R
import com.apele.heyhey.launcher.LauncherActivity
import com.apele.heyhey.message.EXTRA_MESSAGE
import com.apele.heyhey.message.MessagePickerActivity
import com.apele.heyhey.model.User
import com.apele.heyhey.utils.ViewsUtils
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_people.*
import kotlinx.android.synthetic.main.people_content.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by alexandrupele on 28/05/2017.
 */

class PeopleActivity : AppCompatActivity() {

    val PICK_MESSAGE_REQUEST = 1994
    val USERS_PER_ROW = 2

    lateinit var adapter: PeopleAdapter
    lateinit var selectedUser: User
    lateinit var drawerToggle: ActionBarDrawerToggle
    var progressDialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people)

        configureNavigationDrawer()
        configureRecyclerView()

        showProgress(getString(R.string.loading_message_facebook_friends))
        getFriendsFromFacebook { friends ->
            friends.add(HeyHeyApp.currentUser!!)

            adapter.users = friends
            adapter.notifyDataSetChanged()
            hideProgress()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PICK_MESSAGE_REQUEST -> {
                when (resultCode) {
                    RESULT_OK -> {
                        data?.getStringExtra(EXTRA_MESSAGE)?.let { message ->
                            if (selectedUser.fcmDeviceId.isEmpty()) {
                                getDeviceIdForSelectedUser {
                                    if (it == null) {
                                        Toast.makeText(this@PeopleActivity, "Cannot send", Toast.LENGTH_SHORT).show()
                                    } else {
                                        selectedUser.fcmDeviceId  = it
                                        send(message)
                                    }
                                }
                            } else {
                                send(message)
                            }
                        }

                    }
                }
            }
        }
    }

    private fun onUserClicked(user: User) {
        selectedUser = user
        startActivityForResult<MessagePickerActivity>(PICK_MESSAGE_REQUEST);
    }

    private fun getDeviceIdForSelectedUser(onDeviceIdLoaded: (deviceId: String?) -> Unit) {
        val db = FirebaseDatabase.getInstance().reference

        db.child("users")
                .orderByChild("fbUserId")
                .equalTo(selectedUser.fbUserId)
                .limitToFirst(1)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError?) {
                        onDeviceIdLoaded(null)
                    }

                    override fun onDataChange(snapshot: DataSnapshot?) {
                        if (snapshot!!.hasChildren()) {
                            val deviceId = snapshot.children.first().child("fcmDeviceId").getValue(String::class.java)
                            onDeviceIdLoaded(deviceId)
                        }
                    }
                })
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

    private fun send(message: String) {
        showProgress(getString(R.string.loading_message_fcm_push))

        val friendName = selectedUser.name
        HeyHeyApp.notificationManager.send(message = message,
                title = HeyHeyApp.currentUser!!.name,
                targetDeviceId = selectedUser.fcmDeviceId) {
                hideProgress()
                ViewsUtils.createOKSnackBar(coordinatorLayout, "Sent message to $friendName").show()
        }
    }

    private fun configureRecyclerView() {
        adapter = PeopleAdapter(mutableListOf()) {
            onUserClicked(it)
        }

        recyclerView.layoutManager = GridLayoutManager(this, USERS_PER_ROW)
        recyclerView.adapter = adapter
    }

    private fun configureNavigationDrawer() {
        drawerToggle = ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_opened, R.string.drawer_close)

        drawerLayout.addDrawerListener(drawerToggle)

        val drawerItems = listOf("Logout")
        leftDrawer.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawerItems)
        leftDrawer.setOnItemClickListener({ _, _, position, _ ->
            when (drawerItems[position]) {
                "Logout" -> {
                    logout()
                }
            }
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setHomeButtonEnabled(true);
        drawerToggle.syncState();
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        LoginManager.getInstance().logOut()

        Single.fromCallable {
            val userDao = HeyHeyApp.database.userDao()
            userDao.delete(HeyHeyApp.currentUser!!)
            HeyHeyApp.currentUser = null
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _ ->
                    startActivity(intentFor<LauncherActivity>().clearTop())
                    finish()
                }
    }

    private fun showProgress(message: String) {
        progressDialog = MaterialDialog.Builder(this)
                .title(getString(R.string.loading_title_please_wait))
                .content(message)
                .progress(true, 0)
                .show()
    }

    private fun hideProgress() {
        progressDialog?.dismiss()
    }
}

fun JSONArray.asSequence(): Sequence<JSONObject> = object: Sequence<JSONObject> {

    override fun iterator() = object: Iterator<JSONObject> {

        val it = (0..this@asSequence.length() - 1).iterator()

        override fun next(): JSONObject {
            val i = it.next()
            return this@asSequence.getJSONObject(i)
        }

        override fun hasNext() = it.hasNext()
    }
}
