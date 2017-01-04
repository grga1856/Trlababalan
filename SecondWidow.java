package com.example.borna2.trlababalan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ejml.data.DenseMatrix64F;

/**
 * Created by Borna2 on 28-Dec-16.
 */

public class SecondWidow extends Activity {

    private static TextView text10;
    private static TextView text11;
    private static TextView text12;
    private static TextView text13;
    private static TextView text14;
    private static TextView text15;

    private static Button buttonBack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);

        buttonBack = (Button) findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondWidow.this, MainActivity.class));

            }
        });

        text10 = (TextView) findViewById(R.id.textView10);
        text11 = (TextView) findViewById(R.id.textView11);
        text12 = (TextView) findViewById(R.id.textView12);
        text13 = (TextView) findViewById(R.id.textView13);
        text14 = (TextView) findViewById(R.id.textView14);
        text15 = (TextView) findViewById(R.id.textView15);

        //dohvat izračunatih vrijednosti i ispis na drugom window-u
        Intent getData = getIntent();
        double x1 =  getData.getDoubleExtra("xdata1",0);
        double x2 =  getData.getDoubleExtra("xdata2",0);
        double x3 =  getData.getDoubleExtra("xdata3",0);
        double x4 =  getData.getDoubleExtra("xdata4",0);
        double x5 =  getData.getDoubleExtra("xdata5",0);
        double x6 =  getData.getDoubleExtra("xdata6",0);


        text10.setText(Double.toString(x1));
        text11.setText(Double.toString(x2));
        text12.setText(Double.toString(x3));
        text13.setText(Double.toString(x4));
        text14.setText(Double.toString(x5));
        text15.setText(Double.toString(x6));











    }




}
