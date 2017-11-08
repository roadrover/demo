package com.roadrover.demo.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.roadrover.demo.R;
import com.roadrover.demo.ui.view.IVIButton;
import com.roadrover.sdk.car.CarManager;
import com.roadrover.sdk.car.IVICar;
import com.roadrover.sdk.utils.Logcat;

/**
 * 原车Activity.
 */

public class CarActivity extends SDKActivity {

    // 原车管理类对象
    private CarManager mCarManager = null;

    // 原车回调对象
    private CarManager.CarListener mCarListener = new CarManager.CarListener() {
        @Override
        public void onMcuVersion(String version) {
            showCallback("onMcuVersion, version: " + version);
        }

        @Override
        public void onAccChanged(boolean on) {
            showCallback("onAccChanged, on: " + on);
        }

        @Override
        public void onCcdChanged(int status) {
            showCallback("onCcdChanged, status: " + IVICar.Ccd.Status.getName(status));
        }

        @Override
        public void onHandbrakeChanged(boolean hold) {
            showCallback("onHandbrakeChanged, hold: " + hold);
        }

        @Override
        public void onDoorChanged(int changeMask, int statusMask) {
            showCallback("onDoorChanged, changeMask: " + changeMask + " statusMask: " + statusMask);
        }

        @Override
        public void onLightChanged(int changeMask, int statusMask) {
            showCallback("onLightChanged, changeMask: " + changeMask + " statusMask: " + statusMask);
        }

        @Override
        public void onHeadLightChanged(boolean on) {
            showCallback("onHeadLightChanged, on: " + on);
        }

        @Override
        public void onClimateChanged(int id, int rawValue) {
            showCallback("onClimateChanged, id: " + id + " rawValue: " + rawValue);
        }

        @Override
        public void onOutsideTempChanged(float tempC) {
            showCallback("onOutsideTempChanged, tempC: " + tempC);
        }

        @Override
        public void onKeyPushed(int id, int type) {
            showCallback("onKeyPushed, id: " + id + " type: " + type);
        }

        @Override
        public void onAlertMessage(int messageCode) {
            showCallback("onAlertMessage, messageCode: " + messageCode);
        }

        @Override
        public void onRealTimeInfoChanged(int id, float value) {
            showCallback("onRealTimeInfoChanged, id: " + id + " value: " + value);
        }

        @Override
        public void onRadarChanged(IVICar.Radar radar) {
            showCallback("onRadarChanged, radar: " + radar);
        }

        @Override
        public void onTripChanged(int id, int index, float value) {
            showCallback("onTripChanged, id: " + id + " index: " + index + " value: " + value);
        }

        @Override
        public void onExtraStateChanged(int id, float value) {
            showCallback("onExtraStateChanged, id: " + id + " value: " + value);
        }

        @Override
        public void onCarSettingChanged(int carId, byte[] data) {
            showCallback("onCarSettingChanged, carId: " + carId + " data: " + data);
        }

        @Override
        public void onExtraDeviceChanged(int carId, int deviceId, byte[] extraDeviceData) {
            showCallback("onExtraDeviceChanged, carId: " + carId + " deviceId: " + deviceId + " extraDeviceData: " + extraDeviceData);
        }

        @Override
        public void onCmdParamChanged(int id, byte[] paramData) {
            showCallback("onCmdParamChanged, id: " + id + " paramData: " + paramData);
        }

        @Override
        public void onMaintenanceChanged(int id, int mileage, int days) {
            showCallback("onMaintenanceChanged, id: " + id + " mileage: " + mileage + " days: " + days);
        }

        @Override
        public void onCarVINChanged(String VIN, int keyNumber) {
            showCallback("onCarVINChanged, VIN: " + VIN + " keyNumber: " + keyNumber);
        }

        @Override
        public void onCarReportChanged(int carid, int type, int[] list) {
            showCallback("onCarReportChanged, carid: " + carid + " type: " + type + " list: " + list);
        }

        @Override
        public void onAutoParkChanged(IVICar.AutoPark status) {
            showCallback("onAutoParkChanged, status: " + status);
        }

        @Override
        public void onEnergyFlowChanged(int battery, int engineToTyre, int engineToMotor, int motorToTyre, int motorToBattery) {
            showCallback("onEnergyFlowChanged, battery: " + battery + " engineToTyre: " + engineToTyre + " motorToTyre:" + motorToTyre + " motorToBattery: " + motorToBattery);
        }

        @Override
        public void onFastReverseChanged(boolean on) {
            showCallback("onFastReverseChanged, on: " + on);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 创建SDK Manager对象
        mCarManager = new CarManager(this, this, mCarListener);
    }

    @Override
    protected void onDestroy() {
        if (null != mCarManager) {
            mCarManager.disconnect();
            mCarManager = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onViewValid() {
        super.onViewValid();

        // 添加功能视图
        addIVIButtons(new IVIButton(this, R.string.ccd_status, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCarManager) {
                    int status = mCarManager.getCcdStatus();
                    showTips(getString(R.string.ccd_status) + "：" + IVICar.Ccd.Status.getName(status));
                }
            }
        }), new IVIButton(this, R.string.headlight_status, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCarManager) {
                    boolean status = mCarManager.getHeadLightStatus();
                    showTips(getString(R.string.headlight_status) + "：" + status);
                }
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.mcu_version, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCarManager) {
                    String version = mCarManager.getProtocolMcuVersion();
                    showTips(getString(R.string.mcu_version) + "：" + version);
                }
            }
        }), new IVIButton(this, R.string.car_id, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCarManager) {
                    int id = mCarManager.getCarId();
                    showTips(getString(R.string.car_id) + "：" + id);
                }
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.fl_door, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCarManager) {
                    boolean open = mCarManager.isDoorOpen(IVICar.Door.Id.FRONT_LEFT);
                    showTips(getString(R.string.fl_door) + "：" + open);
                }
            }
        }), new IVIButton(this, R.string.fr_door, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCarManager) {
                    boolean open = mCarManager.isDoorOpen(IVICar.Door.Id.FRONT_RIGHT);
                    showTips(getString(R.string.fr_door) + "：" + open);
                }
            }
        }), new IVIButton(this, R.string.hoot, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCarManager) {
                    boolean open = mCarManager.isDoorOpen(IVICar.Door.Id.HOOT);
                    showTips(getString(R.string.hoot) + "：" + open);
                }
            }
        }), new IVIButton(this, R.string.trunk, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCarManager) {
                    boolean open = mCarManager.isDoorOpen(IVICar.Door.Id.TRUNK);
                    showTips(getString(R.string.trunk) + "：" + open);
                }
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.turn_left_light, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCarManager) {
                    boolean on = mCarManager.isLightOn(IVICar.Light.Id.TURN_LEFT);
                    showTips(getString(R.string.turn_left_light) + "：" + on);
                }
            }
        }), new IVIButton(this, R.string.turn_right_light, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCarManager) {
                    boolean on = mCarManager.isLightOn(IVICar.Light.Id.TURN_RIGHT);
                    showTips(getString(R.string.turn_right_light) + "：" + on);
                }
            }
        }), new IVIButton(this, R.string.emergency_light, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCarManager) {
                    boolean on = mCarManager.isLightOn(IVICar.Light.Id.EMERGENCY);
                    showTips(getString(R.string.emergency_light) + "：" + on);
                }
            }
        }));
    }

    @Override
    protected void onViewInvalid() {
        super.onViewInvalid();
    }
}
