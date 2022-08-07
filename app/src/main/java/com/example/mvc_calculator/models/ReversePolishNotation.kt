package com.example.mvc_calculator.models

import java.util.*
import kotlin.math.*

class ReversePolishNotation() {
    fun solution(infixExpr: String): Double {
       return toCalc(toPostFixExpr(infixExpr))
    }

    private fun toCalc(postFixExpr: String):Double {
        val ref = Reference()
        val stack: Stack<Double> = Stack()
        while (ref.i < postFixExpr.length) {
            val c = postFixExpr[ref.i]
            if (c.isDigit()) {
                val num = getNumExpr(postFixExpr, ref)
                if (num.last() == '!')
                    stack.push(toFact(num.substring(0, num.length-1).toDouble()))
                else stack.push(num.toDouble())
            } else if (c.isLetter()) {
                val funStr = getFunStr(postFixExpr, ref)
                if (funStr == "pi") {
                    stack.push(Math.PI)
                    --ref.i
                }
                if (funStr == "e") {
                    stack.push(Math.E)
                    --ref.i
                }
                if (isFunc(funStr)) {
                    val arg: Double = if (stack.size > 0) stack.pop() else Double.NEGATIVE_INFINITY
                    if (arg != Double.NEGATIVE_INFINITY) {
                        stack.push(executeFun(funStr, arg))
                        println("$funStr($arg) = ${stack.peek()}")
                    }
                    --ref.i
                }
            } else if (isOp(c.toString())) {
                if (c == '~') {
                    val operand = if (stack.size > 0) stack.pop() else 0.0
                    stack.push(executeNum('-', 0.0, operand))
                    println("~$operand = ${stack.peek()}")
                    ++ref.i
                    continue
                }
                val second = if (stack.size > 0) stack.pop() else 0.0
                val first = if (stack.size > 0) stack.pop() else 0.0
                stack.push(executeNum(c, first, second))
                println("$first $c $second = ${stack.peek()}")
            }
            ++ref.i
        }
        if (stack.isEmpty()) {
            throw Exception("Returned value is empty!")
            return -1.0
        }
        return stack.pop()
    }

    private fun executeNum(op: Char, first: Double, second: Double): Double {
        var resFirst = first
        when (op) {
            '-' -> resFirst -= second
            '+' -> resFirst += second
            '*' -> resFirst *= second
            '/' -> resFirst /= second
            '^' -> resFirst = resFirst.pow(second)
        }
        return resFirst
    }

    private fun executeFun(token: String, arg: Double): Double {
        var res = arg
        when (token) {
            "sin" -> res = sin(res)
            "cos" -> res = cos(res)
            "tan" -> res = tan(res)
            "log" -> res = log10(res)
            "ln" -> res = ln(res)
            "sqrt" -> res = sqrt(res)
            "abs" -> res = abs(res)
        }
        return res
    }

    private fun toFact(arg: Double): Double {
        return if (arg == 0.0) 1.0 else {
            var res = 1
            for (i in 1..arg.toInt())
                res *= i
            res.toDouble()
        }
    }

    private fun toPostFixExpr(infixExpr: String): String {
        var postFixExpr = ""
        val stack: Stack<String> = Stack()
        val ref = Reference()
        while (ref.i < infixExpr.length) {
            val c = infixExpr[ref.i]
            if (c.isDigit()) {
                postFixExpr += getNumExpr(infixExpr, ref) + " "
            } else if (ref.i < infixExpr.length - 1 && c == '.' && !infixExpr[ref.i - 1].isDigit() &&
                infixExpr[ref.i + 1].isDigit()) {
                postFixExpr += "0${getNumExpr(infixExpr, ref)} "
            } else if (c.isLetter()) {
                val funStr = getFunStr(infixExpr, ref)
                if (isFunc(funStr)) {
                    stack.push("$funStr ")
                    --ref.i
                } else if (funStr == "pi") {
                    postFixExpr += "$funStr "
                    --ref.i
                } else if (funStr == "e")
                    postFixExpr += "$funStr "

            } else if (c == '(') {
                stack.push("(")
            } else if (c == ')') {
                while (stack.size > 0 && stack.peek() != "(")
                    postFixExpr += stack.pop()
                if (stack.size>0)stack.pop()
                if (stack.size > 0 && isFuncs(stack.peek()))
                    postFixExpr += stack.pop()
            } else if (isOp(c.toString())) {
                var op = c
                if (op == '-' && (ref.i == 0 || (ref.i > 1 && isOp(infixExpr[ref.i - 1].toString()))))
                    op = '~'
                while (stack.size > 0 && (isOp(stack.peek()) && priority(stack.peek()) >= priority(op.toString())))
                    postFixExpr += stack.pop()
                stack.push(op.toString())
            }
            ++ref.i
        }

        while (stack.isNotEmpty()) {
            if (stack.peek() == "(")
                stack.pop()
            else postFixExpr += stack.pop()
        }
        println("postfix expression: $postFixExpr")
        return postFixExpr
    }

    private fun getNumExpr(expr: String, pos: Reference): String {
        var number = ""
        while (pos.i < expr.length) {
            if (expr[pos.i].isDigit() || expr[pos.i] == '.' || expr[pos.i] == '!') {
                number += expr[pos.i]
                ++pos.i
            } else {
                --pos.i
                break
            }
        }
        return number
    }

    private fun getFunStr(expr: String, pos: Reference): String {
        var funName = ""
        while (expr[pos.i].isLetter()) {
            funName += expr[pos.i]
            if (pos.i == expr.length - 1)
                break
            ++pos.i
        }
        return funName
    }

    private fun isOp(c: String): Boolean {
        return when (c) {
            "(", "+", "-", "*", "/", "~", "^" -> true
            else -> false
        }
    }

    private  fun priority(op: String): Int {
        return when (op) {
            "(" -> 0
            "+", "-" -> 1
            "/", "*" -> 2
            "^" -> 3
            "~" -> 4
            else -> -1
        }
    }

    private fun isFunc(expr: String): Boolean {
        return when (expr) {
            "sin", "cos", "tan", "log", "ln", "sqrt" -> true
            else -> false
        }
    }

    private fun isFuncs(expr: String): Boolean {
        return when (expr) {
            "sin ", "cos ", "tan ", "log ", "ln ", "sqrt " -> true
            else -> false
        }
    }

    class Reference() {
        var i: Int = 0
    }

}