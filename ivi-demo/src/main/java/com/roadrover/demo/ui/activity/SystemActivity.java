package com.roadrover.demo.ui.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.SeekBar;

import com.roadrover.demo.R;
import com.roadrover.demo.ui.view.IVIButton;
import com.roadrover.sdk.media.IVIMedia;
import com.roadrover.sdk.setting.IVISetting;
import com.roadrover.sdk.system.IVISystem;
import com.roadrover.sdk.system.SystemManager;
import com.roadrover.services.system.IGpsCallback;
import com.roadrover.services.system.ISystemCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统Activity.
 */

public class SystemActivity extends SDKActivity {

    // 系统管理类对象
    private SystemManager mSystemManager = null;

    // system callback
    private ISystemCallback.Stub mSystemCallback = new ISystemCallback.Stub() {

        @Override
        public void onOpenScreen(int from) throws RemoteException {
            showCallback("onOpenScreen");
        }

        @Override
        public void onCloseScreen(int from) throws RemoteException {
            showCallback("onCloseScreen from:" + IVISystem.EventScreenOperate.getFromName(from));
        }

        @Override
        public void onScreenBrightnessChange(int id, int brightness) throws RemoteException {
            showCallback("onScreenBrightnessChange id:" + IVISystem.ScreenBrightnessId.getName(id) + " brightness:" + brightness);
        }

        @Override
        public void onCurrentScreenBrightnessChange(int id, int brightness) throws RemoteException {
            showCallback("onCurrentScreenBrightnessChange id:" + IVISystem.ScreenBrightnessId.getName(id) + " brightness:" + brightness);
        }

        @Override
        public void quitApp() throws RemoteException {
            showCallback("quitApp");
        }

        @Override
        public void startNavigationApp() throws RemoteException {
            showCallback("startNavigationApp");
        }

        @Override
        public void onMediaAppChanged(String packageName, boolean isOpen) throws RemoteException {
            showCallback("onMediaAppChanged packageName:" + packageName + " isOpen:" + isOpen);
        }

        @Override
        public void gotoSleep() throws RemoteException {
            showCallback("gotoSleep");
        }

        @Override
        public void wakeUp() throws RemoteException {
            showCallback("wakeUp");
        }

        @Override
        public void onFloatBarVisibility(int visibility) throws RemoteException {
            showCallback("onFloatBarVisibility visibility:" + visibility);
        }

        @Override
        public void onTboxChange(boolean isOpen) throws RemoteException {
            showCallback("onTboxChange isOpen: " + isOpen);
        }

        @Override
        public void onScreenProtection(boolean isEnterScreenProtection) throws RemoteException {
            showCallback("onScreenProtection isEnterScreenProtection: " + isEnterScreenProtection);
        }
    };

