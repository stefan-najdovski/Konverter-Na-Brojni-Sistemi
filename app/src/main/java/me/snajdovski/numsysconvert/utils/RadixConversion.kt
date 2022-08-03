import kotlin.math.pow

/**
 * @author stefan I modified this lib to work with Kotlin and this app, original author creadits: https://github.com/Mikhail-Polivakha
 *
 */

/**
 * This utility class provides an API for conversion numbers form one radix to another.
 * This class is capable for converting numbers without floating part, as well as
 * with floating part. For the better explanation see documentations of methods:
 *
 * [.convertFloatNumberFromOneBaseToAnother] - related for operations with numbers which contain floating numbers,
 * [.convertNumberFromOneBaseToAnother] - related for operations with numbers which does NOT contain floating part
 *
 * @author Mikhail Polivakha
 * @since 11.02.2021
 */

object RadixConversion {
    /**
     * Method that converts number with floating part from on radix to another. Both Radixes passed as parameters.
     * Number should be passed as string (see explanation) in parameter #numberYouWantToConvert documentation.
     *
     * @param numberYouWantToConvert - as the name suggests, number you want to convert as [String], because
     * if number you want to convert actually has the radix more the 10, then obviously
     * in such a number without any prohibition can present letters in UpperCase, for
     * example 'A' or 'E', depends on initial number radix
     * @param initialNumberBase - radix of the first parameter, I mean radix of the number you want to convert
     * @param resultNumberBase - radix in which you want to convert passed number
     * @return
     */
    fun convertFloatNumberFromOneBaseToAnother(
        numberYouWantToConvert: String,
        initialNumberBase: Int,
        resultNumberBase: Int
    ): String {
        val integerAndFloatPartOfNumber = numberYouWantToConvert.split(".").toTypedArray()
        val integerPart = integerAndFloatPartOfNumber[0]
        var floatPart = integerAndFloatPartOfNumber[1]

        val integerPartOfResultNumber =
            convertNumberFromOneBaseToAnother(integerPart, initialNumberBase, resultNumberBase)
        val floatingPartOfResultNumber = convertFloatPartOfNumberFromOneRadixToAnother(
            floatPart,
            initialNumberBase,
            resultNumberBase
        )
        return "$integerPartOfResultNumber.$floatingPartOfResultNumber"
    }

    private fun convertFloatPartOfNumberFromOneRadixToAnother(
        floatingPart: String,
        initialNumberBase: Int,
        resultNumberBase: Int
    ): String {
        val resultFloatPart: String
        resultFloatPart = if (initialNumberBase == 10) {
            getFloatingPartOfNumberWithInitial10Radix(floatingPart, resultNumberBase)
        } else {
            val resultFloatPartAsIntegerIn10Radix =
                getResultFloatPartAsStringIn10Radix(floatingPart, initialNumberBase).split(".")
                    .toTypedArray()[1]
            getFloatingPartOfNumberWithInitial10Radix(
                resultFloatPartAsIntegerIn10Radix,
                resultNumberBase
            )
        }
        return resultFloatPart
    }

    private fun getResultFloatPartAsStringIn10Radix(
        floatingPart: String,
        initialNumberBase: Int
    ): String {
        val digitsOfNumberFloatingPartAsChars = floatingPart.toCharArray()
        var resultFloatPartAsIntegerIn10Radix = 0f
        for (positionOfDigit in digitsOfNumberFloatingPartAsChars.indices) {
            resultFloatPartAsIntegerIn10Radix += (get10RadixValueFromSymbol(
                digitsOfNumberFloatingPartAsChars[positionOfDigit]
            ) *
                    Math.pow(
                        initialNumberBase.toDouble(),
                        (-1 * (positionOfDigit + 1)).toDouble()
                    )).toLong()
        }
        return resultFloatPartAsIntegerIn10Radix.toString()
    }

    private fun getFloatingPartOfNumberWithInitial10Radix(
        floatingPart: String,
        resultNumberBase: Int
    ): String {
        val resultFloatPart = StringBuilder()
        val defaultNumberOfDigitsAfterDot = 30
        var floatingPartOfNumberForCalculations = ("0.$floatingPart").toDouble()
        for (i in 0 until defaultNumberOfDigitsAfterDot) {
            floatingPartOfNumberForCalculations =
                floatingPartOfNumberForCalculations * resultNumberBase
            resultFloatPart.append(
                getCharacterFromNumber(
                    floatingPartOfNumberForCalculations.toInt().toLong(), resultNumberBase
                )
            )
            floatingPartOfNumberForCalculations -= floatingPartOfNumberForCalculations.toInt().toLong()
            if (!doPassedFloatNumberHaveFloatPart(floatingPartOfNumberForCalculations)) break
        }
        return resultFloatPart.toString()
    }

