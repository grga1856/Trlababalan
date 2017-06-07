package com.example.borna2.trlababalan;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by Borna2 on 07-Apr-17.
 */

public class Method5 extends Fragment implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Sensor senGyroscope;
    private Sensor senMagnetometar;

    Context mContext;
    private long lastUpdate = 0;

    private TextView acc_x;
    private TextView acc_y;
    private TextView acc_z;
    private TextView acc_x_data;
    private TextView acc_y_data;
    private TextView acc_z_data;

    private TextView gyro_x;
    private TextView gyro_y;
    private TextView gyro_z;
    private TextView gyro_x_data;
    private TextView gyro_y_data;
    private TextView gyro_z_data;

    private TextView mag_x;
    private TextView mag_y;
    private TextView mag_z;
    private TextView mag_x_data;
    private TextView mag_y_data;
    private TextView mag_z_data;

    public static void setCtx(Context ctx) {
        Method5.ctx = ctx;
        Log.e("qwqwq","qqqqqqqqqq");
    }

    public static Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = ctx;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView =  inflater.inflate(R.layout.fragment_raw_data, container, false);

        acc_x = (TextView) rootView.findViewById(R.id.acc_x);
        acc_y = (TextView) rootView.findViewById(R.id.acc_y);
        acc_z = (TextView) rootView.findViewById(R.id.acc_z);
        acc_x_data = (TextView) rootView.findViewById(R.id.acc_x_data);
        acc_y_data = (TextView) rootView.findViewById(R.id.acc_y_data);
        acc_z_data = (TextView) rootView.findViewById(R.id.acc_z_data);

        gyro_x = (TextView) rootView.findViewById(R.id.gyro_x);
        gyro_y = (TextView) rootView.findViewById(R.id.gyro_y);
        gyro_z = (TextView) rootView.findViewById(R.id.gyro_z);
        gyro_x_data = (TextView) rootView.findViewById(R.id.gyro_x_data);
        gyro_y_data = (TextView) rootView.findViewById(R.id.gyro_y_data);
        gyro_z_data = (TextView) rootView.findViewById(R.id.gyro_z_data);

        mag_x = (TextView) rootView.findViewById(R.id.mag_x);
        mag_y = (TextView) rootView.findViewById(R.id.mag_y);
        mag_z = (TextView) rootView.findViewById(R.id.mag_z);
        mag_x_data = (TextView) rootView.findViewById(R.id.mag_x_data);
        mag_y_data = (TextView) rootView.findViewById(R.id.mag_y_data);
        mag_z_data = (TextView) rootView.findViewById(R.id.mag_z_data);

        senSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);

        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senGyroscope = senSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        senMagnetometar = senSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_GAME);
        senSensorManager.registerListener(this, senGyroscope , SensorManager.SENSOR_DELAY_GAME);
        senSensorManager.registerListener(this, senMagnetometar , SensorManager.SENSOR_DELAY_GAME);

        acc_x.setText("Akcelerometar x-os : ");
        acc_y.setText("Akcelerometar y-os : ");
        acc_z.setText("Akcelerometar z-os : ");
        gyro_x.setText("Žiroskop x-os : ");
        gyro_y.setText("Žiroskop y-os : ");
        gyro_z.setText("Žiroskop z-os : ");
        mag_x.setText("Magnetometar x-os : ");
        mag_y.setText("Magnetometar y-os : ");
        mag_z.setText("Magnetometar z-os : ");




        return rootView;

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        //čitanje sa senzora
        if (mySensor.getType() == Sensor.TYPE_GYROSCOPE) {

            long curTime = System.currentTimeMillis();

                double x = sensorEvent.values[0];
                double y = sensorEvent.values[1];
                double z = sensorEvent.values[2];

                gyro_x_data.setText(Double.toString(x));
                gyro_y_data.setText(Double.toString(y));
                gyro_z_data.setText(Double.toString(z));





        }

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 10) {
                lastUpdate = curTime;

                acc_x_data.setText(Double.toString(x));
                acc_y_data.setText(Double.toString(y));
                acc_z_data.setText(Double.toString(z));
            }

        }

        if (mySensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 10) {
                lastUpdate = curTime;

                mag_x_data.setText(Double.toString(x));
                mag_y_data.setText(Double.toString(y));
                mag_z_data.setText(Double.toString(z));
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
