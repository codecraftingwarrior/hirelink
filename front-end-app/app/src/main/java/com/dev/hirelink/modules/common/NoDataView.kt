package com.dev.hirelink.modules.common

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.dev.hirelink.R

class NoDataView(private val context: Context, private val rootViewResourceId: Int, private val message: String? = null) {

    private val layoutInflater = LayoutInflater.from(context)
    private val rootView = (context as? Activity)?.findViewById<ViewGroup>(rootViewResourceId)
    private var noDataView: ViewGroup? = null
    private val msg = message ?: context.getString(R.string.no_data_msg_default)

    fun show() {
        if (noDataView == null && rootView != null) {
            noDataView = layoutInflater.inflate(R.layout.no_data_layout, rootView, false) as ViewGroup
            rootView.addView(noDataView)
            rootView.findViewById<TextView>(R.id.no_data_text).text = msg
        }
    }

    fun hide() {
        noDataView?.let {
            rootView?.removeView(it)
            noDataView = null
        }
    }
}