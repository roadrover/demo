package com.roadrover.demo.ui.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.SeekBar;

import com.roadrover.btservice.bluetooth.BluetoothDevice;
import com.roadrover.btservice.bluetooth.IBluetoothCallback;
import com.roadrover.btservice.bluetooth.IBluetoothExecCallback;
import com.roadrover.btservice.bluetooth.IBluetoothLinkDeviceCallback;
import com.roadrover.btservice.bluetooth.IBluetoothStatusCallback;
import com.roadrover.btservice.bluetooth.IDeviceCallback;
import com.roadrover.btservice.bluetooth.ISearchDeviceCallback;
import com.roadrover.demo.R;
import com.roadrover.demo.ui.view.IVIButton;
import com.roadrover.sdk.bluetooth.BluetoothManager;
import com.roadrover.sdk.bluetooth.IVIBluetooth;
import com.roadrover.sdk.media.IVIMedia;
import com.roadrover.sdk.utils.Logcat;

import java.util.List;

/**
 * 蓝牙Activity.
 * @注 蓝牙部门直接调用的接口回调，并没有在主线程，如果直接用数据刷新UI，最好通过runOnUiThread方式
 */

public class BluetoothActivity extends SDKActivity {

    // 蓝牙管理类对象
    private BluetoothManager mBluetoothManager = null;

    // 蓝牙回调对象
    private IBluetoothCallback.Stub mBluetoothCallback = new IBluetoothCallback.Stub() {
        @Override
        public void onConnectStatus(int nStatus, String addr, String name) throws RemoteException {
            showCallback("onConnectStatus status:" + IVIBluetooth.BluetoothConnectStatus.getName(nStatus) +
                    " addr:" + addr + " name:" + name);
        }

        @Override
        public void onCallStatus(int nStatus, String phoneNumber, String contactName) throws RemoteException {
            showCallback("onCallStatus status:" + IVIBluetooth.CallStatus.getName(nStatus) +
                    " phoneNumber:" + phoneNumber + " contactName:" + contactName);
        }

        @Override
        public void onVoiceChange(int type) throws RemoteException {
            showCallback("onVoiceChange type:" + IVIBluetooth.BluetoothAudioTransferStatus.getName(type));
        }

        @Override
        public void onA2DPConnectStatus(int avStatus, boolean isStoped) throws RemoteException {
            showCallback("onA2DPConnectStatus avStatus:" + IVIBluetooth.BluetoothA2DPStatus.getName(avStatus) + " isStoped:" + isStoped);
        }

        @Override
        public void onBtMusicId3Info(String name, String artist, String album, long duration) throws RemoteException {
            showCallback("onBtMusicId3Info name:" + name + " artist:" + artist + " album:" + album + " duration:" + duration);
        }

        @Override
        public void onBtBatteryValue(int value) throws RemoteException {
            showCallback("onBtBatteryValue value:" + value);
        }

        @Override
        public void onBtSignalValue(int value) throws RemoteException {
            showCallback("onBtSignalValue value:" + value);
        }

        @Override
        public void onPowerStatus(boolean value) throws RemoteException {
            showCallback("onPowerStatus value:" + value);
        }
    };

    // 蓝牙音乐控制回调
    private IBluetoothExecCallback.Stub mBluetoothMusicExecCallback = new IBluetoothExecCallback.Stub() {
        @Override
        public void onSuccess(String msg) throws RemoteException {

        }

        @Override
        public void onFailure(int errorCode) throws RemoteException {
            showCallback("exec bluetooth music com onFailure:" + IVIBluetooth.BluetoothExecErrorMsg.getName(errorCode));
        }
    } ;

