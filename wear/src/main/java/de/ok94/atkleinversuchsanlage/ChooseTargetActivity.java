package de.ok94.atkleinversuchsanlage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ChooseTargetActivity extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_target);

        // Enables Always-on
        setAmbientEnabled();

        Intent intent = getIntent();
        final int source = intent.getIntExtra("source", 1);

        FrameLayout button1 = (FrameLayout) findViewById(R.id.buttonTgt1);
        FrameLayout button2 = (FrameLayout) findViewById(R.id.buttonTgt2);
        TextView button1Text = (TextView) findViewById(R.id.buttonTgt1Text);
        TextView button2Text = (TextView) findViewById(R.id.buttonTgt2Text);
        LinearLayout buttonCancel = (LinearLayout) findViewById(R.id.buttonTgtCancel);

        final int target1, target2;
        if (source == 1) {
            button1Text.setText(R.string.two);
            button2Text.setText(R.string.three);
            target1 = 2;
            target2 = 3;
        }
        else if (source == 2) {
            button1Text.setText(R.string.one);
            button2Text.setText(R.string.three);
            target1 = 1;
            target2 = 3;
        }
        else {
            button1Text.setText(R.string.one);
            button2Text.setText(R.string.two);
            target1 = 1;
            target2 = 2;
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSoapWriteRequest(source, target1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSoapWriteRequest(source, target2);
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    public void run() {
                        finish();
                    }
                };
                handler.postDelayed(runnable, 400);
            }
        });
    }

    private void sendSoapWriteRequest(int source, int target) {
        SoapWriteTask soapWriteTask = new SoapWriteTask(true, source, target);
        soapWriteTask.execute();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                finish();
            }
        };
        handler.postDelayed(runnable, 400);
    }
}
