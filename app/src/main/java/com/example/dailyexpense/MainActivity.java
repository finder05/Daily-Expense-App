package com.example.dailyexpense;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText amount, description;
    Spinner category;
    ListView listView;

    ArrayList<String> expenses;
    ArrayAdapter<String> adapter;

    String[] categories = {
            "Office Supplies",
            "Travel / Taxi",
            "Client Meeting",
            "Printing / Xerox",
            "Internet / Phone Bill"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amount = findViewById(R.id.etAmount);
        description = findViewById(R.id.etDescription);
        category = findViewById(R.id.spCategory);
        listView = findViewById(R.id.listExpenses);

        Button add = findViewById(R.id.btnAdd);
        Button call = findViewById(R.id.btnCall);
        Button sms = findViewById(R.id.btnSMS);
        Button email = findViewById(R.id.btnEmail);

        expenses = new ArrayList<>();

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, expenses);

        listView.setAdapter(adapter);

        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        categories);

        category.setAdapter(spinnerAdapter);

        add.setOnClickListener(v -> {

            String amt = amount.getText().toString();
            String desc = description.getText().toString();

            if (!amt.isEmpty() && !desc.isEmpty()) {

                String item = desc + " - $" + amt;

                expenses.add(item);
                adapter.notifyDataSetChanged();

                amount.setText("");
                description.setText("");
            }
        });

        call.setOnClickListener(v -> confirmCall());

        sms.setOnClickListener(v -> confirmSMS());

        email.setOnClickListener(v -> sendEmail());
    }

    void confirmCall() {

        new AlertDialog.Builder(this)
                .setTitle("Confirm Call")
                .setMessage("Do you want to call the accountant?")
                .setPositiveButton("Call", (d, w) -> {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:1234567890"));
                    startActivity(intent);

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    void confirmSMS() {

        new AlertDialog.Builder(this)
                .setTitle("Confirm SMS")
                .setMessage("Send expense details via SMS?")
                .setPositiveButton("Send", (d, w) -> {

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("sms:1234567890"));
                    intent.putExtra("sms_body", "Daily expense report");
                    startActivity(intent);

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    void sendEmail() {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"accountant@email.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Daily Expense Report");

        startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }
}