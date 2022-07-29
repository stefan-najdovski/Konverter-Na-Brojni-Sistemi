package me.snajdovski.numsysconvert.utils

import java.util.*


object NumberConverter{

    private var allowedDecNumList = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    private var allowedOctNumList = arrayOf("0","1","2","3","4","5","6","7")
    private var allowedHexCharList = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")

    /*
    * It works fine right?
    * TODO : Change this Logic to a more universal approach
    *  This ripped off hacky way of doing is bad practice of code usability (just dont look at the else if statement in MainActivity)
    *  Better way would be with an algorithm that uses the base instead of this monstrosity of functions
    */

    fun decToBin(decNumberInput: String): String {
        var tempDecNumInput = decNumberInput
        var i = 0
        val n = tempDecNumInput.length
        while (i < n) {
            val ch = tempDecNumInput[i]
            val c = ch.toString()
            if (!listOf(*allowedDecNumList).contains(c)) {
                tempDecNumInput = "Грешка во полето за внес"
                return tempDecNumInput
            }
            i++
        }
        val d1 = tempDecNumInput.toInt()
        return Integer.toBinaryString(d1)
    }

    
    fun binToDec(binNumberInput: String): String {
        var tempBinNumberInput = binNumberInput
        var i = 0
        val n = tempBinNumberInput.length
        while (i < n) {
            val ch = tempBinNumberInput[i]
            val c = ch.toString()
            if ("1" != c && "0" != c) {
                tempBinNumberInput = "Грешка во полето за внес"
                return tempBinNumberInput
            }
            i++
        }
        val num = tempBinNumberInput.toInt(2)
        return num.toString()
    }
    
    fun decToHex(decNumberInput: String): String {
        var tempdecNumberInput = decNumberInput
        var i = 0
        val n = tempdecNumberInput.length
        while (i < n) {
            val ch = tempdecNumberInput[i]
            val c = ch.toString()
            if (!listOf(*allowedDecNumList).contains(c)) {
                tempdecNumberInput = "Грешка во полето за внес"
                return tempdecNumberInput
            }
            i++
        }
        val d3 = tempdecNumberInput.toInt()
        return Integer.toHexString(d3)
    }
    
    fun hexToDec(hexNumberInput: String): String {
        var tempHexNumberInput = hexNumberInput
        var i = 0
        val n = tempHexNumberInput.length
        while (i < n) {
            val ch = tempHexNumberInput[i]
            val c = ch.toString()
            if (!listOf(*allowedHexCharList).contains(c)) {
                tempHexNumberInput = "Грешка во полето за внес"
                return tempHexNumberInput
            }
            i++
        }
        val d3 = tempHexNumberInput.toInt(16)
        return d3.toString()
    }
    
    
    fun binToHex(binNumberInput: String): String {
        var tempBinNumberInput = binNumberInput
        var i = 0
        val n = tempBinNumberInput.length
        while (i < n) {
            val ch = tempBinNumberInput[i]
            val c = ch.toString()
            if ("1" != c && "0" != c) {
                tempBinNumberInput = "Грешка во полето за внес"
                return tempBinNumberInput
            }
            i++
        }
        val hex3 = tempBinNumberInput.toInt(2)
        return hex3.toString(16)
    }
    
    fun hexToBin(h4: String): String {
        var h4 = h4
        var i = 0
        val n = h4.length
        while (i < n) {
            val ch = h4[i]
            val c = ch.toString()
            if (!listOf(*allowedHexCharList).contains(c)) {
                h4 = "Грешка во полето за внес"
                return h4
            }
            i++
        }
        val bin4 = h4.toInt(16)
        return bin4.toString(2)
    }
    

    fun decToOct(dIn: String): String {
        var dIn = dIn
        var i = 0
        val n = dIn.length
        while (i < n) {
            val ch = dIn[i]
            val c = ch.toString()
            if (!Arrays.asList(*allowedDecNumList).contains(c)) {
                dIn = "Грешка во полето за внес"
                return dIn
            }
            i++
        }
        val d1 = dIn.toInt()
        return Integer.toOctalString(d1)
    }
    
    fun octToDec(hIn: String): String {
        var hIn = hIn
        var i = 0
        val n = hIn.length
        while (i < n) {
            val ch = hIn[i]
            val c = ch.toString()
            if (!listOf(*allowedOctNumList).contains(c)) {
                hIn = "Грешка во полето за внес"
                return hIn
            }
            i++
        }
        val d3 = hIn.toInt(8)
        return d3.toString()
    }

}