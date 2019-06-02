package com.gulusoft.codescaner;

import android.app.Activity;


import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.Result;
import com.gulusoft.com.gulusoft.com.codescanner.model.ServerManager;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanBarCodeActivity extends Activity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
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
        String text = rawResult.getText();
        Toast.makeText(ScanBarCodeActivity.this, text, Toast.LENGTH_SHORT).show();
        Log.v("TAG",text ); // Prints scan results
        MainActivity.SendMessage(text.trim());
        Log.v("TAG", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        // If you would like to resume scanning, call this method below:
        finish();
        mScannerView.resumeCameraPreview(this);
    }
}
