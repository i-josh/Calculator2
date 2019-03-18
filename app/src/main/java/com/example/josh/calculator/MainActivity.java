
package com.example.josh.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private Button equalButton, dotButton;

    private int[] numericButtons = {R.id.btn0,R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,R.id.btn5,R.id.btn6,R.id.btn7,R.id.btn8,R.id.btn9};
    private Button allClearButton, squareRootButton;
    private ImageButton clearButton;
    private int[] operatorButtons = {R.id.btnAdd,R.id.btnSubtract,R.id.btnDivide,R.id.btnMultiply};

    private boolean lastNumeric;
    private boolean stateError;
    private boolean lastDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.textView);
        allClearButton = findViewById(R.id.btnAc);
        clearButton = findViewById(R.id.btnC);
        equalButton = findViewById(R.id.btnEqual);
        dotButton = findViewById(R.id.btnDot);
        squareRootButton = findViewById(R.id.btnSquareRoot);

        allClearButton.setOnClickListener(allClearListener);
        clearButton.setOnClickListener(clearListener);
        equalButton.setOnClickListener(equalListener);
        dotButton.setOnClickListener(dotListener);
        squareRootButton.setOnClickListener(rootListener);

        setNumericOnClickListener();
        setOperatorOnclickListener();
    }
    private View.OnClickListener rootListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                String txt = display.getText().toString();
                if (txt.isEmpty()){
                    display.setText("");
                } else {
                    double res = Double.parseDouble(txt);
                    double ans = Math.sqrt(res);
                    display.setText(String.valueOf(ans));
                }
            }catch (Exception e){
                display.setText(e.getMessage());
            }
            lastNumeric = false;
            lastDot = false;
            stateError = false;

        }
    };

    private View.OnClickListener dotListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (lastNumeric && !stateError && !lastDot){
                display.append(".");
                lastNumeric = false;
                lastDot = true;
            }
        }
    };

    private View.OnClickListener allClearListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            display.setText("");
            lastNumeric = false;
            lastDot = false;
            stateError = false;
        }
    };

    private View.OnClickListener clearListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String displayText = display.getText().toString();
            if (displayText.isEmpty()){
                display.setText("");
                lastNumeric = false;
                lastDot = false;
                stateError = false;
            }else {
                String substring = displayText.substring(0, displayText.length() -1);
                display.setText(substring);
                lastNumeric = false;
                lastDot = false;
                stateError = false;
            }
        }
    };

    private View.OnClickListener equalListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onEqual();
        }
    };

    private void setNumericOnClickListener(){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                if (stateError){
                    display.setText(button.getText());
                    stateError = false;
                } else {
                    display.append(button.getText());
                }
                lastNumeric = true;
            }
        };
        for (int id : numericButtons){
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setOperatorOnclickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                if (lastNumeric && !stateError){
                    display.append(button.getText());
                    lastNumeric = false;
                    lastDot = false;
                }
            }
        };
        for (int id: operatorButtons){
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void onEqual(){
        if (lastNumeric && !stateError){
            String txt = display.getText().toString().replaceAll("×","*").replaceAll("÷","/");
            Expression expression = new ExpressionBuilder(txt).build();
            try {
                double result = expression.evaluate();
                display.setText(String.valueOf(result));
                lastDot = true;
            }catch (ArithmeticException e){
                display.setText("Error");
                stateError = true;
                lastNumeric = false;
            }
        }
    }
}

