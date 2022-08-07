package com.example.mvc_calculator.controllers

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.mvc_calculator.models.Model
import com.example.mvc_calculator.viewers.Viewer

class Controller(_viewer: Viewer) {
    private val viewer: Viewer
    private val model: Model
    init {
        viewer = _viewer
        model = Model(viewer)
    }

    fun clicksOn(view: View) {
        model.onClick(view)
    }

    fun backspaceClick(display: EditText, view: Button) {
        model.backspaceOnClick(display, view)
    }

    fun scopeClick(display: EditText) {
        model.scopeOnClick(display)
    }

    fun evaluate(display: EditText, clrBtn: Button) {
        model.equalOnClick(display, clrBtn)
    }

    fun signClick(display: EditText) {
        model.changeSignOnClick(display)
    }

    //func op.
    fun clicksOnFunc(view: View, display: EditText) {
        model.clickOnFuncButton(view, display)
    }
}