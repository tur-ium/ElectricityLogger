package com.example.electricityloggeruk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static String QRCodeText;
    //SSE sample QR (4815 is estimated annual energy usage in kWh)
    String SampleQR_SSE = ",/,Postal_A,SE(SSE Energy Supply Limited)/,            Standard,,                                          Direct Debit,,  MPAN         ,,              4815,,                     25032016-24032017/,/,";
    //Ovo sample QR ( 2121 is estimated annual elec (kWh), 6570 is estimated gas in kWh)
    String SampleQR_Ovo = ",/,BS5 7UW,Ovo Energy (OVO Electricity Ltd)/Ovo Energy (OVO Gas Ltd),Better Energy (all online),Better Energy (all online),Direct Debit,Direct Debit,2200017349235,4177963101,2121,6570,31052016-30052017/31052016-30052017,/,";
    //E.ON sample QR (E.ON 4916 is annual day elec use 4727 is annual annual night energy use
    String SampleQR_EON = "http://www.eonenergy.com/qr-code/?,/,PR4 1XD,E.ON (E.ON Energy Solutions Limited)/,E.ON EnergyPlan,,On receipt of bill,,1610013716711,,4916/4727,,22062016-21062017/,/,";


    private Button mYesUKButton;
    private Button mNonUKButton;
    /**
     * QR Code Info Class
     *
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_uk);

        mYesUKButton = findViewById(R.id.yesUK);
        mNonUKButton = findViewById(R.id.notUK);


        mYesUKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ScannerActivity.class);
                startActivity(i);

            }
        });
        mNonUKButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ManualEntryActivity.class);
                startActivity(i);
            }
        });

//        UNCOMMENT BELOW TO CHECK SAMPLE QR Codes
//
//        //Check SSE
//        elec = getElectricityUsage(SampleQR_SSE);
//        Log.i("MainActivity","Estimated annual electricity usage: "+elec+" kWh / year");
//
//        //CHECK E.ON
//        elec = getElectricityUsage(SampleQR_EON);
//        Log.i("MainActivity","Estimated annual electricity usage: "+elec+" kWh / year");
//
//        //Check Ovo
//        elec = getElectricityUsage(SampleQR_Ovo);
//        Log.i("MainActivity","Estimated annual electricity usage: "+elec+" kWh / year");
    }
}

