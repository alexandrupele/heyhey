package com.apele.heyhey.launcher

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.apele.heyhey.HeyHeyApp
import com.apele.heyhey.model.User
import com.apele.heyhey.dashboard.DashboardActivity
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.startActivity
import java.util.*


class LauncherActivity : AppCompatActivity() {

    val LOGIN_REQUEST_CODE = 1994

    lateinit var getUser: Disposable
    lateinit var persistUser : Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkLoggedIn()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            LOGIN_REQUEST_CODE -> {
                bootstrapFromFacebook()
            }
        }
    }

    private fun checkLoggedIn() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            openLogInScreen()
        } else {
            if (getMemoryCachedUser() == null) {
                loadUserFromDb { user ->
                    getUser.dispose()

                    if (user == null) {
                        bootstrapFromFacebook()
                    } else {
                        updateDeviceIdIfAvailable(user)
                        cacheUserInMemory(user)
                        goToPeopleScreen()
                    }
                }
            } else {
                goToPeopleScreen()
            }
        }
    }

    private fun bootstrapFromFacebook() {
        getUserFromFacebook { user ->
            updateDeviceIdIfAvailable(user)
            cacheUserInMemory(user)

            persistUserInDb(user) {
                persistUser.dispose()
                goToPeopleScreen()
            }
        }
    }

    private fun updateDeviceIdIfAvailable(user: User) {
        getDeviceId()?.let {
            user.fcmDeviceId = it
            uploadUserToFirebase(user)
        }
    }

    private fun getDeviceId() : String? {
        return FirebaseInstanceId.getInstance().token
    }

    private fun cacheUserInMemory(user: User) {
        HeyHeyApp.currentUser = user
    }

    private fun getMemoryCachedUser() : User? {
        return HeyHeyApp.currentUser
    }

    private fun uploadUserToFirebase(user : User) {
        val db = FirebaseDatabase.getInstance().reference
        val fcmUserId = FirebaseAuth.getInstance().currentUser?.uid

        fcmUserId?.let {
            db.child("users").child(it).setValue(user)
        }
    }

    private fun loadUserFromDb(onUserLoaded : (user : User?) -> Unit) {
        val userDao = HeyHeyApp.database.userDao()

        getUser = userDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { listOfUsers ->
                    onUserLoaded(listOfUsers.firstOrNull())
                }
    }

    private fun openLogInScreen() {
        val ui = AuthUI.getInstance()
        val builder = ui.createSignInIntentBuilder()

        val facebookIdp = AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER)
                .setPermissions(Arrays.asList("user_friends"))
                .build()

        builder.setProviders(Arrays.asList(facebookIdp))
        builder.setIsSmartLockEnabled(false)

        startActivityForResult(builder.build(), LOGIN_REQUEST_CODE)
    }

    private fun getUserFromFacebook(onUserLoaded: (user: User) -> Unit) {
        val request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken()) { jsonObj, _ ->
            val user = User()

            user.fbUserId = jsonObj.getString("id")
            user.name = jsonObj.getString("name")
            user.pictureUrl = jsonObj.getJSONObject("picture").getJSONObject("data").getString("url")

            onUserLoaded(user)
        }

        val requestParameters = Bundle()
        requestParameters.putString("fields", "name,picture.type(large)")
        request.parameters = requestParameters

        request.executeAsync()
    }

    private fun persistUserInDb(user : User, onUserPersisted: () -> Unit) {
        persistUser = Single.fromCallable({
            HeyHeyApp.database.userDao().addUser(user)
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _ ->
                    onUserPersisted()
                }
    }

    private fun goToPeopleScreen() {
        startActivity<DashboardActivity>()
        finish()
    }
}
