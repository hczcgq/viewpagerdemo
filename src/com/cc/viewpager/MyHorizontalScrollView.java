package com.cc.viewpager;import android.content.Context;import android.os.Handler;import android.util.AttributeSet;import android.view.MotionEvent;import android.widget.HorizontalScrollView;public class MyHorizontalScrollView extends HorizontalScrollView {	public MyHorizontalScrollView(Context context, AttributeSet attrs) {		super(context, attrs);	}	public interface ScrollViewListener {		public static final int IDLE = 0;		public static final int TOUCH_SCROLL = 1;		public static final int FLING = 2;		void onScrollChanged(int status);				void onScrollChanged(int l, int t, int oldl, int oldt);	}	private Handler mHandler = new Handler();	private ScrollViewListener scrollViewListener;	/**	 * 记录当前滚动的距离	 */	private int currentX = -9999999;	/**	 * 当前滚动状态	 */	private int scrollStatus = ScrollViewListener.IDLE;	/**	 * 滚动监听间隔	 */	private int scrollDealy = 50;	/**	 * 滚动监听runnable	 */	private Runnable scrollRunnable = new Runnable() {		@Override		public void run() {			if (getScrollX() == currentX) {				// 滚动停止 取消监听线程				scrollStatus = ScrollViewListener.IDLE;				if (scrollViewListener != null) {					scrollViewListener.onScrollChanged(scrollStatus);				}				mHandler.removeCallbacks(this);				return;			} else {				// 手指离开屏幕 view还在滚动的时候				scrollStatus = ScrollViewListener.FLING;				if (scrollViewListener != null) {					scrollViewListener.onScrollChanged(scrollStatus);				}			}			currentX = getScrollX();			mHandler.postDelayed(this, scrollDealy);		}	};	@Override	public boolean onTouchEvent(MotionEvent ev) {		switch (ev.getAction()) {		case MotionEvent.ACTION_MOVE:			scrollStatus = ScrollViewListener.TOUCH_SCROLL;			scrollViewListener.onScrollChanged(scrollStatus);			// 手指在上面移动的时候 取消滚动监听线程			mHandler.removeCallbacks(scrollRunnable);			break;		case MotionEvent.ACTION_UP:			// 手指移动的时候			mHandler.post(scrollRunnable);			break;		}		return super.onTouchEvent(ev);	}		@Override	protected void onScrollChanged(int l, int t, int oldl, int oldt) {		super.onScrollChanged(l, t, oldl, oldt);		if (this.scrollViewListener != null) {			this.scrollViewListener.onScrollChanged(l, t, oldl, oldt);		}	}	/**	 * 必须先调用这个方法设置Handler 不然会出错 2014-12-7 下午3:55:39	 * 	 * @author DZC	 * @return void	 * @param handler	 * @TODO	 *///	public void setHandler(Handler handler) {//		this.mHandler = handler;//	}	/**	 * 设置滚动监听 2014-12-7 下午3:59:51	 * 	 * @author DZC	 * @return void	 * @param listener	 * @TODO	 */	public void setOnScrollStateChangedListener(ScrollViewListener listener) {		this.scrollViewListener = listener;	}}