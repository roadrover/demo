package com.roadrover.demo.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;

import com.roadrover.demo.R;
import com.roadrover.demo.ui.view.IVIButton;
import com.roadrover.sdk.setting.IVISetting;
import com.roadrover.sdk.setting.SettingModel;

/**
 * 监听数据变化设置activity.
 */

public class SettingsMonitorDataActivity extends BaseActivity {

    private SettingModel.ModelListener mAPPDataUsedSpaceListener = new SettingModel.ModelListener() {
        @Override
        public void onChange(String oldVal, String newVal) {
            showChange(R.string.app_data_used_space, oldVal, newVal);
        }
    };

    private SettingModel.ModelListener mCarDoorSwitchListener = new SettingModel.ModelListener() {
        @Override
        public void onChange(String oldVal, String newVal) {
            showChange(R.string.car_door_switch, oldVal, newVal);
        }
    };

    private SettingModel.ModelListener mDefaultNaviPackageListener = new SettingModel.ModelListener() {
        @Override
        public void onChange(String oldVal, String newVal) {
            showChange(R.string.default_navi_package, oldVal, newVal);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 添加功能视图
        addIVIButtons(new IVIButton(this, getString(R.string.register_listener) + "：" + getString(R.string.app_data_used_space), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingModel.registerModelListener(SettingsMonitorDataActivity.this, IVISetting.Global.NAME, IVISetting.Global.APPDataUsedSpace, mAPPDataUsedSpaceListener);
                showRegister(R.string.app_data_used_space);
            }
        }), new IVIButton(this, getString(R.string.unregister_listener) + "：" + getString(R.string.app_data_used_space), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingModel.unregisterModelListener(SettingsMonitorDataActivity.this, mAPPDataUsedSpaceListener);
                showUnregister(R.string.app_data_used_space);
            }
        }));

        addIVIButtons(new IVIButton(this, getString(R.string.register_listener) + "：" + getString(R.string.car_door_switch), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingModel.registerModelListener(SettingsMonitorDataActivity.this, IVISetting.Global.NAME, IVISetting.Global.CarDoorSwitch, mCarDoorSwitchListener);
                showRegister(R.string.car_door_switch);
            }
        }), new IVIButton(this, getString(R.string.unregister_listener) + "：" + getString(R.string.car_door_switch), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingModel.unregisterModelListener(SettingsMonitorDataActivity.this, mCarDoorSwitchListener);
                showUnregister(R.string.car_door_switch);
            }
        }));

        addIVIButtons(new IVIButton(this, getString(R.string.register_listener) + "：" + getString(R.string.default_navi_package), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingModel.registerModelListener(SettingsMonitorDataActivity.this, IVISetting.Global.NAME, IVISetting.Global.DefaultNaviPackage, mDefaultNaviPackageListener);
                showRegister(R.string.default_navi_package);
            }
        }), new IVIButton(this, getString(R.string.unregister_listener) + "：" + getString(R.string.default_navi_package), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingModel.unregisterModelListener(SettingsMonitorDataActivity.this, mDefaultNaviPackageListener);
                showUnregister(R.string.default_navi_package);
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.unregister_all_listener, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingModel.unregisterModelListener(SettingsMonitorDataActivity.this);
                showTips(R.string.unregister_all_listener);
            }
        }));
    }

    @Override
    protected void onDestroy() {
        SettingModel.unregisterModelListener(this);
        super.onDestroy();
    }

    private void showChange(@StringRes int resId, String oldVal, String newVal) {
        showChange(getString(resId), oldVal, newVal);
    }

    private void showChange(@NonNull String string, String oldVal, String newVal) {
        showTips(string + "：" + oldVal + " " + getString(R.string.turn_to) + " " + newVal);
    }

    private void showRegister(@StringRes int resId) {
        showRegister(getString(resId));
    }

    private void showRegister(@NonNull String string) {
        showTips(getString(R.string.register_listener) + "：" + string);
    }

    private void showUnregister(@StringRes int resId) {
        showUnregister(getString(resId));
    }

    private void showUnregister(@NonNull String string) {
        showTips(getString(R.string.unregister_listener) + "：" + string);
    }
}
