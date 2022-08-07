package com.example.mvc_calculator.viewers

import android.view.View

interface Viewer {

    fun updateText(strAdd: String)

    fun showMsgText()

    fun requestedOrientation(view: View)

}