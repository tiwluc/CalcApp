package com.example.calcapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.calcapp.databinding.ActivityMainBinding;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.ServiceConnection;
import android.os.RemoteException;

import com.example.calcapp.ICalculatorService;

public class MainActivity extends AppCompatActivity implements CalculatorCallback, IInterface {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private ICalculatorService calculatorService;
    private boolean isBound = false;

    private EditText num1EditText;
    private EditText num2EditText;
    private TextView resultTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        num1EditText = findViewById(R.id.num1EditText);
        num2EditText = findViewById(R.id.num2EditText);
        resultTextView = findViewById(R.id.resultTextView);

        Button addButton = findViewById(R.id.addButton);
        Button subtractButton = findViewById(R.id.subtractButton);
        Button multiplyButton = findViewById(R.id.multiplyButton);
        Button divideButton = findViewById(R.id.divideButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onClick", "performCalculation");
                performCalculation("add");
            }
        });

        subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCalculation("subtract");
            }
        });

        multiplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCalculation("multiply");
            }
        });

        divideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCalculation("divide");
            }
        });

        // Bind to the CalculatorService
        Intent intent = new Intent(this, CalculatorService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("onServiceConnected", "onServiceConnected");
            calculatorService = ICalculatorService.Stub.asInterface(service);
            isBound = true;
            // Set the callback for the service
            CalculatorCallback callback = MainActivity.this;
            try {
                calculatorService.setCallback(callback);
            }
            catch(RemoteException e){
                e.printStackTrace(); // Handle the exception as needed
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            calculatorService = null;
            isBound = false;
        }
    };

    private void performCalculation(String operation) {
        Log.i("performCalculation", "enters");
        if (isBound) {
            Log.i("performCalculation", "bound");
            double num1 = Double.parseDouble(num1EditText.getText().toString());
            double num2 = Double.parseDouble(num2EditText.getText().toString());

            try {
                switch (operation) {
                    case "add":
                        Log.i("performCalculation", "adding");
                        calculatorService.add(num1, num2);
                        //Log.d("performCalculation","result" +result);
                        break;
                    case "subtract":
                        Log.i("performCalculation", "subtract");
                        calculatorService.substract(num1, num2);
                        //Log.d("performCalculation","result" +result);
                        break;
                    case "multiply":
                        Log.i("performCalculation", "multiply");
                        calculatorService.multiply(num1, num2);
                        //Log.d("performCalculation","result" +result);
                        break;
                    case "divide":
                        Log.i("performCalculation", "divide");
                        calculatorService.divide(num1, num2);
                        //Log.d("performCalculation","result" +result);
                        break;
                }

                //resultTextView.setText(String.valueOf(result));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onCalculationComplete(double result) {
        // Handle the calculation result here in your activity
        Log.d("onCalculationComplete", "Calculation Result: " + result);
        resultTextView.setText(String.valueOf(result));

        // You can update the UI or perform any other actions with the result
        // For example, update a TextView with the result.
        // Example:
        // textViewResult.setText("Result: " + result);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    @Override
    public IBinder asBinder() {
        throw new UnsupportedOperationException("Not implemented");
    }
}