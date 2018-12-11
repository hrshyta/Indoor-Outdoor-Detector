package com.example.harshithamaddina.iodetector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "GnssSwitch";
    Switch mGnssSwitch,mLightSwitch,mMagneticSwitch;
Boolean mGnssSwitchState,mLightSwitchState,mMagneticSwitcState;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart()
    {
        mGnssSwitch = ((Switch) findViewById(R.id.Gnss_IoDetector));
        mLightSwitch = ((Switch) findViewById(R.id.light_IoDetector));
        mMagneticSwitch = ((Switch) findViewById(R.id.Magnetic_IoDetector));
        //Gnss switch to the Gnss Activity
        mGnssSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Is the switch is on?
                boolean on = ((Switch) v).isChecked();
                if(on)
                {
                    startActivity( new Intent(MainActivity.this, GnssActivity.class));
                }
                else
                {
                    Log.i(TAG, "onClick: failed");
                }
            }
        });
        mLightSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Is the switch is on?
                boolean on = ((Switch) v).isChecked();
                if(on)
                {
                    startActivity( new Intent(MainActivity.this,LightSensorActivity.class));
                }
                else
                {
                    Log.i(TAG, "onClick: failed");
                }
            }
        });









        super.onStart();
    }
}
