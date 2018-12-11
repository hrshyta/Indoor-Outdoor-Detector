package com.example.harshithamaddina.iodetector;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.location.GnssStatus;
import android.support.v7.app.AppCompatActivity;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

//import android.content.Context;
//import android.widget.LinearLayout;


@SuppressWarnings("deprecation")
public class GnssActivity extends AppCompatActivity implements LocationListener,SensorEventListener
{
   // private static final String TAG = "CurrentTime";
    private SensorManager mSensorManager;
    private Sensor mLight;
    private float currentLux,proximityvalue;
    TextView status,LightLux,ProximityText,LightSensorUsage,GnssLightStatus;
    private Date currentTime,EndTime,StartTime;
    String sCurrentTime;
    private Sensor mProximity;
    boolean CheckProximitysensor=false,far=false;
    private static final int SENSOR_SENSITIVITY = 4;
    private static final String TAG = "Gnss demo";
    GnssStatus.Callback mGnssCallback;
    int mSatelliteCount, mSvid, mConstellationType;
    boolean mSatFixCount;
    private int mUsedInFix;
    LocationManager mLocationManager;
    Float mSignal;
    TextView tt1,tt4, tt5,tt7;
    String st1,st5,st6,st7,st8,st9,st10="Detected Environment:",st15;

    @TargetApi(24)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnss);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        try {
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)) {
                mGnssCallback = new GnssStatus.Callback() {

                    @Override
                    public void onStarted() {

                        super.onStarted();
                    }

                    @Override
                    public void onStopped() {
                        super.onStopped();
                    }

                    @Override
                    public void onFirstFix(int ttffMillis) {
                        super.onFirstFix(ttffMillis);
                    }

                    @SuppressLint("ResourceType")
                    @Override
                    public void onSatelliteStatusChanged(GnssStatus status) {
                        mUsedInFix = 0;

                        // get available satellite count and print
                        mSatelliteCount = status.getSatelliteCount();
                        Log.i(TAG, "All Available satellites: " + mSatelliteCount);
                        String st2 = "Available satellites:" + mSatelliteCount;
                        tt5 = (TextView) findViewById(R.id.SatelliteCount);
                        tt5.setText(st2);


                        for (int i = 0; i < mSatelliteCount; i++) {
                            //get signal & print
                            mSignal = status.getCn0DbHz(i);
                            String signalst1 = "Signal Strength: " + mSignal + " dBm";
                            tt4 = (TextView) findViewById(R.id.SignalStrengthView);
                            tt4.setText(signalst1);


                            //get used in fix
                            mSatFixCount = status.usedInFix(i);
                            if (mSatFixCount == true) {
                                mUsedInFix++;
                            }

                            String Satst4 = "Used Satellites :" + mUsedInFix;
                            tt7 = (TextView) findViewById(R.id.Satfixcount);
                            tt7.setText(Satst4);

                            //check indoor/outdoor status
                            tt1 = (TextView) findViewById(R.id.TextView1);

                            if (mSignal >= 0 && mSignal <=19)
                            {
                                st7="Indoor";
                                String st11=st10+st7;
                                tt1.setText(st11);
                                if((st6==st7)||(st7.equalsIgnoreCase("Indoor")))
                                {
                                    GnssLightStatus=(TextView)findViewById(R.id.Gnss_Light_Status);
                                    GnssLightStatus.setText("you are Indoor");
                                }

                                else
                                {GnssLightStatus.setText("Poor Confidence");
                                }
                            }
                            else if (mSignal >= 20 && mSignal <= 31)
                            {

                                st8="Semi outdoor";
                                String st12=st10+st8;
                                tt1.setText(st12);
                                if(st8==st5||st8.equalsIgnoreCase("Semi outdoor"))
                                {
                                    GnssLightStatus=(TextView)findViewById(R.id.Gnss_Light_Status);
                                    GnssLightStatus.setText("you are Semi Indoor");
                                }
                                else
                                {GnssLightStatus.setText("Poor Confidence");
                                }

                            }
                            else
                            {

                                st9="Outdoor";
                                String st15=st10+st9;
                                tt1.setText(st15);
                                if(st9==st1||st9.equalsIgnoreCase("Outdoor"))
                                {
                                    GnssLightStatus=(TextView)findViewById(R.id.Gnss_Light_Status);
                                    GnssLightStatus.setText("you are Outdoor");
                                }
                                else
                                {GnssLightStatus.setText("Poor Confidence");
                                }

                            }
                        }

                        super.onSatelliteStatusChanged(status);
                    }

                };
            }
            mLocationManager.registerGnssStatusCallback(mGnssCallback);

        } catch (Exception ex) {
            Log.i(TAG, "onCreate: " + ex);

        }
    }


    //onStart
    // @SuppressLint("MissingPermission")
    @Override
    public void onStart()
    {
        //initializing current time into string
        Calendar c = Calendar.getInstance();
        if (c.getTime() != null)
        {
            sCurrentTime = String.valueOf(c.getTime());
        }
        sCurrentTime = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        super.onStart();
        mLocationManager.registerGnssStatusCallback(mGnssCallback);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        GnssLightStatus=(TextView)findViewById(R.id.Gnss_Light_Status);


        super.onStart();

    }

    //onResume()

    @Override
    protected void onResume()
    {
        mSensorManager.registerListener(this, mLight,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_FASTEST);
        super.onResume();

        super.onResume();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        mSensorManager.unregisterListener(this);
        super.onPause();
    }


    //onStop

    @Override
    protected void onStop() {
        mLocationManager.removeUpdates(this);
        mLocationManager.unregisterGnssStatusCallback(mGnssCallback);
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

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
        LightSensorUsage = (TextView) findViewById(R.id.LightSensorUsage);
        if (currentTime.after(StartTime) && currentTime.before(EndTime)) {

            String st4 = "light sensor can be used";
            LightSensorUsage.setText(st4);
            //Toast.makeText(getApplicationContext(), "time is in between", Toast.LENGTH_SHORT).show();
            CheckProximitysensor = true;
        } else {
            String st4 = "light sensor cannot be used [refer Gnss result]";
            LightSensorUsage.setText(st4);
        }


        if (mLight != null && CheckProximitysensor) {
            //proximity sensor
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                ProximityText = (TextView) findViewById(R.id.Proximity_val);
                if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                    proximityvalue = event.values[0];
                    String st2 = "Proximity value:" + proximityvalue + " cm";
                    ProximityText.setText(st2);
                    Toast.makeText(getApplicationContext(), "near", Toast.LENGTH_SHORT).show();

                } else {
                    proximityvalue = event.values[0];
                    String st3 = "Proximity value:" + proximityvalue + " cm";
                    ProximityText.setText(st3);
                    far = true;
                }
            }
        }

        if (far) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                currentLux = event.values[0];
                status = (TextView) findViewById(R.id.Status);
                LightLux = (TextView) findViewById(R.id.Light_Lux);
                LightLux.setText("AmbientLightSensor Intensity :: " + currentLux + " Lux");
                if (currentLux > 0 && currentLux <= 500)
                {
                    st6 ="Indoor";
                    String st14=st10+st6;
                    status.setText(st14);

                }
                else if (currentLux > 500 && currentLux < 2000)
                {
                    st5 ="Semi outdoor";
                    String st13=st10+st5;
                    status.setText(st13);
                }
                else if (currentLux > 2000)
                {
                    st1 ="Outdoor";
                    String st13=st10+st1;
                    status.setText(st13);

                }
                else
                {
                    status.setText("Detected Environment: Light sensor ineffective");
                }

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
}

