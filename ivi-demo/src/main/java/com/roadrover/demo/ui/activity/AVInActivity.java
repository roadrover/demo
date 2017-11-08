package com.roadrover.demo.ui.activity;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.roadrover.demo.R;
import com.roadrover.demo.ui.view.IVIButton;
import com.roadrover.sdk.avin.AVInManager;
import com.roadrover.sdk.avin.IVIAVIn;
import com.roadrover.sdk.avin.IVITV;
import com.roadrover.sdk.avin.VideoParam;

import java.io.IOException;

/**
 * 外部输入Activity.
 */

public class AVInActivity extends SDKActivity {

    // AVIn Demo的Id
    private static final int AVIN_ID = IVIAVIn.Id.REAR;

    // AVIn视频高度
    private static final int VIDEO_HEIGHT = 200;

    // 摄像头上电延时打开时间
    private static final int CVBS_POWER_SLEEP_MS = 400;

    // AVIn管理类对象
    private AVInManager mAVInManager = null;

    // TextureView对象
    private TextureView mTextureView = null;

    // Camera对象
    private Camera mCamera = null;

    // SurfaceTexture对象
    private SurfaceTexture mSurfaceTexture = null;

    // AVIn回调对象
    private IVIAVIn.AVInListener mAVInListener = new IVIAVIn.AVInListener() {
        @Override
        public void onVideoSignalChanged(int avId, int signal) {
            showCallback("onVideoSignalChanged, avId: " + IVIAVIn.Id.getName(avId) + " signal: " + IVIAVIn.Signal.getName(signal));
        }

        @Override
        public void onCvbsTypeChanged(int avId, int cvbsType) {
            showCallback("onCvbsTypeChanged, avId: " + IVIAVIn.Id.getName(avId) + " cvbsType: " + VideoParam.CvbsType.getName(cvbsType));
        }

        @Override
        public void onVideoPermitChanged(boolean show) {
            showCallback("onVideoPermitChanged, show: " + show);
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
    };

    // TextureView监听对象
    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            mSurfaceTexture = surface;
            showCallback("onSurfaceTextureAvailable, width: " + width + " height: " + height + " surface: " + surface);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            showCallback("onSurfaceTextureSizeChanged, width: " + width + " height: " + height + " surface: " + surface);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            mSurfaceTexture = null;
            showCallback("onSurfaceTextureDestroyed, surface: " + surface);
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            //showCallback("onSurfaceTextureUpdated, surface: " + surface);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 创建SDK Manager对象
        mAVInManager = new AVInManager(this, this, mAVInListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (null != mCamera) {
            showTips(R.string.page_close_camera);

            // 关闭视频
            hideVideo();
            closeCamera();
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // 销毁Manager
        if (null != mAVInManager) {
            mAVInManager.disconnect();
            mAVInManager = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onViewValid() {
        super.onViewValid();

        if (null == mAVInManager) {
            return;
        }

        int id;
        // 添加功能视图
        mTextureView = addTextureView(VIDEO_HEIGHT);
        if (null != mTextureView) {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }

        addIVIButtons(new IVIButton(this, R.string.open_camera, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == mCamera) {
                    showTips(R.string.open_camera);
                    openCamera();
                    showVideo();
                } else {
                    showTips(R.string.camera_already_open);
                }
            }
        }), new IVIButton(this, R.string.close_camera, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mCamera) {
                    showTips(R.string.close_camera);
                    hideVideo();
                    closeCamera();
                } else {
                    showTips(R.string.camera_already_close);
                }
            }
        }));

        id = getId(VideoParam.SubId.BRIGHTNESS);
        if (mAVInManager.isParamAvailable(id)) {
            addProgressBar(
                    id,
                    mAVInManager.getParamMinValue(id),
                    mAVInManager.getParamMaxValue(id),
                    mAVInManager.getParamValue(id),
                    getString(R.string.brightness) + "：",
                    new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (null != mAVInManager && fromUser) {
                        mAVInManager.setParam(getId(VideoParam.SubId.BRIGHTNESS), progress);
                    }
                    showTips(getString(R.string.settings) + getString(R.string.brightness) + "：" + progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        id = getId(VideoParam.SubId.CONTRAST);
        if (mAVInManager.isParamAvailable(id)) {
            addProgressBar(
                    id,
                    mAVInManager.getParamMinValue(id),
                    mAVInManager.getParamMaxValue(id),
                    mAVInManager.getParamValue(id),
                    getString(R.string.contrast) + "：",
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (null != mAVInManager && fromUser) {
                                mAVInManager.setParam(getId(VideoParam.SubId.CONTRAST), progress);
                            }
                            showTips(getString(R.string.settings) + getString(R.string.contrast) + "：" + progress);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });
        }

        id = getId(VideoParam.SubId.SATURATION);
        if (mAVInManager.isParamAvailable(id)) {
            addProgressBar(
                    id,
                    mAVInManager.getParamMinValue(id),
                    mAVInManager.getParamMaxValue(id),
                    mAVInManager.getParamValue(id),
                    getString(R.string.saturation) + "：",
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (null != mAVInManager && fromUser) {
                                mAVInManager.setParam(getId(VideoParam.SubId.SATURATION), progress);
                            }
                            showTips(getString(R.string.settings) + getString(R.string.saturation) + "：" + progress);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });
        }

        id = getId(VideoParam.SubId.HUE);
        if (mAVInManager.isParamAvailable(id)) {
            addProgressBar(
                    id,
                    mAVInManager.getParamMinValue(id),
                    mAVInManager.getParamMaxValue(id),
                    mAVInManager.getParamValue(id),
                    getString(R.string.hue) + "：",
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (null != mAVInManager && fromUser) {
                                mAVInManager.setParam(getId(VideoParam.SubId.HUE), progress);
                            }
                            showTips(getString(R.string.settings) + getString(R.string.hue) + "：" + progress);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });
        }

        addIVIButtons(new IVIButton(this, R.string.reset_video_param, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAVInValue(VideoParam.SubId.BRIGHTNESS);
                resetAVInValue(VideoParam.SubId.CONTRAST);
                resetAVInValue(VideoParam.SubId.SATURATION);
                resetAVInValue(VideoParam.SubId.HUE);
                showTips(R.string.reset_video_param);
            }
        }), new IVIButton(this, R.string.get_cvbs_video_type, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAVInManager) {
                    int type = mAVInManager.getCvbsType(AVIN_ID);
                    showTips(getString(R.string.get_cvbs_video_type) + "：" + VideoParam.CvbsType.getName(type));
                }
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.get_video_signal, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAVInManager) {
                    int signal = mAVInManager.getVideoSignal(AVIN_ID);
                    showTips(getString(R.string.get_video_signal) + "：" + IVIAVIn.Signal.getName(signal));
                }
            }
        }), new IVIButton(this, R.string.video_permit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAVInManager) {
                    boolean permit = mAVInManager.isVideoPermit();
                    showTips(getString(R.string.video_permit) + "：" + permit);
                }
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.tv_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAVInManager) {
                    mAVInManager.controlTV(IVITV.Control.SEARCH);
                    showTips(R.string.tv_search);
                }
            }
        }), new IVIButton(this, R.string.tv_prev, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAVInManager) {
                    mAVInManager.controlTV(IVITV.Control.CH_SUB);
                    showTips(R.string.tv_prev);
                }
            }
        }), new IVIButton(this, R.string.tv_next, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAVInManager) {
                    mAVInManager.controlTV(IVITV.Control.CH_ADD);
                    showTips(R.string.tv_next);
                }
            }
        }), new IVIButton(this, R.string.tv_touch, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mAVInManager) {
                    int x = 100, y = 100;
                    mAVInManager.sendTouch(x, y);
                    showTips(getString(R.string.tv_touch) + "[" + x + ", " + y + "]");
                }
            }
        }));
    }

    @Override
    protected void onViewInvalid() {
        if (null != mCamera) {
            showTips(R.string.close_camera);

            // 关闭视频
            hideVideo();
            closeCamera();
        }
        mTextureView = null;
        super.onViewInvalid();
    }

    /**
     * 获取Id
     * @param subId {@link com.roadrover.sdk.avin.VideoParam.SubId}
     * @return
     */
    public int getId(int subId) {
        return VideoParam.makeId(AVIN_ID, subId);
    }

    /**
     * 重设AVIn参数
     * @param subId {@link com.roadrover.sdk.avin.VideoParam.SubId}
     */
    private void resetAVInValue(int subId) {
        if (null == mAVInManager) {
            return;
        }

        int id = getId(subId);
        int value = mAVInManager.getParamDefaultValue(id);
        mAVInManager.setParam(id, value);
        ProgressBar progressBar = getProgressBar(id);
        if (null != progressBar) {
            progressBar.setProgress(value - mAVInManager.getParamMinValue(id));
        }
    }

    /**
     * 打开摄像头
     */
    private void openCamera() {
        if (null == mAVInManager) {
            return;
        }

        if (null != mCamera) {
            return;
        }

        // 先打开摄像头电源
        mAVInManager.open(AVIN_ID);

        // 上电后延时一段时间，否则视频视频画面会闪烁
        try {
            Thread.sleep(CVBS_POWER_SLEEP_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 打开摄像头
        mCamera = mAVInManager.openAndroidCamera(AVIN_ID);
    }

    /**
     * 显示视频
     */
    private void showVideo() {
        if (null == mCamera) {
            return;
        }

        try {
            mCamera.setPreviewTexture(mSurfaceTexture);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏视频
     */
    private void hideVideo() {
        if (null == mCamera) {
            return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭摄像头
     */
    private void closeCamera() {
        if (null == mAVInManager) {
            return;
        }

        if (null == mCamera) {
            return;
        }

        try {
            mCamera.release();
            mCamera = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAVInManager.close(AVIN_ID);
        mAVInManager.closeAndroidCamera(AVIN_ID);
    }
}
