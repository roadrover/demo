package com.roadrover.demo.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.roadrover.demo.R;
import com.roadrover.sdk.BaseManager;

/**
 * SDK Activity.
 */

public abstract class SDKActivity extends BaseActivity implements BaseManager.ConnectListener {

    private boolean mViewValid;         // 视图是否有效

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 默认无效，等待连接
        onViewInvalid();

        showTips(R.string.service_connecting);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onServiceConnected() {
        onViewValid();

        showTips(R.string.service_connected);
    }

    @Override
    public void onServiceDisconnected() {
        onViewInvalid();

        showTips(R.string.service_disconnected);
    }

    /**
     * 显示回调字符串
     * @param callback
     */
    protected void showCallback(String callback) {
        if (TextUtils.isEmpty(callback)) {
            return;
        }
        callback = getString(R.string.callback) + "：" + callback;
        super.showTips(callback);
    }

    /**
     * 视图有效
     */
    protected void onViewValid() {
        mViewValid = true;
        removeAllViews();
    }

    /**
     * 视图无效
     */
    protected void onViewInvalid() {
        removeAllViews();
        mViewValid = false;
    }

    /**
     * 视图是否有效
     * @return
     */
    protected boolean isViewValid() {
        return mViewValid;
    }
}
