package com.bolivia.sqliteextreme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bolivia.sqliteextreme.android.IntentIntegrator;
import com.bolivia.sqliteextreme.android.IntentResult;

public class ScannerToData1 extends Activity implements OnClickListener {

    //UI instance variables
    private Button scanBtn;
    private TextView formatTxt, contentTxt;
    String at2=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_scanner);

        //instantiate UI items
        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);

        Bundle bundle = getIntent().getExtras();
        at2=bundle.getString("enviodelunobaldosc");
        //listen for clicks
        scanBtn.setOnClickListener(this);
    }

    public void onClick(View v) {
        //check for scan button
        if (v.getId() == R.id.scan_button) {
            //instantiate ZXing integration class
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            //start scanning
            scanIntegrator.initiateScan();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve result of scanning - instantiate ZXing object
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        //check we have a valid result
        if (scanningResult != null) {
            //get content from Intent Result
            String scanContent = scanningResult.getContents();
            String resultadofinal = scanningResult.getContents();
            //get format name of data scanned
            String scanFormat = scanningResult.getFormatName();

            //output to UI
            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);
            Intent resultIntent1 = new Intent();
            resultIntent1.putExtra("result1", "1");

            if (at2==null){resultIntent1.putExtra("lalala1", scanContent);}
            else { resultIntent1.putExtra("lalala1", at2);}
            resultIntent1.putExtra("lalala1", scanContent);
            //resultIntent1.putExtra("lalala1", scanContent);

            //String datosdelscanner = null;
            //resultIntent.putExtra("datosdelescaner", resultadofinal);
            setResult(RESULT_OK, resultIntent1);
            finish();

        }

        else{
            //invalid scan data or scan canceled
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}