    private fun doPassedFloatNumberHaveFloatPart(resultNumberAfterOneMultiplyingIteration: Double): Boolean {
        val integerAndFloatPartOfPassedNumber =
            resultNumberAfterOneMultiplyingIteration.toString().split(".").toTypedArray()
        val floatPartOfPassedNumber = integerAndFloatPartOfPassedNumber[1]
        return isFirstDigitOfPassedStringNotEqualsToZero(floatPartOfPassedNumber) || isPassedFloatingPartContainsMoreThenOneDigit(
            floatPartOfPassedNumber
        )
    }

    private fun isFirstDigitOfPassedStringNotEqualsToZero(floatPartOfPassedNumber: String): Boolean {
        return Character.getNumericValue(floatPartOfPassedNumber[0]) != 0
    }

    private fun isPassedFloatingPartContainsMoreThenOneDigit(floatPartOfPassedNumber: String): Boolean {
        return floatPartOfPassedNumber.length != 1
    }

    fun convertNumberFromOneBaseToAnother(
        numberYouWantToConvert: String,
        initialNumberBase: Int,
        resultNumberBase: Int
    ): String {
        if (resultNumberBase == 10) {
            return get10BaseNumberFromPassedNumber(
                numberYouWantToConvert,
                initialNumberBase
            ).toString()
        } else {
            val inputNumberIn10Base =
                get10BaseNumberFromPassedNumber(numberYouWantToConvert, initialNumberBase)
            return convert10BaseNumberIntoNumberWithPassedBase(
                resultNumberBase,
                inputNumberIn10Base
            )
        }
    }

    private fun convert10BaseNumberIntoNumberWithPassedBase(
        resultNumberBase: Int,
        inputNumberIn10Base: Long
    ): String {
        var dividerResult = inputNumberIn10Base
        val resultNumber = StringBuilder()
        while (dividerResult > resultNumberBase) {
            val remainer =
                getCharacterFromNumber(dividerResult % resultNumberBase, resultNumberBase)
            dividerResult /= resultNumberBase.toLong()
            resultNumber.append(remainer)
        }
        return resultNumber.append(getCharacterFromNumber(dividerResult, resultNumberBase))
            .reverse().toString()
    }

    private fun getCharacterFromNumber(number: Long, radix: Int): Char {
        return Character.forDigit(number.toInt(), radix).uppercaseChar()
    }

    fun get10BaseNumberFromPassedNumber(
        numberYouWantToConvertTo10BaseAsString: String,
        initialBaseOfTheInputNumber: Int
    ): Long {
        verifyNumberForPresenceIllegalSymbolsInSuchRadixAndIfSoThrowException(
            numberYouWantToConvertTo10BaseAsString,
            initialBaseOfTheInputNumber
        )
        return if (initialBaseOfTheInputNumber > 10) {
            calculate10BasedLongNumberFromNumberWhichBaseIsHigherThen10(
                numberYouWantToConvertTo10BaseAsString,
                initialBaseOfTheInputNumber
            )
        } else {
            calculate10BasedLongNumberFromNumberWhichBaseIsLessOrEqualTo10(
                java.lang.Long.valueOf(numberYouWantToConvertTo10BaseAsString),
                initialBaseOfTheInputNumber
            )
        }
    }

    private fun verifyNumberForPresenceIllegalSymbolsInSuchRadixAndIfSoThrowException(
        numberYouWantToConvertTo10BaseAsString: String,
        initialBaseOfTheInputNumber: Int
    ) {
        val charactersLegalInPassedRadix =
            buildCharactersAllowedInPassedBaseArray(initialBaseOfTheInputNumber)
        for (symbolInNumber: Char in numberYouWantToConvertTo10BaseAsString.toCharArray()) {
            var isSymbolInNumberLegal = false
            for (legalCharacter: Char in charactersLegalInPassedRadix) {
                if (legalCharacter == symbolInNumber) {
                    isSymbolInNumberLegal = true
                    break
                }
            }
            if (!isSymbolInNumberLegal) {
                throwExceptionAboutPassedSymbolIsNotAllowedInPassedRadix(
                    symbolInNumber,
                    initialBaseOfTheInputNumber
                )
            }
        }
    }

    private fun throwNumberFormatExceptionWithMessageAboutInvalidSymbolInNumber(
        invalidSymbol: Char,
        radixInWhichPassedSymbolIsInvalid: Int
    ) {
        throw NumberFormatException(
            "This is impossible, that number with radix '" + radixInWhichPassedSymbolIsInvalid + "' will contain " +
                    " symbol: '" + invalidSymbol + "', and hence you see this exception message"
        )
    }

