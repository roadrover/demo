package com.roadrover.demo.ui.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.roadrover.demo.R;
import com.roadrover.demo.ui.view.IVIButton;
import com.roadrover.sdk.media.IVIMedia;
import com.roadrover.sdk.media.MediaManager;
import com.roadrover.sdk.media.MediaSqlManager;
import com.roadrover.sdk.radio.IVIRadio;
import com.roadrover.sdk.system.IVISystem;
import com.roadrover.sdk.utils.ListUtils;
import com.roadrover.services.media.IGetMediaListCallback;
import com.roadrover.services.media.IMediaInfoCallback;
import com.roadrover.services.media.IMediaScannerCallback;
import com.roadrover.services.media.StMusic;

import java.util.List;

/**
 * 多媒体Activity.
 */

public class MediaActivity extends SDKActivity {

    // 媒体管理类对象
    private MediaManager mMediaManager = null;
    private MediaSqlManager mMediaSqlManager = null;

    // 媒体显示区
    private ImageView mIvArtist = null;
    private TextView mTvName = null;
    private TextView mTvArtist = null;

    // 媒体扫描的监听
    private IMediaScannerCallback.Stub mIMediaScannerCallback = new IMediaScannerCallback.Stub() {
        @Override
        public void onScanStart(int type, int sqlType) throws RemoteException {
            showCallback("onScanStart type:" + IVIMedia.MediaScannerType.getName(type) +
                    " file type:" + IVIMedia.MediaSqlDataType.getName(sqlType));
        }

        @Override
        public void onScanFinish(int type, int sqlType, String path, String oldPath) throws RemoteException {
            showCallback("onScanFinish type:" + IVIMedia.MediaScannerType.getName(type) +
                    " file type:" + IVIMedia.MediaSqlDataType.getName(sqlType) +
                    " curPath:" + path + " oldPath:" + oldPath);

            switch (type) {
                case IVIMedia.MediaScannerType.MOUNT_TYPE: // 盘符mount
                    List<String> paths = mMediaSqlManager.query(IVIMedia.MediaSqlDataType.AUDIO_TYPE, path); // 查询指定目录下的所有文件

                    // 获取当前目录下的所有歌曲的id3信息
                    mMediaManager.getAllMediaList(IVIMedia.MediaSqlDataType.AUDIO_TYPE, path,
                            new IGetMediaListCallback.Stub() {
                                @Override
                                public void onProgress(List<StMusic> stAudios, String path) throws RemoteException {
                                    if (!ListUtils.isEmpty(stAudios)) {
                                        for (int i = 0; i < stAudios.size(); ++i) {
                                            showCallback("onProgress i:" + i + " name:" + stAudios.get(i).mName +
                                                    " path:" + stAudios.get(i).mPath +
                                                    " artist:" + stAudios.get(i).mArtist +
                                                    " duration:" + stAudios.get(i).mDuration);
                                        }
                                    }
                                }

                                @Override
                                public void onFinish(List<StMusic> stAudios, String path) throws RemoteException {
                                    if (!ListUtils.isEmpty(stAudios)) {
                                        for (int i = 0; i < stAudios.size(); ++i) {
                                            showCallback("onFinish i:" + i + " name:" + stAudios.get(i).mName +
                                                    " path:" + stAudios.get(i).mPath +
                                                    " artist:" + stAudios.get(i).mArtist +
                                                    " duration:" + stAudios.get(i).mDuration);
                                        }
                                    }
                                    showCallback("onFinish path:" + path);
                                }
                            });
                    break;

                case IVIMedia.MediaScannerType.SCAN_ALL_TYPE: // 开启扫描全部
                    List<String> audioPaths = mMediaSqlManager.queryAudioFile(); // 获取所有音乐路径
                    break;
            }
        }

        @Override
        public void onEject(String path, boolean isDiskPowerDown) throws RemoteException {
            showCallback("onEject path:" + path +
                    " isDiskPowerDown:" + isDiskPowerDown);

            if (isDiskPowerDown) { // 该种情况一般为系统acc时候盘符被卸载了，此时可以只停止音乐，不处理列表

            }
        }

        @Override
        public void onMount(String path) throws RemoteException {
            showCallback("onMount path:" + path);
        }
    };

