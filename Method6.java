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

import org.ejml.data.DenseMatrix64F;


/**
 * Created by Borna2 on 07-Apr-17.
 */

public class Method6 extends Fragment implements SensorEventListener {

    private KalmanFilterOperation kalman1;
    private KalmanFilterOperation kalman2;
    private DenseMatrix64F F;
    private DenseMatrix64F Q;
    private DenseMatrix64F H;
    private DenseMatrix64F Rmat;
    private DenseMatrix64F z1;
    private DenseMatrix64F z2;

    private static double xdata1;
    private static double xdata2;
    private static double xdata3;
    private static double xdata4;
    private static double xdata5;
    private static double xdata6;

    private double XH;
    private double YH;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Sensor senGyroscope;
    private Sensor senMagnetometar;

    Context mContext;
    private long lastUpdate = 0;

    private TextView acc_roll;
    private TextView acc_roll_data;
    private TextView acc_pitch;
    private TextView acc_pitch_data;
    private TextView kalman_roll;
    private TextView kalman_roll_data;
    private TextView kalman_pitch;
    private TextView kalman_pitch_data;
    private TextView kalman_yaw;
    private TextView kalman_yaw_data;

    private double acc_x;
    private double acc_y;
    private double acc_z;
    private double gyro_x;
    private double gyro_y;
    private double gyro_z;
    private double mag_x;
    private double mag_y;
    private double mag_z;


    public static void setCtx(Context ctx) {
        Method6.ctx = ctx;
        Log.e("qwqwq","qqqqqqqqqq");
    }

    public static Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = ctx;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView =  inflater.inflate(R.layout.fragment_pitch_and_roll, container, false);

        acc_roll = (TextView) rootView.findViewById(R.id.acc_roll);
        acc_roll_data = (TextView) rootView.findViewById(R.id.acc_roll_data);
        acc_pitch = (TextView) rootView.findViewById(R.id.acc_pitch);
        acc_pitch_data = (TextView) rootView.findViewById(R.id.acc_pitch_data);
        kalman_roll = (TextView) rootView.findViewById(R.id.kalman_roll);
        kalman_roll_data = (TextView) rootView.findViewById(R.id.kalman_roll_data);
        kalman_pitch = (TextView) rootView.findViewById(R.id.kalman_ptich);
        kalman_pitch_data = (TextView) rootView.findViewById(R.id.kalman_pitch_data);
        kalman_yaw = (TextView) rootView.findViewById(R.id.kalman_yaw);
        kalman_yaw_data = (TextView) rootView.findViewById(R.id.kalman_yaw_data);



        senSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);

        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senGyroscope = senSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        senMagnetometar = senSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_GAME);
        senSensorManager.registerListener(this, senGyroscope , SensorManager.SENSOR_DELAY_GAME);
        senSensorManager.registerListener(this, senMagnetometar , SensorManager.SENSOR_DELAY_GAME);

        acc_roll.setText("Akcelerometar  roll : ");
        acc_pitch.setText("Akcelerometar pitch : ");
        kalman_roll.setText("Kalman roll : ");
        kalman_pitch.setText("Kalman pitch : ");
        kalman_yaw.setText("Kalman yaw : ");
        return rootView;

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        //čitanje sa senzora
        if (mySensor.getType() == Sensor.TYPE_GYROSCOPE) {


            double x = sensorEvent.values[0];
            double y = sensorEvent.values[1];
            double z = sensorEvent.values[2];

            gyro_x = x;
            gyro_y = y;
            gyro_y = y;

            invokeKalman();
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void invokeKalman() {

        //izračun pitch i roll iz akcelerometra
        double pitch = calcPitch(acc_x,acc_y,acc_z);
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
        final double finalRoll = xdata1 * 57.3;
        final double finalPitch = xdata4 * 57.3;
        final double finalYaw = calcYaw(xdata1,xdata4) * 57.3;;

        acc_roll_data.setText(Double.toString(roll * 57.3));
        acc_pitch_data.setText(Double.toString(pitch * 57.3));
        kalman_roll_data.setText(Double.toString(finalRoll));
        kalman_pitch_data.setText(Double.toString(finalPitch));
        kalman_yaw_data.setText(Double.toString(finalYaw));
    }

    private double calcRoll(double acc_y, double acc_z) {
        return Math.atan2(acc_y,acc_z);

}

    private double calcPitch(double acc_x, double acc_y, double acc_z) {
        return Math.atan2((-acc_x),Math.sqrt(acc_y * acc_y + acc_z*acc_z));

    }

    private double calcYaw(double roll, double pitch) {
        XH = mag_x*Math.cos(pitch) + mag_y* Math.sin(pitch)*Math.sin(roll)
                +mag_z*Math.sin(pitch)*Math.cos(roll);
        YH = mag_y*Math.cos(roll)+ mag_z* Math.sin(roll);
        double yaw = Math.atan2(-YH,XH);
        return yaw ;
    }
}