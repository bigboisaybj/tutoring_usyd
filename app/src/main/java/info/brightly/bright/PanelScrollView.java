package info.brightly.bright;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * This class is an extension of the ScrollView class within Android that allows for the disabling
 * of scrolling programmatically.
 *
 * @author Justin Ng (jn3141)
 */
public class PanelScrollView extends ScrollView {

    // Flag for whether scrolling is enabled or disabled.
    private boolean enableScrolling = false;

    /**
     * Just runs the ScrollView's constructor.
     */
    public PanelScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Just runs the ScrollView's constructor.
     */
    public PanelScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Just runs the ScrollView's constructor.
     */
    public PanelScrollView(Context context) {
        super(context);
    }

    /**
     * Check whether scrolling is enabled or disabled.
     *
     * @return boolean whether or not scrolling is enabled.
     */
    public boolean isEnableScrolling() {
        return enableScrolling;
    }

    /**
     * Sets whether scrolling is enabled or disabled.
     *
     * @param enableScrolling whether or not you want scrolling to be enabled or disabled
     */
    public void setEnableScrolling(boolean enableScrolling) {
        this.enableScrolling = enableScrolling;
    }

    /**
     * Controls what happens when this view intercepts a touch event.
     * If the enableScrolling flag is set to true, then it just uses the ScrollView's implementation;
     * otherwise, it just returns and does nothing.
     *
     * @param ev the motion event that was intercepted.
     * @return boolean whether or not the method was successful.
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isEnableScrolling()) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    /**
     * Controls what happens when this view intercepts a touch event.
     * If the enableScrolling flag is set to true, then it just uses the ScrollView's implementation;
     * otherwise, it just returns and does nothing.
     *
     * @param ev the motion event that was intercepted.
     * @return boolean whether or not the method was successful.
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isEnableScrolling()) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }
}
