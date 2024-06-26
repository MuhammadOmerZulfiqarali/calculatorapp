package com.codebase.calculator_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity



class MainActivity : AppCompatActivity() {
    private var canAddOperation = false
    private var canAddDecimal = true
    lateinit var workingTv: TextView
    lateinit var resultTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workingTv = findViewById(R.id.workingsTV)
        resultTv = findViewById(R.id.resultsTV)
    }

    @SuppressLint("SuspiciousIndentation")
    fun numberAction(view: View) {
        if (view is Button) {
            if(view.text == ".") {
                if (canAddDecimal)
                    workingTv.append(view.text)

                canAddDecimal = false
            }
            else

            workingTv.append(view.text)
            canAddOperation = true

        }
    }

    fun operationAction(view: View) {
        if (view is Button && canAddOperation) {
            val operation = view.text.toString()
            workingTv.append(view.text)
            if (operation == "*" || operation == "+" || operation == "-" || operation == "/")
                workingTv.append(operation)
            canAddOperation = false
            canAddDecimal = true

        }

    }

    fun allClearAction(view: View) {
        workingTv.text = ""
        resultTv.text = ""
    }

    fun backSpaceAction(view: View) {
        val length = workingTv.length()
        if (length > 0)
            workingTv.text = workingTv.text.subSequence(0,  length - 1)
    }

    fun equalsAction(view: View) {
        resultTv.text = calculateResults()
    }

    private fun calculateResults(): String
    {
        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList
        while (list.contains('x') || list.contains('/'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices)
        {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when (operator)
                {
                    'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }

                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1

                    }

                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }

                }
            }

            if (i > restartIndex)
                newList.add(passedList[i])

        }
        return newList

    }

    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in workingTv.text) {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if (currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }
}
