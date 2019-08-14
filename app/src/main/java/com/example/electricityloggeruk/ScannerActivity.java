package com.example.electricityloggeruk;

import android.view.View;
import com.example.electricityloggeruk.QRCodeResults;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.zxing.Result;
import com.google.zxing.qrcode.encoder.QRCode;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import org.w3c.dom.Text;

public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    private String QRCodeText;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v("ScannerActivity", rawResult.getText()); // Prints scan results
        Log.v("ScannerActivity", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        Toast.makeText(getApplicationContext(),"Captured QR code "+rawResult.getText(),Toast.LENGTH_LONG);
        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
        Intent i = new Intent(getApplicationContext(), QRCodeResults.class);
        MainActivity.QRCodeText = rawResult.getText();
        startActivity(i);

    }

}