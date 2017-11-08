package com.roadrover.demo.ui.view;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Button;

/**
 * 自定义按钮.
 */

public class IVIButton extends Button {

    public static int COLOR1 = 0xFF5698BB;
    public static int COLOR2 = 0xFF569874;
    public static int COLOR3 = 0xaa5698CD;
    public static int COLOR4 = 0x66CD9856;

    private static int[] COLORS = new int[] { COLOR1, COLOR2, COLOR3, COLOR4 };
    private static int COLOR_INDEX = 0;

    public static void nextColor() {
        COLOR_INDEX = (COLOR_INDEX + 1) % COLORS.length;
    }

    public IVIButton(@NonNull Context context, @NonNull String name,
                     @ColorInt int color, @NonNull OnClickListener listener) {
        super(context);

        init(name, color, listener);
    }

    public IVIButton(@NonNull Context context, @StringRes int resId,
                      @NonNull OnClickListener listener) {
        super(context);

        init(getResources().getText(resId), COLORS[COLOR_INDEX % COLORS.length], listener);
    }

    public IVIButton(@NonNull Context context, @NonNull String name,
                     @NonNull OnClickListener listener) {
        super(context);

        init(name, COLORS[COLOR_INDEX % COLORS.length], listener);
    }

    private void init(@NonNull CharSequence name,
                      @ColorInt int color, @NonNull OnClickListener listener) {
        this.setText(name);
        this.setBackgroundColor(color);
        this.setOnClickListener(listener);
        this.setTextSize(18.0f);
    }
}
