package com.example.cs5520_spring2025_a1_ycaptain;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class QuicCalcActivity extends AppCompatActivity {
    private TextView displayText;
    private StringBuilder currentExpression = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quic_calc);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Quic Calc");
        }

        displayText = findViewById(R.id.displayText);
        displayText.setText("CALC");

        // Set click listeners for all number and operator buttons
        setButtonClickListener(R.id.button0, "0");
        setButtonClickListener(R.id.button1, "1");
        setButtonClickListener(R.id.button2, "2");
        setButtonClickListener(R.id.button3, "3");
        setButtonClickListener(R.id.button4, "4");
        setButtonClickListener(R.id.button5, "5");
        setButtonClickListener(R.id.button6, "6");
        setButtonClickListener(R.id.button7, "7");
        setButtonClickListener(R.id.button8, "8");
        setButtonClickListener(R.id.button9, "9");
        setButtonClickListener(R.id.buttonPlus, "+");
        setButtonClickListener(R.id.buttonMinus, "-");

        // Delete button
        findViewById(R.id.buttonX).setOnClickListener(v -> {
            if (currentExpression.length() > 0) {
                currentExpression.deleteCharAt(currentExpression.length() - 1);
                displayText.setText(currentExpression.length() > 0 ? currentExpression.toString() : "CALC");
            }
        });

        // Equals button
        findViewById(R.id.buttonEquals).setOnClickListener(v -> {
            try {
                String result = evaluateExpression(currentExpression.toString());
                currentExpression = new StringBuilder(result);
                displayText.setText(result);
            } catch (Exception e) {
                displayText.setText("Error");
                currentExpression = new StringBuilder();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setButtonClickListener(int buttonId, String value) {
        findViewById(buttonId).setOnClickListener(v -> {
            if (currentExpression.toString().equals("CALC")) {
                currentExpression = new StringBuilder();
            }
            currentExpression.append(value);
            displayText.setText(currentExpression.toString());
        });
    }

    private String evaluateExpression(String expression) {
        try {
            String[] parts;
            if (expression.contains("+")) {
                parts = expression.split("\\+");
                long result = Long.parseLong(parts[0]) + Long.parseLong(parts[1]);
                if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
                    return "Error";
                }
                return String.valueOf(result);
            } else if (expression.contains("-")) {
                parts = expression.split("-");
                long result = Long.parseLong(parts[0]) - Long.parseLong(parts[1]);
                if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
                    return "Error";
                }
                return String.valueOf(result);
            }
            return expression;
        } catch (NumberFormatException e) {
            return "Error";
        }
    }
} 