package io.virtualapp.widgets;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import io.virtualapp.VApp;

/**
 * @author Lody
 */
public class ViewHelper {

    public static int dip2px(float dpValue) {
        final float scale = VApp.getApp().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 设置视图的透明度
     *
     * @param view  要设置透明度的视图
     * @param alpha 透明度值，范围从 0.0f（完全透明）到 1.0f（完全不透明）
     */
    public static void setAlpha(View view, float alpha) {
        if (view == null) {
            return;
        }

        // 检查 API 版本，兼容旧版本 Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Android 3.0+ 直接使用 View.setAlpha()
            view.setAlpha(alpha);
        } else {
            // Android 3.0 以下使用 setAlpha 方法的替代实现
            view.getBackground().setAlpha((int) (alpha * 255));

            // 如果是 TextView 或子类，还需要设置文本的透明度
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(adjustAlpha(((TextView) view).getTextColors(), alpha));
            }
        }
    }

    /**
     * 调整颜色的透明度
     *
     * @param colors 原始颜色
     * @param factor 透明度因子 (0.0f 到 1.0f)
     * @return 调整后的颜色
     */
    private static int adjustAlpha(ColorStateList colors, float factor) {
        int color = colors.getDefaultColor();
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

}