    // 多媒体控制的监听
    private IVIMedia.MediaControlListener mMediaControlListener = new IVIMedia.MediaControlListener() {
        @Override
        public void suspend() {
            showCallback("suspend");
        }

        @Override
        public void stop() {
            showCallback("stop");
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
        public void setVolume(float volume) {
            showCallback("setVolume:" + volume);
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
        public void select(int index) {
            showCallback("select:" + index);
        }

        @Override
        public void setFavour(boolean isFavour) {
            showCallback("setFavour isFavour:" + isFavour);
        }

        @Override
        public void filter(String title, String singer) {
            showCallback("filter title:" + title + " singer:" + singer);
        }

        @Override
        public void playRandom() {
            showCallback("playRandom");
        }

        @Override
        public void setPlayMode(int mode) {
            showCallback("setPlayMode:" + mode);
        }

        @Override
        public void quitApp() {
            showCallback("quitApp");
        }

        @Override
        public void onVideoPermitChanged(boolean show) {
            showCallback("onVideoPermitChanged isShow:" + show);
        }
    };

    // 多媒体信息的监听
    private IMediaInfoCallback.Stub mIMediaInfoCallback = new IMediaInfoCallback.Stub() {
        @Override
        public void onMediaChange(int mediaType, String name, String info, int artWidth, int artHeight, byte[] artPixels, int index, int totalCount, boolean popup) throws RemoteException {
            showCallback("onMediaChange mediaType:" + IVIMedia.Type.getName(mediaType) +
                    " name:" + name +
                    " info:" + info +
                    " index:" + index +
                    " totalCount:" + totalCount);
            if (mTvName != null && mTvArtist != null && mIvArtist != null) {
                mTvName.setText(name);
                mTvArtist.setText(info);
                IVIMedia.ArtImage image = new IVIMedia.ArtImage(artWidth, artHeight, artPixels);
                mIvArtist.setBackground(new BitmapDrawable(image.getBitmap()));
            }
        }

        @Override
        public void onPlayStateChange(int mediaType, int playState, int position, int duration) throws RemoteException {
            showCallback("onPlayStateChange mediaType:" + IVIMedia.Type.getName(mediaType) +
                    " playState:" + IVIMedia.MediaState.getName(playState) +
                    " position:" + position +
                    " duration:" + duration);
        }

        @Override
        public void onMediaZoneChanged(int mediaType, int zone) throws RemoteException {
            showCallback("onMediaZoneChanged mediaType:" + IVIMedia.Type.getName(mediaType) +
                    " zone:" + IVIMedia.Zone.getName(zone));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMediaSqlManager = new MediaSqlManager(this);

		// 创建SDK Manager对象
        mMediaManager = new MediaManager(this, this, mMediaControlListener);

        // 监听音乐类型文件文件变化
        mMediaManager.registerScannerListener(mIMediaScannerCallback, IVIMedia.MediaSqlDataType.AUDIO_TYPE);

        // 注册多媒体信息监听回调，该回调可以在luancher, systemui等你需要监听的位置进行监听
        mMediaManager.registerMediaInfoListener(mIMediaInfoCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mMediaManager != null) {
            mMediaManager.unRegisterScannerListener(mIMediaScannerCallback);
            mMediaManager.unregisterMediaInfoListener(mIMediaInfoCallback);
            mMediaManager.disconnect();
            mMediaManager = null;
        }

        if (mMediaSqlManager != null) {
            mMediaSqlManager = null;
        }
    }

    @Override
    protected void onViewValid() {
        super.onViewValid();

        addMediaLayout();

        addIVIButtons(new IVIButton(this, getString(R.string.open) + getString(R.string.media), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaManager != null) {
                    mMediaManager.open(IVIMedia.Type.MUSIC);
                }
            }
        }), new IVIButton(this, getString(R.string.close) + getString(R.string.media), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaManager != null) {
                    mMediaManager.close(IVIMedia.Type.MUSIC);
                }
            }
        }));

        // 音乐内部调用接口 start
        addIVIButtons(new IVIButton(this, R.string.set_media_info, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaManager != null) {
                    mMediaManager.setMediaInfo(IVIMedia.Type.MUSIC,
                            "dream it possible",
                            "张靓颖",
                            null,
                            1,
                            100,
                            false);
                }
            }
        }), new IVIButton(this, R.string.set_media_status, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaManager != null) {
                    mMediaManager.setMediaState(IVIMedia.Type.MUSIC,
                            IVIMedia.MediaState.PLAYING,
                            10,
                            123);
                }
            }
        }), new IVIButton(this, R.string.get_media_list, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaSqlManager != null) {
                    List<String> paths = mMediaSqlManager.queryAudioFile();
                    if (!ListUtils.isEmpty(paths)) {
                        for (int i = 0; i < paths.size(); ++i) {
                            showTips("path " + i + " :" + paths.get(i));
                        }
                    }
                }
            }
        }));

        // 音乐内部调用接口 end

        // 外部想控制音乐的接口 start
        addIVIButtons(new IVIButton(this, R.string.next, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaManager != null) {
                    mMediaManager.next();
                }
            }
        }), new IVIButton(this, R.string.prev, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaManager != null) {
                    mMediaManager.prev();
                }
            }
        }), new IVIButton(this, R.string.play, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaManager != null) {
                    mMediaManager.play();
                }
            }
        }), new IVIButton(this, R.string.pause, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaManager != null) {
                    mMediaManager.pause();
                }
            }
        }), new IVIButton(this, R.string.launch_app, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaManager != null) {
                    mMediaManager.launchApp(IVIMedia.Type.MUSIC); // 启动当前播放的媒体app
                }
            }
        }));

        // 获取当前媒体播放信息
        addIVIButtons(new IVIButton(this, R.string.get_cur_play_info, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaManager != null) {
                    mMediaManager.requestMediaInfoAndStateEvent();
                }
            }
        }), new IVIButton(this, R.string.get_cur_video_permit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaManager != null) {
                    mMediaManager.requestVideoPermitEvent();
                }
            }
        }), new IVIButton(this, R.string.can_media_play, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaManager != null) {
                    showTips("Can media play:" + mMediaManager.canPlayMedia());
                }
            }
        }));
        // 外部想控制音乐的接口 end
    }

    @Override
    protected void onViewInvalid() {
        super.onViewInvalid();
    }

    /**
     * 添加一个媒体的layout
     */
    private void addMediaLayout() {
        if (mFunctionsLayout != null) {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_media, mFunctionsLayout);

            mIvArtist = (ImageView) view.findViewById(R.id.iv_artist_image);
            mTvName   = (TextView) view.findViewById(R.id.tv_name);
            mTvArtist = (TextView) view.findViewById(R.id.tv_artist);
        }
    }
}
