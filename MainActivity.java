package com.example.borna2.trlababalan;

import android.content.Context;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ejml.data.DenseMatrix64F;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private KalmanFilterOperation kalman1;
    private KalmanFilterOperation kalman2;

    private DenseMatrix64F F;
    private DenseMatrix64F Q;
    private DenseMatrix64F H;
    private DenseMatrix64F Rmat;
    private DenseMatrix64F z1;
    private DenseMatrix64F z2;




    private Sensor senAccelerometer;
    private Sensor senGyroscope;
    private Sensor senMagnetometar;
    private SensorManager senSensorManager;
    private SensorManager senSensorManager2;
    private SensorManager senSensorManager3;

    private long lastUpdate = 0;
    private float acc_x, acc_y, acc_z;
    private float gyro_x,gyro_y,gyro_z;
    private float mag_x, mag_y,mag_z;


    private static Button button;

    private static TextView text1;
    private static TextView text2;
    private static TextView text3;
    private static TextView text4;
    private static TextView text5;
    private static TextView text6;
    private static TextView text7;
    private static TextView text8;
    private static TextView text9;

    private static TextView proba;
    private static TextView proba2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kalman1 = new KalmanFilterOperation();
        kalman2 = new KalmanFilterOperation();

        //Gumb za prebacivanje prozora
        button = (Button) findViewById(R.id.button );
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondWidow.class));

            }
        });


        //podaci za matrice kalmanovog filtera
        double data1[][]={{1,0.01,-0.01},{0,1,0},{0,0,1}};
        double data2[][]={{1,0,0},{0,1,0},{0,0,1}};
        double data3[][]={{1,0,0},{0,1,0}};
        double data4[][]={{0},{0},{0}};
        double data5[][]={{0,0,0},{0,0,0},{0,0,0}};
        double data6[][]={{1,0},{0,1}};

        // matrice kalmanovog filtera
        F = new DenseMatrix64F(data1);
        Q = new DenseMatrix64F(data2);
        H = new DenseMatrix64F(data3);
        Rmat = new DenseMatrix64F(data6);
        z1 = new DenseMatrix64F(2,1);
        z2 = new DenseMatrix64F(2,1);

        kalman1.configure(F,Q,H);
        kalman2.configure(F,Q,H);

        DenseMatrix64F x = new DenseMatrix64F(data4);
        DenseMatrix64F P = new DenseMatrix64F(data5);

        kalman1.setState(x,P);
        kalman2.setState(x,P);


        proba = (TextView) findViewById(R.id.textViewProba);
        proba2 = (TextView) findViewById(R.id.textViewProba2);


        text1 = (TextView) findViewById(R.id.textView);
        text2 = (TextView) findViewById(R.id.textView2);
        text3 = (TextView) findViewById(R.id.textView3);
        text4 = (TextView) findViewById(R.id.textView4);
        text5 = (TextView) findViewById(R.id.textView5);
        text6 = (TextView) findViewById(R.id.textView6);
        text7 = (TextView) findViewById(R.id.textView7);
        text8 = (TextView) findViewById(R.id.textView8);
        text9 = (TextView) findViewById(R.id.textView9);


        //Dohvačanje senzora
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        senSensorManager2 = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senGyroscope = senSensorManager2.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        senSensorManager2.registerListener(this,senGyroscope,SensorManager.SENSOR_DELAY_NORMAL);

        senSensorManager3 = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senMagnetometar = senSensorManager3.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        senSensorManager3.registerListener(this,senMagnetometar,SensorManager.SENSOR_DELAY_NORMAL);







    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        //čitanje sa senzora
        if (mySensor.getType() == Sensor.TYPE_GYROSCOPE) {


            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];


            long curTime = System.currentTimeMillis();
            //brzina uzimanja podataka sa senzora
            if ((curTime - lastUpdate) >0) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                gyro_x = x;
                gyro_y = y;
                gyro_z = z;
                text4.setText(Float.toString(gyro_x));
                text5.setText(Float.toString(gyro_y));
                text6.setText(Float.toString(gyro_z));
                invokeKalman();

            }
        }

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 0) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                acc_x = x;
                acc_y = y;
                acc_z = z;
                text1.setText(Float.toString(acc_x));
                text2.setText(Float.toString(acc_y));
                text3.setText(Float.toString(acc_z));
                invokeKalman();

            }
        }

        if (mySensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 0) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                mag_x = x;
                mag_y = y;
                mag_z = z;
                text7.setText(Float.toString(mag_x));
                text8.setText(Float.toString(mag_y));
                text9.setText(Float.toString(mag_z));

            }

        }








    }

    private void invokeKalman() {
        //izračun pitch i roll iz akcelerometra
        double pitch = calcPitch(acc_x,acc_z);
        double roll = calcRoll(acc_y,acc_z);

        proba.setText(Double.toString(pitch));
        proba2.setText(Double.toString(roll));


        // e ovaj dio ne šljaka ... možda prebrzo dobiva podatke il tak nešto ...

        z1.set(0, 0, roll);
        z1.set(1, 0, gyro_x);

        z2.set(0, 0, pitch);
        z2.set(1, 0, gyro_y);

        kalman1.predict();
        kalman2.predict();

        kalman1.update(z1,Rmat);
        kalman2.update(z2,Rmat);

        DenseMatrix64F kalmanRoll = new DenseMatrix64F(1,3);
        kalmanRoll = kalman1.getState();
        DenseMatrix64F kalmanPitch =  new DenseMatrix64F(1,3);
        kalmanPitch = kalman2.getState();



       // System.out.println(kalmanRoll.getState().getNumCols() +" " +  kalman1.getState().getNumRows() );

        double xdata1 =  kalmanRoll.get(0,0);
        double xdata2 =  kalmanRoll.get(1,0);
        double xdata3 =  kalmanRoll.get(2,0);
        double xdata4 =  kalmanPitch.get(0,0);
        double xdata5 =  kalmanPitch.get(1,0);
        double xdata6 =  kalmanPitch.get(2,0);



        Intent sendData = new Intent(this, SecondWidow.class);
        sendData.putExtra("xdata1", xdata1);
        sendData.putExtra("xdata2", xdata2);
        sendData.putExtra("xdata3", xdata3);
        sendData.putExtra("xdata4", xdata4);
        sendData.putExtra("xdata5", xdata5);
        sendData.putExtra("xdata6", xdata6);

        startActivity(sendData);



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
        senSensorManager2.unregisterListener(this);
        senSensorManager3.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager2.registerListener(this, senGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        senSensorManager3.registerListener(this, senMagnetometar, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private double calcPitch(double acc_x, double acc_z) {
        return Math.atan(acc_x/(  (acc_x * acc_x) + (acc_z * acc_z)  ));
    }

    private double calcRoll(double acc_y, double acc_z) {
        return Math.atan(acc_y/(  (acc_y * acc_y) + (acc_z * acc_z)  ));
    }

}
