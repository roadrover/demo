package com.roadrover.demo.ui.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;

import com.roadrover.demo.R;
import com.roadrover.demo.ui.view.IVIButton;
import com.roadrover.sdk.media.IVIMedia;
import com.roadrover.sdk.media.MediaManager;
import com.roadrover.sdk.radio.IVIRadio;
import com.roadrover.sdk.radio.RadioManager;
import com.roadrover.sdk.system.IVISystem;
import com.roadrover.sdk.voice.IVIVoice;
import com.roadrover.sdk.voice.VoiceManager;
import com.roadrover.services.voice.IVoiceCallback;

/**
 * 语音Activity.
 */

public class VoiceActivity extends SDKActivity {

    // 语音管理类对象
    private VoiceManager mVoiceManager = null;
    private MediaManager mMediaManager = null;
    private RadioManager mRadioManager = null;

    // 语音回调
    private IVoiceCallback.Stub mVoiceCallback = new IVoiceCallback.Stub() {
        @Override
        public void openVoiceApp() throws RemoteException {
            // 打开语音应用
            showCallback("openVoiceApp");
        }

        @Override
        public void closeVoiceApp() throws RemoteException {
            // 关闭语音应用
            showCallback("closeVoiceApp");
        }
    };

    // 多媒体控制回调接口
    private IVIMedia.MediaControlListener mMediaControlListener = new IVIMedia.MediaControlListener() {
        @Override
        public void suspend() {
            showCallback("MediaListener suspend");
        }

        @Override
        public void stop() {
            showCallback("MediaListener stop");
        }

        @Override
        public void resume() {
            showCallback("MediaListener resume");
        }

        @Override
        public void pause() {
            showCallback("MediaListener pause");
        }

        @Override
        public void play() {
            showCallback("MediaListener play");
        }

        @Override
        public void playPause() {
            showCallback("MediaListener playPause");
        }

        @Override
        public void setVolume(float volume) {
            showCallback("MediaListener setVolume volume:" + volume);
        }

        @Override
        public void next() {
            showCallback("MediaListener next");
        }

        @Override
        public void prev() {
            showCallback("MediaListener prev");
        }

        @Override
        public void select(int index) {
            showCallback("MediaListener select index:" + index);
        }

        @Override
        public void setFavour(boolean isFavour) {
            showCallback("MediaListener setFavour isFavour:" + isFavour);
        }

        @Override
        public void filter(String title, String singer) {
            showCallback("MediaListener filter title:" + title + " singer:" + singer);
        }

        @Override
        public void playRandom() {
            showCallback("MediaListener playRandom");
        }

        @Override
        public void setPlayMode(int mode) {
            showCallback("MediaListener setPlayMode mode:" + IVIVoice.MediaPlayMode.getName(mode));
        }

        @Override
        public void quitApp(int quitSource) {
            showCallback("MediaListener quitApp" );
        }

        @Override
        public void onVideoPermitChanged(boolean show) {
            showCallback("MediaListener onVideoPermitChanged show:" + show);
        }

        @Override
        public void seekTo(int msec) {
            showCallback("MediaListener seekTo msec:" + msec);
        }
    };

