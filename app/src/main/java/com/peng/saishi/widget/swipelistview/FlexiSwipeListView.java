package com.peng.saishi.widget.swipelistview;


import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;

public class FlexiSwipeListView extends SwipeListView {
    private boolean outBound = false;
    private int distance;
    private int firstOut;  

    public FlexiSwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public FlexiSwipeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }


    @SuppressWarnings("deprecation")
	GestureDetector gestureDetector = new GestureDetector(
            new OnGestureListener() {

                public boolean onSingleTapUp(MotionEvent e) {
                    // TODO Auto-generated method stub
                    return false;
                }


                public void onShowPress(MotionEvent e) {
                    // TODO Auto-generated method stub
                }



                public boolean onScroll(MotionEvent e1, MotionEvent e2,
                        float distanceX, float distanceY) {

                    int firstPos = getFirstVisiblePosition();
                    int lastPos = getLastVisiblePosition();
                    int itemCount = getCount();
                    // outbound Top
                    if (outBound && firstPos != 0 && lastPos != (itemCount - 1)) {
                        scrollTo(0, 0);
                        return false;
                    }
                    View firstView = getChildAt(firstPos);
                    if (!outBound)
                        firstOut = (int) e2.getRawY();
                    if (firstView != null
                            && (outBound || (firstPos == 0
                                    && firstView.getTop() == 0 && distanceY < 0))) {
                        // Record the length of each slide
                        distance = firstOut - (int) e2.getRawY();
                        scrollTo(0, distance / 2);
                        return true;
                    }

                    if (lastPos != (itemCount - 1))
                        return false;
                    View lastView = getChildAt(lastPos - firstPos);
                    int GridHeight = getHeight(); 
                    if (lastView != null && (outBound || 
                        ((lastView.getBottom() + 8) >= GridHeight && distanceY > 0))) { 
                        distance = firstOut - (int) e2.getRawY();
                        scrollTo(0, distance);
                        return true;
                    } 


                    return false;
                }


                public void onLongPress(MotionEvent e) {
                    // TODO Auto-generated method stub
                }

                public boolean onFling(MotionEvent e1, MotionEvent e2,
                        float velocityX, float velocityY) {
                    // TODO Auto-generated method stub
                    return false;
                }

                public boolean onDown(MotionEvent e) {
                    // TODO Auto-generated method stub
                    return false;
                }
            });

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        int act = event.getAction();

        if ((act == MotionEvent.ACTION_UP || act == MotionEvent.ACTION_CANCEL)
                && outBound) {
            outBound = false;

        }
        if (!gestureDetector.onTouchEvent(event)) {
            outBound = false;

        } else {
            outBound = true;
        }
        Rect rect = new Rect();
        getLocalVisibleRect(rect);
        TranslateAnimation am = new TranslateAnimation(0, 0, -rect.top, 0);
        am.setDuration(300);
        startAnimation(am);
        scrollTo(0, 0);
        return super.dispatchTouchEvent(event);
    }
}