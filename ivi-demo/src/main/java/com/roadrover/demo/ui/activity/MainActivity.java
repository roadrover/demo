package com.roadrover.demo.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.roadrover.demo.R;
import com.roadrover.demo.ui.view.IVIButton;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 第一排
        addIVIButtons(new IVIButton(this, R.string.car, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChildDemo(((IVIButton) v).getText(),
                        CarActivity.class);
            }
        }), new IVIButton(this, R.string.system, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChildDemo(((IVIButton) v).getText(), SystemActivity.class);
            }
        }));

        // 第二排
        addIVIButtons(new IVIButton(this, R.string.radio, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChildDemo(((IVIButton) v).getText(), RadioActivity.class);
            }
        }), new IVIButton(this, R.string.bluetooth, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChildDemo(((IVIButton) v).getText(), BluetoothActivity.class);
            }
        }), new IVIButton(this, R.string.media, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChildDemo(((IVIButton) v).getText(), MediaActivity.class);
            }
        }));

        // 第三排
        addIVIButtons(new IVIButton(this, R.string.audio, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChildDemo(((IVIButton) v).getText(),
                        AudioActivity.class);
            }
        }), new IVIButton(this, R.string.avin, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChildDemo(((IVIButton) v).getText(), AVInActivity.class);
            }
        }));

        // 第四排
        addIVIButtons(new IVIButton(this, R.string.settings, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChildDemo(((IVIButton) v).getText(),
                        SettingsActivity.class);
            }
        }), new IVIButton(this, R.string.voice, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChildDemo(((IVIButton) v).getText(),
                        VoiceActivity.class);
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
