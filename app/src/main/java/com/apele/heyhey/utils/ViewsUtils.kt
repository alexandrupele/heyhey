package com.apele.heyhey.utils

import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import com.apele.heyhey.R

/**
 * Created by alexandrupele on 01/06/2017.
 */

class ViewsUtils {

    companion object {
        fun createOKSnackBar(layout: View, message: String) : Snackbar {
            val snackBar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT)
            
            snackBar.view.setBackgroundColor(ContextCompat.getColor(layout.context, R.color.colorAccent))
            snackBar.setAction("OK", {})
            snackBar.setActionTextColor(ContextCompat.getColor(layout.context, R.color.white))
            
            return snackBar
        }
    }
}