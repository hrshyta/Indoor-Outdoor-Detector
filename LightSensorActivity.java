package com.example.harshithamaddina.iodetector;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LightSensorActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "CurrentTime";
    private SensorManager mSensorManager;
    private Sensor mLight;
    private float currentLux,proximityvalue;
    TextView status,LightLux,ProximityText;
    private Date currentTime,EndTime,StartTime;
    String sCurrentTime;
    private Sensor mProximity;
    boolean CheckProximitysensor=false;
    private static final int SENSOR_SENSITIVITY = 4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_sensor);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    protected void onStart()
    {
        //initializing current time into string
        Calendar c = Calendar.getInstance();
        if (c.getTime() != null)
        {
            sCurrentTime = String.valueOf(c.getTime());
        }
        sCurrentTime = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        super.onStart();
    }

    @Override
    protected void onResume() {
        mSensorManager.registerListener(this, mLight,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_FASTEST);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //current time,start time,endtime

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        try {
            currentTime = sdf.parse(sCurrentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            StartTime = sdf.parse("08:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            EndTime = sdf.parse("17:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }



        //proximity sensor
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                ProximityText = (TextView) findViewById(R.id.Proximity_val);
                if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                    proximityvalue = event.values[0];
                    String st2 = "Proximity value:" + proximityvalue;
                    ProximityText.setText(st2);
                    Toast.makeText(getApplicationContext(), "near", Toast.LENGTH_SHORT).show();

                } else {
                    proximityvalue = event.values[0];
                    String st3 = "Proximity value:" + proximityvalue;
                    ProximityText.setText(st3);
                }
            }

            //light sensor
                if (event.sensor.getType() == Sensor.TYPE_LIGHT)
                {
                    currentLux = event.values[0];
                    status = (TextView) findViewById(R.id.Status);
                    LightLux = (TextView) findViewById(R.id.Light_Lux);
                    LightLux.setText("AmbientLightSensor Intensity :: " + currentLux + " Lux");
                    if (currentLux > 2000) {
                        String st1 = "Outdoor";
                        status.setText(st1);

                    } else if (currentLux > 500 && currentLux < 2000) {
                        status.setText("semi outdoor");
                    } else {
                        status.setText("Indoor");
                    }

                }


        }




    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
}
