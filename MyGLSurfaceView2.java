package com.example.borna2.trlababalan;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;

import org.ejml.data.DenseMatrix64F;

/**
 * Created by Borna2 on 07-Apr-17.
 */

public class MyGLSurfaceView2 extends GLSurfaceView implements SensorEventListener {
    MyGLRenderer renderer;




    //definicija senzora
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

  

    //Definicija varijabli za pospremanje očitanja senzora
    private long lastUpdate = 0;
    private float acc_x, acc_y, acc_z;
    private float gyro_x,gyro_y,gyro_z;
    private float mag_x, mag_y,mag_z;

    //Definicija varijabli za izračun yaw
    private double XH;
    private double YH;

    //Varijable sa dohvat iz kalmanovog filtera
    private static double xdata1;
    private static double xdata2;
    private static double xdata3;
    private static double xdata4;
    private static double xdata5;
    private static double xdata6;



    // For touch event
    //private final float TOUCH_SCALE_FACTOR = 180.0f / 320.0f;
    private float previousX;
    private float previousY;
    Context mContext;

    // Constructor - Allocate and set the renderer
    public MyGLSurfaceView2(Context context) {
        super(context);
        this.mContext = context;
        renderer = new MyGLRenderer(context);
        this.setRenderer(renderer);
        // Request focus, otherwise key/button won't react
        this.requestFocus();
        this.setFocusableInTouchMode(true);

        senSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_GAME);


    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        //čitanje sa senzora


        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 10) {
                lastUpdate = curTime;

                acc_x = x;
                acc_y = y;
                acc_z = z;

                double roll = calcRoll(acc_y,acc_z);
                double pitch = calcPitch(acc_x,acc_z);

                double currentX = roll;
                double currentY = pitch;
                double deltaX, deltaY;
                deltaX = (currentX * 57.2958) - (previousX * 57.2958); //prebacio u stupnjeve
                deltaY = (currentY * 57.2958) - (previousY * 57.2958);
                renderer.angleX += deltaX;
                renderer.angleY += deltaY;

            }

        }



    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private double calcRoll(double acc_y, double acc_z) {
        return Math.atan(acc_y/(  (acc_y * acc_y) + (acc_z * acc_z)  ));
    }

    private double calcPitch(double acc_x, double acc_z) {
        return Math.atan(acc_x/(  (acc_x * acc_x) + (acc_z * acc_z)  ));
    }

    private double calcYaw(double roll, double pitch) {
        XH = mag_x*Math.cos(pitch) + mag_y* Math.sin(pitch)*Math.sin(roll)
                +mag_z*Math.sin(pitch)*Math.cos(roll);
        YH = mag_y*Math.cos(roll)+ mag_z* Math.sin(roll);
        double yaw = Math.atan2(-YH,XH);   //nezz jel sam točno stavio u atan2 argumente
        return yaw ;
    }
}