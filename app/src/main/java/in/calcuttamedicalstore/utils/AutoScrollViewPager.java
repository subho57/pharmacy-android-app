package in.calcuttamedicalstore.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public class AutoScrollViewPager extends ViewPager {

  public static final int DEFAULT_INTERVAL = 1500;

  public static final int SCROLL_WHAT = 0;
  /** scroll factor for auto scroll animation, default is 1.0 */
  private final double autoScrollFactor = 1.0;
  /** scroll factor for swipe scroll animation, default is 1.0 */
  private final double swipeScrollFactor = 1.0;
  /** auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL} */
  private long interval = DEFAULT_INTERVAL;
  /** whether automatic cycle when auto scroll reaching the last or first item, default is true */
  private boolean isCycle = true;
  /** whether stop auto scroll when touching, default is true */
  private boolean stopScrollWhenTouch = true;

  private Handler handler;
  private boolean isAutoScroll = false;
  private boolean isStopByTouch = false;
  private CustomDurationScroller scroller = null;

  public AutoScrollViewPager(Context paramContext) {
    super(paramContext);
    init();
  }

  public AutoScrollViewPager(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init();
  }

  private void init() {
    handler = new MyHandler(this);
    setViewPagerScroller();
  }

  public void startAutoScroll() {
    isAutoScroll = true;
    sendScrollMessage(
        (long) (interval + scroller.getDuration() / autoScrollFactor * swipeScrollFactor));
  }

  /** stop auto scroll */
  public void stopAutoScroll() {
    isAutoScroll = false;
    handler.removeMessages(SCROLL_WHAT);
  }

  /** remove messages before, keeps one message is running at most */
  private void sendScrollMessage(long delayTimeInMills) {

    handler.removeMessages(SCROLL_WHAT);
    handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
  }

  /** set ViewPager scroller to change animation duration when sliding */
  private void setViewPagerScroller() {
    try {
      Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
      scrollerField.setAccessible(true);
      Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
      interpolatorField.setAccessible(true);

      scroller =
          new CustomDurationScroller(getContext(), (Interpolator) interpolatorField.get(null));
      scrollerField.set(this, scroller);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** scroll only once */
  public void scrollOnce() {
    PagerAdapter adapter = getAdapter();
    int currentItem = getCurrentItem();
    int totalCount;
    if (adapter == null || (totalCount = adapter.getCount()) <= 1) {
      return;
    }

    int nextItem = ++currentItem;
    if (nextItem < 0) {
      if (isCycle) {
        setCurrentItem(totalCount - 1, true);
      }
    } else if (nextItem == totalCount) {
      if (isCycle) {
        setCurrentItem(0, true);
      }
    } else {
      setCurrentItem(nextItem, true);
    }
  }

  /**
   *
   *
   * <ul>
   *   if stopScrollWhenTouch is true
   *   <li>if event is down, stop auto scroll.
   *   <li>if event is up, start auto scroll again.
   * </ul>
   */
  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    int action = ev.getActionMasked();

    if (stopScrollWhenTouch) {
      if ((action == MotionEvent.ACTION_DOWN) && isAutoScroll) {
        isStopByTouch = true;
        stopAutoScroll();
      } else if (ev.getAction() == MotionEvent.ACTION_UP && isStopByTouch) {
        startAutoScroll();
      }
    }

    getParent().requestDisallowInterceptTouchEvent(true);

    return super.dispatchTouchEvent(ev);
  }

  /**
   * set auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL}
   *
   * @param interval the interval to set
   */
  public void setInterval(long interval) {
    this.interval = interval;
  }

  /**
   * set whether automatic cycle when auto scroll reaching the last or first item, default is true
   *
   * @param isCycle the isCycle to set
   */
  public void setCycle(boolean isCycle) {
    this.isCycle = isCycle;
  }

  /** set whether stop auto scroll when touching, default is true */
  public void setStopScrollWhenTouch(boolean stopScrollWhenTouch) {
    this.stopScrollWhenTouch = stopScrollWhenTouch;
  }

  private static class MyHandler extends Handler {

    private final WeakReference<AutoScrollViewPager> autoScrollViewPager;

    public MyHandler(AutoScrollViewPager autoScrollViewPager) {
      this.autoScrollViewPager = new WeakReference<>(autoScrollViewPager);
    }

    @Override
    public void handleMessage(@NotNull Message msg) {
      super.handleMessage(msg);

      if (msg.what == SCROLL_WHAT) {
        AutoScrollViewPager pager = this.autoScrollViewPager.get();
        if (pager != null) {
          pager.scroller.setScrollDurationFactor(pager.autoScrollFactor);
          pager.scrollOnce();
          pager.scroller.setScrollDurationFactor(pager.swipeScrollFactor);
          pager.sendScrollMessage(pager.interval + pager.scroller.getDuration());
        }
      }
    }
  }
}