    // 蓝牙DTFM指令回调
    private IBluetoothExecCallback.Stub mBluetoothDTMFExecCallback = new IBluetoothExecCallback.Stub() {
        @Override
        public void onSuccess(String msg) throws RemoteException {

        }

        @Override
        public void onFailure(int errorCode) throws RemoteException {
            showCallback("DTMF onFailure:" + IVIBluetooth.BluetoothExecErrorMsg.getName(errorCode));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 创建SDK Manager对象
        mBluetoothManager = new BluetoothManager(this, this);
        mBluetoothManager.registerBluetoothCallback(mBluetoothCallback);
    }

    @Override
    protected void onDestroy() {
        if (null != mBluetoothManager) {
            mBluetoothManager.unregisterBluetoothCallback(mBluetoothCallback);
            mBluetoothManager.disconnect();
            mBluetoothManager = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onViewValid() {
        super.onViewValid();

        // 获取蓝牙设备本身的信息 start
        addIVIButtons(new IVIButton(this, R.string.get_bluetooth_name, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    mBluetoothManager.getBluetoothName(new IBluetoothExecCallback.Stub() {
                        @Override
                        public void onSuccess(String msg) throws RemoteException {
                            showTips(getString(R.string.get_bluetooth_name) + ":" + msg);
                        }

                        @Override
                        public void onFailure(int errorCode) throws RemoteException {

                        }
                    });
                }
            }
        }), new IVIButton(this, R.string.get_bluetooth_pin, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    mBluetoothManager.getBluetoothPin(new IBluetoothExecCallback.Stub() {
                        @Override
                        public void onSuccess(String msg) throws RemoteException {
                            showTips(getString(R.string.get_bluetooth_pin) + ":" +msg);
                        }

                        @Override
                        public void onFailure(int errorCode) throws RemoteException {

                        }
                    });
                }
            }
        }), new IVIButton(this, R.string.get_bluetooth_ver, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logcat.d("");
                if (mBluetoothManager != null) {
                    mBluetoothManager.getBluetoothVer(new IBluetoothExecCallback.Stub() {
                        @Override
                        public void onSuccess(String msg) throws RemoteException {
                            showTips(getString(R.string.get_bluetooth_ver) + " onSuccess:" + msg);
                        }

                        @Override
                        public void onFailure(int errorCode) throws RemoteException {
                            showTips(getString(R.string.get_bluetooth_ver) + " onFailure:" +
                                    IVIBluetooth.BluetoothExecErrorMsg.getName(errorCode));
                        }
                    });
                }
            }
        }));
        // 获取蓝牙设备本身的信息 end

        // 获取蓝牙状态信息 start
        addIVIButtons(new IVIButton(this, R.string.link_devices, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    mBluetoothManager.linkDevice("", // 设备地址
                            new IBluetoothExecCallback.Stub() {
                                @Override
                                public void onSuccess(String msg) throws RemoteException {

                                }

                                @Override
                                public void onFailure(int errorCode) throws RemoteException {
                                    showTips("linkDevice onFailure:" + IVIBluetooth.BluetoothExecErrorMsg.getName(errorCode));
                                }
                            });
                }
            }
        }), new IVIButton(this, R.string.unlink_devices, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    mBluetoothManager.unlinkDevice(new IBluetoothExecCallback.Stub() {
                        @Override
                        public void onSuccess(String msg) throws RemoteException {

                        }

                        @Override
                        public void onFailure(int errorCode) throws RemoteException {
                            showTips("unlinkDevice onFailure:" + IVIBluetooth.BluetoothExecErrorMsg.getName(errorCode));
                        }
                    });
                }
            }
        }));
        // 获取蓝牙状态信息 end

        // 蓝牙连接设备，断开连接，连接最后一个设备接口 start
        addIVIButtons(new IVIButton(this, R.string.get_bluetooth_status, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    mBluetoothManager.getBluetoothModuleStatus(new IBluetoothStatusCallback.Stub() {
                        @Override
                        public void onSuccess(int status, int hfpStatus, int a2dpStatus) throws RemoteException {
                            showTips("getBluetoothModuleStatus onSuccess status:" + IVIBluetooth.BluetoothConnectStatus.getName(status) +
                                    " hfpStatus:" + IVIBluetooth.BluetoothHIDStatus.getName(hfpStatus) +
                                    " a2dpStatus:" + IVIBluetooth.BluetoothA2DPStatus.getName(a2dpStatus));
                        }

                        @Override
                        public void onFailure(int errorCode) throws RemoteException {
                            showTips("getBluetoothModuleStatus onFailure errorCode:" + IVIBluetooth.BluetoothExecErrorMsg.getName(errorCode));
                        }
                    });
                }
            }
        }), new IVIButton(this, R.string.get_connect_bluetooth_status, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    mBluetoothManager.getBluetoothState(new IBluetoothLinkDeviceCallback.Stub() {
                        @Override
                        public void onSuccess(int status, String addr, String name) throws RemoteException {
                            showTips("getBluetoothState onSuccess status:" + IVIBluetooth.BluetoothConnectStatus.getName(status) +
                                    " addr:" + addr +
                                    " name:" + name);
                        }

                        @Override
                        public void onFailure(int errorCode) throws RemoteException {
                            showTips("getBluetoothState onFailure errorCode:" + IVIBluetooth.BluetoothExecErrorMsg.getName(errorCode));
                        }
                    });
                }
            }
        }));
        // 蓝牙连接设备，断开连接，连接最后一个设备接口 end

        // a2dp 控制指令 start，蓝牙控制命令的回调可以不传，也可以传递，在onFailure位置做一些错误处理，也可以通过监听onA2DPConnectStatus连接状态做处理
        addIVIButtons(new IVIButton(this, R.string.play, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    mBluetoothManager.playBtMusic(mBluetoothMusicExecCallback);
                }
            }
        }), new IVIButton(this, R.string.pause, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    mBluetoothManager.pauseBtMusic(mBluetoothMusicExecCallback);
                }
            }
        }), new IVIButton(this, R.string.next, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    mBluetoothManager.nextBtMusic(mBluetoothMusicExecCallback);
                }
            }
        }), new IVIButton(this, R.string.prev, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    mBluetoothManager.prevBtMusic(mBluetoothMusicExecCallback);
                }
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.play_pause, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) { // 建议蓝牙音乐用playAndPause接口做UI上的播放暂停动作，因为存在手机状态和车机不同步的现象
                    // 或者可以点击播放，先刷新UI，然后开启定时器，3s左右检测是否有播放的回调过来，如果没有再刷新成暂停
                    mBluetoothManager.playAndPause(mBluetoothMusicExecCallback);
                }
            }
        }), new IVIButton(this, R.string.get_bluetooth_music_id3_info, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) { // 调用该方法之后，id3信息会通过 onBtMusicId3Info 回调
                    mBluetoothManager.getBtMusicId3Info(mBluetoothMusicExecCallback);
                }
            }
        }));

        // 蓝牙音乐音量条，该指令需要在媒体回调的 setVolume 里面调用，否则有可能出现导航时，蓝牙音乐未被降音的情况
        addProgressBar(100, 100, getString(R.string.bluetooth_music_volume), new SeekBar.OnSeekBarChangeListener() {
            private boolean mIsTouch = false;
            @Override
            public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mIsTouch = true;
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                if (mIsTouch) {
                    if (mBluetoothManager != null) {
                        mBluetoothManager.setBtMusicVolumePercent((float) seekBar.getProgress() / seekBar.getMax(),
                                new IBluetoothExecCallback.Stub() {
                                    @Override
                                    public void onSuccess(String msg) throws RemoteException {
                                        showCallback("setBtMusicVolumePercent success progress:" + seekBar.getProgress() +
                                                " max:" + seekBar.getMax());
                                    }

                                    @Override
                                    public void onFailure(int errorCode) throws RemoteException {

                                    }
                                });
                    }
                }
                mIsTouch = false;
            }
        });
        // a2dp 控制指令 end

        // 蓝牙搜索设备，获取配对列表 start
        addIVIButtons(new IVIButton(this, R.string.search_new_devices, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    showTips("searchNewDevice start");
                    mBluetoothManager.searchNewDevice(0, new ISearchDeviceCallback.Stub() { // 建议在播放蓝牙音乐的时候不要进行搜索动作，有可能会对音乐有影响
                        @Override
                        public void onSuccess(BluetoothDevice bluetoothDevices) throws RemoteException {
                            showCallback("searchNewDevice onSuccess name:" + bluetoothDevices.name + " addr:" + bluetoothDevices.addr);
                        }

                        @Override
                        public void onProgress(BluetoothDevice bluetoothDevices) throws RemoteException {
                            showCallback("searchNewDevice onProgress name:" + bluetoothDevices.name + " addr:" + bluetoothDevices.addr);
                        }

                        @Override
                        public void onFailure(int errorCode) throws RemoteException {
                            showCallback("searchNewDevice onFailure errorCode:" + IVIBluetooth.BluetoothExecErrorMsg.getName(errorCode));
                        }
                    });
                }
            }
        }), new IVIButton(this, R.string.get_paired_list, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    showTips("getPairedDevice start");
                    mBluetoothManager.getPairedDevice(0, new IDeviceCallback.Stub() {
                        @Override
                        public void onSuccess(List<BluetoothDevice> bluetoothDevices, BluetoothDevice curBluetoothDevices) throws RemoteException {
                            for (int i = 0; i < bluetoothDevices.size(); ++i) {
                                showCallback("getPairedDevice onSuccess " + i +
                                        " name:" + bluetoothDevices.get(i).name +
                                        " addr:" + bluetoothDevices.get(i).addr);
                            }
                            showCallback("getPairedDevice onSuccess curBluetoothDevices name:" + curBluetoothDevices.name +
                                    " addr:" + curBluetoothDevices.addr);
                        }

                        @Override
                        public void onFailure(int errorCode) throws RemoteException {
                            showCallback("getPairedDevice onFailure errorCode:" + IVIBluetooth.BluetoothExecErrorMsg.getName(errorCode));
                        }
                    });
                }
            }
        }));
        // 蓝牙搜索设备，获取配对列表 end

        // 蓝牙电话操作接口 start
        addIVIButtons(new IVIButton(this, R.string.call, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    mBluetoothManager.callPhone("10086", new IBluetoothExecCallback.Stub() {
                        @Override
                        public void onSuccess(String msg) throws RemoteException {

                        }

                        @Override
                        public void onFailure(int errorCode) throws RemoteException {
                            showTips("callPhone onFailure:" + IVIBluetooth.BluetoothExecErrorMsg.getName(errorCode));
                        }
                    });
                }
            }
        }), new IVIButton(this, R.string.listen, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    mBluetoothManager.listenPhone(new IBluetoothExecCallback.Stub() {
                        @Override
                        public void onSuccess(String msg) throws RemoteException {

                        }

                        @Override
                        public void onFailure(int errorCode) throws RemoteException {
                            showTips("callPhone onFailure:" + IVIBluetooth.BluetoothExecErrorMsg.getName(errorCode));
                        }
                    });
                }
            }
        }), new IVIButton(this, R.string.hangup, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    mBluetoothManager.hangPhone(new IBluetoothExecCallback.Stub() {
                        @Override
                        public void onSuccess(String msg) throws RemoteException {

                        }

                        @Override
                        public void onFailure(int errorCode) throws RemoteException {
                            showTips("hangPhone onFailure:" + IVIBluetooth.BluetoothExecErrorMsg.getName(errorCode));
                        }
                    });
                }
            }
        }), new IVIButton(this, R.string.recall, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    mBluetoothManager.recallPhone(new IBluetoothExecCallback.Stub() {
                        @Override
                        public void onSuccess(String msg) throws RemoteException {

                        }

                        @Override
                        public void onFailure(int errorCode) throws RemoteException {
                            showTips("hangPhone onFailure:" + IVIBluetooth.BluetoothExecErrorMsg.getName(errorCode));
                        }
                    });
                }
            }
        }));

        // DTMF
        addIVIButtons(new IVIButton(this, R.string.switch_phone_car, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) { // 接口通过 onVoiceChange 回调返回
                    mBluetoothManager.transferCall(null);
                }
            }
        }), new IVIButton(this, "0", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    mBluetoothManager.requestDTMF(IVIBluetooth.KeyPositionDefined.ZERO_POSITION, mBluetoothDTMFExecCallback);
                }
            }
        }), new IVIButton(this, "1", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    mBluetoothManager.requestDTMF(IVIBluetooth.KeyPositionDefined.ONE_POSITION, mBluetoothDTMFExecCallback);
                }
            }
        }), new IVIButton(this, "*", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    mBluetoothManager.requestDTMF(IVIBluetooth.KeyPositionDefined.ASTERISK_POSITION, mBluetoothDTMFExecCallback);
                }
            }
        }), new IVIButton(this, "#", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothManager != null) {
                    mBluetoothManager.requestDTMF(IVIBluetooth.KeyPositionDefined.POUND_POSITION, mBluetoothDTMFExecCallback);
                }
            }
        }));
        // 蓝牙电话操作接口 end
    }

    @Override
    protected void onViewInvalid() {
        super.onViewInvalid();
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        if (mBluetoothManager != null) {
            // 注：暂时涉及到的蓝牙模块不支持closeBluetoothModule操作
            mBluetoothManager.openBluetoothModule(new IBluetoothExecCallback.Stub() {
                @Override
                public void onSuccess(String msg) throws RemoteException {
                    showCallback("openBluetoothModule onSuccess msg:" + msg);
                }

                @Override
                public void onFailure(int errorCode) throws RemoteException {
                    showCallback("openBluetoothModule onFailure errorCode:" + IVIBluetooth.BluetoothExecErrorMsg.getName(errorCode));
                }
            });
        }
    }
}
