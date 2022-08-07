package com.example.mvc_calculator.models

import android.text.SpannableStringBuilder
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.mvc_calculator.R
import com.example.mvc_calculator.viewers.Viewer

class Model(_viewer: Viewer) {
    private val viewer: Viewer
    private val rpn: ReversePolishNotation
    private var eqClicked = false
    private var inSin = false
    private var inCos = false
    private var inTan = false
    private var inLog = false
    private var inLn = false
    private var inSqrt = false

    init {
        viewer = _viewer
        rpn = ReversePolishNotation()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.zeroBtn -> viewer.updateText("0")
            R.id.oneBtn -> viewer.updateText("1")
            R.id.twoBtn -> viewer.updateText("2")
            R.id.threeBtn -> viewer.updateText("3")
            R.id.fourBtn -> viewer.updateText("4")
            R.id.fiveBtn -> viewer.updateText("5")
            R.id.sixBtn -> viewer.updateText("6")
            R.id.sevenBtn -> viewer.updateText("7")
            R.id.eightBtn -> viewer.updateText("8")
            R.id.nineBtn -> viewer.updateText("9")
            R.id.divBtn -> viewer.updateText("÷")
            R.id.mulBtn -> viewer.updateText("×")
            R.id.addBtn -> viewer.updateText("+")
            R.id.subBtn -> viewer.updateText("-")
            R.id.pointBtn -> viewer.updateText(",")
            R.id.powBtn -> viewer.updateText("^")
            R.id.simpleCalc -> viewer.requestedOrientation(view)
            R.id.engineerCalc -> viewer.requestedOrientation(view)
        }
    }

    fun changeSignOnClick(display: EditText) {
        val cursor = display.selectionStart
        var i = cursor - 1
        var digitPos = cursor
        if (display.text.toString().isEmpty()) {
            viewer.updateText("(")
            viewer.updateText("-")
            return
        }
        if (isOp(display.text.toString()[i])) {
            viewer.updateText("(")
            viewer.updateText("-")
        } else if (display.text.toString()[i] == ')') {
            viewer.updateText("×")
            viewer.updateText("(")
            viewer.updateText("-")
        } else {
            while (i >= 0) {
                if (display.text.toString()[i].isDigit() || display.text.toString()[i] == ','
                    || display.text.toString()[i] == 'E' || display.text.toString()[i] == '.')
                    --digitPos
                else break
                --i
            }
            display.setSelection(digitPos)
            viewer.updateText("(")
            viewer.updateText("-")
            display.setSelection(display.text.toString().length)
        }
    }

    fun backspaceOnClick(display: EditText, view: Button) {
        if (eqClicked) {
            display.textSize = 40F
            display.setText("")
            view.text = "DEL"
            eqClicked = false
        } else {
            val cursor = display.selectionStart
            val textLen = display.text.toString().length

            if (cursor != 0 && textLen != 0) {
                val selection: SpannableStringBuilder = display.text as SpannableStringBuilder
                selection.replace(cursor - 1, cursor, "")
                display.text = selection
                display.setSelection(cursor - 1)
            }
        }
    }

    fun scopeOnClick(display: EditText) {
        if (inSin || inCos || inTan || inLog || inLn || inSqrt) {
            display.setSelection(display.selectionStart + 1)
            inSin = false
            inCos = false
            inTan = false
            inLog = false
            inLn = false
            inSqrt = false
            return
        }
        val cursor = display.selectionStart
        val textLen = display.text.toString().length
        var closedScopes = 0
        var openedScopes = 0
        for (i in 0 until cursor) {
            if (display.text.toString()[i] == '(') {
                openedScopes++
            }
            if (display.text.toString()[i] == ')') {
                closedScopes++
            }
        }
        if (closedScopes == openedScopes || display.text.toString()[textLen - 1] == '(') {
            viewer.updateText("(")
        }
        if (closedScopes < openedScopes && display.text.toString()[textLen - 1] != '(') {
            viewer.updateText(")")
        }
        display.setSelection(cursor + 1)
    }

    fun equalOnClick(display: EditText, clrButton: Button) {
        clrButton.text = "CLR"
        eqClicked = true
        val cursor = display.selectionStart
        for (i in 0 until cursor - 1) {
            if (isOp(display.text.toString()[i]) && isOp(display.text.toString()[i + 1]) ||
                display.text.toString()[i] == ',' && display.text.toString()[i + 1] == ',') {
                viewer.showMsgText()
                return
            }
        }

        if (display.text.toString().isEmpty()) {
            viewer.showMsgText()
            return
        }
        if (display.text.toString().length == 1 && (isOp(display.text.toString()[0]) ||
                    display.text.toString()[0] == '(' || display.text.toString()[0] == '.'
                    || display.text.toString()[0] == ',')) {
            display.setText("")
            viewer.showMsgText()
            return
        }
        display.textSize = 50F

        //evaluate..
        calcExpr(display)
    }

    private fun isOp(c: Char): Boolean {
        return when (c) {
            '^', '÷', '×', '+', '-' -> true
            else -> false
        }
    }

    private fun calcExpr(display: EditText) {
        var expr = display.text.toString()
        expr = expr.replace("÷".toRegex(), "/")
        expr = expr.replace("×".toRegex(), "*")
        expr = expr.replace(",".toRegex(), ".")
        expr = expr.replace("√".toRegex(), "sqrt")
        expr = expr.replace("π".toRegex(), "pi")
        val res: Double = try {
            rpn.solution(expr)
        } catch (e: Exception) {
            return
        }
        if (res == Double.POSITIVE_INFINITY) {
            display.setText("Infinity")
            display.setSelection(display.text.toString().length)
            return
        }
        if (res == Double.NEGATIVE_INFINITY) {
            display.setText("-Infinity")
            display.setSelection(display.text.toString().length)
            return
        }
        val leftCh = res.toString().split(".")[1]
        if (leftCh.length == 1 && leftCh == "0") {
            display.setText(res.toInt().toString())
            display.setSelection(res.toInt().toString().length)
        } else {
            var resStr = res.toString()
            if (resStr.indexOf('E') == -1)
                resStr = resStr.replace('.', ',')
            display.setText(resStr)
            display.setSelection(resStr.length)
        }
    }

    //func op.
    fun clickOnFuncButton(view: View, display: EditText) {
        val cursor = display.selectionStart
        when(view.id) {
            R.id.sinBtn -> {
                inSin = true
                val signature = "sin()"
                display.setText(signature)
                display.setSelection(cursor + signature.length - 1)
            }
            R.id.cosBtn -> {
                inCos = true
                val signature = "cos()"
                display.setText(signature)
                display.setSelection(cursor + signature.length - 1)
            }
            R.id.tanBtn -> {
                inTan = true
                val signature = "tan()"
                display.setText(signature)
                display.setSelection(cursor + signature.length - 1)
            }
            R.id.logBtn -> {
                inLog = true
                val signature = "log()"
                display.setText(signature)
                display.setSelection(cursor + signature.length - 1)
            }
            R.id.lnBtn -> {
                inLn = true
                val signature = "ln()"
                display.setText(signature)
                display.setSelection(cursor + signature.length - 1)
            }
            R.id.sqrtBtn -> {
                inSqrt = true
                val signature = "√()"
                display.setText(signature)
                display.setSelection(cursor + signature.length - 1)
            }
            R.id.factBtn -> {
                viewer.updateText("!")
            }
            R.id.eBtn -> {
                viewer.updateText("e")
            }
            R.id.piBtn -> {
                viewer.updateText("π")
            }

        }
    }
}