    private fun calculate10BasedLongNumberFromNumberWhichBaseIsHigherThen10(
        numberYouWantToConvertTo10Base: String,
        initialBaseOfTheInputNumber: Int
    ): Long {
        val charactersAllowedInInputNumberBase =
            buildCharactersAllowedInPassedBaseArray(initialBaseOfTheInputNumber)
        val charactersOfNumberWeWantToConvert = numberYouWantToConvertTo10Base.toCharArray()
        var result10BaseNumber = 0L
        for (indexFromTheBeginning in charactersOfNumberWeWantToConvert.indices) {
            val currentSymbol =
                charactersOfNumberWeWantToConvert[charactersOfNumberWeWantToConvert.size - (indexFromTheBeginning + 1)]
            verifySymbolIsPresentInTheAllowedCharactersInThisBaseArrayOrElseThrowException(
                charactersAllowedInInputNumberBase,
                currentSymbol,
                initialBaseOfTheInputNumber
            )
            val valueOfTheCurrentCharIn10Radix = get10RadixValueFromSymbol(currentSymbol)
            result10BaseNumber += (valueOfTheCurrentCharIn10Radix * initialBaseOfTheInputNumber.toDouble()
                .pow(indexFromTheBeginning.toDouble())).toLong()
        }
        return result10BaseNumber
    }

    private fun get10RadixValueFromSymbol(currentSymbol: Char): Int {
        if (!Character.isDigit(currentSymbol)) {
            when (currentSymbol) {
                'A' -> return 10
                'B' -> return 11
                'C' -> return 12
                'D' -> return 13
                'E' -> return 14
                'F' -> return 15
                else -> throwExceptionAboutNumberRadixIsHigherThen16(currentSymbol)
            }
        } else {
            return Character.getNumericValue(currentSymbol)
        }
        return 0
    }

    private fun throwExceptionAboutNumberRadixIsHigherThen16(currentSymbol: Char) {
        throw NumberFormatException(
            ("Symbol '" + currentSymbol + "' cannot be a part of an 16 or less radix number. " +
                    "As mentioned in documentation, this library is working only with numbers less or equal then 16")
        )
    }

    private fun verifySymbolIsPresentInTheAllowedCharactersInThisBaseArrayOrElseThrowException(
        charactersAllowedInInputNumberBase: CharArray,
        currentSymbolToVerify: Char,
        initialBaseOfTheInputNumber: Int
    ) {
        var isPassedCharAllowed = false
        for (charInArrayWithAllowedSymbols: Char in charactersAllowedInInputNumberBase) {
            if (isPassedCharAllowed) return
            if (charInArrayWithAllowedSymbols == currentSymbolToVerify) isPassedCharAllowed = true
        }
        if (!isPassedCharAllowed) {
            throwExceptionAboutPassedSymbolIsNotAllowedInPassedRadix(
                currentSymbolToVerify,
                initialBaseOfTheInputNumber
            )
        }
    }

    private fun throwExceptionAboutPassedSymbolIsNotAllowedInPassedRadix(
        invalidSymbol: Char,
        radix: Int
    ) {
        throw NumberFormatException(
            ("You passed a number with symbol '" + invalidSymbol + "', but " + "in number with radix '" +
                    radix + "' such a symbol is not allowed")
        )
    }

    private fun buildCharactersAllowedInPassedBaseArray(initialBaseOfTheInputNumber: Int): CharArray {
        val arrayOfCharactersToReturn = CharArray(initialBaseOfTheInputNumber)
        for (i in 0 until initialBaseOfTheInputNumber) {
            arrayOfCharactersToReturn[i] =
                Character.forDigit(i, initialBaseOfTheInputNumber).uppercaseChar()
        }
        return arrayOfCharactersToReturn
    }

    private fun calculate10BasedLongNumberFromNumberWhichBaseIsLessOrEqualTo10(
        numberYouWantToConvertTo10Base: Long,
        inputNumberBase: Int
    ): Long {
        val numberToConvertAsString = numberYouWantToConvertTo10Base.toString()
        val digitsOfInputNumberAsCharArray = numberToConvertAsString.toCharArray()
        return calculate10BasedLongNumberFromCharDigitsArray(
            digitsOfInputNumberAsCharArray,
            inputNumberBase
        )
    }

    private fun calculate10BasedLongNumberFromCharDigitsArray(
        digits: CharArray,
        inputNumberBase: Int
    ): Long {
        var resultNumberToReturn = 0L
        for (indexOfDigitInNumberFromTheBeginning in digits.indices) {
            val currentDigit =
                Character.getNumericValue(digits[digits.size - (indexOfDigitInNumberFromTheBeginning + 1)])
            resultNumberToReturn += (currentDigit * Math.pow(
                inputNumberBase.toDouble(),
                indexOfDigitInNumberFromTheBeginning.toDouble()
            ).toInt()).toLong()
        }
        return resultNumberToReturn
    }
}