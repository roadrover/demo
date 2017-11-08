package com.roadrover.demo.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.roadrover.demo.R;
import com.roadrover.demo.ui.view.IVIButton;
import com.roadrover.sdk.setting.IVISetting;

/**
 * 获取数据设置activity.
 */

public class SettingsGetDataActivity extends BaseActivity {

    private static final String SEPARATE = ": ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 添加功能视图
        addIVIButtons(new IVIButton(this, R.string.ccd_camera, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips(getString(R.string.ccd_camera) + SEPARATE + IVISetting.isCcdEnable(SettingsGetDataActivity.this));
            }
        }), new IVIButton(this, R.string.ccd_line, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips(getString(R.string.ccd_line) + SEPARATE + IVISetting.isCcdLineEnable(SettingsGetDataActivity.this));
            }
        }), new IVIButton(this, R.string.ccd_mirror, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips(getString(R.string.ccd_mirror) + SEPARATE + IVISetting.isCcdMirror(SettingsGetDataActivity.this));
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.wifi_signal_strength, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips(getString(R.string.wifi_signal_strength) + SEPARATE + IVISetting.getWifiSignalStrength(SettingsGetDataActivity.this));
            }
        }), new IVIButton(this, R.string.wifi_signal_level, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips(getString(R.string.wifi_signal_level) + SEPARATE + IVISetting.getWifiSignalLevel(SettingsGetDataActivity.this));
            }
        }), new IVIButton(this, R.string.wifi_signal_max_level, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips(getString(R.string.wifi_signal_max_level) + SEPARATE + IVISetting.getWifiSignalMaxLevel(SettingsGetDataActivity.this));
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.mobile_signal_strength, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips(getString(R.string.mobile_signal_strength) + SEPARATE + IVISetting.getMobileSignalStrength(SettingsGetDataActivity.this));
            }
        }), new IVIButton(this, R.string.mobile_signal_level, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips(getString(R.string.mobile_signal_level) + SEPARATE + IVISetting.getMobileSignalLevel(SettingsGetDataActivity.this));
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.hotspot_name, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips(getString(R.string.hotspot_name) + SEPARATE + IVISetting.getHotspotName(SettingsGetDataActivity.this));
            }
        }), new IVIButton(this, R.string.hotspot_password, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTips(getString(R.string.hotspot_password) + SEPARATE + IVISetting.getHotspotPassword(SettingsGetDataActivity.this));
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
