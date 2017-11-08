package com.roadrover.demo.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.roadrover.demo.R;
import com.roadrover.demo.ui.view.IVIButton;
import com.roadrover.sdk.setting.IVISetting;

/**
 * 跳转页面设置activity</br>
 *
 * 跳转接口为{@link IVISetting#startSettings(Context, String)}
 */

public class SettingsJumpPageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 添加功能视图
        addIVIButtons(new IVIButton(this, R.string.wifi_settings, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IVISetting.startSettings(SettingsJumpPageActivity.this, IVISetting.Network.WifiStatus);
            }
        }), new IVIButton(this, R.string.mobile_settings, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IVISetting.startSettings(SettingsJumpPageActivity.this, IVISetting.Network.MobileNetwork);
            }
        }), new IVIButton(this, R.string.hotspot_settings, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IVISetting.startSettings(SettingsJumpPageActivity.this, IVISetting.Network.PortableHotSpot);
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.bluetooth_pair, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IVISetting.startSettings(SettingsJumpPageActivity.this, IVISetting.Global.BluetoothPaired);
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.volume_settings, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IVISetting.startSettings(SettingsJumpPageActivity.this, IVISetting.Global.Volume);
            }
        }), new IVIButton(this, R.string.speaker_settings, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IVISetting.startSettings(SettingsJumpPageActivity.this, IVISetting.Global.Speaker);
            }
        }), new IVIButton(this, R.string.eq_settings, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IVISetting.startSettings(SettingsJumpPageActivity.this, IVISetting.Global.EQ);
            }
        }), new IVIButton(this, R.string.sound_settings, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IVISetting.startSettings(SettingsJumpPageActivity.this, IVISetting.Global.Sound);
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.car_door_switch, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IVISetting.startSettings(SettingsJumpPageActivity.this, IVISetting.Global.CarDoorSwitch);
            }
        }), new IVIButton(this, R.string.default_navi_package, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IVISetting.startSettings(SettingsJumpPageActivity.this, IVISetting.Global.DefaultNaviPackage);
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
