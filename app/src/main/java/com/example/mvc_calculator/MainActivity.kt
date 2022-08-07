package com.example.mvc_calculator

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mvc_calculator.controllers.Controller
import com.example.mvc_calculator.viewers.Viewer

class MainActivity : AppCompatActivity(), Viewer {
    private lateinit var display: EditText
    private lateinit var controller: Controller

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.input)
        display.showSoftInputOnFocus = false
        display.bottom = 0
        if (getString(R.string.display) == display.text.toString()) {
            display.setText("")
        }

        controller = Controller(this)
        val buttonSetPortrait = findViewById<ImageButton>(R.id.simpleCalc)
        val buttonSetLandscape = findViewById<ImageButton>(R.id.engineerCalc)
        buttonSetPortrait.setOnClickListener { controller.clicksOn(it) }
        buttonSetLandscape.setOnClickListener { controller.clicksOn(it) }
        // digits
        val zero = findViewById<Button>(R.id.zeroBtn)
        zero.setOnClickListener { controller.clicksOn(it) }
        val one = findViewById<Button>(R.id.oneBtn)
        one.setOnClickListener { controller.clicksOn(it) }
        val two = findViewById<Button>(R.id.twoBtn)
        two.setOnClickListener { controller.clicksOn(it) }
        val three = findViewById<Button>(R.id.threeBtn)
        three.setOnClickListener { controller.clicksOn(it) }
        val four = findViewById<Button>(R.id.fourBtn)
        four.setOnClickListener { controller.clicksOn(it) }
        val five = findViewById<Button>(R.id.fiveBtn)
        five.setOnClickListener { controller.clicksOn(it) }
        val six = findViewById<Button>(R.id.sixBtn)
        six.setOnClickListener { controller.clicksOn(it) }
        val seven = findViewById<Button>(R.id.sevenBtn)
        seven.setOnClickListener { controller.clicksOn(it) }
        val eight = findViewById<Button>(R.id.eightBtn)
        eight.setOnClickListener { controller.clicksOn(it) }
        val nine = findViewById<Button>(R.id.nineBtn)
        nine.setOnClickListener { controller.clicksOn(it) }

        // operations
        val scope = findViewById<Button>(R.id.scopesBtn)
        val divide = findViewById<Button>(R.id.divBtn)
        val multiply = findViewById<Button>(R.id.mulBtn)
        val add = findViewById<Button>(R.id.addBtn)
        val subtract = findViewById<Button>(R.id.subBtn)
        val pow = findViewById<Button>(R.id.powBtn)
        val point = findViewById<Button>(R.id.pointBtn)
        val sign = findViewById<Button>(R.id.signBtn)
        val equal = findViewById<Button>(R.id.equalBtn)
        val delClear = findViewById<Button>(R.id.clearBtn)
        scope.setOnClickListener { controller.scopeClick(display) }
        divide.setOnClickListener { controller.clicksOn(it) }
        multiply.setOnClickListener { controller.clicksOn(it) }
        add.setOnClickListener { controller.clicksOn(it) }
        pow.setOnClickListener { controller.clicksOn(it) }
        subtract.setOnClickListener { controller.clicksOn(it) }
        point.setOnClickListener { controller.clicksOn(it) }
        sign.setOnClickListener { controller.signClick(display) }
        equal.setOnClickListener { controller.evaluate(display, delClear) }
        delClear.setOnClickListener { controller.backspaceClick(display, it as Button) }

        //functions
        val sin = findViewById<Button>(R.id.sinBtn)
        val cos = findViewById<Button>(R.id.cosBtn)
        val tan = findViewById<Button>(R.id.tanBtn)
        val log = findViewById<Button>(R.id.logBtn)
        val ln = findViewById<Button>(R.id.lnBtn)
        val sqrt = findViewById<Button>(R.id.sqrtBtn)
        val pi = findViewById<Button>(R.id.piBtn)
        val fact = findViewById<Button>(R.id.factBtn)
        val e = findViewById<Button>(R.id.eBtn)
        sin?.setOnClickListener { controller.clicksOnFunc(it, display) }
        cos?.setOnClickListener { controller.clicksOnFunc(it, display) }
        tan?.setOnClickListener { controller.clicksOnFunc(it, display) }
        log?.setOnClickListener { controller.clicksOnFunc(it, display) }
        ln?.setOnClickListener { controller.clicksOnFunc(it, display) }
        sqrt?.setOnClickListener { controller.clicksOnFunc(it, display) }
        pi?.setOnClickListener { controller.clicksOnFunc(it, display) }
        fact?.setOnClickListener { controller.clicksOnFunc(it, display) }
        e?.setOnClickListener { controller.clicksOnFunc(it, display) }

    }

    override fun updateText(strAdd: String) {
        if (strAdd.isEmpty()) {
            return
        }
        val old = display.text.toString()
        val cursor: Int = display.selectionStart
        val leftStr = old.substring(0, cursor)
        val rightStr = old.substring(cursor)
        if (getString(R.string.display) == display.text.toString())
            display.setText(strAdd)
        else
            display.setText(String.format("%s%s%s", leftStr, strAdd, rightStr))
        display.setSelection(cursor + 1)
    }

    override fun showMsgText() {
        val toast = Toast.makeText(applicationContext, "Invalid format used.", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM, 0, 0)
        toast.show()
    }

    override fun requestedOrientation(view: View) {
        when (view.id) {
            R.id.engineerCalc -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            R.id.simpleCalc -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}