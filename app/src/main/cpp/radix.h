/**
https://github.com/wheelercj/Radix-Converter
MIT License

Copyright (c) 2018 Christopher Wheeler

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */


#ifndef RADIX_H
#define RADIX_H

#include <string>
#include <vector>
#include <jni.h>

namespace settings
{
    static std::string standardDigits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    const std::string DEFAULT_STANDARD_DIGITS = standardDigits;
    const std::string RESERVED_DIGITS = ".,- ";
    static int resultPrecision = 16;
    const int DEFAULT_RESULT_PRECISION = resultPrecision;

    void setStandardDigits(std::string);
    void setResultPrecision(int);
    std::string getStandardDigits();
    int getResultPrecision();
}

struct Vectors
{
    std::vector<int> whole, fraction;
    int size();
    int operator[](int);
};

std::string changeBase(std::string, std::string, std::string);
bool isInteger(std::string);
void assertInteger(std::string);
int charToInt(char);
Vectors splitNumeralsString(const std::string&);
void strToInts(const std::string, std::vector<int>&, int&, int&);
Vectors splitString(std::string);
int lcm(int, int);
int gcd(int, int);

class Digit
{
private:
    int digit;
    int base; // this program supports numbers of mixed bases
public:
    Digit();
    Digit(int, int);
    int get();
    void set(int);
    int getBase();
    void setBase(int);
};

class Part  // either the whole or fractional part of the number
{
protected:
    std::vector<Digit> part;
public:
    Part();
    ~Part();
    Digit operator[](int); // goes through the Number's Digits starting from the digit closest to the period
    int size();
    std::vector<Digit>::iterator begin();
    std::vector<Digit>::iterator end();
    void addPart(std::vector<int>, std::vector<int>);
    void addDigit(int, int);
};

class Whole : public Part
{
private:
    unsigned long long toDecimal();
    void fromDecimal(unsigned long long, std::vector<int>);
public:
    void changeBase(std::vector<int>);
};

class Fraction : public Part
{
private:
    void toDecimal();
    void fromDecimal(std::vector<int>);
public:
    void changeBase(std::vector<int>);
};

class Number
{
private:
    Whole whole;
    Fraction fraction;
    bool negative;
    bool numeralsOnly;
    Digit operator[](int); // goes through the Number's Digits in reading order
    bool detectNumerals(std::string, Vectors);
public:
    Number();
    void set(std::string, std::string);
    int size();
    void changeBase(std::string);
    std::string toString();
};

#endif // !RADIX_H
