package lab1_206_03.uwaterloo.ca.lab1_206_03;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;

import java.util.Arrays;

import ca.uwaterloo.sensortoy.LineGraphView;

import java.lang.String;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity {
    public LineGraphView graph;
    public TextView light;
    public TextView accel;
    public TextView magnet;
    public TextView rotation;
    public Button reset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout l = (LinearLayout) findViewById(R.id.linearLayout);
        l.setOrientation(LinearLayout.VERTICAL);
        light = new TextView(getApplicationContext());
        light.setTextColor(Color.BLACK);
        l.addView(light);
        accel = new TextView(getApplicationContext());
        accel.setTextColor(Color.BLACK);
        l.addView(accel);
        magnet = new TextView(getApplicationContext());
        magnet.setTextColor(Color.BLACK);
        l.addView(magnet);
        rotation = new TextView(getApplicationContext());
        rotation.setTextColor(Color.BLACK);
        l.addView(rotation);
        graph = new LineGraphView(getApplicationContext(), 100, Arrays.asList("x", "y", "z"));
        l.addView(graph);
        graph.setVisibility(View.VISIBLE);
        reset = new Button(getApplicationContext());
        reset.setText("RESET");
        reset.setGravity(Gravity.CENTER_HORIZONTAL);
        l.addView(
                reset,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
        );

        //request the sensor manager
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //request various sensors
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor rotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        //instantiate sensor listeners
        SensorEventListener ll = new SensorEventListeners(light);
        sensorManager.registerListener(ll, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        SensorEventListener ml = new SensorEventListeners(magnet);
        sensorManager.registerListener(ml, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
        SensorEventListener rl = new SensorEventListeners(rotation);
        sensorManager.registerListener(rl, rotationVector, SensorManager.SENSOR_DELAY_NORMAL);
        SensorEventListener al = new SensorEventListeners(accel, graph);
        sensorManager.registerListener(al, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graph.purge();
            }
        });
    }
}

class SensorEventListeners implements SensorEventListener {
    TextView output;
    LineGraphView Graph;
    float a, b, c, d;
    float max1 = 0, max2 = 0, max3 = 0, max4 = 0;

    public SensorEventListeners(TextView outputView, LineGraphView grp) {
        output = outputView;
        Graph = grp;
    }

    public SensorEventListeners(TextView outputView) {
        output = outputView;
    }

    public void onAccuracyChanged(Sensor s, int i) {
    }

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor.getType() == Sensor.TYPE_LIGHT) {
            String LS = String.format("(%.2f)", se.values[0]);
            output.setText("Light Sensor: \n" + LS + "\n");
        }
        if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            String x = String.format("(%.2f, %.2f, %.2f)", se.values[0], se.values[1], se.values[2]);
            Graph.addPoint(se.values);
            a = se.values[0];
            b = se.values[1];
            c = se.values[2];
            if (max1 < abs(a) && max2 < abs(b) && max3 < abs(c)) {
                max1 = a;
                max2 = b;
                max3 = c;
            } else if (max1 < abs(a)) {
                max1 = abs(a);
            } else if (max2 < abs(b)) {
                max2 = abs(b);
            } else if (max3 < abs(c)) {
                max3 = abs(c);
            }
            String AS = String.format("(%.2f, %.2f, %.2f)", max1, max2, max3);
            output.setText("Accelerometer: \n" + x + "\nMaximum acceleration:\n" + AS + "\n");
        }
        if (se.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            String x = String.format("(%.2f, %.2f, %.2f)", se.values[0], se.values[1], se.values[2]);
            a = se.values[0];
            b = se.values[1];
            c = se.values[2];
            if (max1 < abs(a) && max2 < abs(b) && max3 < abs(c)) {
                max1 = a;
                max2 = b;
                max3 = c;
            } else if (max1 < abs(a)) {
                max1 = abs(a);
            } else if (max2 < abs(b)) {
                max2 = abs(b);
            } else if (max3 < abs(c)) {
                max3 = abs(c);
            }
            String MS = String.format("(%.2f, %.2f, %.2f)", max1, max2, max3);
            output.setText("Magnet Sensor: \n" + x + "\nMaximum value:\n" + MS + "\n");
        }
        if (se.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            String x = String.format("(%.2f, %.2f, %.2f, %.2f)", se.values[0], se.values[1], se.values[2], se.values[3]);
            a = se.values[0];
            b = se.values[1];
            c = se.values[2];
            d = se.values[3];
            if (max1 < abs(a) && max2 < abs(b) && max3 < abs(c) && max4 < abs(d)) {
                max1 = a;
                max2 = b;
                max3 = c;
                max4 = d;
            } else if (max1 < abs(a)) {
                max1 = abs(a);
            } else if (max2 < abs(b)) {
                max2 = abs(b);
            } else if (max3 < abs(c)) {
                max3 = abs(c);
            } else if (max4 < abs(d)) {
                max4 = abs(d);
            }
            String RS = String.format("(%.2f, %.2f, %.2f, %.2f)", max1, max2, max3, max4);
            output.setText("Rotation Vector: \n" + x + "\nMaximum value:\n" + RS + "\n");
        }
    }
}