package com.roadrover.demo.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.roadrover.demo.R;
import com.roadrover.demo.ui.view.IVIButton;
import com.roadrover.sdk.radio.IVIRadio;
import com.roadrover.sdk.radio.RadioManager;

/**
 * 收音机Activity.
 */

public class RadioActivity extends SDKActivity {

    // 收音管理类对象
    private RadioManager mRadioManager = null;

    // 收音监听对象
    private RadioManager.RadioListener mRadioListener = new RadioManager.RadioListener() {
        @Override
        public void onFreqChanged(int freq) {
            showCallback("onFreqChanged, freq: " + freq);
        }

        @Override
        public void onScanResult(int freq, int signalStrength) {
            showCallback("onScanResult, freq: " + freq + " signalStrength: " + signalStrength);
        }

        @Override
        public void onScanStart(boolean isScanAll) {
            showCallback("onScanStart, isScanAll: " + isScanAll);
        }

        @Override
        public void onScanEnd(boolean isScanAll) {
            showCallback("onScanEnd, isScanAll: " + isScanAll);
        }

        @Override
        public void onScanAbort(boolean isScanAll) {
            showCallback("onScanAbort, isScanAll: " + isScanAll);
        }

        @Override
        public void onSignalUpdate(int freq, int signalStrength) {
            showCallback("onSignalUpdate, freq: " + freq + " signalStrength: " + signalStrength);
        }

        @Override
        public void suspend() {
            showCallback("suspend");
        }

        @Override
        public void resume() {
            showCallback("resume");
        }

        @Override
        public void pause() {
            showCallback("pause");
        }

        @Override
        public void play() {
            showCallback("play");
        }

        @Override
        public void playPause() {
            showCallback("playPause");
        }

        @Override
        public void stop() {
            showCallback("stop");
        }

        @Override
        public void next() {
            showCallback("next");
        }

        @Override
        public void prev() {
            showCallback("prev");
        }

        @Override
        public void quitApp() {
            showCallback("quitApp");
        }

        @Override
        public void select(int index) {
            showCallback("select, index: " + index);
        }

        @Override
        public void setFavour(boolean isFavour) {
            showCallback("setFavour, isFavour: " + isFavour);
        }

        @Override
        public void onRdsPsChanged(int pi, int freq, String ps) {
            showCallback("onRdsPsChanged, pi: " + pi + " freq: " + freq + " ps: " + ps);
        }

        @Override
        public void onRdsRtChanged(int pi, int freq, String rt) {
            showCallback("onRdsRtChanged, pi: " + pi + " freq: " + freq + " rt: " + rt);
        }

        @Override
        public void onRdsMaskChanged(int pi, int freq, int pty, int tp, int ta) {
            showCallback("onRdsMaskChanged, pi: " + pi + " freq: " + freq +
                    " pty: " + pty + " tp: " + tp + " ta: " + ta);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 创建SDK Manager对象
        mRadioManager = new RadioManager(this, this, mRadioListener);
    }

    @Override
    protected void onDestroy() {
        if (null != mRadioManager) {
            mRadioManager.close();
            mRadioManager.disconnect();
            mRadioManager = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onViewValid() {
        super.onViewValid();

        // 添加功能视图
        addIVIButtons(new IVIButton(this, getString(R.string.open) + getString(R.string.radio), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.open();
                }
            }
        }), new IVIButton(this, getString(R.string.close) + getString(R.string.radio), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.close();
                }
            }
        }));

        addIVIButtons(new IVIButton(this, getString(R.string.set_freq) + "104.3MHz", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.setFreq(104300);
                }
            }
        }), new IVIButton(this, getString(R.string.set_freq) + "801KHz", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.setFreq(801);
                }
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.scan_all, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.scanAll();
                }
            }
        }), new IVIButton(this, R.string.scan_up, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.scanUp(104300);
                }
            }
        }), new IVIButton(this, R.string.scan_down, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.scanDown(104300);
                }
            }
        }), new IVIButton(this, R.string.scan_stop, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.scanStop();
                }
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.step_up, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.step(IVIRadio.Direction.UP);
                }
            }
        }), new IVIButton(this, R.string.step_down, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.step(IVIRadio.Direction.DOWN);
                }
            }
        }));

        addIVIButtons(new IVIButton(this, getString(R.string.set_band) + "FM", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.setBand(IVIRadio.Band.FM);
                }
            }
        }), new IVIButton(this, getString(R.string.set_band) + "AM", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.setFreq(IVIRadio.Band.AM);
                }
            }
        }));

        addIVIButtons(new IVIButton(this, getString(R.string.set_location) + getString(R.string.asia), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.setLocation(IVIRadio.Location.ASIA);
                }
            }
        }), new IVIButton(this, getString(R.string.set_location) + getString(R.string.europe), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.setLocation(IVIRadio.Location.EUROPE);
                }
            }
        }), new IVIButton(this, getString(R.string.set_location) + getString(R.string.us), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.setLocation(IVIRadio.Location.US);
                }
            }
        }), new IVIButton(this, getString(R.string.set_location) + getString(R.string.latin), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.setLocation(IVIRadio.Location.LATIN);
                }
            }
        }));

        addIVIButtons(new IVIButton(this, getString(R.string.open) + "TA", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.selectRdsTa(true);
                }
            }
        }), new IVIButton(this, getString(R.string.close) + "TA", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.selectRdsTa(false);
                }
            }
        }), new IVIButton(this, getString(R.string.open) + "AF", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.selectRdsAf(true);
                }
            }
        }), new IVIButton(this, getString(R.string.close) + "AF", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRadioManager) {
                    mRadioManager.selectRdsAf(false);
                }
            }
        }));
    }

    @Override
    protected void onViewInvalid() {
        super.onViewInvalid();
    }
}
