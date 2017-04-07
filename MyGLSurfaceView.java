package com.example.borna2.trlababalan;



import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;

import org.ejml.data.DenseMatrix64F;


public class MyGLSurfaceView extends GLSurfaceView implements SensorEventListener {
    MyGLRenderer renderer;

    //definicija varijabli za kalmanov filter
    private KalmanFilterOperation kalman1;
    private KalmanFilterOperation kalman2;
    private DenseMatrix64F F;
    private DenseMatrix64F Q;
    private DenseMatrix64F H;
    private DenseMatrix64F Rmat;
    private DenseMatrix64F z1;
    private DenseMatrix64F z2;


    //definicija senzora
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Sensor senGyroscope;
    private Sensor senMagnetometar;

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
    public MyGLSurfaceView(Context context) {
        super(context);
        this.mContext = context;
        renderer = new MyGLRenderer(context);
        this.setRenderer(renderer);
        // Request focus, otherwise key/button won't react
        this.requestFocus();
        this.setFocusableInTouchMode(true);

        senSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);

        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senGyroscope = senSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        senMagnetometar = senSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_GAME);
        senSensorManager.registerListener(this, senGyroscope , SensorManager.SENSOR_DELAY_GAME);
        senSensorManager.registerListener(this, senMagnetometar , SensorManager.SENSOR_DELAY_GAME);

        kalman1 = new KalmanFilterOperation();
        kalman2 = new KalmanFilterOperation();

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
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        //čitanje sa senzora
        if (mySensor.getType() == Sensor.TYPE_GYROSCOPE) {

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 10) {

                lastUpdate = curTime;

                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];


                gyro_x = x;
                gyro_y = y;
                gyro_z = z;


                invokeKalman();
            }

        }

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

                invokeKalman();
            }

        }

        if (mySensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 10) {
                lastUpdate = curTime;

                mag_x = x;
                mag_y = y;
                mag_z = z;
            }
        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void invokeKalman() {
        //izračun pitch i roll iz akcelerometra
        double pitch = calcPitch(acc_x,acc_z);
        double roll = calcRoll(acc_y,acc_z);


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


        //xdata1 i xdata4 su pravi pitch i roll (kutevi)
        xdata1 =  kalmanRoll.get(0,0);
        xdata2 =  kalmanRoll.get(1,0);
        xdata3 =  kalmanRoll.get(2,0);
        xdata4 =  kalmanPitch.get(0,0);
        xdata5 =  kalmanPitch.get(1,0);
        xdata6 =  kalmanPitch.get(2,0);

        //konačni brojevi za roll, pitch i yaw
        final double finalRoll = xdata1;
        final double finalPitch = xdata4;
        final double finalYaw = calcYaw(xdata1,xdata4);;

        //Treba pogledat kaj renderer.angleX prima !!
        double currentX = finalRoll;
        double currentY = finalPitch;
        double deltaX, deltaY;
        deltaX = (currentX * 57.2958) - (previousX * 57.2958); //prebacio u stupnjeve
        deltaY = (currentY * 57.2958) - (previousY * 57.2958);
        renderer.angleX += deltaX;
        renderer.angleY += deltaY;

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