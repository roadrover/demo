package com.roadrover.demo.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.LayoutDirection;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.roadrover.demo.R;
import com.roadrover.demo.ui.view.IVIButton;
import com.roadrover.demo.utils.ScreenUtils;
import com.roadrover.sdk.system.IVISystem;
import com.roadrover.sdk.utils.Logcat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity基类.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Toast          mToast;               // Toast对象
    protected LinearLayout   mFunctionsLayout;     // 功能按钮区域Layout
    private List<TextView> mContentTextViews;    // 内容TextView列表
    private Map<Integer, ProgressBar> mProgressBarMap;  // 进度条Map

    protected String     mTitle;               // 标题文字

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取对象
        mProgressBarMap = new HashMap<>();
        mToast =           Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mContentTextViews = new ArrayList<>();
        mContentTextViews.add((TextView) findViewById(R.id.tv_content1));
        mContentTextViews.add((TextView) findViewById(R.id.tv_content2));
        mContentTextViews.add((TextView) findViewById(R.id.tv_content3));
        mFunctionsLayout = (LinearLayout) findViewById(R.id.ll_functions);

        // 设置Title文字
        Intent intent = this.getIntent();
        if (intent != null) {
            mTitle = intent.getStringExtra("title");
        }
        if (TextUtils.isEmpty(mTitle)) {
            mTitle = getString(R.string.app_name);
        }

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setTitle(mTitle);
        }

        // 设置返回按钮功能
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (null != mProgressBarMap) {
            mProgressBarMap.clear();
            mProgressBarMap = null;
        }
        mFunctionsLayout = null;
        mToast = null;
        super.onDestroy();
    }

    /**
     * 添加一行按钮
     *
     * @param btns
     */
    public void addIVIButtons(IVIButton... btns) {
        if (null == mFunctionsLayout) {
            return;
        }
        final int PADDING = getResources().getDimensionPixelSize(R.dimen.ivi_button_padding);
        LinearLayout llButtons = new LinearLayout(this);
        mFunctionsLayout.addView(llButtons);

        int widthScreen = ScreenUtils.getScreenWidth(BaseActivity.this);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(widthScreen / btns.length - 2
                * PADDING, 72);

        for (int i = 0; i < btns.length; ++i) {
            IVIButton bt = btns[i];
            FrameLayout fbt = new FrameLayout(this);
            fbt.setPadding(PADDING, PADDING, PADDING, PADDING);
            fbt.addView(bt, lp);
            llButtons.addView(fbt);
        }

        IVIButton.nextColor();
    }

    /**
     * 添加一个进度条
     * @param max 最大值
     * @param def 默认值
     * @Param prompt 提示
     * @param listener 进度条监听
     */
    public void addProgressBar(int max, int def, final String prompt, final SeekBar.OnSeekBarChangeListener listener) {
        addProgressBar(0, max, def, prompt, listener);
    }

    /**
     * 添加一个进度条
     * @param min 最小值
     * @param max 最大值
     * @param def 默认值
     * @Param prompt 提示
     * @param listener 进度条监听
     */
    public void addProgressBar(final int min, final int max, int def, final String prompt, final SeekBar.OnSeekBarChangeListener listener) {
        addProgressBar(-1, min, max, def, prompt, listener);
    }

    /**
     * 添加一个进度条
     * @param id 进度条id
     * @param min 最小值
     * @param max 最大值
     * @param def 默认值
     * @Param prompt 提示
     * @param listener 进度条监听
     */
    public void addProgressBar(final int id, final int min, final int max, int def, final String prompt, final SeekBar.OnSeekBarChangeListener listener) {
        if (null == mFunctionsLayout) {
            return;
        }
        if (max <= min) {
            return;
        }
        if (def < min || def > max) {
            return;
        }

        int widthScreen = ScreenUtils.getScreenWidth(BaseActivity.this);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(widthScreen, 36);
        LinearLayout llProgress = new LinearLayout(this);
        mFunctionsLayout.addView(llProgress, lp);

        final TextView tvCur = new TextView(this);

        SeekBar seekBar = new SeekBar(this);
        mProgressBarMap.put(id, seekBar);

        llProgress.addView(tvCur, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        llProgress.addView(seekBar, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        tvCur.setText(prompt + def);
        seekBar.setMax(max - min);
        seekBar.setProgress(def - min);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final int value = progress + min;
                tvCur.setText(prompt + value);
                if (listener != null) {
                    listener.onProgressChanged(seekBar, value, fromUser);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (listener != null) {
                    listener.onStartTrackingTouch(seekBar);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (listener != null) {
                    listener.onStopTrackingTouch(seekBar);
                }
            }
        });
    }

    /**
     * 获取进度条
     * @param id
     * @return
     */
    public ProgressBar getProgressBar(int id) {
        if (null != mProgressBarMap) {
            for (Map.Entry<Integer, ProgressBar> entry : mProgressBarMap.entrySet()) {
                if (id == entry.getKey()) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 添加一个TextureView
     *
     * @param height 高度
     * @return TextureView对象
     */
    public TextureView addTextureView(int height) {
        if (null == mFunctionsLayout) {
            return null;
        }
        if (height < 50) {
            return null;
        }
        final int width = 1024 * height / 600;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        lp.gravity = Gravity.CENTER;
        LinearLayout llTextureView = new LinearLayout(this);
        llTextureView.setBackgroundColor(Color.BLACK);
        llTextureView.setOrientation(LinearLayout.HORIZONTAL);
        llTextureView.setGravity(Gravity.CENTER);
        mFunctionsLayout.addView(llTextureView, lp);

        TextureView textureView = new TextureView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llTextureView.addView(textureView, params);
        return textureView;
    }

    /**
     * 移除所有视图
     */
    public void removeAllViews() {
        if (null != mProgressBarMap) {
            mProgressBarMap.clear();
        }
        if (null != mFunctionsLayout) {
            mFunctionsLayout.removeAllViews();
        }
    }

    /**
     * 显示字符串ID
     * @param tips
     */
    protected void showTips(@StringRes int tips) {
        showTips(getString(tips));
    }

    /**
     * 显示字符串
     * @param tips
     */
    protected void showTips(final String tips) {
        if (TextUtils.isEmpty(tips)) {
            return;
        }
        Logcat.d(tips);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 循环显示tips
                if (null != mContentTextViews) {
                    final int max = mContentTextViews.size();
                    if (max > 0) {
                        if (max > 1) {
                            for (int i = max - 1;i > 0;i--) {
                                TextView textView = mContentTextViews.get(i - 1);
                                TextView replaceTextView = mContentTextViews.get(i);
                                if (null != textView && null != replaceTextView) {
                                    CharSequence text = textView.getText();
                                    if (!TextUtils.isEmpty(text)) {
                                        replaceTextView.setText(text);
                                    }
                                }
                            }
                        }
                        mContentTextViews.get(0).setText(tips);
                    }
                }
            }
        });
    }
	
	/**
     * 显示Toast
     * @param toast
     */
    protected void showToast(@StringRes int toast) {
        showToast(getString(toast));
    }

    /**
     * 显示Toast
     * @param toast
     */
    protected void showToast(String toast) {
        if (TextUtils.isEmpty(toast)) {
            return;
        }
        Logcat.d(toast);
        if (null != mToast) {
            mToast.setText(toast);
            mToast.show();
        }
    }

    /**
     * 进入子demo
     *
     * @param title
     * @param activity
     */
    protected void startChildDemo(CharSequence title,
                                Class<? extends Activity> activity) {
        if (TextUtils.isEmpty(title)) {
            showToast(R.string.demo_not_allow_empty);
            return;
        }
        if (activity == BluetoothActivity.class) {
            if (!isAPKExist(this, IVISystem.PACKAGE_BT_SERVICE)) {
                showToast(R.string.please_install_roadrover_bt_service);
                return;
            }
        } else if (activity == SettingsActivity.class) {
            if (!isAPKExist(this, IVISystem.PACKAGE_SETTINGS)) {
                showToast(R.string.please_install_roadrover_settings);
                return;
            }
        } else {
            if (!isAPKExist(this, IVISystem.PACKAGE_IVI_SERVICES)) {
                showToast(R.string.please_install_roadrover_services);
                return;
            }
        }

        showToast(getString(R.string.enter_demo) + title.toString());

        Intent intentModule = new Intent();
        ComponentName cn = new ComponentName(BaseActivity.this, activity);
        intentModule.setComponent(cn);
        intentModule.putExtra("title", title);
        startActivity(intentModule);
    }

    /**
     * 判断指定包名程序是否存在.
     *
     * @param context       the context.
     * @param strPacketName the packet name.
     * @return true is exist, otherwise is not exist.
     */
    public static boolean isAPKExist(Context context, String strPacketName) {
        if (TextUtils.isEmpty(strPacketName)) {
            return false;
        }

        if (null != context) {
            try {
                context.getPackageManager().getApplicationInfo(strPacketName, PackageManager.GET_UNINSTALLED_PACKAGES);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
