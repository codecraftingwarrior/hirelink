package com.dev.hirelink.modules.common

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dev.hirelink.R

class NoDataView(private val context: Context, private val rootViewResourceId: Int) {

    private val layoutInflater = LayoutInflater.from(context)
    private val rootView = (context as? Activity)?.findViewById<ViewGroup>(rootViewResourceId)
    private var noDataView: ViewGroup? = null

    fun show() {
        if (noDataView == null && rootView != null) {
            noDataView = layoutInflater.inflate(R.layout.no_data_layout, rootView, false) as ViewGroup
            rootView.addView(noDataView)
        }
    }

    fun hide() {
        noDataView?.let {
            rootView?.removeView(it)
            noDataView = null
        }
    }
}