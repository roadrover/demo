package com.roadrover.demo.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.roadrover.demo.R;
import com.roadrover.demo.ui.view.IVIButton;
import com.roadrover.sdk.audio.AudioParam;
import com.roadrover.sdk.audio.IVIAudio;
import com.roadrover.sdk.audio.IVIAudioManager;

/**
 * 音频Activity.
 */

public class AudioActivity extends SDKActivity {

    // 音频管理类对象
    private IVIAudioManager mAudioManager = null;

    // 音频监听对象
    private IVIAudioManager.AudioListener mAudioListener = new IVIAudioManager.AudioListener() {
        @Override
        public void onVolumeChanged(int id, int value) {
            showCallback("onVolumeChanged, id: " + id + " value: " + value);
        }

        @Override
        public void onMuteChanged(boolean mute, int source) {
            showCallback("onMuteChanged, mute: " + mute + " source: " + source);
        }
    };

    // 音量条状态监听对象
    private IVIAudioManager.VolumeBarListener mVolumeBarListener = new IVIAudioManager.VolumeBarListener() {
        @Override
        public void onShowVolumeBar(int id, int value, int maxValue) {
            showCallback("onShowVolumeBar, id: " + AudioParam.Id.getName(id) + " value: " + value + " maxValue: " + maxValue);
        }

        @Override
        public void onHideVolumeBar() {
            showCallback("onHideVolumeBar");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 创建SDK Manager对象
        mAudioManager = new IVIAudioManager(this, this, mAudioListener);
    }

    @Override
    protected void onDestroy() {
        if (null != mAudioManager) {
            mAudioManager.disconnect();
            mAudioManager = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onViewValid() {
        super.onViewValid();

        // 添加功能视图
        addIVIButtons(new IVIButton(this, R.string.get_master_volume, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAudioManager) {
                    int value = mAudioManager.getParamValue(AudioParam.Id.VOLUME_MASTER);
                    showTips(getString(R.string.get_master_volume) + "：" + value);
                }
            }
        }), new IVIButton(this, R.string.set_master_volume, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAudioManager) {
                    final int value = 6;
                    mAudioManager.setParam(AudioParam.Id.VOLUME_MASTER, value);
                    showTips(getString(R.string.set_master_volume) + "：" + value);
                }
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.show_volume_bar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAudioManager) {
                    mAudioManager.showVolumeBar();
                    showTips(getString(R.string.show_volume_bar));
                }
            }
        }), new IVIButton(this, R.string.set_volume_bar_listener, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAudioManager) {
                    mAudioManager.setVolumeBarListener(mVolumeBarListener);
                    showTips(getString(R.string.set_volume_bar_listener));
                }
            }
        }), new IVIButton(this, R.string.get_active_volume_id, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAudioManager) {
                    int id = mAudioManager.getActiveVolumeId();
                    showTips(getString(R.string.get_active_volume_id) + "：" + AudioParam.Id.getName(id));
                }
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.get_user_eq_gains, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAudioManager) {
                    int[] gains = mAudioManager.getEqGains(AudioParam.EqMode.USER);
                    showTips(getString(R.string.get_user_eq_gains) + "：" + intsToString(gains));
                }
            }
        }), new IVIButton(this, R.string.get_pop_eq_gains, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAudioManager) {
                    int[] gains = mAudioManager.getEqGains(AudioParam.EqMode.POP);
                    showTips(getString(R.string.get_pop_eq_gains) + "：" + intsToString(gains));
                }
            }
        }), new IVIButton(this, R.string.get_live_eq_gains, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAudioManager) {
                    int[] gains = mAudioManager.getEqGains(AudioParam.EqMode.LIVE);
                    showTips(getString(R.string.get_live_eq_gains) + "：" + intsToString(gains));
                }
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.request_internal_short_mute, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAudioManager) {
                    mAudioManager.requestInternalShortMute(500);
                    showTips(R.string.request_internal_short_mute);
                }
            }
        }), new IVIButton(this, R.string.set_analog_media_volume_percent, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAudioManager) {
                    mAudioManager.setAnalogMediaVolumePercent(40);
                    showTips(R.string.set_analog_media_volume_percent);
                }
            }
        }), new IVIButton(this, R.string.get_master_audio_channel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAudioManager) {
                    int channel = mAudioManager.getMasterAudioChannel();
                    showTips(getString(R.string.get_master_audio_channel) + "：" + IVIAudio.Channel.getName(channel));
                }
            }
        }));
    }

    @Override
    protected void onViewInvalid() {
        super.onViewInvalid();
    }

    /**
     * 转换成string
     * @param buff
     * @return
     */
    public static String intsToString(int[] buff) {
        StringBuilder sb = new StringBuilder();
        if (buff != null) {
            for (int i = 0; i < buff.length; ++i) {
                String string = " " + buff[i];
                sb.append(string);
            }
        }
        return sb.toString();
    }
}
