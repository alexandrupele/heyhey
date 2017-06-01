package com.apele.heyhey.message

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.apele.heyhey.R
import kotlinx.android.synthetic.main.activity_inbox.*

/**
 * Created by alexandrupele on 31/05/2017.
 */

const val INBOX_EXTRA_FROM = "from"
const val INBOX_EXTRA_MESSAGE = "message"

class InboxActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)

        senderTextView.text = intent.getStringExtra(INBOX_EXTRA_FROM)
        messageTextView.text  = intent.getStringExtra(INBOX_EXTRA_MESSAGE)

        dismissButton.setOnClickListener {
            finish()
        }
    }
}