package com.roadrover.demo.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.roadrover.demo.ui.view.IVIButton;
import com.roadrover.sdk.utils.EnvironmentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 存储设备activity.
 */

public class StorageDeviceActivity extends BaseActivity {

    // 一行显示最多的存储设备
    private static final int MAX_DEVICE = 3;

    // 存储设备工具类对象
    private EnvironmentUtils mEnvironmentUtils = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 创建存储设备工具类对象
        mEnvironmentUtils = new EnvironmentUtils(this);

        // 获取支持的存储设备
        List<String> paths = mEnvironmentUtils.getStoragePaths();

        // 增加功能视图
        List<IVIButton> buttons = new ArrayList<>();
        if (null != paths) {
            for (final String path : paths) {
                if (mEnvironmentUtils.isValidPath(path)) {
                    buttons.add(new IVIButton(this, mEnvironmentUtils.getStorageName(path), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null == mEnvironmentUtils) {
                                return;
                            }

                            showTips(mEnvironmentUtils.getStorageName(path) +
                            " path: " +
                            path +
                            " mounted: " +
                            mEnvironmentUtils.isStorageMounted(path));
                        }
                    }));
                }
            }
        }

        // 布局存储设备视图
        final int size = buttons.size();
        List<IVIButton> items = new ArrayList<>();
        for (int i = 0;i < size; i++) {
            final int index = i % MAX_DEVICE;
            if (0 == index) {
                items.clear();
            }
            items.add(buttons.get(i));
            if (index == MAX_DEVICE - 1) {
                addIVIButtons(items);
            } else if (i == size - 1) {
                addIVIButtons(items);
            }
        }
    }

    @Override
    protected void onDestroy() {
        mEnvironmentUtils = null;
        super.onDestroy();
    }
}
