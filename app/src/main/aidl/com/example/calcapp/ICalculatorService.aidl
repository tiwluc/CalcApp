// ICalculatorService.aidl
package com.example.calcapp;
import com.example.calcapp.CalculatorCallback;
// Declare any non-default types here with import statements

interface ICalculatorService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void setCallback(CalculatorCallback callback);
    int getPid();
    void add(double num1,double num2);
    void substract(double num1,double num2);
    void multiply(double num1,double num2);
    void divide(double num1,double num2);
    //int result();
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}