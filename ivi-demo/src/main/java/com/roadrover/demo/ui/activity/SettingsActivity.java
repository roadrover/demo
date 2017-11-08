package com.roadrover.demo.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.roadrover.demo.R;
import com.roadrover.demo.ui.view.IVIButton;

/**
 * 设置Activity.
 */

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 添加功能视图
        addIVIButtons(new IVIButton(this, R.string.settings_jump_page, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChildDemo(((IVIButton) v).getText(),
                        SettingsJumpPageActivity.class);
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.settings_get_data, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChildDemo(((IVIButton) v).getText(),
                        SettingsGetDataActivity.class);
            }
        }));

        addIVIButtons(new IVIButton(this, R.string.settings_monitor_data, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChildDemo(((IVIButton) v).getText(),
                        SettingsMonitorDataActivity.class);
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
