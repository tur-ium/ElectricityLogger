package com.example.electricityloggeruk;


import android.widget.Toast;
import com.example.electricityloggeruk.Results;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManualEntryActivity extends Activity {

    private Button mNext;
    private EditText mElecUsage;
    private EditText mSupplier;
    private EditText mCountry;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.manual_entry);

        mNext = findViewById(R.id.enteredElecUsageButton);
        mElecUsage = findViewById(R.id.manualElecUsageInput);
        mSupplier = findViewById(R.id.supplierEditText);
        mCountry = findViewById(R.id.countryEditText);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double elecUsage = Double.parseDouble(mElecUsage.getText().toString());
                String country = mCountry.getText().toString();
                Intent i = new Intent(getApplicationContext(), Results.class);
                Results.elecUsage = elecUsage;
                Results.supplier = mSupplier.getText().toString();
                Results.country = country;
                startActivity(i);
            }
        });
    }


}
