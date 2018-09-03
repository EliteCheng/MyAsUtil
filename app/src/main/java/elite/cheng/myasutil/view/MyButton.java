package elite.cheng.myasutil.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


/**
 * 自定义Button，支持圆角矩形，圆形按钮等样式，可通过配置文件改变按下后的样式
 * 若通过代码设置圆角或者圆形，需要先调用setFillet方法将fillet设置为true
 */
public class MyButton extends Button {
    private String TAG =  getClass().getSimpleName();
    int mLastX,mLastY;

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        View v = this;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels-v.getHeight()/2;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //获得移动的位置
                int dx = (int) event.getRawX() - mLastX;
                int dy = (int) event.getRawY() - mLastY;
                int l = v.getLeft() + dx;
                int b = v.getBottom() + dy;
                int r = v.getRight() + dx;
                int t = v.getTop() + dy;

                //下面判断移动是否超出屏幕
                if (l < 0) {
                    l = 0;
                    r = l + v.getWidth();
                }
                if (t < 0) {
                    t = 0;
                    b = t + v.getHeight();
                }
                if (r > screenWidth) {
                    r = screenWidth;
                    l = r - v.getWidth();
                }
                if (b > screenHeight) {
                    b = screenHeight;
                    t = b - v.getHeight();
                }
                //改变View的布局参数实现移动的效果
                v.layout(l, t, r, b);
                v.postInvalidate();
                break;
        }
        mLastX = (int) event.getRawX();//获取触摸事件触摸位置的原始X坐标
        mLastY = (int) event.getRawY();
        return true;
    }
}
