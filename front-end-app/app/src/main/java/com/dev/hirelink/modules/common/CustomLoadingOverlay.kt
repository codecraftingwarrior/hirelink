package com.dev.hirelink.modules.common

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dev.hirelink.R

class CustomLoadingOverlay(private val context: Context, private val rootViewResourceId: Int) {

    private val layoutInflater = LayoutInflater.from(context)
    private val rootView = (context as? Activity)?.findViewById<ViewGroup>(rootViewResourceId)
    private var overlayView: ViewGroup? = null

    fun showLoading() {
        if (overlayView == null && rootView != null) {
            overlayView = layoutInflater.inflate(R.layout.loading_overlay, rootView, false) as ViewGroup
            rootView.addView(overlayView)
        }
    }

    fun hideLoading() {
        overlayView?.let {
            rootView?.removeView(it)
            overlayView = null
        }
    }
}