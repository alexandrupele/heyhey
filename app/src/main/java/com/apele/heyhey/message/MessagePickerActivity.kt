package com.apele.heyhey.message

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import com.apele.heyhey.R
import kotlinx.android.synthetic.main.activity_message_picker.*
import com.afollestad.materialdialogs.MaterialDialog
import android.text.InputType
import android.view.Display
import android.view.MenuItem
import com.apele.heyhey.HeyHeyApp
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by alexandrupele on 28/05/2017.
 */

const val EXTRA_MESSAGE = "extra.msg"

class MessagePickerActivity : ListActivity() {

    lateinit var adapter: ArrayAdapter<MessageOption>
    var progressDialog: MaterialDialog? = null
    var messageOptions = mutableListOf<MessageOption>()
    var getMessages : Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_picker)

        setActionBar(toolbar)
        actionBar.setDisplayHomeAsUpEnabled(true)
        title = "Pick a message"

        adapter = ArrayAdapter<MessageOption>(this, android.R.layout.simple_list_item_1, messageOptions)
        listAdapter = adapter

        fab_meu.setClosedOnTouchOutside(true)

        fabCompose.setOnClickListener {
            fab_meu.close(true)

            showDialog("Send custom message", "Send") {
                sendMessageResult(it)
            }
        }

        fabAddMessage.setOnClickListener {
            fab_meu.close(true)

            showDialog("Save as predefined", "Save") { message ->
                showProgress()
                savePredefinedMessageInDb(message) {
                    messageOptions.add(message.msg)
                    adapter.notifyDataSetChanged()
                    hideProgress()
                }
            }
        }

        getMessageOptionsFromDb {
            getMessages?.dispose()
            if (it.isEmpty()) {
                insertDefaultMessages {
                    getMessageOptionsFromDb {
                        getMessages?.dispose()
                        messageOptions.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                }
            } else {
                messageOptions.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true;
        }

        return super.onOptionsItemSelected(item)
    }

    private fun savePredefinedMessageInDb(message: String, onMessageSaved: () -> Unit) {
        val messageOptionDao = HeyHeyApp.database.messageOptionDao()

        Single.fromCallable {
            messageOptionDao.addMessage(message.msg)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _ ->
                    onMessageSaved()
                }
    }

    private fun showDialog (title: String, buttonText: String, onInputRetrieved: (text: String) -> Unit) {
        MaterialDialog.Builder(this)
                .title(title)
                .positiveText(buttonText)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Your message goes here", null, { _, message ->
                    onInputRetrieved(message.toString())
                }).show()
    }

    private fun insertDefaultMessages(onInsertionComplete: () -> Unit) {
        val messageOptionDao = HeyHeyApp.database.messageOptionDao()

        Single.fromCallable {
            val defaults = resources.getStringArray(R.array.default_messages).toList().map {
                it.msg
            }

            messageOptionDao.addMessages(defaults)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _ ->
                    onInsertionComplete()
                }
    }


    private fun getMessageOptionsFromDb(onMessagesLoaded: (messages: List<MessageOption>) -> Unit) {
        val messageOptionDao = HeyHeyApp.database.messageOptionDao()

        getMessages = messageOptionDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { listOfMessageOptions ->
                    onMessagesLoaded(listOfMessageOptions)
                }
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        sendMessageResult(messageOptions[position].text)
    }

    private fun sendMessageResult(message: String) {
        val intent = Intent()
        intent.putExtra(EXTRA_MESSAGE, message)

        setResult(RESULT_OK, intent)
        finish()
    }

    private fun showProgress() {
        progressDialog = MaterialDialog.Builder(this)
                .title("Sending")
                .content("Please wait")
                .progress(true, 0)
                .show()
    }

    private fun hideProgress() {
        progressDialog?.dismiss()
    }
}

private val String.msg: MessageOption
    get() {
        val messageOption = MessageOption()
        messageOption.text = this

        return messageOption
    }

