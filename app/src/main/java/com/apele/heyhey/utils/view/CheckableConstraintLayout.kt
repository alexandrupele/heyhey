package com.apele.heyhey.utils.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable

/**
 * Created by alexandrupele on 05/06/2017.
 */
class CheckableConstraintLayout : ConstraintLayout, Checkable {

    private var isChecked: Boolean = false
    var views : MutableList<Checkable> = ArrayList()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun toggle() {
        isChecked = isChecked.not()

        views.forEach {
            it.toggle()
        }
    }

    override fun isChecked() = isChecked

    override fun setChecked(checked: Boolean) {
        this.isChecked = checked

        views.forEach {
            it.isChecked = checked
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        for (i in 0..childCount) {
            findCheckableChildren(getChildAt(i))
        }
    }

    fun findCheckableChildren(view: View?) {
        if (view is Checkable) {
            views.add(view)
        }

        if (view is ViewGroup) {
            for (i in 0..view.childCount) {
                findCheckableChildren(view.getChildAt(i))
            }
        }
    }
}