    private RadioManager.RadioListener mRadioListener = new RadioManager.RadioListener() {
        @Override
        public void onFreqChanged(int freq) {
            showCallback("RadioListener onFreqChanged freq:" + freq);
        }

        @Override
        public void onScanResult(int freq, int signalStrength) {
            showCallback("RadioListener onScanResult freq:" + freq + " signalStrength:" + signalStrength);
        }

        @Override
        public void onScanStart(boolean isScanAll) {
            showCallback("RadioListener onScanStart isScanAll:" + isScanAll);
        }

        @Override
        public void onScanEnd(boolean isScanAll) {
            showCallback("RadioListener onScanEnd isScanAll:" + isScanAll);
        }

        @Override
        public void onScanAbort(boolean isScanAll) {
            showCallback("RadioListener onScanAbort isScanAll:" + isScanAll);
        }

        @Override
        public void onSignalUpdate(int freq, int signalStrength) {
            showCallback("RadioListener onSignalUpdate freq:" + freq + " signalStrength:" + signalStrength);
        }

        @Override
        public void suspend() {
            showCallback("RadioListener suspend");
        }

        @Override
        public void resume() {
            showCallback("RadioListener resume");
        }

        @Override
        public void pause() {
            showCallback("RadioListener pause");
        }

        @Override
        public void play() {
            showCallback("RadioListener play");
        }

        @Override
        public void playPause() {
            showCallback("RadioListener playPause");
        }

        @Override
        public void stop() {
            showCallback("RadioListener stop");
        }

        @Override
        public void next() {
            showCallback("RadioListener next");
        }

        @Override
        public void prev() {
            showCallback("RadioListener prev");
        }

        @Override
        public void quitApp() {
            showCallback("RadioListener quitApp");
        }

        @Override
        public void select(int index) {
            showCallback("RadioListener select index:" + index);
        }

        @Override
        public void setFavour(boolean isFavour) {
            showCallback("RadioListener setFavour isFavour:" + isFavour);
        }

        @Override
        public void onRdsPsChanged(int pi, int freq, String ps) {
            showCallback("onRdsPsChanged pi:" + pi + " freq:" + freq + " ps:" + ps);
        }

        @Override
        public void onRdsRtChanged(int pi, int freq, String rt) {
            showCallback("onRdsRtChanged pi:" + pi + " freq:" + freq + " rt:" + rt);
        }

        @Override
        public void onRdsMaskChanged(int pi, int freq, int pty, int tp, int ta) {
            showCallback("onRdsMaskChanged pi:" + pi + " freq:" + freq + " pty:" + pty + " tp:" + tp + " ta:" + ta);
        }

        @Override
        public void scanUp() {
            showCallback("scanUp");
        }

        @Override
        public void scanDown() {
            showCallback("scanDown");
        }

        @Override
        public void scanAll() {
            showCallback("scanAll");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 多媒体管理类
        mMediaManager = new MediaManager(this, null, mMediaControlListener);

        // 收音机管理类
        mRadioManager = new RadioManager(this, null, mRadioListener);

        // 创建SDK Manager对象
        mVoiceManager = new VoiceManager(this, this);
        // 注册语音回调
        mVoiceManager.registerVoiceCallback(mVoiceCallback);
    }

    @Override
    protected void onDestroy() {
        if (mMediaManager != null) {
            mMediaManager.close(IVIMedia.Type.MUSIC);
            mMediaManager.disconnect();
            mMediaManager = null;
        }

        if (mRadioManager != null) {
            mRadioManager.close();
            mRadioManager.disconnect();
            mRadioManager = null;
        }

        if (null != mVoiceManager) {
            mVoiceManager.unregisterVoiceCallback(mVoiceCallback);
            mVoiceManager.disconnect();
            mVoiceManager = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onViewValid() {
        super.onViewValid();

        // 操作app start
        addIVIButtons(new IVIButton(this, R.string.open_voice_app, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) {
                    mVoiceManager.openVoiceApp();
                }
            }
        }), new IVIButton(this, getString(R.string.open) + getString(R.string.radio), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) {
                    mVoiceManager.openApp(IVISystem.PACKAGE_RADIO, IVISystem.ACTIVITY_RADIO);
                }
            }
        }), new IVIButton(this, getString(R.string.close) + getString(R.string.radio), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) {
                    mVoiceManager.closeApp(IVISystem.PACKAGE_RADIO);
                }
            }
        }));
        // 操作app end

        // 控制哪些app start，注：实际调用的时候，这些接口并不需要调用，只是为了能用callback回调显示，才需要调用
        addIVIButtons(new IVIButton(this, R.string.control_music, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaManager != null) {
                    mMediaManager.open(IVIMedia.Type.MUSIC);
                }
                showTips("start control music!");
            }
        }), new IVIButton(this, getString(R.string.control_radio), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRadioManager != null) {
                    mRadioManager.open();
                }
                showTips("start control radio!");
            }
        }));
        // 控制哪些app end

        // 语音录制流程 start
        addIVIButtons(new IVIButton(this, R.string.start_voice, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) { // 开启语音，其他媒体会收到 suspend
                    mVoiceManager.startVoice();
                }
            }
        }), new IVIButton(this, R.string.end_voice, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) { // 暂停语音，其他媒体会收到 resume
                    mVoiceManager.endVoice();
                }
            }
        }), new IVIButton(this, R.string.mute, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) { // 静音
                    mVoiceManager.setMute(true);
                }
            }
        }), new IVIButton(this, R.string.un_mute, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) { // 解除静音
                    mVoiceManager.setMute(false);
                }
            }
        }));
        // 语音录制流程 end

        // 多媒体控制 start
        addIVIButtons(new IVIButton(this, R.string.play, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) { // 播放音乐，收到该指令，媒体回调会收到 play()
                    mVoiceManager.play();
                }
            }
        }), new IVIButton(this, R.string.pause, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) { // 暂停音乐，媒体回调会收到 pause
                    mVoiceManager.pause();
                }
            }
        }), new IVIButton(this, R.string.next, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) { // 下一曲
                    mVoiceManager.next();
                }
            }
        }), new IVIButton(this, R.string.prev, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) { // 上一曲
                    mVoiceManager.prev();
                }
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.set_play_mode, new View.OnClickListener() {
            private int mPlayMode = IVIVoice.MediaPlayMode.MIN;
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) {
                    mPlayMode = ++mPlayMode > IVIVoice.MediaPlayMode.MAX ?
                            IVIVoice.MediaPlayMode.MIN : mPlayMode;
                    mVoiceManager.setPlayMode(mPlayMode);
                }
            }
        }), new IVIButton(this, R.string.switch_music, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) {
                    mVoiceManager.selectMediaItem(10);
                }
            }
        }), new IVIButton(this, R.string.play_random, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) {
                    mVoiceManager.playRandom();
                }
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.play_appoint_music, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) {
                    mVoiceManager.playMedia("Dream it possible", "张靓颖");
                }
            }
        }), new IVIButton(this, R.string.favour_music, new View.OnClickListener() {
            private boolean mIsFavour = false;
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) {
                    mIsFavour = !mIsFavour;
                    mVoiceManager.favourMedia(mIsFavour);
                }
            }
        }));
        // 多媒体控制 end

        // 收音机控制 start
        addIVIButtons(new IVIButton(this, R.string.set_freq, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) {
                    mVoiceManager.setFreq(97100);
                }
            }
        }), new IVIButton(this, R.string.set_band, new View.OnClickListener() {
            private int mBand = IVIRadio.Band.AM;
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) {
                    mBand = (mBand == IVIRadio.Band.AM ? IVIRadio.Band.FM : IVIRadio.Band.AM);
                    mVoiceManager.setBand(mBand);
                }
            }
        }), new IVIButton(this, R.string.start_scan, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) {
                    mVoiceManager.startScan();
                }
            }
        }), new IVIButton(this, R.string.stop_scan, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) {
                    mVoiceManager.stopScan();
                }
            }
        }));
        // 收音机控制 end

        // 音量控制 start
        addIVIButtons(new IVIButton(this, R.string.add_volume, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) {
                    mVoiceManager.incVolume();
                }
            }
        }), new IVIButton(this, R.string.del_volume, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) {
                    mVoiceManager.delVolume();
                }
            }
        }), new IVIButton(this, R.string.set_volume, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoiceManager != null) {
                    mVoiceManager.setVolume(10);
                }
            }
        }));
        // 音量控制 end
    }

    @Override
    protected void onViewInvalid() {
        super.onViewInvalid();
    }
}