    // gps 回调监听
    private IGpsCallback.Stub mGpsCallback = new IGpsCallback.Stub() {
        @Override
        public void onGpsLocationInfoChanged(String longitude, String latitude, float accuracy, double altitude, float fSpeed) throws RemoteException {
            showCallback("onGpsLocationInfoChanged longitude:" + longitude + " latitude:" + latitude +
                         " accuracy:" + accuracy + " altitude:" + altitude + " fSpeed:" + fSpeed);
        }

        @Override
        public void onGpsCountChanged(int iGpsInView, int iGpsInUse, int iGlonassInView, int iGLonassInUse) throws RemoteException {
            showCallback("onGpsCountChanged iGpsInView:" + iGpsInView + " iGpsInUse:" + iGpsInUse +
                    " iGlonassInView:" + iGlonassInView + " iGLonassInUse:" + iGLonassInUse);
            if (mSystemManager != null) {
                showCallback("isInPosition:" + mSystemManager.isInPosition());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 创建SDK Manager对象
        mSystemManager = new SystemManager(this, this);
        mSystemManager.registerSystemCallback(mSystemCallback);
    }

    @Override
    protected void onViewValid() {
        super.onViewValid();

        // 背光亮度 start
        // 白天亮度，注意，获取参数必须在 onServiceConnected里面获取，当时UI创建可以不在这里面创建
        addProgressBar(100, mSystemManager.getScreenBrightness(IVISystem.ScreenBrightnessId.DAY), getString(R.string.day_brightness),
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        if (mSystemManager != null) {
                            mSystemManager.setScreenBrightness(IVISystem.ScreenBrightnessId.DAY, seekBar.getProgress());
                        }
                    }
                });

        // 夜晚亮度
        addProgressBar(100, mSystemManager.getScreenBrightness(IVISystem.ScreenBrightnessId.NIGHT), getString(R.string.night_brightness),
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        if (mSystemManager != null) {
                            mSystemManager.setScreenBrightness(IVISystem.ScreenBrightnessId.NIGHT, seekBar.getProgress());
                        }
                    }
                });

        addIVIButtons(new IVIButton(this, R.string.get_current_brightness_id, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemManager != null) {
                    showTips(getString(R.string.get_current_brightness_id) + " :" +
                            IVISystem.ScreenBrightnessId.getName(mSystemManager.getCurrentBrightnessId()));
                }
            }
        }), new IVIButton(this, R.string.get_current_brightness, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemManager != null) {
                    showTips(getString(R.string.get_current_brightness) + " brightness:" +
                            mSystemManager.getScreenBrightness(mSystemManager.getCurrentBrightnessId()));
                }
            }
        }), new IVIButton(this, R.string.get_max_brightness, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemManager != null) {
                    showTips(getString(R.string.get_max_brightness) + " brightness:" +
                            mSystemManager.getMaxScreenBrightness());
                }
            }
        }));

        // 背光亮度 end

        // 屏幕操作 start
        addIVIButtons(new IVIButton(this, R.string.open_back_light, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemManager != null) {
                    mSystemManager.openBackLight();
                }
            }
        }), new IVIButton(this, R.string.close_back_light, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemManager != null) {
                    mSystemManager.closeBackLight();
                }
            }
        }), new IVIButton(this, R.string.is_open_back_light, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemManager != null) {
                    showTips(getString(R.string.is_open_back_light) + ":" +
                            mSystemManager.isOpenBackLight());
                }
            }
        }));
        // 屏幕操作 end

        // app 操作 start
        addIVIButtons(new IVIButton(this, R.string.close_app, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemManager != null) {
                    mSystemManager.closeApp(IVISystem.PACKAGE_RADIO);
                }
            }
        }), new IVIButton(this, R.string.close_all_app, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemManager != null) {
                    mSystemManager.closeAllApp();
                }
            }
        }), new IVIButton(this, R.string.get_not_run_apps, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemManager != null) {
                    List<String> historyPackages = new ArrayList<String>();
                    historyPackages.add(IVISystem.PACKAGE_RADIO);
                    historyPackages.add(IVISystem.PACKAGE_MUSIC);
                    historyPackages.add(IVISystem.PACKAGE_VIDEO);
                    historyPackages = mSystemManager.getNotRunActivityPackageNames(historyPackages);
                    for (String packageName : historyPackages) {
                        showTips(getString(R.string.get_not_run_apps) + ":" + packageName);
                    }
                }
            }
        }));

        // 该方法一般是在断电起来时，launcher进行调用，在launcher完全进入主界面之后，就可以拉起记忆应用
        addIVIButtons(new IVIButton(this, R.string.get_memory_app, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemManager != null) {
                    showTips(getString(R.string.get_memory_app) + " :" + mSystemManager.getMemoryAppPackageName());
                }
            }
        }), new IVIButton(this, R.string.open_memory_app, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemManager != null) {
                    mSystemManager.openMemoryApp();
                }
            }
        }), new IVIButton(this, R.string.open_navi_app, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemManager != null) {
                    mSystemManager.openNaviApp(); // 该方法调用，会接受到 startNavigationApp 回调，launcher接到回调之后打开导航
                }
            }
        }));
        // app 操作 end

        // gps 操作 start
        addIVIButtons(new IVIButton(this, R.string.open_gps_listener, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemManager != null) {
                    mSystemManager.setGpsInfoListener(mGpsCallback);
                }
            }
        }), new IVIButton(this, R.string.close_gps_listener, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemManager != null) {
                    mSystemManager.setGpsInfoListener(null);
                }
            }
        }));
        // gps 操作 end

        // 重启，恢复出厂设置等操作 start
        addIVIButtons(new IVIButton(this, R.string.reboot, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemManager != null) {
                    mSystemManager.reboot();
                }
            }
        }), new IVIButton(this, R.string.recovery, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemManager != null) {
                    mSystemManager.recoverySystem(false);
                }
            }
        }));
        // 重启，恢复出厂设置等操作 end

        // 升级系统 start
        addIVIButtons(new IVIButton(this, R.string.upgrade_system, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemManager != null) {
                    mSystemManager.upgradePackage("", // 系统升级文件
                            new IVISystem.ProgressListener() {
                                @Override
                                public void onProgress(int progress) {
                                    showCallback("upgradePackage onProgress progress:" + progress);
                                }

                                @Override
                                public void onUpgradeStatus(int status, String filepath) {
                                    showCallback("upgradePackage onUpgradeStatus status:" + IVISystem.UpgradeStatus.getName(status) +
                                            " filepath:" + filepath);
                                }
                            });
                }
            }
        }), new IVIButton(this, R.string.cancel_upgrade, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSystemManager != null) {
                    mSystemManager.cancelUpgradePackage();
                }
            }
        }));
        // 升级系统 end
    }

    @Override
    protected void onDestroy() {
        if (null != mSystemManager) {
            mSystemManager.unRegisterSystemCallback(mSystemCallback);
            mSystemManager.setGpsInfoListener(null);
            mSystemManager.disconnect();
            mSystemManager = null;
        }
        super.onDestroy();
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();

        showTips(getString(R.string.day_brightness) + " " + mSystemManager.getScreenBrightness(IVISystem.ScreenBrightnessId.DAY));
        showTips(getString(R.string.night_brightness) + " " + mSystemManager.getScreenBrightness(IVISystem.ScreenBrightnessId.NIGHT));
    }

    @Override
    protected void onViewInvalid() {
        super.onViewInvalid();
    }
}
