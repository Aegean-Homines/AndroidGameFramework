package com.egemen.framework.implementation;

import java.util.ArrayList;
import java.util.List;

import com.egemen.framework.Pool;
import com.egemen.framework.Input.TouchEvent;
import com.egemen.framework.Pool.PoolObjectFactory;

import android.view.MotionEvent;
import android.view.View;

public class SingleTouchHandler implements TouchHandler {
	boolean isTouched;
	int touchX, touchY;
	Pool<TouchEvent> touchEventPool;
	// REMEMBER: Pool is a helper class we created to fabricate and store things
	// properly
	// A.K.A. a home-made container class
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	float scaleX, scaleY;

	public SingleTouchHandler(View view, float scaleX, float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;

		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
			@Override
			public TouchEvent createObject() {
				return new TouchEvent();
			}
		};

		touchEventPool = new Pool<TouchEvent>(factory, 100);
		view.setOnTouchListener(this);
	}

	@Override
	public boolean isTouchDown(int pointer) {
		synchronized (this) {
			if(pointer == 0)
				return isTouched;
			else
				return false;			
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		synchronized (this) {
			TouchEvent touchEvent = touchEventPool.newObject();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touchEvent.type = TouchEvent.TOUCH_DOWN;
				isTouched = true;
				break;
			case MotionEvent.ACTION_MOVE:
				touchEvent.type = TouchEvent.TOUCH_DRAGGED;
				isTouched = true;
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				touchEvent.type = TouchEvent.TOUCH_UP;
				isTouched = false;
				break;
			}

			touchEvent.x = touchX = (int) (event.getX() * scaleX);
			touchEvent.y = touchY = (int) (event.getY() * scaleY);
			touchEventsBuffer.add(touchEvent);

			return true;
		}
	}

	@Override
	public int getTouchX(int pointer) {
		synchronized (this) {
			return touchX;			
		}
	}

	@Override
	public int getTouchY(int pointer) {
		synchronized (this) {
			return touchY;
		}
	}

	@Override
	public List<TouchEvent> getTouchEvents() {
		synchronized(this){
			for(int i = 0; i < touchEvents.size(); i++)
				touchEventPool.free(touchEvents.get(i));
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			
			return touchEvents;
		}
	}

}
