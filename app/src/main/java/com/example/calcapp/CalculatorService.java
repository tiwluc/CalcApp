package com.example.calcapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import android.os.Looper;
import android.os.RemoteException;
import android.os.Process;
import android.util.Log;

public class CalculatorService extends Service {
    private final Handler calchandler;
    private enum OperationType {
        ADD, SUBTRACT, MULTIPLY, DIVIDE
    }

    public CalculatorService() {
        this.calchandler = new Handler(Looper.getMainLooper());
    }
//    public interface CalculatorCallback {
//        void onCalculationComplete(double result);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        //binder = new CalculatorBinder();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("onBind", "Service onBind called");
        return binder;
    }
    private final ICalculatorService.Stub binder = new ICalculatorService.Stub() {
        private CalculatorCallback callback;
        public void setCallback(CalculatorCallback callback) {
            this.callback = callback;
        }
        public int getPid(){
            return Process.myPid();
        }
        @Override
        public void add(double num1,double num2){
            //Log.d(TAG, "Adding " + num1 + " and " + num2);
            Log.d("add","Adding" +num1 + " and " + num2);
            performOperationInBackground(num1,num2,OperationType.ADD,callback);
        }
        @Override
        public void substract(double num1,double num2){
            Log.d("subtract","subtract" +num1 + " and " + num2);
            performOperationInBackground(num1,num2,OperationType.SUBTRACT,callback);
        }
        @Override
        public void multiply(double num1,double num2){
            Log.d("multiply","multiply" +num1 + " and " + num2);
            performOperationInBackground(num1,num2,OperationType.MULTIPLY,callback);
        }
        @Override
        public void divide(double num1,double num2) throws RemoteException {
            if (num2 == 0) {
                throw new RemoteException("Division by zero is not allowed.");
            }
            Log.d("divide","divide" +num1 + " and " + num2);
            performOperationInBackground(num1,num2,OperationType.DIVIDE,callback);
        }
        public void basicTypes(int anInt, long aLong, boolean aBoolean,
                               float aFloat, double aDouble, String aString) {
            // Does nothing.
        }
        private void notifyCallback(double result) {
            try {
                callback.onCalculationComplete(result);
            } catch (RemoteException e) {
                e.printStackTrace(); // Handle the exception as needed
            }
        }
    private void performOperationInBackground(final double num1, final double num2, final OperationType operationType,CalculatorCallback callback) {
        calchandler.post(new Runnable() {
            @Override
            public void run() {
                double result = 0.0;
                switch (operationType) {
                    case ADD:
                        result = num1 + num2;
                        notifyCallback(result);
                        break;
                    case SUBTRACT:
                        result = num1 - num2;
                        notifyCallback(result);
                        break;
                    case MULTIPLY:
                        result = num1 * num2;
                        notifyCallback(result);
                        break;
                    case DIVIDE:
                        result = num1 / num2;
                        notifyCallback(result);
                        break;
                }
                try {
                    callback.onCalculationComplete(result);
                } catch (RemoteException e) {
                    e.printStackTrace(); // Handle the exception as needed
                }
            }
        });
    }
};
};
